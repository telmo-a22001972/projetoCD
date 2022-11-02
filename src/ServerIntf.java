import java.rmi.*;
import java.util.ArrayList;

public interface ServerIntf extends Remote {
  boolean reservarMesa(String idMesa) throws RemoteException;
  boolean cancelarMesa(String idMesa, String data, String horario) throws RemoteException;
  ArrayList<String> listarMesas() throws RemoteException;
  boolean registarUtilizador(String username, String password) throws RemoteException;
}
