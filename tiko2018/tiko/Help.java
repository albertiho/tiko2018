/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package tiko;
import java.sql.*;

/**
 *
 * @author aLppu
 */
public class Help {
    // operation which connects to the database when needed
    public Connection cn(){
          // private static final String AJURI = "org.postgresql.Driver";
    String PROTOKOLLA = "jdbc:postgresql:";
    String PALVELIN = "localhost";
    int PORTTI = 5432;
    String TIETOKANTA = "postgres";  // tähän oma käyttäjätunnus
    String KAYTTAJA = "postgres";  // tähän oma käyttäjätunnus
    String SALASANA = "salasana";  // tähän tietokannan salasana
    
    Connection con = null;
    try {
      con = DriverManager.getConnection(PROTOKOLLA + "//" + PALVELIN + ":" + PORTTI + "/" + TIETOKANTA, KAYTTAJA, SALASANA);
    } catch (SQLException e) {
        SQLErrormessage(e);    
    }
        return con;
    }
    
    // operation which is used to check that no illegal characters are used while making a new user
    public boolean check(String c){
        char[] illegalCharacters = new char[3];
        illegalCharacters[0] = '\'';
        illegalCharacters[1] = '\"';
        illegalCharacters[2] = ' ';
        
        for(int i = 0; i < c.length(); i++){
            for (int j = 0; j < illegalCharacters.length; j++) {
                if(c.charAt(i) == illegalCharacters[j]){
                    return false;
                }
            }
        }
        return true;
    }
    
    // operation which is used to print the illegal characters
    public void illegalCharacters(){
        System.out.println("(While using the operation, characters \''\', \'\"\' and \' \' are not allowed.");
        System.out.println("You can use \'_\' to replace \' \' if needed.)");
    }
    
    public void invalidSearch(){
        System.out.println("\tInvalid input, please re-enter your searchterms.");
    }
    public void invalidCommand(){
        System.out.println("\tInvalid input, please use the commands given above.");
    }
    public void SQLErrormessage(Exception e){
        System.out.println("The following error occurred: " + e.getMessage());
    }
    
    public void reset(){
        String truncate = "TRUNCATE TABLE keskus.Lahetys, keskus.Tilaustiedot, keskus.Tilaus, keskus.Teos, "
                + "keskus.Kirja, haara.Teos, haara.Kirja";
        String insertKeskus = "INSERT INTO keskus.Kirja VALUES(\n" +
                    "9789510431122, 'Mika Waltari', 'Turms kuolematon','Dekkari','Romaani',1995);\n" +
                    "INSERT INTO keskus.Kirja VALUES(\n" +
                    "9789510320990, 'Mika Waltari', 'Komisario Palmun erehdys','Dekkari','Romaani',1940);\n" +
                    "INSERT INTO keskus.Kirja VALUES(\n" +
                    "9789515430670, 'Madeleine Brent', 'Elektran tytär','Romantiikka','Romaani',1986);\n" +
                    "INSERT INTO keskus.Kirja VALUES(\n" +
                    "9156381451, 'Madeleine Brent', 'Tuulentavoittelijan morsian','Romantiikka','Romaani',1978);\n" +
                    "INSERT INTO keskus.Kirja VALUES(\n" +
                    "9789510396230, 'Dale Carnegien', 'Miten saan ystäviä, menestystä, vaikutusvaltaa','Opas','Tietokirja',1939);\n" +
                    "INSERT INTO keskus.Kirja VALUES(\n" +
                    "'951-9201-78-5', 'Shelton Gilbert', 'Friikkilän pojat Mexicossa','Huumori','Sarjakuva',1989);" +
                    "INSERT INTO keskus.Teos VALUES(\n" +
                    "'1', '20.10', TRUE,'620','9789510431122');\n" +
                    "INSERT INTO keskus.Teos VALUES(\n" +
                    "'2', '20.10', TRUE,'620','9789510431122');\n" +
                    "INSERT INTO keskus.Teos VALUES(\n" +
                    "'3', '20.10', TRUE,'620','9789510431122');\n" +
                    "INSERT INTO keskus.Teos VALUES(\n" +
                    "'14', '20.10', TRUE,'620','9789510431122');\n" +
                    "INSERT INTO keskus.Teos VALUES(\n" +
                    "'15', '20.10', TRUE,'620','9789510431122');\n" +
                    "INSERT INTO keskus.Teos VALUES(\n" +
                    "'4', '24.90', TRUE,'273','9789510320990');\n" +
                    "INSERT INTO keskus.Teos VALUES(\n" +
                    "'5', '24.90', TRUE,'273','9789510320990');\n" +
                    "INSERT INTO keskus.Teos VALUES(\n" +
                    "'6', '22.30', TRUE,'420','9789515430670');\n" +
                    "INSERT INTO keskus.Teos VALUES(\n" +
                    "'7', '22.30', TRUE,'420','9789515430670');\n" +
                    "INSERT INTO keskus.Teos VALUES(\n" +
                    "'13', '22.30', TRUE,'420','9789515430670');\n" +
                    "INSERT INTO keskus.Teos VALUES(\n" +
                    "'8', '21.90', TRUE,'390','9156381451');\n" +
                    "INSERT INTO keskus.Teos VALUES(\n" +
                    "'9', '21.90', TRUE,'390','9156381451');\n" +
                    "INSERT INTO keskus.Teos VALUES(\n" +
                    "'10', '10.50', TRUE,'192','9789510396230');\n" +
                    "INSERT INTO keskus.Teos VALUES(\n" +
                    "'11', '10.50', TRUE,'192','9789510396230');  \n" +
                    "INSERT INTO keskus.Teos VALUES(\n" +
                    "'12', '25.00', TRUE,'210','951-9201-78-5');";
                    
        String insertHaara = "INSERT INTO haara.Kirja VALUES(\n" +
                    "9789513244668, 'Don Rosa', 'Sammon salaisuus', 'Seikkailu','Sarjakuva',1999);";
        try{
            Connection con = cn();
            Statement stmt = con.createStatement();
            stmt.executeUpdate(truncate);
            stmt.executeUpdate(insertKeskus);
            stmt.executeUpdate(insertHaara);
        } catch (SQLException e) {
            SQLErrormessage(e);
        }
    }

}
