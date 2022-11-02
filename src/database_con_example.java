import java.sql.*;

public class database_con_example {
    public static void main(String[] args) {

        try {
            String url ="jdbc:mysql://restaurantegourmet.mysql.database.azure.com:3306/db1?useSSL=true&requireSSL=false";

            String user = "chefe@restaurantegourmet";
            String password = "fuiCOMPRARcebolas2021!";

            //Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, "chefe@restaurantegourmet", "fuiCOMPRARcebolas2021!");

            System.out.println("Conexão Feita");

            Statement st = con.createStatement();

            String s = "insert into mesa(id, nPessoas, reservado) values(3, 4, false)";

            st.execute(s);

            st.close();
            con.close();

            System.out.println("Big done");


        }catch(Exception e){
            System.out.println(e);
        }
    }
}
