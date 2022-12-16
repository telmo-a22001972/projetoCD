import java.rmi.*;
import java.util.ArrayList;

public interface ServerIntf extends Remote {
  boolean reservarMesa(String idMesa, String data, String horario, int idPessoa) throws RemoteException;
  String cancelarMesa(String idMesa, String data, String horario) throws RemoteException;
  String listarMesas(String data) throws RemoteException;

  int autenticar(String nome, char[] password) throws RemoteException;

  boolean registar(String nome, char[] password) throws  RemoteException;


}
