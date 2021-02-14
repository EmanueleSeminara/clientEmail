package org.emanueleseminara.clientEmail;

import javax.mail.MessagingException;
import javax.mail.Message;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Message[] messages = new Message[0];

        System.out.println("############### CLIENT EMAIL ###############\n" +
                "\n**At the moment it only works with GMAIL**\n" +
                "\n################## LOGIN ##################");

        System.out.print("Email: ");
        String username = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();
        Email email = new Email();

        try {
            messages = email.loadEmail("Gmail", "pop3", username, password);
            System.out.println("\nLOGIN SUCCESSFUL!");
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        while(true) {
            System.out.println(messages.length + " Email to read");
            System.out.print("-> ");
            switch(sc.nextLine().toLowerCase()){
                case("help"):
                    System.out.println("\n\n############### COMMAND LIST ###############\n" +
                            //"Configure - Enter the Email information\n" +
                            //"Connect - Establishes a connection\n" +
                            "Exit - Close the program\n" +
                            "############################################\n");
                    break;

                case("exit"):
                    System.exit(0);

                default:
                    System.out.println("***** COMMAND NOT FOUND *****");
            }
        }
    }
}
