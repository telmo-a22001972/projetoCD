import java.rmi.*;
import java.rmi.server.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ServerImpl extends UnicastRemoteObject
  implements ServerIntf {

  public ServerImpl() throws RemoteException {
  }

  public boolean reservarMesa(String idMesa, String data, String horario, int idPessoa) throws RemoteException {
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

      if(rs.next() && !rs.getBoolean(6)){
        //existe
        System.out.println("Registo Encontrado\n");
        System.out.println("Resposta ao pedido reservar mesa efetuada");

        //Já existe reserva
        return false;
      }else {
        System.out.println("Nao existe registo\n");
        System.out.println("Resposta ao pedido reservar mesa efetuada");

        String s = "insert into reserva(dia, horario, pessoa_id, mesa_id) values(" + "\"" + data+ "\"" + "," + "\""+ horario + "\"" + "," + idPessoa + "," + idMesa + ");";
        st.execute(s);

        /*if(rs.getBoolean(6)){
          System.out.println("entrei aqui");
          s = "update reserva set cancelado = 0 where dia = " + "\"" + data+ "\"" + " and horario = " + "\"" + horario+ "\"" + "and mesa_id = " + idMesa;
          st.execute(s);
        }*/

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
  public String cancelarMesa(String idMesa, String data, String horario) throws RemoteException {
    System.out.println("Pedido cancelar mesa recebido\n");
    String result;
    Credentials cred = new Credentials();
    try{

      Connection con = DriverManager.getConnection(cred.getUrl(), cred.getUser(), cred.getPassword());
      Statement st = con.createStatement();
      // BUSCAR MESAS COM RESERVA

      String s = "select * from reserva where dia = " + "\"" + data+ "\"" + " and horario = " + "\"" + horario+ "\"" + "and mesa_id = " + idMesa;

      ResultSet rs = st.executeQuery(s);

      while (rs.next()) {
        s = "update reserva set cancelado = 1 where dia = " + "\"" + data+ "\"" + " and horario = " + "\"" + horario+ "\"" + "and mesa_id = " + idMesa;
        st.execute(s);
        return "Reserva cancelada com sucesso\n";
      }

      st.close();
      con.close();

    } catch(Exception e){
      System.out.println(e);
    }
    return "Não foi possível cancelar a reserva\n";
  }

  public String listarMesas(String data) throws RemoteException {
    Credentials cred = new Credentials();
    HashMap<Integer,ArrayList<String>> mesasReservadas= new HashMap<Integer,ArrayList<String>>();


    String result = "";
    System.out.println("Pedido listar mesas recebido\n");
    try{

      Connection con = DriverManager.getConnection(cred.getUrl(), cred.getUser(), cred.getPassword());
      Statement st = con.createStatement();

      // BUSCAR MESAS COM RESERVA
      String s = "select mesa_id, horario, cancelado from reserva where dia = " + "\"" + data+ "\"";
      st.execute(s);

      ResultSet rs = st.executeQuery(s);

      while (rs.next()) {
        ArrayList<String> temp = new ArrayList<>();

        //Confirmar se reserva não foi cancelada
        if(rs.getInt(3) == 0){
          int mesa_id = rs.getInt(1);

          if(mesasReservadas.containsKey(mesa_id)){
            //Update ao Arraylist da chave
            temp = mesasReservadas.get(mesa_id);
            temp.add(rs.getString(2));

            //Update ao HashMap
            mesasReservadas.put(mesa_id, temp);
          }else{
            //System.out.println("criei o id");
            temp.add(rs.getString(2));
            mesasReservadas.put(mesa_id ,temp);
          }
        }


        //result += "Mesa ID: "+rs.getInt(1)+ " Horário: "+rs.getString(2)+" \n";

      }

      s = "select * from mesa";

      rs = st.executeQuery(s);

      while (rs.next()) {

        int idMesa = rs.getInt(1);

        if(mesasReservadas.containsKey(idMesa)){

          if(mesasReservadas.get(idMesa).size() == 2){

            result += "Mesa ID: "+rs.getInt(1)+ " Numero de Pessoas: "+rs.getInt(2)+ " Mesa indisponível " + " \n";

          } else if (mesasReservadas.get(idMesa).get(0).equals("almoço") || mesasReservadas.get(idMesa).get(0).equals("jantar")) {

            result += "Mesa ID: "+rs.getInt(1)+ " Numero de Pessoas: "+rs.getInt(2)+ " Mesa reservada para o " + mesasReservadas.get(idMesa).get(0)+ " \n";
          }

        }else{
          result += "Mesa ID: "+rs.getInt(1)+ " Numero de Pessoas: "+rs.getInt(2)+ " Dispnível" + " \n";
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

  public int autenticar(String email, char[] password){
    Credentials cred = new Credentials();
    String result = "";
    System.out.println("Pedido autenticar recebido\n");
    int idPessoa = -1;

    try{
      String passwordHashed = encryptPassword(new String(password));

      Connection con = DriverManager.getConnection(cred.getUrl(), cred.getUser(), cred.getPassword());

      PreparedStatement query = con.prepareStatement("SELECT * FROM pessoa WHERE email = ? AND password = ?");
      query.setString(1, email);
      query.setString(2, passwordHashed);


      Statement st = con.createStatement();
      ResultSet rs = query.executeQuery();

      if(rs.next()){
        //existe
        idPessoa = rs.getInt(1);
        System.out.println("Registo Encontrado\n");
        System.out.println("Resposta ao pedido reservar mesa efetuada");

        //Credenciais batem certo
        return idPessoa;
      }else {

        System.out.println("Nao existe registo\n");
        System.out.println("Resposta ao pedido autenticar efetuada");

        //Credenciais não batem certo
        return idPessoa;
      }

    } catch(Exception e){
      System.out.println(e);
      return idPessoa;
    }
  }

  public  boolean registar(String email, char[] password){
    Credentials cred = new Credentials();
    String result = "";
    System.out.println("Pedido registar utilizador recebido\n");

    try{

      Connection con = DriverManager.getConnection(cred.getUrl(), cred.getUser(), cred.getPassword());

      PreparedStatement query = con.prepareStatement("SELECT * FROM pessoa WHERE email = ?");
      query.setString(1, email);

      Statement st = con.createStatement();
      ResultSet rs = query.executeQuery();

      if(rs.next()){
        //existe
        System.out.println("Registo Encontrado\n");
        System.out.println("Resposta ao registar utilizador efetuado");


        //Já existe reserva
        return false;
      }else {
        System.out.println("Nao existe registo\n");
        System.out.println("Resposta ao pedido registar utilizador efetuado");

        String passwordHashed = encryptPassword(new String(password));

        String s = "insert into pessoa(email, password) values(" + "\"" + email+ "\"" + "," + "\""+ passwordHashed + "\"" +");";
        st.execute(s);


        //Não existe utilizador
        return true;
      }

    } catch(Exception e){
      System.out.println(e);
      return false;
    }


  }


    public static String encryptPassword(String input) {
      try {
        // getInstance() method is called with algorithm SHA-512
        MessageDigest md = MessageDigest.getInstance("SHA-512");

        // digest() method is called
        // to calculate message digest of the input string
        // returned as array of byte
        byte[] messageDigest = md.digest(input.getBytes());

        // Convert byte array into signum representation
        BigInteger no = new BigInteger(1, messageDigest);

        // Convert message digest into hex value
        String hashtext = no.toString(16);

        // Add preceding 0s to make it 32 bit
        while (hashtext.length() < 32) {
          hashtext = "0" + hashtext;
        }

        // return the HashText
        return hashtext;
      }

      // For specifying wrong message digest algorithms
      catch (NoSuchAlgorithmException e) {
        throw new RuntimeException(e);
      }
    }

}
