import java.rmi.*;
public class Client {



  public static void main(String args[]) {
    try {
      String ServerURL = "rmi://" + args[0] + "/Server";
      ServerIntf ServerIntf =
                    (ServerIntf)Naming.lookup(ServerURL);
      String opcao = args[1].trim();

      switch (opcao) {
        case "1":
          System.out.println(ServerIntf.reservarMesa("0"));

        case "2":
          System.out.println(ServerIntf.listarMesas());
      }
    }
    catch(Exception e) {
      System.out.println("Exception: " + e);
    }
  }
}