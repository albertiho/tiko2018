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
public class Delivery {
    Help help = new Help();
    Connection con = help.cn();
    
    // creates a new deliverynumber that is free
    public int dNum(){
        String cnt = "SELECT COUNT(lahetys_id) as amount FROM keskus.Lahetys";
        String chk = "SELECT lahetys_id FROM keskus.Lahetys";
        int dnum = Integer.MAX_VALUE;   // new delivery id

        try{
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(cnt);
            while(rs.next()){
                dnum = rs.getInt("amount") + 1;
            }
            // checking if the dnum is already in use
            boolean free = false;
            while(!free){
                boolean found = false;
                rs = stmt.executeQuery(chk);
                while(rs.next() && !found){
                    if (dnum == rs.getInt("lahetys_id")){
                        found = true;   // imply that the number was found and exit the loop
                    }
                }
                if(!rs.next() && !found){  // if the dnum wasnt found in the list of delivery_id's
                    free = true;
                } else {
                    dnum++; // raise the dnum by 1 to keep the dnums low
                }
            }
            

        } catch (SQLException e) {
            help.SQLErrormessage(e);
        }
        return dnum;
    }
    
    
    public void addInto(int dNum, int cart, int item_id){
        String addN = "INSERT INTO "
                + "keskus.Lahetys(lahetys_id, tilaus_id, hinta, paino )"
                + "VALUES(" + dNum + "," + cart + ", 0, 0)";
        String addE = "UPDATE keskus.Lahetys "
                + "SET hinta = ?, paino = ?, pvm = ?"
                + "WHERE lahetys_id = " + dNum;
        String search = "SELECT lahetys_id, hinta, paino FROM keskus.Lahetys";
        String bInfo = "SELECT hinta, paino FROM keskus.Teos WHERE teos_id = " + item_id;
        String upd = "UPDATE keskus.Tilaustiedot SET tilausvalmis = 1 WHERE teos_id = " + item_id;
        String temp = "0";
        int weight = 0;
        String tempb = "0";
        int weightb = 0;
        
        try{
            PreparedStatement addExisting = con.prepareStatement(addE);
            Statement stmt = con.createStatement();
            ResultSet rsE = stmt.executeQuery(search);
            boolean found = false;
            while(rsE.next()){    
                if(dNum == rsE.getInt("lahetys_id")){
                    temp = rsE.getString("hinta");
                    weight = rsE.getInt("paino");
                    found = true;
                }  
            } 
            if(!found){
                stmt.executeUpdate(addN);
            }
            ResultSet rsB = stmt.executeQuery(bInfo);
            while(rsB.next()){
                tempb = rsB.getString("hinta");
                weightb = rsB.getInt("paino");
            }
            BigDecimal price = new BigDecimal(temp);
            price = price.add(new BigDecimal(tempb));
            weight += weightb;
            
            addExisting.setBigDecimal(1, price);
            addExisting.setInt(2, weight);
            addExisting.setDate(3,  new java.sql.Date(System.currentTimeMillis()));
            addExisting.executeUpdate();
            stmt.executeUpdate(upd);
        } catch (SQLException e) {
            help.SQLErrormessage(e);
        } 
    }
    
    // operation which returns the total deliverycosts
    public float cost(int cart){
        String d = "SELECT paino FROM keskus.Lahetys WHERE tilaus_id = " + cart;
        float price = 0;
        try{
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(d);
            while(rs.next()){
                int weight = rs.getInt("paino");
                if(weight > 1000){
                    price += 14.00f;
                } else if (weight > 500){
                    price += 8.40f;
                } else if (weight > 250){
                    price += 5.60f;
                } else if (weight > 100){
                    price += 2.80f;
                } else if (weight > 50){
                    price += 2.10f;
                } else {
                    price += 1.40f;
                }
            }
        } catch (SQLException e){
            help.SQLErrormessage(e);
        }
        
        return price;
    }
    
    // operation which returns the amount of deliveries the order is split into
    public int amount(int cart){
        String a = "SELECT COUNT(lahetys_id) as amount FROM keskus.Lahetys "
                + "WHERE tilaus_id = " + cart;
        int rvalue = 0;
        try{
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(a);
            while(rs.next()){
                rvalue = rs.getInt("amount");
            }
        } catch (SQLException e) {
            help.SQLErrormessage(e);
        }
        return rvalue;
    }
}
