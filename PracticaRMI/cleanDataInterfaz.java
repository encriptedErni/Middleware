import java.rmi.Remote;
import java.rmi.RemoteException;

public interface cleanDataInterfaz extends Remote{

    public static final String LOOKUP_NAME = "CleanData_Service";
    // interfaz clean_data para oficinas
    public boolean clean_data (String informacion) throws RemoteException;
    // implementar el metodo en oficinas, que lo invocara remotamente.
}
