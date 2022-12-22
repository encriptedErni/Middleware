import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Iterator;


public class statisticImpl extends UnicastRemoteObject implements StatisticInterfaz{
	
	//Declara la memoria que correspondentes campos. En la memoria se almacenan las medias de los datos de todos los distritos
	private Map<String, Map<String,String>> memoria = new HashMap<>();
	private Map<String, String> Centro = new HashMap<>();
	private Map<String, String> Norte = new HashMap<>();
	private Map<String, String> Sur = new HashMap<>();
	private Map<String, String> Este = new HashMap<>();
	private Map<String, String> Oeste = new HashMap<>();
	
	public statisticImpl() throws RemoteException{
		super();
		//Inicializa la memoria
		Centro.put("Centro", "");
		Centro.put("Arganzuela", "");
		Centro.put("Retiro", "");
		Centro.put("Barrio-de-Salamanca", "");
		Norte.put("Chamartin", "");
		Norte.put("Tetuan", "");
		Norte.put("Chamberi", "");
		Norte.put("Fuencarral-El-Pardo", "");
		Sur.put("Puente-de-Vallecas", "");
		Sur.put("Moratalaz", "");
		Sur.put("Villaverde", "");
		Sur.put("Villa-de-Vallecas", "");
		Sur.put("Vicalvaro", "");
		Este.put("Ciudad-Lineal", "");
		Este.put("Hortaleza", "");
		Este.put("San-Blas", "");
		Este.put("Barajas", "");
		Oeste.put("Moncloa-Aravaca", "");
		Oeste.put("Latina", "");
		Oeste.put("Carabanchel", "");
		Oeste.put("Usera", "");
		memoria.put("Centro", Centro);
		memoria.put("Norte", Norte);
		memoria.put("Sur", Sur);
		memoria.put("Este", Este);
		memoria.put("Oeste", Oeste);
	}

	 @Override
	public boolean statistics(String delegacion, String distrito, String propertyData) {
		
		String s;
		String[] datosMemoria;
		String[] res = new String[7];
		Iterator<Entry<String,String>> it;
		Entry<String,String> e;
		
		//Comprueba que el paso de parametros es correcto
		if(delegacion == null || distrito == null || propertyData == null) {
			return false;
		}
	
		//Como propertyData no es null se hace el split para poder acceder a los campos
		String[] datos = propertyData.split(",");
		
		//Accede a los datos de la delegacion correspondiente
		s = memoria.get(delegacion).get(distrito);
		
		//hace el split para poder acceder a los campos
		datosMemoria = propertyData.split(",");
		
		//Si es el primer inmueble registrado se añade directamente
		if(datosMemoria.length == 1) {
			memoria.get(delegacion).replace(distrito, propertyData);
		}
		
		else {
			s = "";
			res[0] = distrito;
			s = res[0] + ",";
			//hace la media de cada campo y los añade al string
			for(int i = 2; i < datos.length; i++) {
				Double media = (Double.parseDouble(datosMemoria[i]) + Double.parseDouble(datos[i]))/2;
				res[i] = Double.toString(media);
				if(i == datos.length-1) {
					s += res[i];
				}
				else {
					s += res[i] + ", ";
				}
			}
			memoria.get(delegacion).replace(distrito, s);
		}
		
		//imprime los datos de los distritos de una delegacion
		it = memoria.get(delegacion).entrySet().iterator();
		System.out.println("DISTRITO, PRECIO, HABITACIONES, BAÑOS, SUPERFICIE, PRECIO_M2");
		while(it.hasNext()) {
			e = it.next();
			if(e.getValue().length() == 0){
				System.out.println(e.getKey() + ",");
			}
			else{
				System.out.println(e.getValue());
			}
		}
		return true;
	}
}
