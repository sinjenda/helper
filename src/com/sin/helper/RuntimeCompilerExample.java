package com.sin.helper;

import javax.tools.*;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject.Kind;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.util.*;

/**
 * An example showing how to use the RuntimeCompiler utility class
 */
public class RuntimeCompilerExample
{
    public static void main(String[] args) throws Exception
    {
        System.out.println(RuntimeCompilerExample.class.getName());
        simpleExample();
        twoClassExample();
        useLoadedClassExample();
    }

    /**
     * Simple example: Shows how to add and compile a class, and then
     * invoke a static method on the loaded class.
     */
    private static void simpleExample()
    {
        String classNameA = "ExampleClass";
        String codeA =
                """
                        public class ExampleClass {
                            public static void exampleMethod(String name) {
                                System.out.println("Hello, "+name);
                            }
                        }
                        """;

        RuntimeCompiler r = new RuntimeCompiler();
        r.addClass(classNameA, codeA);
        r.compile();

        MethodInvocationUtils.invokeStaticMethod(
                r.getCompiledClass(classNameA),
                "exampleMethod", "exampleParameter");
    }

    /**
     * An example showing how to add two classes (where one refers to the
     * other), compile them, and invoke a static method on one of them
     */
    private static void twoClassExample()
    {
        String classNameA = "ExampleClassA";
        String codeA =
                """
                        public class ExampleClassA {
                            public static void exampleMethodA(String name) {
                                System.out.println("Hello, "+name);
                            }
                        }
                        """;

        String classNameB = "ExampleClassB";
        String codeB =
                """
                        public class ExampleClassB {
                            public static void exampleMethodB(String name) {
                                System.out.println("Passing to other class");
                                ExampleClassA.exampleMethodA(name);
                            }
                        }
                        """;

        RuntimeCompiler r = new RuntimeCompiler();
        r.addClass(classNameA, codeA);
        r.addClass(classNameB, codeB);
        r.compile();

        MethodInvocationUtils.invokeStaticMethod(
                r.getCompiledClass(classNameB),
                "exampleMethodB", "exampleParameter");
    }

    /**
     * An example that compiles and loads a class, and then uses an
     * instance of this class
     */
    private static void useLoadedClassExample() throws Exception
    {
        String classNameA = "ExampleComparator";
        String codeA =
                """
                        import java.util.Comparator;
                        public class ExampleComparator\s
                            implements Comparator<Integer> {
                            @Override
                            public int compare(Integer i0, Integer i1) {
                                System.out.println(i0+" and "+i1);
                                return Integer.compare(i0, i1);
                            }
                        }
                        """;

        RuntimeCompiler r = new RuntimeCompiler();
        r.addClass(classNameA, codeA);
        r.compile();

        Class<?> c = r.getCompiledClass("ExampleComparator");
        @SuppressWarnings("unchecked") Comparator<Integer> comparator = (Comparator<Integer>) c.getConstructor().newInstance();

        System.out.println("Sorting...");
        List<Integer> list = new ArrayList<>(Arrays.asList(3, 1, 2));
        list.sort(comparator);
        System.out.println("Result: "+list);
    }

    public static void execute(String code){
        RuntimeCompiler r=new RuntimeCompiler();
        String clazz="import java.io.*;\n public class Example{ public static void exec(){"+code+"}}";
        r.addClass("Example",clazz);
        r.compile();
        MethodInvocationUtils.invokeStaticMethod(r.getCompiledClass("Example"),"exec");
    }
    public static Class<?> toClass(String str){
        Scanner scnr=new Scanner(str);
        //noinspection StatementWithEmptyBody
        while (!scnr.next().equals("class"));
        String name=scnr.next();
        RuntimeCompiler r=new RuntimeCompiler();
        r.addClass(name,str);
        r.compile();
        return r.getCompiledClass(name);
    }

}


/**
 * Utility class for compiling classes whose source code is given as
 * strings, in-memory, at runtime, using the JavaCompiler tools.
 */
class RuntimeCompiler
{
    /**
     * The Java Compiler
     */
    private final JavaCompiler javaCompiler;

    /**
     * The mapping from fully qualified class names to the class data
     */
    private final Map<String, byte[]> classData;

    /**
     * A class loader that will look up classes in the {@link #classData}
     */
    private final MapClassLoader mapClassLoader;

    /**
     * The JavaFileManager that will handle the compiled classes, and
     * eventually put them into the {@link #classData}
     */
    private final ClassDataFileManager classDataFileManager;

    /**
     * The compilation units for the next compilation task
     */
    private final List<JavaFileObject> compilationUnits;


    /**
     * Creates a new RuntimeCompiler
     *
     * @throws NullPointerException If no JavaCompiler could be obtained.
     * This is the case when the application was not started with a JDK,
     * but only with a JRE. (More specifically: When the JDK tools are
     * not in the classpath).
     */
    public RuntimeCompiler()
    {
        this.javaCompiler = ToolProvider.getSystemJavaCompiler();
        if (javaCompiler == null)
        {
            throw new NullPointerException(
                    "No JavaCompiler found. Make sure to run this with "
                            + "a JDK, and not only with a JRE");
        }
        this.classData = new LinkedHashMap<>();
        this.mapClassLoader = new MapClassLoader();
        this.classDataFileManager =
                new ClassDataFileManager(
                        javaCompiler.getStandardFileManager(null, null, null));
        this.compilationUnits = new ArrayList<>();
    }

    /**
     * Add a class with the given name and source code to be compiled
     * with the next call to {@link #compile()}
     *
     * @param className The class name
     * @param code The code of the class
     */
    public void addClass(String className, String code)
    {
        String javaFileName = className + ".java";
        JavaFileObject javaFileObject =
                new MemoryJavaSourceFileObject(javaFileName, code);
        compilationUnits.add(javaFileObject);
    }

    /**
     * Compile all classes that have been added by calling
     * {@link #addClass(String, String)}
     *
     * @return Whether the compilation succeeded
     */
    boolean compile()
    {
        DiagnosticCollector<JavaFileObject> diagnosticsCollector =
                new DiagnosticCollector<>();
        CompilationTask task =
                javaCompiler.getTask(null, classDataFileManager,
                        diagnosticsCollector, null, null,
                        compilationUnits);
        boolean success = task.call();
        compilationUnits.clear();
        for (Diagnostic<?> diagnostic : diagnosticsCollector.getDiagnostics())
        {
            System.out.println(
                    diagnostic.getKind() + " : " +
                            diagnostic.getMessage(null));
            System.out.println(
                    "Line " + diagnostic.getLineNumber() +
                            " of " + diagnostic.getSource());
            System.out.println();
        }
        return success;
    }


    /**
     * Obtain a class that was previously compiled by adding it with
     * {@link #addClass(String, String)} and calling {@link #compile()}.
     *
     * @param className The class name
     * @return The class. Returns <code>null</code> if the compilation failed.
     */
    public Class<?> getCompiledClass(String className)
    {
        return mapClassLoader.findClass(className);
    }

    /**
     * In-memory representation of a source JavaFileObject
     */
    private static final class MemoryJavaSourceFileObject extends
            SimpleJavaFileObject
    {
        /**
         * The source code of the class
         */
        private final String code;

        /**
         * Creates a new in-memory representation of a Java file
         *
         * @param fileName The file name
         * @param code The source code of the file
         */
        private MemoryJavaSourceFileObject(String fileName, String code)
        {
            super(URI.create("string:///" + fileName), Kind.SOURCE);
            this.code = code;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return code;
        }
    }

    /**
     * A class loader that will look up classes in the {@link #classData}
     */
    private class MapClassLoader extends ClassLoader
    {
        @Override
        public Class<?> findClass(String name)
        {
            byte[] b = classData.get(name);
            return defineClass(name, b, 0, b.length);
        }
    }

    /**
     * In-memory representation of a class JavaFileObject
     * @author User
     *
     */
    private class MemoryJavaClassFileObject extends SimpleJavaFileObject
    {
        /**
         * The name of the class represented by the file object
         */
        private final String className;

        /**
         * Create a new java file object that represents the specified class
         *
         * @param className THe name of the class
         */
        private MemoryJavaClassFileObject(String className)
        {
            super(URI.create("string:///" + className + ".class"),
                    Kind.CLASS);
            this.className = className;
        }

        @Override
        public OutputStream openOutputStream() {
            return new ClassDataOutputStream(className);
        }
    }


    /**
     * A JavaFileManager that manages the compiled classes by passing
     * them to the {@link #classData} map via a ClassDataOutputStream
     */
    private class ClassDataFileManager extends
            ForwardingJavaFileManager<StandardJavaFileManager>
    {
        /**
         * Create a new file manager that delegates to the given file manager
         *
         * @param standardJavaFileManager The delegate file manager
         */
        private ClassDataFileManager(
                StandardJavaFileManager standardJavaFileManager)
        {
            super(standardJavaFileManager);
        }

        @Override
        public JavaFileObject getJavaFileForOutput(final Location location,
                                                   final String className, Kind kind, FileObject sibling) {
            return new MemoryJavaClassFileObject(className);
        }
    }


    /**
     * An output stream that is used by the ClassDataFileManager
     * to store the compiled classes in the  {@link #classData} map
     */
    private class ClassDataOutputStream extends OutputStream
    {
        /**
         * The name of the class that the received class data represents
         */
        private final String className;

        /**
         * The output stream that will receive the class data
         */
        private final ByteArrayOutputStream baos;

        /**
         * Creates a new output stream that will store the class
         * data for the class with the given name
         *
         * @param className The class name
         */
        private ClassDataOutputStream(String className)
        {
            this.className = className;
            this.baos = new ByteArrayOutputStream();
        }

        @Override
        public void write(int b) throws IOException
        {
            baos.write(b);
        }

        @Override
        public void close() throws IOException
        {
            classData.put(className, baos.toByteArray());
            super.close();
        }
    }
}

/**
 * Utility methods not directly related to the RuntimeCompiler
 */
class MethodInvocationUtils
{
    /**
     * Utility method to invoke the first static method in the given
     * class that can accept the given parameters.
     *
     * @param c The class
     * @param methodName The method name
     * @param args The arguments for the method call
     * @return The return value of the method call
     * @throws RuntimeException If either the class or a matching method
     * could not be found
     */
    public static Object invokeStaticMethod(
            Class<?> c, String methodName, Object... args)
    {
        Method m = findFirstMatchingStaticMethod(c, methodName, args);
        if (m == null)
        {
            throw new RuntimeException("No matching method found");
        }
        try
        {
            return m.invoke(null, args);
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Utility method to find the first static method in the given
     * class that has the given name and can accept the given
     * arguments. Returns <code>null</code> if no such method
     * can be found.
     *
     * @param c The class
     * @param methodName The name of the method
     * @param args The arguments
     * @return The first matching static method.
     */
    private static Method findFirstMatchingStaticMethod(
            Class<?> c, String methodName, Object ... args)
    {
        Method[] methods = c.getDeclaredMethods();
        for (Method m : methods)
        {
            if (m.getName().equals(methodName) &&
                    Modifier.isStatic(m.getModifiers()))
            {
                Class<?>[] parameterTypes = m.getParameterTypes();
                if (areAssignable(parameterTypes, args))
                {
                    return m;
                }
            }
        }
        return null;
    }

    /**
     * Returns whether the given arguments are assignable to the
     * respective types
     *
     * @param types The types
     * @param args The arguments
     * @return Whether the arguments are assignable
     */
    private static boolean areAssignable(Class<?>[] types, Object ...args)
    {
        if (types.length != args.length)
        {
            return false;
        }
        for (int i=0; i<types.length; i++)
        {
            Object arg = args[i];
            Class<?> type = types[i];
            if (arg != null && !type.isAssignableFrom(arg.getClass()))
            {
                return false;
            }
        }
        return true;
    }

}

