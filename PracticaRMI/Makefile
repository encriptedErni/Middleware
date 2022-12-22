
JFLAGS = -cp ${MQ_HOME}/lib/imq.jar:${MQ_HOME}/lib/jms.jar:.
JC = javac
JVM= java 
FILE= 

.SUFFIXES: .java .class


.java.class:
	$(JC) $(JFLAGS) *.java


MAIN_OFI = oficinas
MAIN_DEL = delegaciones
MAIN_NEG = Negocio
MAIN_DIR = Direccion
MAIN_DEL_EXTRA = delegaciones_extra
MAIN_SERVIDOR = Servidor

default: .java.class

run_ofi: $(MAIN_OFI).class
	$(JVM) $(JFLAGS) $(MAIN_OFI)

run_del: $(MAIN_DEL).class
	$(JVM) $(JFLAGS) $(MAIN_DEL) 

run_del_extra: $(MAIN_DEL_EXTRA).class
	$(JVM) $(JFLAGS) $(MAIN_DEL_EXTRA)

run_neg: $(MAIN_NEG).class
	$(JVM) $(JFLAGS) $(MAIN_NEG) 

run_dir: $(MAIN_DIR).class
	$(JVM) $(JFLAGS) $(MAIN_DIR)

run_servidor: $(MAIN_SERVIDOR).class
	$(JVM) $(JFLAGS) $(MAIN_SERVIDOR)

clean:
	$(RM) *.class
