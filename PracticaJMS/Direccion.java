import java.text.DecimalFormat;

import javax.jms.*;

import javax.jms.ConnectionFactory;
import javax.jms.Connection;
import javax.jms.Session;
import javax.jms.MessageConsumer;
import javax.jms.Message;
import javax.jms.TextMessage;

class crearDireccion {
    public MessageConsumer msgConsumer;
    public Session mySess;
    public Topic topic;
    public boolean done=false;
    // public String distrito;
    // public int precio;

    class TextListener implements MessageListener {
        private final DecimalFormat df = new DecimalFormat("0.00");

        crearDireccion direccion;
        public TextListener(crearDireccion direccion) {
            this.direccion = direccion;
        }

        public void onMessage(Message message) {
            if (message instanceof TextMessage) {
                TextMessage msg = (TextMessage) message;
                String a="";
                try {
                    a=msg.getText();
                    if (msg.getText().equals("TerminoDeMandar")) {
                        System.out.println("Termino");
                        done=true;
                    } else {
                        String distrito = msg.getStringProperty("distrito");
                        int precio = msg.getIntProperty("precio");
                        Double preciom2 = msg.getDoubleProperty("preciom2");
                        String[] datos = msg.getText().split(",");
                        if ((distrito.equals("Barrio-de-Salamanca") || distrito.equals("Chamberi"))
                                && precio > 500000) {

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
                    System.out.println(a);
                    System.out.println("Exception in onMessage(): " + e.toString());
                }
            }

        }

    }

    public crearDireccion(Session session, Topic topic) {
        try {
            System.out.println("Waiting for messages...");
            msgConsumer = session.createConsumer(topic);
            TextListener textListener = new TextListener(this);
            msgConsumer.setMessageListener(textListener);

        } catch (Exception jmse) {
            System.out.println("Exception occurred : " + jmse.toString());
            jmse.printStackTrace();
        }
    }
}

public class Direccion {
    static crearDireccion direccion;

    public static void main(String[] args) {
        try {
            ConnectionFactory myConnFactory;
            Topic myTopic;

            myConnFactory = new com.sun.messaging.ConnectionFactory();

            Connection myConn = myConnFactory.createConnection();

            Session mySess = myConn.createSession(false, Session.AUTO_ACKNOWLEDGE);

            myTopic = new com.sun.messaging.Topic("equiposGestion");

            direccion = new crearDireccion(mySess, myTopic);
            myConn.start();

            while (!direccion.done) {
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