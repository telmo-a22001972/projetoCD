import java.rmi.*;
public class Server {
  public static void main(String args[]) {
    try {
      ServerImpl ServerImpl = new ServerImpl();
      Naming.rebind("Server", ServerImpl);
      System.out.println("Server onlinis");
    }
    catch(Exception e) {
      System.out.println("Exception: " + e);
    }
  }
}