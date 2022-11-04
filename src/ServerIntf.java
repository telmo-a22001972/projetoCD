import java.rmi.*;
import java.util.ArrayList;

public interface ServerIntf extends Remote {
  boolean reservarMesa(String idMesa, String data, String horario) throws RemoteException;
  boolean cancelarMesa(String idMesa, String data, String horario) throws RemoteException;
  String listarMesas() throws RemoteException;

}
