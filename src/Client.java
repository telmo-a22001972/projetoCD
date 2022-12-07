import java.rmi.*;
import java.sql.SQLOutput;
import java.util.Scanner;
import java.io.*;

public class Client {

  public static char[] lerPassword() {

    Console cnsl = System.console();

    //Erro de consola
    if(cnsl == null) {
      System.out.println("No console available");
      return new char[1];
    }

    char[] password = cnsl.readPassword(
            "\nInsira a password: "
    );

    return password;
  }

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
            System.out.println("Para reservar uma mesa precisa estar autenticado, insira abaixo as suas credenciais abaixo");

            System.out.println("\nInsira o emailr: ");
            String username_reservar= reader.nextLine();

            if(ServerIntf.autenticar(username_reservar, lerPassword())){

            System.out.println("\nPara qual data pretende consultar a disponibilidade das mesas?.\nExemplo: 5 2022-12-31 jantar\n");
            String input = reader.nextLine();
            String[] dados = input.split(" ");
            String idMesa = dados[0].trim();
            String data = dados[1].trim();
            String horario = dados[2].trim();

            boolean resultado = ServerIntf.reservarMesa(idMesa,data, horario);

            System.out.println(resultado? "Mesa reservada" : "Não foi possível reservar a mesa. Verifique se os parâmetros foram inseridos corretamente");

          }else{
              System.out.println("Credenciais inválidas. Tente novamente ou registe-se.");
            }
            break;

          case "listar mesas":

            System.out.println("\nPara qual data pretende consultar a disponibilidade das mesas?.\nExemplo: 2022-12-31\n");
            String input2 = reader.nextLine();
            System.out.println(ServerIntf.listarMesas(input2.trim()));
            break;

          case "cancelar mesa":
            System.out.println("\nIndique qual reserva pretende cancelar.\nExemplo: 5 2022-12-31 jantar\n");
            String input3 = reader.nextLine();
            String[] dados3 = input3.split(" ");
            String idMesa3 = dados3[0].trim();
            String data3 = dados3[1].trim();
            String horario3 = dados3[2].trim();

            System.out.println(ServerIntf.cancelarMesa(idMesa3,data3, horario3));
            break;

          case "registar utilizador":
            System.out.println("\nInsira o nome do utilizador: ");
            String username = reader.nextLine();

            Console cnsl2 = System.console();

            //Erro de consola
            if(cnsl2 == null) {
              System.out.println("No console available");
              return;
            }

            char[] password = cnsl2.readPassword(
                    "\nInsira a password: "
            );

            System.out.println("Password: "+ password);

            System.out.println(ServerIntf.registar(username, password));
        }
      }
    }
    catch(Exception e) {
      System.out.println("Exception: " + e);
    }
  }
}