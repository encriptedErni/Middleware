import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javax.jms.*;

class oficinasConstruccion {
    // Al leer el fichero tienes que inicializar una oficina con lo que leas
    public int numero;
    public String delegacion;
    public MessageProducer msgProducer;

    public oficinasConstruccion(int numero, String delegacion, Session mySess, Topic myTopic) {
        this.numero = numero;
        this.delegacion = delegacion;
        try {
            msgProducer = mySess.createProducer(myTopic);
        } catch (Exception jmse) {
            System.out.println("Exception occurred : " + jmse.toString());
            jmse.printStackTrace();
        }
    }
}

public class oficinas {
    static oficinasConstruccion[] oficina;

    public static void pasarInformacion(Session mySess, int i, int num) {
        try {
            String linea = "";
            TextMessage myTextMsg = mySess.createTextMessage();
            FileReader f;
            BufferedReader lector = null;
            String fichero = String.format("%s_OFICINA_%d.txt", oficina[i].delegacion, oficina[i].numero);
            try {
                f = new FileReader(String.format("./Ficheros_Practica_1/%s", fichero));
                lector = new BufferedReader(f);
            } catch (Exception error) {
                System.out.println("Error al abrir fichero: " + fichero);
                error.printStackTrace();
            }
            // leer primera linea
            lector.readLine();
            linea = lector.readLine();
            while (linea != null) {
                String informacion = oficina[i].delegacion + "," + linea;
                myTextMsg.setText(informacion);
                myTextMsg.setIntProperty("numero", num);
                oficina[i].msgProducer.send(myTextMsg);
                System.out.println("Sending Object: " + informacion);
                linea = lector.readLine();
            }
            myTextMsg.setText("Termino");
            myTextMsg.setIntProperty("numero", num);
            oficina[i].msgProducer.send(myTextMsg);

        } catch (Exception jmse) {
            System.out.println("Exception occurred : " + jmse.toString());
            jmse.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            ConnectionFactory myConnFactory;
            Topic myTopic1;
            Topic myTopic2;
            Topic myTopic3;
            Topic myTopic4;
            Topic myTopic5;
            myConnFactory = new com.sun.messaging.ConnectionFactory();
            Connection myConn = myConnFactory.createConnection();
            Session mySess = myConn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            myTopic1 = new com.sun.messaging.Topic("Sur");
            myTopic2 = new com.sun.messaging.Topic("Norte");
            myTopic3 = new com.sun.messaging.Topic("Este");
            myTopic4 = new com.sun.messaging.Topic("Oeste");
            myTopic5 = new com.sun.messaging.Topic("Centro");
            oficina = new oficinasConstruccion[16];
            for (int i = 0; i < 16; i++) {
                File folder = new File("./Ficheros_Practica_1");
                File[] listOfFiles = folder.listFiles();
                int num = 0;
                // System.out.println(listOfFiles[i].getName());
                if (listOfFiles[i].isFile()) {
                    // System.out.println(listOfFiles[i].getName());
                    String[] partes = listOfFiles[i].getName().split("_");
                    String delegacion = partes[0];
                    int numero = Integer.parseInt(partes[2].substring(0, 1));

                    if (delegacion.equals("Sur")) {
                        oficina[i] = new oficinasConstruccion(numero, delegacion, mySess, myTopic1);
                         num = 0;
                    } else if  (delegacion.equals("Norte")) {
                        oficina[i] = new oficinasConstruccion(numero, delegacion, mySess, myTopic2);
                        num = 1;
                    } else if  (delegacion.equals("Este")) {
                        oficina[i] = new oficinasConstruccion(numero, delegacion, mySess, myTopic3);
                        num = 2;
                    } else if  (delegacion.equals("Oeste")) {
                        oficina[i] = new oficinasConstruccion(numero, delegacion, mySess, myTopic4);
                        num = 3;
                    } else if  (delegacion.equals("Centro")) {
                        oficina[i] = new oficinasConstruccion(numero, delegacion, mySess, myTopic5);
                        num = 4;
                    }
                }
                pasarInformacion(mySess, i, num);
            }

            mySess.close();
            myConn.close();
            
        } catch (Exception jmse) {
            System.out.println("Exception occurred : " + jmse.toString());
            jmse.printStackTrace();
        }
    }
}