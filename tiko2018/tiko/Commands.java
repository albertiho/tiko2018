/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package tiko;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Scanner;

/**
 *
 * @author aLppu
 */
public class Commands {
    Help help = new Help();
    Connection con = help.cn();
    Scanner sc = new Scanner(System.in);
    Order order = new Order();
    Delivery delivery = new Delivery();
    
    int totalCommands = 16; // total amount of commands in Divari Database
    int userLimit = 9;  // amount of commands that a non-admin user is able to use
    
    

    // operation which stores and shares commands
    public String[] commands(){
        String[] commands = new String[totalCommands];
        commands[0] = "help";
        commands[1] = "newCart";
        commands[2] = "search";
        commands[3] = "add";
        commands[4] = "remove";
        commands[5] = "cart";
        commands[6] = "cashout";
        commands[7] = "discard";
        commands[8] = "exit";
        commands[9] = "query";
        commands[10] = "addItem";
        commands[11] = "promote";
        commands[12] = "demote";
        commands[13] = "reset";
        commands[14] = "triggerTest";
        commands[15] = "r2";
        return commands;
    }
    
    // operation which stores and shares explanations to commands
    public String[] explanations(){
        String[] explanations = new String[totalCommands];
        // help
        explanations[0] = " -\t displays all commands that can be used in Divari Database.\n";
        // newCart
        explanations[1] = " - creates a new shopping cart for you to use\n";
        // search
        explanations[2] = " - lets you search the Divari Database for books with different search operands/filters.\n";
        // add
        explanations[3] = " -\t used to search and then add items into your shoppingcart.\n";
        // remove
        explanations[4] = " - used to remove items from your shoppingcart.\n";
        // cart
        explanations[5] = " -\t used to display your current shoppingcart, its id, items in it and the c"
                + "ombined cost of items.\n";
        // cashout
        explanations[6] = " - proceed to paying your shoppingcart.\n";
        // discard
        explanations[7] = " - used to discard your shoppingcart and free reserved items.\n";
        // exit
        explanations[8] = " -\t logout from Divari Database. \n";
        // query
        explanations[9] = " - enter and run sql-queries. can choose from execute(e) or executequery(eq)";
        // addItem
        explanations[10] = " - adds an instance of a book.";
        // promote
        explanations[11] = " - promotes an user to admin.";
        // demote
        explanations[12] = " - demotes an user to normal user";
        // reset
        explanations[13]= " - resets the database into a preset state.";
        // triggerTest
        explanations[14] = " - shows that the implemented trigger works.";
        //r2
        explanations[15] = " - tulostaa tehtävänannossa pyydetyn tiedon.";
        return explanations;
    }
    
    public String[] columns(){
        // the column order in the book-table.
        //0 = adminSearch, 1 = author, 2 = title, 3 = genre, 4 = type, 5 = year
        String[] columns = new String[6];
        columns[0] = "";
        columns[1] = "tekija";
        columns[2] = "k_nimi";
        columns[3] = "genre";
        columns[4] = "tyyppi";
        columns[5] = "vuosi";
        return columns;
        
    }
    
    // operation which is called when the users input is "help"
    public void help(){
        String[] cmd = commands();
        String[] expl = explanations();
        for (int i = 0; i < userLimit; i++) {
            System.out.print(cmd[i]);
            System.out.println(expl[i]);
        }
    }
    
    public void adminHelp(){
        String[] cmd = commands();
        String[] expl = explanations();
        for(int i =0; i<totalCommands; i++){
            System.out.println(cmd[i]);
            System.out.println(expl[i]);
        }
    }
    
    public void newCart(String user) {
        System.out.println("This is the operation which generates a new shopping cart for you.");
        System.out.println("Press \"1\" to generate a new shopping cart, or \"0\" to exit back.");
        int input = sc.nextInt();
        boolean done = false;
        
        while(!done)
        if(input == 0){
            System.out.println("Exiting operation.");
            done = true;
        } else if (input == 1){
            int orderNum = order.newOrder(user);
            System.out.println("Congratulations, you now have a new shopping cart.");
            System.out.println("The cart id of your new cart is :" + orderNum + ".");
            System.out.println("Please try to remember it, as you will need it later.");
            System.out.println("You can always check it by using the cart-operation.");
            done = true;
        } else {
            help.invalidCommand();
        }
        
    }

    // operation which narrows down the users search, and calls for search operation which contacts the database
    public void search(){
        System.out.println("This is the search operation of Divari Database.");
        System.out.println("Please use the following commands to narrow down the methods of search.");
        System.out.println("\"1\" to search the items by author.");
        System.out.println("\"2\" to search the items by title.");
        System.out.println("\"3\" to search the items by genre.(use the command \"33\" to list all possible genres)");
        System.out.println("\"4\" to search the items by type. (use the command \"44\" to list all types.)");
        System.out.println("\"5\" to search the items by releaseyear. (use the command \"55\" to list all years)");
        System.out.println("\"0\" to exit the searching operation.");
        help.illegalCharacters();
        int narrowing = sc.nextInt();
        
        Scanner xd = new Scanner(System.in);
        if(narrowing == 0){
            System.out.println("\tExiting the search-operation.\n");
        } else if (narrowing == 1){
            System.out.println("Please enter the name or part of the authors name you wish to search for:");
            String input = xd.nextLine();
            if(help.check(input)){
                search(1, input);
            } else {
                help.invalidSearch();
            }
        } else if (narrowing == 2){
            System.out.println("Please enter the name or part of the title you wish to search for:");
            String input = xd.nextLine();
            if(help.check(input)){
                search(2, input);
            }else {
                help.invalidSearch();
            }
        } else if (narrowing == 3){
            System.out.println("Please enter the name or part of the genres name you wish to search for:");
            String input = xd.nextLine();
            if(help.check(input)){
                search(3, input);
            }else {
                help.invalidSearch();
            }
        } else if (narrowing == 4){
            System.out.println("Please enter the name or part of the types name you wish to search for:");
            String input = xd.nextLine();
            if(help.check(input)){
                search(4, input);
            }else {
                help.invalidSearch();
            }
        } else if (narrowing == 5){
            System.out.println("Please enter the year or part of the one you wish to search for:");
            String input = xd.nextLine();
            if(help.check(input)){
                search(5, input);
            }else {
                help.invalidSearch();
            }
        } else {
            System.out.println("\tInvalid input, please enter a number to narrow down your search.\n");
        }
        System.out.println("Returning to main menu. Use \"help\"  to see all the commands.");
    }
    
    public void add(String username){
        System.out.println("\nThis is the add-to-cart operation in Divari Database.");
        System.out.println("It works in 4 steps: 1. insert cart to add into, 2. search, 3. choose item, 4 confirm\n");
        System.out.println("1. Please insert the id (number) of the shopping cart you wish to add items into.");
        // customers current shopping carts order_id
        int orderNum = sc.nextInt();
        boolean shopping = order.checkCart(username, orderNum);
        while(shopping){
            Scanner xd = new Scanner(System.in);
            System.out.println("\n2. Search for the book by using its author or name.");
            help.illegalCharacters();
            System.out.println("You can exit the add-operation by entering \"0\" as search value.");
            String input = xd.nextLine();
            if(help.check(input)){  // checking the input for illegal characters
                if("0".equals(input)){
                    shopping = false;
                    System.out.println("Exiting add-operation.\n");
                } else {
                    String searchTerms = "SELECT DISTINCT k_nimi, isbn FROM keskus.Kirja "
                            + "WHERE (tekija LIKE '%" + input + "%') "
                            + "OR (k_nimi LIKE '%" + input + "%')";
                    String itemInformation = "SELECT DISTINCT teos_id, hinta, tila, paino "
                            +"FROM keskus.Teos "
                            +"WHERE isbn LIKE ?";
                    try{
                        // this statement finds the correct book
                        PreparedStatement book = con.prepareStatement(searchTerms);
                        ResultSet rsBook = book.executeQuery();
                        while(rsBook.next()){
                            // this statement selects the information of the book by isbn
                            PreparedStatement item = con.prepareStatement(itemInformation);
                            item.setString(1, rsBook.getString("isbn"));
                            ResultSet rsItem = item.executeQuery();
                            while(rsItem.next()){
                                String rvalue = "|book name: " + rsBook.getString("k_nimi") + " |item_id: " + rsItem.getInt(1) + 
                                        " |price: " + rsItem.getBigDecimal(2) + " |item state: " + rsItem.getBoolean(3) + 
                                        " |item weight: " + rsItem.getInt(4);
                                System.out.println(rvalue);
                            }
                        }
                    }catch (SQLException e){
                        help.SQLErrormessage(e);
                        shopping = false;
                    }
                    System.out.println("\n3. Please enter the item_id (number) you wish to add into your cart: ");
                    System.out.println("If the items state is false, then the item is not currently available.");
                    System.out.println("To cancel the adding, use \"0\" to exit the search.");
                    int itemId = xd.nextInt();
                    String src = "SELECT k_nimi, hinta, paino, tila FROM keskus.kirjat "
                            + "WHERE teos_id = '" + itemId + "'";
                    boolean freeBook = true;
                    if(itemId != 0) {
                        try{
                            Statement id = con.createStatement();
                            ResultSet rsId = id.executeQuery(src);
                            while(rsId.next()){
                                String rvalue = "|book name: " + rsId.getString("k_nimi") + 
                                        " |price: " + rsId.getBigDecimal("hinta") +
                                        " |weight: " + rsId.getInt("paino");
                                System.out.println(rvalue);
                                freeBook = rsId.getBoolean("tila");
                            }
                        }catch (SQLException e){
                            help.SQLErrormessage(e);
                            shopping = false;
                        }
                        if(freeBook){
                            System.out.println("\n4. Press \"1\" to add this item to your shopping cart");
                            System.out.println("Or Press \"0\" to discard the item and go back to searching items.");
                            int confirmation = xd.nextInt();
                            if(confirmation  == 1){
                                order.addItem(orderNum, itemId);
                                order.update(orderNum);
                            } else if (confirmation == 0) {
                                System.out.println("\t Returning to search.\n");
                            } else {
                                help.invalidCommand();
                            }
                        } else {
                            System.out.println("\tBook currently inavailable. Please choose another one.");
                        }
                    } else {
                        System.out.println("\tReturning to search.\n");
                    }
                }
            } else {
                help.invalidSearch();
            }
        } System.out.println("\tInvalid cart, exiting add-operation.");
    }
    
    public void remove(){
        System.out.println("\nThis is the removing operation for Divari Database.");
        System.out.println("Please enter the cart you wish to modify: ");
        System.out.println("Or press \"0\" to exit back to managing your shopping cart");
        int cart = sc.nextInt();
        boolean removing = true;
        while(removing) {
            System.out.println("Please enter the item_id of the item you wish to remove.");
            System.out.println("Or press \"0\" to exit back.");
            int item = sc.nextInt();
            if(item == 0){
                System.out.println("\t\tRemove-operation exited.");
                removing = false;
            } else {
                boolean success = order.remove(cart, item);
                if(success){
                    System.out.println("Remove succesfull.");
                    order.update(cart);
                } else {
                    System.out.println("Item removal unsuccessfull, check the values you entered.");
                }
            }
        }
    }
    
    public void cart(String user){
        String print = "SELECT tilaus_id FROM keskus.Tilaus Where tilaaja LIKE '"
                + user + "' AND myyty = FALSE";
        try{
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(print);
            System.out.print("Currently unfinished shopping carts(id):");
            int i = 0;
            while(rs.next()){
                System.out.print(" " + rs.getInt("tilaus_id"));
                if(i > 0) {
                    System.out.println(",");
                }
                i++;
            }
            System.out.println(".");
        } catch (SQLException e) {
            help.SQLErrormessage(e);
        }
        System.out.println("Your shopping carts currently include following items: ");
        order.printItems(user);
    }
    
    public void cashout(String user){
        System.out.println("\nThis is the cashout operation for Divari Database.");
        System.out.println("\tPlease enter the order_id you wish to cashout:");
        int cartNumber = sc.nextInt();
        float orderPrice = order.price(cartNumber);
        order.shipping(cartNumber); // separates the order into multiple deliveries if needed
        float deliveryCost = delivery.cost(cartNumber);
        int deliveryAmount = delivery.amount(cartNumber);
        float totalCost = orderPrice + deliveryCost;
        System.out.println("\n The price of your cart is: " + orderPrice + " "
                + "and it will be shipped in " + deliveryAmount + " packets.");
        System.out.println("Total cost of your order is: " + totalCost + "e.");
        System.out.println("Proceed to paying by using \"1\", or decline shipping by using \"0\"");
        int input = sc.nextInt();
        if(input == 1){
            order.cashout(cartNumber);
            System.out.println("\tCart payed, you can continue shopping normally.");
        } else if (input == 0){
            System.out.println("\tExiting cashout-operation.\n");
            order.undoShipping(cartNumber);
        } else {
            help.invalidCommand();
        }
        
    }
    
    public void discard(String user){
        System.out.println("\nThis is the discard-operation of Divari Database.");
        System.out.println("It will fully delete any given order.");
        System.out.println("Please enter the number of your shopping cart you wish to delete.");
        System.out.println("You can exit the operation by using \"0\".");
        int cartId = sc.nextInt();
        System.out.println("Confirm discarding of cart#"+ cartId + " by pressing \"1\", cancel with \"0\"");
        int input = sc.nextInt();
        boolean exit = false;
        while(!exit){
            if(input == 0){
                System.out.println("Exiting discard-operation.");
                exit = true;
            } else if (input == 1){
                order.discard(cartId, user);
                System.out.println("Exiting discard-operation.");
                exit = true;
            } else {
                help.invalidCommand();
            }
        }
        
    }
    
    public void search(int index, String input){
        // index codes: 1 = author, 2 = title, 3 = genre, 4 = type, 5 = year
        String[] columns = columns();
        String temp = columns[index];
        input = input.replaceAll("_", " ");
        try{
            PreparedStatement search = con.prepareStatement("SELECT * FROM keskus.kirjat_lkm "
                    +"WHERE " + temp + " LIKE ?");
            search.setString(1, "%" + input + "%");
            ResultSet rs = search.executeQuery();
            
            System.out.println("\tFollowing items found with "+ columns[index] + " as "+input+":");
            while(rs.next()){
                String rvalue = "|author: " + rs.getString("tekija") + " |book name: "+ rs.getString("k_nimi") 
                        + " |type: " + rs.getString("tyyppi") + " |genre: " + rs.getString("genre") 
                        + " |price: " + rs.getBigDecimal("hinta") + " |copies: " + rs.getString("teokset_lkm");
                System.out.println(rvalue);
            }
        }catch (SQLException e){
            help.SQLErrormessage(e);
        }
    }
    
    public void query(){
        boolean done = false;
        while(!done){
            System.out.println("insert sql query, empty line means that query is done.");
            Scanner xd = new Scanner(System.in);
            String q = "";
            boolean read = true;
            while(read){
                String line = xd.nextLine();
                if(line.isEmpty()){
                    read = false;
                }
                q += line + "\n";
            }
            System.out.println("press 1 to execute, 2 to executequery, 3 to update or 0 to exit");
            int inp = sc.nextInt();
            
            try{
                Statement stmt = con.createStatement();
                if(inp == 0){
                    System.out.println("\texiting query.");
                    done = true;
                } else if (inp == 1) {
                    System.out.println("rvalue was: " + stmt.execute(q));
                } else if (inp == 2) {
                    ResultSet rs = stmt.executeQuery(q);
                    System.out.println("press 1 to print resultset, 0 to continue without printing");
                    int print = sc.nextInt();
                    if(print == 0){
                        System.out.println("\tcontinuing without printing.");
                    } else if (print == 1){
                        ResultSetMetaData rsmd = rs.getMetaData();
                        int c = rsmd.getColumnCount();
                        while(rs.next()){
                            for (int i = 0; i <= c; i++){
                                if(i > 0){
                                    String line = rs.getString(i);
                                    System.out.print(" |" + rsmd.getColumnName(i) + ":" + line);
                                }
                            }
                            System.out.println("");
                        }
                    }
                } else if (inp == 3) {
                    stmt.executeUpdate(q);
                    System.out.println("update executed.");
                }
            }catch (SQLException e){
                help.SQLErrormessage(e);
            }
        }
    }
    
    public void read(){
        System.out.println("give file name to read");
        String nimi = sc.nextLine();
        String q ="";
        File file = new File(q);
        try{
            Scanner qc = new Scanner(file);
            while(qc.hasNextLine()){
                q += qc.nextLine();
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
        System.out.println(q);
    }
    
    public void addItem(){
        boolean adding = true;
        while(adding){
            System.out.println("adding a new item, please enter an existing isbn");
            String isbn = sc.nextLine();
            System.out.println("please enter the items price.");
            String price = sc.nextLine();
            System.out.println("please enter the items weight.");
            int weight = sc.nextInt();

            int newId =0;
            String id = "SELECT teos_id, COUNT(teos_id) as amount FROM keskus.Teos";
            String insert = "INSERT INTO "
                    + "Teos(teos_id, hinta, paino, isbn) "
                    + "VALUES('" + newId + "','" + price + "','"
                    + weight + "','" + isbn + "')";
            try{
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(id);
                ResultSet amount = rs;
                while(amount.next()){
                    newId = amount.getInt("amount") + 1;
                }
                boolean free = false;
                boolean found = false;
                while(!free){
                    ResultSet check = rs;
                    while(check.next()){
                        if(newId == check.getInt("teos_id")){
                            found = true;
                        }
                    }
                    if(found){
                        newId++;
                    } else {
                        free = true;
                    }
                }
                stmt.executeUpdate(insert);
                System.out.println("item added into teos.");
        
            
            } catch (SQLException e){
                help.SQLErrormessage(e);
            }
            System.out.println("press 1 to add more items, 0 to exit.");
            int inp = sc.nextInt();
            if(inp == 0){
                adding = false;
                System.out.println("\texiting add.");
            }
        }

        
    }
    
    public void promote(){
        System.out.println("Enter username to promote to admin");
        String username = sc.nextLine();
        String q = "UPDATE keskus.Asiakas SET taso = 1 WHERE sposti = '" + username + "'";
        try{
            Statement stmt = con.createStatement();
            if(stmt.execute(q)){
                System.out.println("user promoted to admin.");
            }
        } catch (SQLException e) {
            help.SQLErrormessage(e);
        }
    }
    
    public void demote(){
        System.out.println("Enter username to demote from admin to normal user");
        String username = sc.nextLine();
        String q = "UPDATE keskus.Asiakas SET taso = 0 WHERE sposti = '" + username + "'";
        try{
            Statement stmt = con.createStatement();
            if(stmt.execute(q)){
                System.out.println("user demoted to normal user.");
            }
        } catch (SQLException e) {
            help.SQLErrormessage(e);
        }
    }   
    
    public void triggerTest(){
        // todistetaan triggerin ja päivitysoperaation toiminta.
        String before = "SELECT * FROM keskus.Kirjat WHERE isbn = '9789513244668'";
        String tst = "INSERT INTO haara.Teos VALUES('24051995','10.80', '27.60', '1234', TRUE, '9789513244668')";
        String after = "SELECT k_nimi, teos_id FROM keskus.Kirja, keskus.Teos "
                + "WHERE (keskus.Teos.isbn = '9789513244668') AND (keskus.Kirja.isbn = '9789513244668')";
        try{
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(before);
            System.out.println("executed " + before + "\nrvalue should not print.");
            System.out.print("\trvalue = ");
            while(rs.next()){
                System.out.println(rs.getString("k_nimi"));
            }
            System.out.println("");
            System.out.println("inserting a new item into haara, trigger should go bang");
            stmt.executeUpdate(tst);
            System.out.println("After trigger bang a new book and item should be found in keskus Kirja and Teos\n");
            rs = stmt.executeQuery(after);
            System.out.println("executing after, this time rvalue should include name and id.");
            System.out.print("rvalue = ");
            while(rs.next()){
                System.out.println("nimi: " + rs.getString("k_nimi") + ", id: " + rs.getInt("teos_id"));
            }
        }catch (SQLException e){
            help.SQLErrormessage(e);
        }
    }
    
    public void r2(){
        String koko = "SELECT count(isbn) as amount from keskus.kirjat";
        String info = "SELECT genre, hinta FROM keskus.kirjat";
        String prn = "SELECT * FROM keskus.kirjat WHERE tila = TRUE ORDER BY genre, k_nimi ASC";
        int size = 0;
        String[][] information;
        String[][] result;
        
        try{
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(koko);
            while(rs.next()){
                size = rs.getInt("amount");
            }
            information = new String[size][2];
            result = new String[size][3];
            rs = stmt.executeQuery(info);
            int j = 0;
            while(rs.next()){
                information[j][0] = rs.getString("genre");
                information[j][1] = rs.getString("hinta");
                j++;
            }
            int index = 0;
            int iterations = 0;
            boolean empty = false;
            while(!empty) {
                System.out.println(iterations + "iterations");
                String temp = information[index][0];
                BigDecimal price = new BigDecimal("0");
                int occurrences = 1;
                for(int i = 0; i < information.length; i++) {
                    if(information[i][0].equals(temp)){
                        occurrences++;
                        price = price.add(new BigDecimal(information[i][1]));
                        information[i][0] = "0";
                    }
                }
                String occ = "" + (occurrences -1);
                BigDecimal avg = price.divide(new BigDecimal(occ), 2);
                result[iterations][0] = temp;
                result[iterations][1] = price.toString();
                result[iterations][2] = avg.toString();
                boolean found = false;
                for(int q = 0; q < information.length; q++){
                    if(!information[q][0].equals("0")){
                        index = q;
                        iterations++;
                        found = true;
                        q = information.length;
                    }
                }
                if(!found){
                    empty = true;
                }
            }
            rs = stmt.executeQuery(prn);
            while(rs.next()){
                String rvalue = "|nimi: "  + rs.getString("k_nimi") + " |genre: " + rs.getString("genre")
                        + " |hinta: " + rs.getString("hinta");
                System.out.println(rvalue);
            }
            for(int i = 0; i <= iterations; i++){
                String rvalue = "Genren " + result[i][0] + " kokonaismyyntihinta = " + result[i][1] +
                        ", keskihinta = " + result[i][2] + ".";
                System.out.println(rvalue);
            }
        } catch (SQLException e) {
            help.SQLErrormessage(e);
        }
    }
}
