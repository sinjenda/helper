package com.sin.helper.jobs;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public record Book (String nazev, String autor, int year,@Nullable Vypujcitel vypujcitel){
    @Override
    public String toString() {
        return BookOutputStream.stringToHtml("nazev: ")+nazev+BookOutputStream.stringToHtml(" autor: ")+autor;
    }

    public static class BookInputStream extends ObjectInputStream {
        /**
         * Creates a DataInputStream that uses the specified
         * underlying InputStream.
         *
         * @param in the specified input stream
         */
        public BookInputStream(@NotNull InputStream in) throws IOException {
            super(in);
        }
        public Book readBook() {
            try {
                return new Book(readUTF(), readUTF(), readInt(), (Vypujcitel) readObject());
            } catch (IOException | ClassNotFoundException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public static class BookOutputStream extends ObjectOutputStream {

        /**
         * Creates an output stream filter built on top of the specified
         * underlying output stream.
         *
         * @param out the underlying output stream to be assigned to
         *            the field {@code this.out} for later use, or
         *            {@code null} if this instance is to be
         *            created without an underlying stream.
         */
        public BookOutputStream(OutputStream out) throws IOException {
            super(out);
        }

        public void writeBook(Book book){
            try {
                writeUTF(stringToHtml(book.autor()));
                writeUTF(stringToHtml(book.nazev()));
                writeInt(book.year());
                writeObject(book.vypujcitel);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public static String stringToHtml(String str){
            ArrayList<Character>toChange=new ArrayList<>(Arrays.asList('ě','š','č','ř','ž','ý','á','í','é','ú','ů','ó','ď','ť','ň'));
            StringBuilder ret= new StringBuilder();
            for (char c:str.toCharArray()){
                if (toChange.contains(c)){
                    ret.append("&#").append((int) c).append(";");
                }
                else {
                    ret.append(c);
                }
            }
            return ret.toString();
        }

    }
}
