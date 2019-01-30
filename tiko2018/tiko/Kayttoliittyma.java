/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tiko;
import java.util.*;

/**
 *
 * @author aLppu
 */
public class Kayttoliittyma {
    Scanner sc = new Scanner(System.in);
    boolean running = true;
    String input = "";
    Login log = new Login();
    Help help = new Help();
    
    public void run(){
        System.out.println("Welcome to Divari Database, please proceed by using the following commands:");
        while(running){
            System.out.println("\"login\" to log into an existing account.");
            System.out.println("\"signup\" to create a new account (requires email verification).");
            System.out.println("\"exit\" to exit the program.");
            input = sc.nextLine();
            if ("exit".equals(input)){
                running = false;
            } else {
                if("login".equals(input)){
                    if(log.checkUserInformation()){
                        // login successful, the continuing operations will be done in Login class
                    } else {
                        System.out.println("\tLogin unsuccessful.");
                    }
                } else if ("signup".equals(input)){
                    if(log.createNewUser()){
                        System.out.println("Signup successful, please login with your newly created user.");
                    } else {
                        System.out.println("Signup unsuccessful, please retry using the signup.");
                    }
                } else {
                    help.invalidCommand();
                }
            } 
        }
    }
}
