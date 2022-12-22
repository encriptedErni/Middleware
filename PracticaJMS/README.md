<h1>Middleware Practica 1 </h1>

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
<li> delegaciones.java</li>
<li> Direccion.java </li>
<li>Negocio.java</li>
<li>Directorio Ficheros_Practica_1: informacion inmuebles (ficheros .txt)</li>
<li>Makefile</li>
<li>README.md</li>
</ul>

<h2>Explicacion de ejecución: </h2>

<h3>Funcionalidad basica:</h3> 
Con el fichero Makefile proporcionado: 

Ejecutar en distintos shell (direccion y negocio) = make run_neg y make run_dir

Ejecutar delegaciones.java = make run_del

Ejecutar oficinas.java = make run_ofi

<h3>Funcionalidad extra:</h3> 
 
Ejecutar en distintos shell (Direccion.java y Negocio.java) = make run_neg y make run_dir

Ejecutar delegaciones_extra.java = make run_del_extra

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

<h3>Funcionalidad extra:</h3>
Se añade a a todo lo descrito en delegaciones, una función enlace que nos permite comprobar si la delegación es correcta con la función comprobarDelegación. En envía vivienda se añade el mecanismo necesario para mandar a la delegación correcta la vivienda corregida. Además en el main se envían unos mensajes de Terminación justo después de que se hayan recibido todos los mensajes de oficinas para aseguranos de que se han terminado de enviar las viviendas que han sido corregidas.
    
<h2>Requisitos implementados:</h2>
<h3>Se han implementado todos los requisitos de la funcionalidad basica:</h3>
- Las oficinas leen, procesan y envían información de inmuebles a sus respectivas delaciones. (oficinas.java)
- Las delegaciones reciben información de las oficinas, generando nuevos datos y enviando todo a los equipos gestores.(delegaciones.java)
- Los equipos gestores esperan recibir la información filtrada conforme a las restricciones antes descritas. Tambien imprimirán por pantalla la información de inmuebles recibida. (Negocio.java y Direccion.java)

<h3>Se han implementado todos los requisitos de la funcionalidad extra:</h3>
- Delegaciones sean las encargadas de intercambiarse la información de inmuebles recibidos de sus oficinas cuando son de un distrito fuera de esa delegación. (delegaciones_extra.java). Debido a esto, a pesar de tener la comunicación entre oficinas y delegaciones implementadas con colas, decidimos cambiarlas a Topic porque resultaba mucho más sencillo comunicar las distintas delegaciones.












