
//Import the classes to use JNDI.
//Import the JMS API classes.
import javax.jms.*;

//import oficinas.java;

class CreaDelegacion { // He cambiado la clase delegacion para que almacene las viviendas que le
                       // llegan,
                       
    // CreaDelegacion delegacion[];
    Topic myTopic;
    // array con 5 espacios representando las delegaciones a la que envian los
    // grupos de oficinas.
    public String agrupacionOficinas[] = new String[5];
    // Al leer el fichero tienes que inicializar una oficina con lo que leas
    String nombre;
    public MessageConsumer msgConsumer;
    public MessageProducer msgProducer;
    public Session mySess;
    public int finalizo;
    public static String informacion;
    public boolean termino = false;// Linea con toda la informacion leida de oficinas.
    // public static boolean done = false;

    class TextListener implements MessageListener {

        CreaDelegacion del;

        public TextListener(CreaDelegacion del) {
            this.del = del;
        }

        public void onMessage(Message message) {
            if (message instanceof TextMessage) {
                System.out.println("Message received");
                TextMessage msg = (TextMessage) message;
                try {
                    int num = msg.getIntProperty("numero");
                    System.out.println("Numero: " + num);
                    del.setInformacion(num, msg.getText());
                    System.out.println("\tReading message: " + msg.getText());
                    del.enviaVivienda(mySess, num);

                } catch (Exception e) {
                    System.out.println("Exception in onMessage(): " + e.toString());
                }
            }
        }
    }

    public void enviaVivienda(Session mySess, int i) {
        try {
            TextMessage TextMsg;
            String vivienda = null;

            TextMsg = mySess.createTextMessage();
            if (getInformacion(i).equals("Termino")) {
                sumarFinalizo();

            } else if (getInformacion(i).equals("TerminoDeMandar")) {
                this.termino = true;
                System.out.println("Termino de mandar " + nombre);
            } else {
                vivienda = conversor(getInformacion(i));
                System.out.println("Vivienda: " + vivienda);
                if (vivienda != null) {
                    String[] datos = vivienda.split(",");
                    TextMsg.setText(vivienda + "," + datos[5]);
                    TextMsg.setIntProperty("precio", Integer.parseInt(datos[2]));
                    TextMsg.setDoubleProperty("preciom2", Double.parseDouble(datos[6]));
                    TextMsg.setStringProperty("distrito", datos[1]);
                    msgProducer.send(TextMsg);
                    System.out.println("Sending Object: " + vivienda);
                }
            }
        } catch (

        Exception e) {
            e.printStackTrace();
        }
    }

    public void sumarFinalizo() {
        synchronized (this) {
            this.finalizo++;
        }
    }

    public int getFinalizo() {
        synchronized (this) {
            return finalizo;
        }
    }

    // Solucionar condiciones de carrera
    public void setInformacion(int num, String informacion) {
        // establecer informacion que llega
        synchronized (this) {
            this.agrupacionOficinas[num] = informacion;
        }
    }

    public String getInformacion(int num) {
        // leer informacion que establecimos
        synchronized (this) {
            return agrupacionOficinas[num];
        }
    }

    public CreaDelegacion(String nombre, Session mySess, Topic myTopic, Topic destination) {
        this.nombre = nombre;
        this.mySess = mySess;
        try {
            // Argument 'boolean noLocal = true' for skipping own messages
            System.out.println("Waiting for messages...");
            msgConsumer = mySess.createConsumer(myTopic);
            TextListener textListener = new TextListener(this);
            msgConsumer.setMessageListener(textListener);
            msgProducer = mySess.createProducer(destination);
        } catch (Exception jmse) {
            System.out.println("Exception occurred : " + jmse.toString());
            jmse.printStackTrace();
        }
    }

    /*
     * public static void mandarInfo(Session mySess) {
     * 
     * }
     */

    public String conversor(String informacion) {// creo la funcion para convertir el objeto inmueble en uno
        // vivienda
        // filtrar si la informacion es correcta

        if (informacion == null) {
            return null;
        }
        String[] datos = informacion.split(",");

        if (datos.length < 6 || datos[2].equals("") || Integer.parseInt(datos[5]) == 0) {
            return null; // descartar info
        }
        try {
            Integer.parseInt(datos[2]);
        } catch (final NumberFormatException e) {
            return null;
        } // descartar informacion

        String vivienda = informacion + "," + Double.parseDouble(datos[2]) / Double.parseDouble(datos[5]); // meter
                                                                                                           // datos
        return vivienda;
    }

}

public class delegaciones {
    static CreaDelegacion delegacion[] = new CreaDelegacion[5];
    static String[] nombres = { "Sur", "Norte", "Este", "Oeste", "Centro" };

    public static void main(String[] args) {
        try {
            // CONSUMIDOR
            ConnectionFactory myConnFactory;
            Topic myTopic;

            myConnFactory = new com.sun.messaging.ConnectionFactory();

            Connection myConn = myConnFactory.createConnection();

            Session mySess = myConn.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Topic TopicEnvio = mySess.createTopic("equiposGestion");

            for (int i = 0; i < 5; i++) {
                myTopic = new com.sun.messaging.Topic(nombres[i]);
                delegacion[i] = new CreaDelegacion(nombres[i], mySess, myTopic, TopicEnvio);
            }

            myConn.start();

            while (delegacion[0].getFinalizo() < 4 && delegacion[1].getFinalizo() < 3 && delegacion[2].getFinalizo() < 3
                    && delegacion[3].getFinalizo() < 3 && delegacion[4].getFinalizo() < 3) {
                Thread.sleep(1000);
            }
            TextMessage TextMsg = mySess.createTextMessage();
            MessageProducer msgProducer;
            TextMsg.setText("TerminoDeMandar");
            for (int i = 0; i < 5; i++) {
                TextMsg.setIntProperty("numero", i);
                myTopic = new com.sun.messaging.Topic(nombres[i]);
                msgProducer = mySess.createProducer(myTopic);
                msgProducer.send(TextMsg);
            }
            while (!delegacion[0].termino || !delegacion[1].termino
                    || !delegacion[2].termino
                    || !delegacion[3].termino || !delegacion[4].termino) {
                Thread.sleep(1000);
            }
            delegacion[0].msgProducer.send(TextMsg);
            System.out.println("Termino todo");
            myConn.close();
            mySess.close();

        } catch (Exception jmse) {
            System.out.println("Exception occurred : " + jmse.toString());
            jmse.printStackTrace();
        }

    }

}