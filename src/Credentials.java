public class Credentials {
    private String user = "chefe@restaurantegourmet";
    private String password = "fuiCOMPRARcebolas2021!";

    private String url = "jdbc:mysql://restaurantegourmet.mysql.database.azure.com:3306/db1?useSSL=true&requireSSL=false";


    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getUrl() {
        return url;
    }
}
