import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Servidor {

    public static void main(String[] args) {
        Registry registro;
        try{
			//declaracion de los objetos remotos 
            registro = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            registro.rebind(cleanDataInterfaz.LOOKUP_NAME, new cleanDataImpl());
            registro.rebind(persistenceInt.LOOKUP_NAME, new PersistenceImp());
            registro.rebind(StatisticInterfaz.LOOKUP_NAME, new statisticImpl());
            registro.rebind(getFilterDataInterfaz.LOOKUP_NAME, new getFilterDataImpl());
        } catch (RemoteException e){
            e.printStackTrace();
        }
    }

}