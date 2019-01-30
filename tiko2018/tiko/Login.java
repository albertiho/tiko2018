/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tiko;

import java.util.Scanner;
import java.sql.*;

/**
 *
 * @author aLppu
 */
public class Login {
    Scanner sc = new Scanner(System.in);
    Help help = new Help();
    Connection con = help.cn();
    User user = new User();
    
    // operation used to check the login information from user
    public boolean checkUserInformation(){ 
        //Scanner sc = new Scanner(System.in);
        boolean returnValue = false;    // return value of this operation
        boolean logging = true; // boolean to check if the client wishes to continue trying to log in
        
        System.out.println("To log in, enter your email and password, separated with a space");
        System.out.println("(Example: Examplemail@gmail.com Password123)");
        System.out.println("You can use \"back\" to move back");
        while(logging){ // kaatuu jos yks parametri
            String userInformation = sc.nextLine();
            if(userInformation.equals("back")){
                logging = false;
            }
            if(userInformation.equals("override")){
                user.adminUser("override");
                return true;
            }
            String [] information = userInformation.split(" ");
            if(information.length == 2){
                String email = information[0];
                String password = information[1];
                String check = "SELECT sposti, salasana, taso FROM keskus.Asiakas "
                        + "WHERE sposti = \'" + email + "\'";
                if(logging){
                    int userLevel = Integer.MAX_VALUE;
                    try {
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(check);
                        if (!rs.isBeforeFirst()){
                            System.out.println("Username not found. Please check given values.");
                        } else {
                            returnValue = true;
                        }
                        if(returnValue) {
                            while(rs.next()) {
                                userLevel = rs.getInt("taso");
                                String dbPassword = rs.getString("salasana");
                                if(password.length() == dbPassword.length()){
                                    for(int i = 0; i< password.length(); i++){
                                        if(password.charAt(i) != dbPassword.charAt(i)){
                                            returnValue = false;
                                        }
                                    }
                                } else {
                                    returnValue = false;
                                }
                            }
                            if(!returnValue){
                                System.out.println("Invalid password. Plaese check given values.");
                            }
                        }

                    } catch (SQLException e) {
                        help.SQLErrormessage(e);
                        /*System.out.println("\nThere was a problem finding your login information.");
                        System.out.println("Please check given values.");*/
                        return false;
                    }
                    // launch Divari Database with right level of commands
                    if (returnValue){
                        if (userLevel == 0){
                            user.normalUser(email);
                            logging = false;
                        } else if (userLevel == 1){
                            user.adminUser(email);
                            logging = false;
                        } else {
                            System.out.println("Userlevel verification unsuccessful.");
                            return false;
                        }
                    }
                }
            } else {
            System.out.println("Invalid login parameters, please check given values.");
            }
        }
        return returnValue; 
    }
    
    // operation which is used to create a new user and add its information to the database
    public boolean createNewUser(){
        boolean createSuccessful = false;
        String[] informationTemplate = {"1. firstname", "2. lastname", "3. address", 
            "4. email-address (will be used as your username",
            "5. phonenumber", "6. password"};
        String[] userInformation = new String[6];
        
        System.out.println("Welcome to creating a new user into Divari Database!");
        System.out.println("Please enter the required information:");
        help.illegalCharacters();
        

        // filling the user information to the userInformation table
        for(int i = 0; i < userInformation.length; i++){
            System.out.println("what is your " + informationTemplate[i] + "?");
            String temp = sc.nextLine();
            if(help.check(temp)){
                userInformation[i] = temp;
            }else {
                System.out.println("Illegal chracters detected, please re-enter information.");
                i--;
            }
        }
        boolean finished = false;
        while(!finished){
            Scanner xd = new Scanner(System.in);
            System.out.println("Please check that the information you have submitted is true:");
            for (int i = 0; i < userInformation.length; i++){
                System.out.println(informationTemplate[i] + ": " + userInformation[i]);
            }
            System.out.println("Enter the number 1 to insert information again.");
            System.out.println("Enter the number 0 to confirm and continue.");
            // editvalue is used as an indexnumber for the information that the user wishes to edit
            int editvalue = xd.nextInt();
            if(editvalue == 0){
                finished = true;
            } else if (editvalue == 1){
                for(int i = 0; i < userInformation.length; i++){
                    System.out.println("what is your " + informationTemplate[i] + "?");
                    String temp = sc.nextLine();
                    if(help.check(temp)){
                        userInformation[i] = temp;
                    }else {
                        System.out.println("Illegal chracters detected, please re-enter information.");
                        i--;
                    }
                }
            } else {
                help.invalidCommand();
            }
        }
        // updating the correct user information to SQL acceptable form
        for(int i = 0; i < userInformation.length; i++){
            userInformation[i] = "\'" + userInformation[i] + "\'";
        }
        try{
            /* aNro shows the amount of customers already registered into the database
             * and is used to give the customers their unique customerNumbers
            */
            Statement aNro = con.createStatement(); 
            ResultSet rsA = aNro.executeQuery("SELECT COUNT(asiakas_id) as current_amount FROM keskus.Asiakas");
            rsA.next();
            int newCustomerNumber = Integer.parseInt(rsA.getString("current_amount"))+1;
            String update = "INSERT INTO keskus.Asiakas"
                    + "(etunimi, sukunimi, asiakas_id, a_osoite, sposti, puh_nro, salasana) "
                    +"VALUES(" + userInformation[0] + "," + userInformation[1] + "," + newCustomerNumber 
                    + "," + userInformation[2] + "," + userInformation[3] + "," + userInformation[4]
                    + "," + userInformation[5] + ")";
            Statement updater = con.createStatement();
            int updatedValues = updater.executeUpdate(update);
            if (updatedValues == 1){
                createSuccessful = true;
            }
            
        }catch (SQLException e){
            help.SQLErrormessage(e);
        }
        
        return createSuccessful;
    }
}