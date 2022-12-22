<h1>Middleware Practica 2 </h1>

<body>
<h2>Developers: </h2>
<ul>
<li>Ramiro Lopez Cento</li>
<li>Esteban Aspe Ruiz</li>
<li>Javier Santana</li>
<li>Ernesto Naval Rodriguez</li>
<li>Daniel Garcia Rodriguez</li>
<li>Alvaro garcia Mosquera</li>
</ul>
</body>

<h2>Contenido Directorio Raiz: </h2>
<ul>
<li>oficinas.java</li>
<li>delegaciones.java</li>
<li>Direccion.java </li>
<li>Negocio.java</li>
<li>StatisticInterfaz.java </li>
<li>statisticImpl.java</li>
<li>persistenceInt.java</li>
<li>PersistenceImp.java</li>
<li>cleanDataInterfaz.java</li>
<li>cleanDataImpl.java</li>
<li>getFilterDataInterfaz.java</li>
<li>getFilterDataImpl.java</li>
<li>Servidor.java</li>
<li>Directorio Ficheros_Practica_1: informacion inmuebles (ficheros .txt)</li>
<li>Makefile</li>
<li>README.md</li>
</ul>

<h2>Explicacion de ejecución: </h2>

<h3>Funcionalidad basica:</h3> 
Con el fichero Makefile proporcionado y el broker en funcionamiento: 

Ejecutar servidor.java = make run_servidor

Ejecutar delegaciones.java = make run_del

Ejecutar oficinas.java = make run_ofi

<h3>Funcionalidad extra:</h3> 
Con el fichero Makefile proporcionado y el broker en funcionamiento: 
 
Ejecutar servidor.java = make run_servidor

Ejecutar en distintos shell (direccion y negocio) = make run_neg y make run_dir

Ejecutar delegaciones.java = make run_del

Ejecutar oficinas.java = make run_ofi 

<h2>Listado de componentes:</h2>

<h3>Funcionalidad basica:</h3> 
oficinasConstruccion = Constructor de las oficinas. Los argumentos son el numero, la delegacion, la sesion y el queue al que enviar el mensaje.

oficinas = clase publica que contiene la funcion pasarInformacion encargada de abrir los ficheros, leerlos y enviarlos. Tambien contiene el main que inicializa las oficinas y llama a pasarInformacion(). 

CreaDelegacion = constructor de las delegaciones que  contiene al propio constructor CreaDelegacion, el TextListener, la funcion enviaVivienda (encargada de tomar la informacion recibida con getInformacion() y llamar al conversor()), la funcion conversor(encargada de añadir el preciom2 y verificar que los datos son correctos), las funciones synchronized setInformacion() y getInformacion() para guardar y acceder a la informacion recibida y las funciones sumarFinalizo() y getFinalizo() para implementar un contador que indique cuando finalizar la ejecucion de delegaciones de manera controlada. 

delegaciones = Contiene el main, que inicializa las las delegaciones con su respectivo queue. Tambien incluye un while que verifica cada segundo si todos los mensajes se han enviado paera finalizar de forma controlada. Para esto, primero se comprueba si han llegado todos los mensajes de terminacion de las oficinas, y después se envía otro mensaje de finalización para comprobar que delegaciones a terminado de enviar de todas las viviendas a Negocio y Direccion. Para esto se comprueba cada segundo si la variables terminado de cada delegacion estan a true y se envia el mensaje de terminación "TerminoDeMandar" a Dirección y Negocio.

CrearDireccion = constructor de las direcciones que contiene el TextListener encargado de filtrar la informacion a recibir.

Direccion = Inicializa la direccion y ejecuta un bucle while que verifica cada segundo si todos los mensajes se han recibido para finalizar de forma controlada, para esto comprobamos la variable done que se pone a true si se recibe el mensaje de terminación "TerminoDeMandar".

crearNegocio = constructor de los negocios que contiene el TextListener encargado de filtrar la informacion a recibir.

Negocio = Inicializa el negocio y ejecuta un bucle while que verifica cada segundo si todos los mensajes se han recibido para finalizar de forma controlada, para esto comprobamos la variable done que se pone a true si se recibe el mensaje de terminación "TerminoDeMandar".

Servidor = Crea y exporta el registro y se encarga de enlazar las interfaces con las referencias de los objetos remotos.

StatisticInterfaz = Creacion de la interfaz asociada a statisticImpl. El 3º argumento hemos optado que sea de tipo string

statisticImpl = Implementacion del método correspondiente a StatisticInterfaz donde se crea una memoria que contiene un atributo memoria formado por delegaciones que tienen asociados sus respectivos distritos y, estos distritos, tienen asociado un string con la media de los inmuebles que les van llegando.

persistenceInt = Creación de la interfaz asociada a PersistenceImp. El 3º argumento hemos optado por que el tipo de dato sea string

PersistenceImp = Implementacion del método correspondiente a persistenceInt. Este crea un fichero con el nombre de la delegación correspondiente y, a traves de un escritor de fichero (java.io.File), escribe en el fichero los datos del inmueble asociado a la delgacion correspondiente.

cleanDataInterfaz = Creación de la interfaz asociada a cleanDataImpl. 

cleanDataImpl = Recibe como parámetro un string con la información del inmueble y va comprobando si los diferentes campos de la información de la información del inmueble son válidos.


<h3>Funcionalidad extra:</h3>

getFilterDataInterfaz = Creación de la interfaz asociada a getFilterDataImpl.

getFilterDataImpl = Recibe un string con la información de a que parte de los equipos va dirigida, y se encarga de incluir en un HashMap la información del filtro correspondiente.


    
<h2>Requisitos implementados:</h2>
<h3>Se han implementado todos los requisitos de la funcionalidad basica:</h3>
- Las oficinas leen, procesan y envían información de inmuebles a sus respectivas delaciones. (oficinas.java)
- Las delegaciones reciben información de las oficinas, generando nuevos datos y enviando todo a los equipos gestores.(delegaciones.java)
- Los equipos gestores esperan recibir la información filtrada conforme a las restricciones antes descritas. Tambien imprimirán por pantalla la información de inmuebles recibida. (Negocio.java y Direccion.java)
- Las interfazes son correctamente invocadas por el servidor remoto (StatisticInterfaz.java, PersistenceImp.java, cleanDataImpl.java, Servidor.java).
- La implementación del método statistic (statisticImpl.java) es invocada correctamente con los parámetros correspondientes y imprime el resultado de las medias de la información de los inmuebles adecuadamente
- La implementación del método Persistence (PersistenceImp.java) crea los ficheros correctamente y la información de los inmuebles dentro de estos ficheros es completa y correcta.
- La implementación del método cleanData (cleanDataImpl.java) comprueba adecuadamente si la información del inmueble es correcta o no.

<h3>Se han implementado todos los requisitos de la funcionalidad extra:</h3>
- Las interfazes son correctamente invocadas por el servidor remoto (getFilterDataInterfaz.java). 
- La implementación del método getFilterData (getFilterDataImpl.java) devuelve adecuadamente el mapa que contiene el filtro correspondiente al equipo




