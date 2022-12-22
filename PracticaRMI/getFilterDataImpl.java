import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
public class getFilterDataImpl extends UnicastRemoteObject implements getFilterDataInterfaz, Serializable {
    HashMap<String, List<String>> filtro;
    protected getFilterDataImpl() throws RemoteException {
        super();
        this.filtro = new HashMap<>();
    }

    public HashMap<String, List<String>> getFilterData (String informacion) throws RemoteException{
        String campo;
        List<String> valores= new ArrayList<>();
        filtro.clear();
        if(informacion.equals("Direccion")){
            campo="Distritos";
            valores.add("Chamberi");
            valores.add("Barrio-de-Salamnca");
            filtro.put(campo, valores);
            campo="Precios";
            valores= new ArrayList<>();
            valores.add("500000");
            filtro.put(campo, valores);
        } else if(informacion.equals("Negocio")){
            campo="Precio_m2";
            valores.add("1500");
            valores.add("3000");
            filtro.put(campo, valores);
        } else{
            return null;
        }
        return filtro;
    }
}
