import java.rmi.*;
import java.rmi.server.*;
import java.util.ArrayList;

public class ServerImpl extends UnicastRemoteObject
  implements ServerIntf {

  public ServerImpl() throws RemoteException {
  }

  public boolean reservarMesa(String idMesa) throws RemoteException {
    ReadFiles.readMesas();

    return true;
  }

  public boolean cancelarMesa(String idMesa, String data, String horario) throws RemoteException {
    return true;
  }

  public ArrayList<String> listarMesas() throws RemoteException {
    return new ArrayList<>();
  }

  public boolean registarUtilizador(String username, String password) throws RemoteException {
    return true;
  }
}
