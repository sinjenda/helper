package com.sin.helper;


import javax.mail.*;
import java.io.IOException;
import java.util.Properties;

public class test {
    public static void check(String user,
                             String password,String emailType) {
        try {
            if (emailType.equals("pop")) {
                //create properties field
                Properties properties = new Properties();

                properties.put("mail.pop3.host", "pop.gmail.com");
                properties.put("mail.pop3.port", "995");
                properties.put("mail.pop3.starttls.enable", "true");
                Session emailSession = Session.getDefaultInstance(properties);

                //create the POP3 store object and connect with the pop server
                Store store = emailSession.getStore("pop3s");

                store.connect("pop.gmail.com", user, password);

                //create the folder object and open it
                Folder emailFolder = store.getFolder("INBOX");
                emailFolder.open(Folder.READ_ONLY);

                // retrieve the messages from the folder in an array and print it
                Message[] messages = emailFolder.getMessages();
                System.out.println("messages.length---" + emailFolder.getUnreadMessageCount());
                read(messages);
                emailFolder.close(false);
                store.close();
            }

            //close the store and folder objects


            else{
            Properties props = System.getProperties();
            props.setProperty("mail.store.protocol", "imap");
            props.setProperty("mail.imap.ssl.enable", "true");
            props.setProperty("mail.imaps.partialfetch", "false");
            props.put("mail.mime.base64.ignoreerrors", "true");

            Session session = Session.getInstance(props);
            Store store = session.getStore("imap");
            store.connect("outlook.office365.com", user, password);
            Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_WRITE);
            Message[] messages = folder.getMessages();
            read(messages);
        }

            } catch(Exception e){
                e.printStackTrace();
            }
    }

    public static void main(String[] args) {

        String username = "jan.sin@gcelakovice.cz";// change accordingly
        String password = "Homahaba1";// change accordingly

        check( username, password,"smtp");

    }

    private static void read(Message[] messages) throws MessagingException, IOException {
        for (int i = 0, n = messages.length; i < n; i++) {

            Message message = messages[i];
            if (!message.isSet(Flags.Flag.SEEN)) {
                //message.setFlags(new Flags("geted"),true);
                System.out.println("---------------------------------");
                System.out.println("Email Number " + (i + 1));
                System.out.println("Subject: " + message.getSubject());
                String mess = message.getFrom()[0].toString();
                try {
                    System.out.println("From: " + mess.substring(mess.indexOf("<") + 1, mess.indexOf(">")));
                }
                catch (StringIndexOutOfBoundsException ignore){
                    System.out.println(mess);
                    System.out.println("Text: " + message.getContent().toString());
                    i++;
                    continue;
                }
                System.out.println("Text: " + message.getContent().toString());
            }
            i++;
        }

    }
}
