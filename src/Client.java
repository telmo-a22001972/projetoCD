import java.rmi.*;
import java.util.Scanner;

public class Client {



  public static void main(String args[]) {
    try {
      String ServerURL = "rmi://" + args[0] + "/Server";
      ServerIntf ServerIntf =
                    (ServerIntf)Naming.lookup(ServerURL);

      Scanner reader = new Scanner(System.in);  // Create a Scanner object
      System.out.println(
              "\tMenu\n"+
              "\nlistar mesas\n"+
              "reservar mesa\n"+
              "cancelar mesa\n"+
              "registar utilizador\n"
      );
      while(true){
        String opcao = reader.nextLine().trim();

        switch (opcao) {
          case "reservar mesa":
            System.out.println("Introduza o ID da mesa, a data e o horário.\nExemplo: 5 2022-12-31 almoço");
            String input = reader.nextLine();
            String[] dados = input.split(" ");
            String idMesa = dados[0].trim();
            String data = dados[1].trim();
            String horario = dados[2].trim();

            boolean resultado = ServerIntf.reservarMesa(idMesa,data, horario);

            System.out.println(resultado? "Mesa reservada" : "Não foi possível reservar a mesa. Verifique se os parâmetros foram inseridos corretamente");

            break;
          case "listar mesas":
            System.out.println(ServerIntf.listarMesas("2022-11-04"));
            break;

          case "cancelar mesa":
            //TODO implementar cancelar mesa
            System.out.println("Implementar cancelar mesa");
            break;
        }
      }
    }
    catch(Exception e) {
      System.out.println("Exception: " + e);
    }
  }
}