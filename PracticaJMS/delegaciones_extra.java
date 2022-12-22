
//Import the classes to use JNDI.
//Import the JMS API classes.
import java.util.HashMap;
import java.util.Map;

import javax.jms.*;

//import oficinas.java;

class CreaDelegacion_extra { // He cambiado la clase delegacion para que almacene las viviendas que le
    // llegan,
    // esto es PROVISIONAL ya que no es mi tarea el gestionar como se almacena
    // CreaDelegacion_extra delegacion[];
    Topic myTopic;

    // array con 5 espacios representando las delegaciones a la que envian los
    // grupos de oficinas.
    public String agrupacionOficinas[] = new String[5];
    // Al leer el fichero tienes que inicializar una oficina con lo que leas
    String nombre;
    public MessageConsumer msgConsumer;
    public MessageProducer msgProducerTopic;
    public Session mySess;
    public int finalizo;
    public static String informacion; // Linea con toda la informacion leida de oficinas.
    public boolean corregidosTerminados = false;

    public Map<String, String> enlace = new HashMap<String, String>() {
        {
            put("Centro", "Centro");
            put("Arganzuela", "Centro");
            put("Retiro", "Centro");
            put("Barrio-de-Salamanca", "Centro");
            put("Chamartin", "Norte");
            put("Tetuan", "Norte");
            put("Chamberi", "Norte");
            put("Fuencarral-El-Pardo", "Norte");
            put("Puente-de-Vallecas", "Sur");
            put("Moratalaz", "Sur");
            put("Villaverde", "Sur");
            put("Villa-de-Vallecas", "Sur");
            put("Vicalvaro", "Sur");
            put("Ciudad-Lineal", "Este");
            put("Hortaleza", "Este");
            put("San-Blas", "Este");
            put("Barajas", "Este");
            put("Moncloa-Aravaca", "Oeste");
            put("Latina", "Oeste");
            put("Carabanchel", "Oeste");
            put("Usera", "Oeste");

        }

    };

    class TextListener implements MessageListener {

        CreaDelegacion_extra del;

        public TextListener(CreaDelegacion_extra del) {
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

    public boolean comprobarDelegacion(String delegacionOficina, String distrito) {

        boolean resul = false;
        String delegacionDistrito = enlace.get(distrito);
        if (delegacionOficina.equals(delegacionDistrito)) {
            resul = true;
        }

        return resul;
    }

    public void enviaVivienda(Session mySess, int i) {
        try {
            TextMessage TextMsg;
            String vivienda = null;
            String info = null;

            TextMsg = mySess.createTextMessage();
            if (getInformacion(i).equals("Termino")) {
                sumarFinalizo();
            } else if (getInformacion(i).equals("corregidosTerminados")) {
                this.corregidosTerminados = true;
            } else {
                info = getInformacion(i);
                vivienda = conversor(info);
                System.out.println("Vivienda: " + vivienda + "\n");
                if (vivienda != null) {
                    String[] datos = vivienda.split(",");
                    String distrito = datos[1];
                    boolean correccion = comprobarDelegacion(nombre, distrito);
                    if (correccion) {
                        TextMsg.setIntProperty("precio", Integer.parseInt(datos[2]));
                        TextMsg.setDoubleProperty("preciom2", Double.parseDouble(datos[6]));
                        TextMsg.setStringProperty("distrito", datos[1]);
                        TextMsg.setText(vivienda + "," + datos[5]);
                        msgProducerTopic.send(TextMsg);
                        System.out.println("Sending Object: " + vivienda);
                    } else {
                        System.out.println("Delegación Incorrecta\n");
                        String delegacionCorrecta = enlace.get(distrito);
                        System.out
                                .println("la delegacion correcta de " + distrito + " es :" + delegacionCorrecta + "\n");
                        datos = info.split(",");
                        info = String.format("%s,%s,%s,%s,%s,%s", delegacionCorrecta, datos[1], datos[2], datos[3],
                                datos[4], datos[5]);
                        System.out.println(info + "\n");
                        TextMsg.setText(info);
                        TextMsg.setIntProperty("numero", i);
                        myTopic = new com.sun.messaging.Topic(delegacionCorrecta);
                        MessageProducer msgProducerTopic = mySess.createProducer(myTopic);
                        msgProducerTopic.send(TextMsg);
                        System.out.println("Envio a delegacion correcta " + info + "\n");
                    }
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

    public CreaDelegacion_extra(String nombre, Session mySess, Topic myTopic, Topic destination) {
        this.nombre = nombre;
        this.mySess = mySess;
        try {
            System.out.println("Waiting for messages...");
            ;
            msgConsumer = mySess.createConsumer(myTopic);
            TextListener textListener = new TextListener(this);
            msgConsumer.setMessageListener(textListener);
            msgProducerTopic = mySess.createProducer(destination);
        } catch (Exception jmse) {
            System.out.println("Exception occurred : " + jmse.toString());
            jmse.printStackTrace();
        }
    }

    public String conversor(String informacion) {
        // creo la funcion para convertir el objeto inmueble en uno
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

        String vivienda = informacion + "," + Integer.parseInt(datos[2]) / Integer.parseInt(datos[5]); // meter datos
        return vivienda;
    }

}

public class delegaciones_extra {
    static CreaDelegacion_extra delegacion[] = new CreaDelegacion_extra[5];
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
                delegacion[i] = new CreaDelegacion_extra(nombres[i], mySess, myTopic, TopicEnvio);
            }

            myConn.start();

            while (delegacion[0].getFinalizo() < 4 && delegacion[1].getFinalizo() < 3 && delegacion[2].getFinalizo() < 3
                    && delegacion[3].getFinalizo() < 3 && delegacion[4].getFinalizo() < 3) {
                Thread.sleep(1000);
            }
            TextMessage TextMsg;
            MessageProducer msgProducerTopic;
            for (int i = 0; i < 5; i++) {
                TextMsg = mySess.createTextMessage();
                TextMsg.setText("corregidosTerminados");
                TextMsg.setIntProperty("numero", i);
                myTopic = new com.sun.messaging.Topic(nombres[i]);
                msgProducerTopic = mySess.createProducer(myTopic);
                msgProducerTopic.send(TextMsg);
            }
            while (!delegacion[0].corregidosTerminados || !delegacion[1].corregidosTerminados
                    || !delegacion[2].corregidosTerminados
                    || !delegacion[3].corregidosTerminados || !delegacion[4].corregidosTerminados) {
                Thread.sleep(1000);
            }

            TextMsg = mySess.createTextMessage();
            TextMsg.setText("TerminoDeMandar");
            delegacion[0].msgProducerTopic.send(TextMsg);
            System.out.println("Termino");
            mySess.close();
            myConn.close();
        } catch (Exception jmse) {
            System.out.println("Exception occurred : " + jmse.toString());
            jmse.printStackTrace();
        }

    }

}
