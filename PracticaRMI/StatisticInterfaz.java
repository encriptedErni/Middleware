import java.rmi.Remote;
import java.rmi.RemoteException;

public interface StatisticInterfaz extends Remote {

    public static final String LOOKUP_NAME = "Statistic_Service";
    public boolean statistics ( String delegacion , String distrito , String propertyData) throws RemoteException;
}
