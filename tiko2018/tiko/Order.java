/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package tiko;
import java.math.BigDecimal;
import java.sql.*;

/**
 *
 * @author aLppu
 */
public class Order {
    Help help = new Help();
    Connection con = help.cn();
    Delivery delivery = new Delivery();
    
    public int newOrder(String userName) {
        try{
            // used to check if the newOrderNumber is in use
            PreparedStatement numberCheck = con.prepareStatement("SELECT * FROM keskus.Tilaus WHERE tilaus_id = ? ");
            // used to make the new order with given values
            PreparedStatement orderTable = con.prepareStatement("INSERT INTO "
                    + "keskus.Tilaus(tilaus_id, tilaaja, hinta, paino, myyty) "
                    + "VALUES(?,?,0,0,FALSE)");
            
            Statement orderNumber = con.createStatement(); 
            ResultSet rsOn = orderNumber.executeQuery("SELECT COUNT(tilaus_id) as current_amount FROM keskus.Tilaus");
            int newOrderNumber = 0;
            while(rsOn.next()){
                newOrderNumber = rsOn.getInt("current_amount");
            }
            
            boolean free = false;
            while(!free){   // until the a free ordernumber is found
                newOrderNumber++;   // ordernumber grows by 1 every iteration
                numberCheck.setInt(1, newOrderNumber);
                ResultSet rsNc = numberCheck.executeQuery();
                if(!rsNc.next()){
                    orderTable.setInt(1, newOrderNumber);
                    orderTable.setString(2, userName);
                    orderTable.executeUpdate();
                    return newOrderNumber;
                }
            }
        } catch (SQLException e){
            help.SQLErrormessage(e);
        }
        System.out.println("\n ### Order creating failed. ###");
        return 0;
    }
    
    public void addItem(int orderNumber, int itemId){
        // searching for the correct book
        String searchValues = "SELECT hinta, paino "
                    +"FROM keskus.Teos WHERE teos_id = ?";
        // adding the item into the order_items table
        String addItem = "INSERT INTO "
                    +"keskus.Tilaustiedot(tilaus_id, teos_id, hinta, paino) "
                    +"VALUES('"+ orderNumber + "','" + itemId + "',?,?)";
        
        // updating the item in Item-table
        String setState = "UPDATE keskus.Teos SET tila = FALSE "
                + "WHERE teos_id = '" + itemId + "'";
        String price = "";
        int weight = 0;
        
        try{
            // prepare the searching statement which gives the adding statement vallues to add
            PreparedStatement search = con.prepareStatement(searchValues);
            search.setInt(1, itemId);   // search for the item_id customer wishes to add to cart
            ResultSet rsSearch = search.executeQuery();
            while(rsSearch.next()){
                price = rsSearch.getString("hinta");
                weight = rsSearch.getInt("paino");
            }
            //prepare the adding statement
            PreparedStatement newItem = con.prepareStatement(addItem);
            newItem.setBigDecimal(1, new BigDecimal(price));
            newItem.setInt(2, weight);
            newItem.executeUpdate();
            
            Statement stmt = con.createStatement();
            stmt.executeUpdate(setState);

            System.out.println("\tItem added to cart successfully.");
            
        }catch (SQLException e){
            help.SQLErrormessage(e);
        }
    }
    
    public boolean checkCart(String user, int cartId){
        String q = "SELECT tilaus_id, myyty FROM keskus.Tilaus WHERE tilaaja = '" + user + "'";
        boolean rvalue = false;
        try{
            Statement stmt = con.createStatement();
            ResultSet rs  = stmt.executeQuery(q);
            while(rs.next()){
                if((rs.getInt("tilaus_id") == cartId) && (rs.getBoolean("myyty") == false)){
                    rvalue = true;
                }
            }
        } catch (SQLException e) {
            help.SQLErrormessage(e);
        }
        return rvalue;
    }
    
    public void update(int cartId){
        String[] priceStorage;
        int weight = 0;
        int sz = 0;
        String size = "SELECT COUNT(*)as size FROM keskus.Tilaustiedot WHERE tilaus_id = " + cartId;
        String info = "SELECT hinta, paino FROM keskus.Tilaustiedot WHERE tilaus_id = " + cartId;
        String upd = "UPDATE keskus.Tilaus "
                + "SET hinta = ?, paino = ? WHERE tilaus_id = " + cartId;

        try{
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(size);
            while(rs.next()){
                sz = rs.getInt("size");
            }
            priceStorage = new String[sz];
            rs = stmt.executeQuery(info);
            int q = 0;
            while(rs.next()){
                priceStorage[q] = rs.getString("hinta");
                weight += rs.getInt("paino");
                q++;
            }
            BigDecimal price = new BigDecimal(priceStorage[0]);
            for(int i = 1; i < priceStorage.length; i++){
                price = price.add(new BigDecimal(priceStorage[i]));
            }
            PreparedStatement ps = con.prepareStatement(upd);
            ps.setBigDecimal(1, price);
            ps.setInt(2, weight);
            ps.executeUpdate();
            
        } catch (SQLException e){
            help.SQLErrormessage(e);
        }
    }
    
    public void printItems(String user){
        String userCart = "SELECT tt.tilaus_id, k.k_nimi, te.teos_id, te.hinta, te.paino "
        +"FROM keskus.Tilaustiedot tt, keskus.Teos te, keskus.Tilaus ti, keskus.Kirja k "
        +"WHERE tt.tilaus_id = ti.tilaus_id AND tt.teos_id = te.teos_id AND k.isbn = te.isbn "
        +"AND ti.tilaaja LIKE ? AND tt.tilausvalmis != ?";
        try{
            // statement is used to search for the users unfinished shoppingcarts
            PreparedStatement cart = con.prepareStatement(userCart);
            cart.setString(1, user);
            cart.setInt(2, 2);
            ResultSet rsCart = cart.executeQuery();
            while(rsCart.next()){
                String rvalue = "|order_id: " + rsCart.getInt(1) + " |book name: " + rsCart.getString(2)
                        + " |item_id: " + rsCart.getInt(3) + " |item price: " + rsCart.getBigDecimal(4)
                        + " |item weight: " + rsCart.getInt(5);
                System.out.println(rvalue);
            }
        }catch (SQLException e) {
            help.SQLErrormessage(e);
        }
    }
    
    public void shipping(int cartId){
        String view = "CREATE OR REPLACE VIEW Active_order AS "
                + "SELECT * FROM keskus.Tilaustiedot "
                + "WHERE tilaus_id = "+ cartId +" AND tilausvalmis = 0";
        String orderValues = "SELECT teos_id, paino "
                + "FROM Active_order";
        String countB = "SELECT COUNT(teos_id) as books FROM Active_order";
        int[][] information;
        int booksAmount = 0;

        try{
            Statement stmt = con.createStatement();
            boolean done = false;
            while(!done){
                stmt.executeUpdate(view);           
                ResultSet rsB = stmt.executeQuery(countB);
                while(rsB.next()){
                    booksAmount = rsB.getInt("books");
                }
                // updating the size of information table
                information = new int[booksAmount][2];
                ResultSet rsV = stmt.executeQuery(orderValues);
                int j = 0;
                int totalWeight = 0;
                while (rsV.next()){ // inserting the weight and item_id into information table
                    information[j][0] = rsV.getInt("paino");
                    information[j][1] = rsV.getInt("teos_id");
                    j++;
                    totalWeight += rsV.getInt("paino");
                }

                int dnum = delivery.dNum();
                if(totalWeight <= 2000){    // if the whole cart can be sent in one delivery
                    for(int i = 0; i < information.length; i++){
                        delivery.addInto(dnum, cartId, information[i][1]);
                    }
                    done = true;    // exit loop
                    stmt.executeUpdate("DROP VIEW Active_order"); // drop view
                }else {
                    totalWeight = 0;    // reset the weight to use it in new delivery
                    delivery.addInto(dnum, cartId, information[0][1]);  // add the first value in table to delivery
                    totalWeight += information[0][0];   // update delivery weight
                    for(int i = 1; i < information.length; i++){    // check if any other item can be added
                        if (totalWeight + information[i][0] <= 2000){
                            delivery.addInto(dnum, cartId, information[i][1]);  // add items item_id into delivery
                            totalWeight += information[i][0];   // update delivery weight
                        }
                    }
                }
            }
            
        } catch (SQLException e){
            help.SQLErrormessage(e);
        }
    }
    
    public float price(int cartId){
        String orderPrice = "SELECT hinta FROM keskus.Tilaus WHERE tilaus_id = " + cartId;
        float price = 0;
        
        try{
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(orderPrice);
            while(rs.next()){
                String temp = (rs.getString("hinta"));
                price = Float.valueOf(temp);
            }
        } catch (SQLException e){
            help.SQLErrormessage(e);
        }
        return price;
    }
    
    public void cashout(int cartId){
        String upd = "UPDATE keskus.Tilaustiedot SET tilausvalmis = 2 WHERE tilaus_id = " + cartId;
        String upt = "UPDATE keskus.Tilaus SET myyty = TRUE WHERE tilaus_id = " + cartId;
        try{
            Statement stmt = con.createStatement();
            stmt.executeUpdate(upd);
            stmt.executeUpdate(upt);
            
        } catch (SQLException e){
            help.SQLErrormessage(e);
        }
    }
    
    public boolean undoShipping(int cartId){
        String del = "DELETE FROM keskus.Lahetys WHERE tilaus_id = " + cartId;
        String upd = "UPDATE keskus.Tilaustiedot SET tilausvalmis = 0 WHERE tilaus_id = " + cartId;
        boolean rvalue = false;
        boolean value1 = false;
        boolean value2 = false;
        
        try{
            Statement stmt = con.createStatement();
            value1 = stmt.execute(del);
            if(stmt.executeUpdate(upd) != 0){
                value2 = true;
            }
            
        } catch (SQLException e){
            help.SQLErrormessage(e);
        }
        if(value1 && value2){
            rvalue = true;
        }
        return rvalue;
    }
    
    public boolean remove(int cartId, int itemId){ // set item tila true paikassa teos
        String rmv = "DELETE FROM keskus.Tilaustiedot WHERE tilaus_id = " + cartId
                + " AND teos_id = " + itemId;
        String upd = "UPDATE keskus.Teos SET tila = TRUE WHERE teos_id = " + itemId;
        boolean rvalue = false;
        boolean value1 = false;
        boolean value2 = false;
                
        try{
            Statement stmt = con.createStatement();
            value1 = stmt.execute(rmv);
            value2 = stmt.execute(upd);
        } catch (SQLException e){
            help.SQLErrormessage(e);
        }
        if(!value1 && !value2){
            rvalue = true;
        }
        return rvalue;
    }
    
    public void discard(int cartId, String user){
        String info = "SELECT teos_id FROM keskus.Tilaustiedot WHERE tilaus_id = " + cartId;
        String pre = "UPDATE keskus.Teos SET tila = TRUE WHERE teos_id = ?";
        String del = "DELETE FROM keskus.Tilaustiedot WHERE tilaus_id = " + cartId;
        String del2 = "DELETE FROM keskus.Tilaus WHERE tilaus_id = " + cartId;
        String check = "SELECT tilaaja FROM keskus.Tilaus WHERE tilaus_id = " + cartId;
        
        try{
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(check);
            boolean owner = false;
            while(rs.next()){
                if(rs.getString("tilaaja").equals(user)){
                    owner = true;
                }
            }
            if(owner) {
                PreparedStatement prstmt = con.prepareStatement(pre);
                rs = stmt.executeQuery(info);
                while(rs.next()){
                    prstmt.setInt(1, rs.getInt("teos_id"));
                    prstmt.executeUpdate();
                }
                stmt.execute(del);
                stmt.execute(del2);
                System.out.println("Discard successful.");
                
            } else {
                System.out.println("Discard unsuccessful, you werent the owner of the cart.");
            }
        } catch (SQLException e) {
            help.SQLErrormessage(e);
        }
        
    }
}
