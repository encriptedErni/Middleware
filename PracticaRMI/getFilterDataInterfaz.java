import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;

public interface getFilterDataInterfaz extends Remote {
    public static final String LOOKUP_NAME = "GetFilterData_Service";
    public HashMap<String, List<String>> getFilterData (String equipo) throws RemoteException;
   
}
