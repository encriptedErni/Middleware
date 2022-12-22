import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class cleanDataImpl extends UnicastRemoteObject implements cleanDataInterfaz, Serializable {
    protected cleanDataImpl() throws RemoteException {
        super();
    }
    public boolean clean_data (String informacion) throws RemoteException{
        if (informacion == null) {
            return false;
        }
        String[] datos = informacion.split(",");

        if (datos.length < 6 || datos[2].equals("") || Integer.parseInt(datos[5]) == 0) {
            return false; // descartar info
        }
        try {
            Integer.parseInt(datos[2]);
        } catch (final NumberFormatException e) {
            return false;
        } // descartar informacion
        return true;
    }
}
