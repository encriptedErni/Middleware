
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface persistenceInt extends Remote{

    public static final String LOOKUP_NAME = "Persistence_Service";
    // interfaz persistence para delegaciones
    public boolean persistence ( String delegacion, String propertyData) throws RemoteException;
    // implementar el metodo en delegaciones, que lo invocara remotamente.
}
