package org.emanueleseminara.clientEmail;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Email: ");
        String username = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();
        Email email = new Email();
        try {
            email.receiveEmail("Gmail", "pop3", username, password);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        while(true) {
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
