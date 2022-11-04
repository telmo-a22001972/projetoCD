import java.rmi.*;
import java.rmi.server.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.sql.*;

public class ServerImpl extends UnicastRemoteObject
  implements ServerIntf {

  public ServerImpl() throws RemoteException {
  }

  public boolean reservarMesa(String idMesa, String data, String horario) throws RemoteException {
    Credentials cred = new Credentials();
    String result = "";
    System.out.println("Pedido reservar mesas recebido\n");

    try{

      Connection con = DriverManager.getConnection(cred.getUrl(), cred.getUser(), cred.getPassword());

      PreparedStatement query = con.prepareStatement("SELECT * FROM reserva WHERE mesa_id = ? AND horario = ? AND dia = ?");
      query.setInt(1, Integer.parseInt(idMesa));
      query.setString(2, horario);
      query.setDate(3, Date.valueOf(data));

      Statement st = con.createStatement();
      ResultSet rs = query.executeQuery();

      if(rs.next()){
        //existe
        System.out.println("Registo Encontrado\n");
        System.out.println("Resposta ao pedido reservar mesa efetuada");

        //Já existe reserva
        return false;
      }else {
        System.out.println("Nao existe registo\n");
        System.out.println("Resposta ao pedido reservar mesa efetuada");

        String s = "insert into reserva(dia, horario, pessoa_id, mesa_id) values(" + "\"" + data+ "\"" + "," + "\""+ horario + "\"" + ", 1," + idMesa + ");";

        st.execute(s);


        //Não existe reserva
        return true;
      }

    } catch(Exception e){
      System.out.println(e);
      return false;
    }
  }

  public boolean cancelarMesa(String idMesa, String data, String horario) throws RemoteException {
    return true;
  }

  public ArrayList<String> listarMesas() throws RemoteException {
    return new ArrayList<>();
  }


}
