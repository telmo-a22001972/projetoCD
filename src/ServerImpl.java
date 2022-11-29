import java.rmi.*;
import java.rmi.server.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.sql.*;
import java.util.HashMap;

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

        System.out.println("Mesa entry updated");
        //Não existe reserva
        return true;
      }

    } catch(Exception e){
      System.out.println(e);
      return false;
    }
  }

  //TODO
  //Acho que esta função tem de ser divida em duas, uma, Função 1, é a que cancela e que recebe só o ID da mesa
  //e outra, Função 2,  que recebe a data e o horario e retorna que a reserva existe ou não.

  //A função 2 é chamada primeiro, e ou retorna null, ou retorna a reserva e depois passa o ID da mesa
  //dessa reserva para a função 1, onde a reserva vai ser apagada.


  /*
    Ideia do Luís

    - Não apagar o registo da reserva, mas usar um status para a reserva
      Onde diz 'Cancelada', 'Fechada', 'Aberta'
    - Pois esses registos mesmo estando como cancelados podem ser uteis para vários usos da empresa

    - Faz sentido.
   */
  public boolean cancelarMesa(String idMesa, String data, String horario) throws RemoteException {
    //Fazer um select * from para ver se a reserva existe


    //Pedir confirmação ao utilizador

    //Executar query para apagar registo na reserva
    /*
      DELETE FROM employees
      WHERE last_name = 'Johnson'
      AND employee_id >= 80;
     */

    return true;
  }

  public String listarMesas(String data) throws RemoteException {
    Credentials cred = new Credentials();
    HashMap<Integer,String> mesasReservadas= new HashMap<Integer,String>() ;


    String result = "";
    System.out.println("Pedido listar mesas recebido\n");
    try{

      Connection con = DriverManager.getConnection(cred.getUrl(), cred.getUser(), cred.getPassword());
      Statement st = con.createStatement();

      // BUSCAR MESAS COM RESERVA
      String s = "select mesa_id, horario from reserva where dia = " + "\"" + data+ "\"";
      st.execute(s);

      ResultSet rs = st.executeQuery(s);

      while (rs.next()) {


        mesasReservadas.put(rs.getInt(1),rs.getString(2));
        //result += "Mesa ID: "+rs.getInt(1)+ " Horário: "+rs.getString(2)+" \n";

      }

      s = "select * from mesa";

      rs = st.executeQuery(s);

      while (rs.next()) {
        int idMesa = rs.getInt(1);

        if(mesasReservadas.containsKey(rs.getInt(1))){
          if(mesasReservadas.get(idMesa) == "almoço"){
            result += "Mesa ID: "+rs.getInt(1)+ "Numero de Pessoas: "+rs.getInt(2)+ " Reservado para " + mesasReservadas.get(idMesa) + " \n";
          }


        }else{
          result += "Mesa ID: "+rs.getInt(1)+ "Numero de Pessoas: "+rs.getInt(2)+ " Dispnível" + " \n";
        }

      }

      st.close();
      con.close();

    } catch(Exception e){
      System.out.println(e);
    }
    System.out.println("Resposta ao pedido listar mesas efetuada");
    return result;

  }


}
