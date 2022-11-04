import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;

public class database_con_example {
    public static void main(String[] args) {

        try {
            String url ="jdbc:mysql://restaurantegourmet.mysql.database.azure.com:3306/db1?useSSL=true&requireSSL=false";

            String user = "chefe@restaurantegourmet";
            String password = "fuiCOMPRARcebolas2021!";

            //Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, password);

            System.out.println("Conexão Feita");

            Statement st = con.createStatement();

            //String s = "insert into mesa(id, nPessoas, reservado) values(3, 4, false)";

            //Como mostrar um SELECT * FROM
            ResultSet rs = st.executeQuery("SELECT * FROM mesa");
            while (rs.next()) {
                System.out.println("Mesa ID: "+rs.getInt(1)+ " Número Pessoas: "+rs.getInt(2)+" Reservado: "+rs.getBoolean(3));
            }
            //st.execute(s);

            st.close();
            con.close();

            System.out.println("\nBig done");


        }catch(Exception e){
            System.out.println(e);
        }
    }
}
