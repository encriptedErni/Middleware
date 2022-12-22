import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import javax.jms.*;

import javax.jms.ConnectionFactory;
import javax.jms.Connection;
import javax.jms.Session;
import javax.jms.MessageConsumer;
import javax.jms.Message;
import javax.jms.TextMessage;

class crearNegocio {
    public MessageConsumer msgConsumer;
    public Session mySess;
    public Topic topic;
    public boolean done=false;
    List<String> intervalo_preciom2;
    // public String distrito;
    // public int precio;

    class TextListener implements MessageListener {
        private final DecimalFormat df = new DecimalFormat("0.00");

        crearNegocio negocio;

        public TextListener(crearNegocio negocio) {
            this.negocio = negocio;
        }

        public void onMessage(Message message) {
            if (message instanceof TextMessage) {
                TextMessage msg = (TextMessage) message;
                try {
                    if (msg.getText().equals("TerminoDeMandar")) {
                        System.out.println("Termino");
                        done = true;
                    } else {
                        String distrito = msg.getStringProperty("distrito");
                        int precio = msg.getIntProperty("precio");
                        double preciom2 = msg.getDoubleProperty("preciom2");
                        String[] datos = msg.getText().split(",");
                        if (preciom2 > Integer.parseInt(intervalo_preciom2.get(0)) && preciom2 < Integer.parseInt(intervalo_preciom2.get(1))) {
                            System.out.println("-Delegacion: " + datos[0] + "\n"
                                    + "-Distrito: " + distrito
                                    + "\n-Precio: " + precio + " euros\n"
                                    + "-Habitaciones: " + Integer.parseInt(datos[3]) + " habitaciones\n"
                                    + "-Baños: " + datos[4] + " baños\n"
                                    + "-Superficie: " + Integer.parseInt(datos[5]) + " m2\n"
                                    + "-Precio/m2 " + df.format(preciom2) + " euros\n");
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Exception in onMessage(): " + e.toString());
                }
            }

        }

    }

    public crearNegocio(Session session, Topic topic) {
        try {
            System.out.println("Waiting for messages...");
            Registry registro = LocateRegistry.getRegistry();
            getFilterDataInterfaz getFilterData1=(getFilterDataInterfaz) registro.lookup("GetFilterData_Service");
            HashMap<String,List<String>> filtros= getFilterData1.getFilterData("Negocio");
            this.intervalo_preciom2=filtros.get("Precio_m2");
            msgConsumer = session.createConsumer(topic);
            TextListener textListener = new TextListener(this);
            msgConsumer.setMessageListener(textListener);

        } catch (Exception jmse) {
            System.out.println("Exception occurred : " + jmse.toString());
            jmse.printStackTrace();
        }
    }
}

public class Negocio {
    static crearNegocio negocio;

    public static void main(String[] args) {
        try {
            ConnectionFactory myConnFactory;
            Topic myTopic;

            myConnFactory = new com.sun.messaging.ConnectionFactory();

            Connection myConn = myConnFactory.createConnection();

            Session mySess = myConn.createSession(false, Session.AUTO_ACKNOWLEDGE);

            myTopic = new com.sun.messaging.Topic("equiposGestion");

            negocio = new crearNegocio(mySess, myTopic);
            myConn.start();

            while (!negocio.done) {
                Thread.sleep(1000);
            }
            mySess.close();
            myConn.close();
        } catch (Exception jmse) {
            System.out.println("Exception occurred : " + jmse.toString());
            jmse.printStackTrace();
        }
    }
}