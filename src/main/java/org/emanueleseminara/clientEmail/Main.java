package org.emanueleseminara.clientEmail;

import javax.mail.MessagingException;
import javax.mail.Message;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String username, password;
        Email email = new Email();
        Message[] messages = null;

        System.out.println("############### CLIENT EMAIL ###############\n" +
                "\n**At the moment it only works with GMAIL**\n" +
                "Type Help for the command list" +
                "\n################## LOGIN ###################");

        do {
            System.out.print("Email: ");
            username = sc.nextLine();
            System.out.print("Password: ");
            password = sc.nextLine();


            try {
                messages = email.loadEmail("Gmail", "pop3", username, password);
                System.out.println("\nLOGIN SUCCESSFUL!\n");
            } catch (MessagingException e) {
                System.out.println("\n***** WRONG EMAIL OR PASSWORD *****\n");
                //e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }while(messages == null);




        while(true) {
            System.out.println(messages.length + " Email to read");
            System.out.print("-> ");
            switch(sc.nextLine().toLowerCase()){
                case("help"):
                    System.out.println("\n\n############### COMMAND LIST ###############\n" +
                            "Read - Reads Email\n" +
                            "Send - Send and Email\n" +
                            "Exit - Close the program\n" +
                            "############################################\n");
                    break;
                case("read"):
                    try {
                        email.readEmail(messages);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;
                case("send"):
                    System.out.print("To: ");
                    String to = sc.nextLine();
                    System.out.print("Object: ");
                    String object = sc.nextLine();
                    System.out.print("Text: ");
                    String text = sc.nextLine();

                    try {
                        email.sendEmail("Gmail", username, password, to, object, text);
                        System.out.println("Email sent!");
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }

                    break;
                case("exit"):
                    System.exit(0);

                default:
                    System.out.println("***** COMMAND NOT FOUND *****");
            }
        }
    }
}
