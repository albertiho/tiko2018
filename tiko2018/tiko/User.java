/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package tiko;
import java.util.Scanner;

/**
 *
 * @author aLppu
 */
public class User {
    Scanner sc = new Scanner(System.in);
    // Commands class stores all the commands that are available on the Divari Database
    Commands cmd = new Commands();
    Help help = new Help();
    
    public void normalUser(String userName){
        System.out.println("Welcome back " + userName + ", all of the databases commands are found under the command \"help\".");
        boolean shopping = true;
        while(shopping) {
            String input = sc.nextLine();
            if("help".equals(input)){
                cmd.help();
            } else if ("newCart".equals(input)){
                cmd.newCart(userName); 
            } else if ("search".equals(input)){
                cmd.search();
            } else if ("add".equals(input)){
                cmd.add(userName);
            } else if ("remove".equals(input)){
                cmd.remove();
            } else if ("cart".equals(input)){
                cmd.cart(userName);
            } else if ("cashout".equals(input)){
                cmd.cashout(userName);
            } else if ("discard".equals(input)){
                cmd.discard(userName);
            } else if ("exit".equals(input)){
                shopping = false;
            } else {
                help.invalidCommand();
            }
        }
    }
    
    public void adminUser(String userName){
        // press x to view all commands, press y to view admincommands
        System.out.println("\tadmin login successful. use help to view all commands.");
        boolean using = true;
        while(using) {
            String input = sc.nextLine();
            if("help".equals(input)){
                cmd.help();
            } else if ("newCart".equals(input)){
                cmd.newCart(userName); 
            } else if ("search".equals(input)){
                cmd.search();
            } else if ("add".equals(input)){
                cmd.add(userName);
            } else if ("remove".equals(input)){
                cmd.remove();
            } else if ("cart".equals(input)){
                cmd.cart(userName);
            } else if ("cashout".equals(input)){
                cmd.cashout(userName);
            } else if ("discard".equals(input)){
                cmd.discard(userName);
            } else if ("query".equals(input)){
                cmd.query();
            } else if ("read".equals(input)){
                cmd.read();
            } else if ("addItem".equals(input)){
                cmd.addItem();
            }else if ("promote".equals(input)){
                cmd.promote();
            }else if ("demote".equals(input)){
                cmd.demote();
            }else if ("reset".equals(input)){
                help.reset();
            }else if ("triggerTest".equals(input)){
                cmd.triggerTest();
            }else if ("r2".equals(input)){
                cmd.r2();
            } else if ("exit".equals(input)){
                using = false;
            } else {
                help.invalidCommand();
            }
        }
    }
    
}
