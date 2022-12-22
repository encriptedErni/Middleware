import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.io.*;
import java.io.File;

public class PersistenceImp extends UnicastRemoteObject implements persistenceInt {
    
    public PersistenceImp()throws RemoteException{
        super();
    }

    public boolean persistence(String delegacion, String propertyData){
        
        File f;
        f = new File(delegacion + "_data.txt");
        FileWriter flwriter = null;
        if(!f.exists()){
            try{
                //crea el flujo para escribir en el archivo
			flwriter = new FileWriter(delegacion + "_data.txt");
			//crea un buffer o flujo intermedio antes de escribir directamente en el archivo
			BufferedWriter bfwriter = new BufferedWriter(flwriter);
		    //escribe los datos en el archivo
			bfwriter.write(propertyData + "\n");
			
			//cierra el buffer intermedio
			bfwriter.close();
           
            }catch(IOException e){
                e.printStackTrace();
            }finally {
                if (flwriter != null) {
                    try {//cierra el flujo principal
                        flwriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            
        }else{
		try {
            //además de la ruta del archivo recibe un parámetro de tipo boolean, que le indican que se va añadir más registros 
			flwriter = new FileWriter(delegacion + "_data.txt", true);
			BufferedWriter bfwriter = new BufferedWriter(flwriter);
			bfwriter.write(propertyData + "\n");
			
			bfwriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (flwriter != null) {
				try {
					flwriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
        }
        return false;
    }

}

