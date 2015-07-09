JDK_PATH=/opt/jdk1.8.0_25
INCDIRS=-I${JDK_PATH}/include -I${JDK_PATH}/include/linux -I.
CNAME=Controls
CFILE=${CNAME}.c
CFILELIB=lib${CNAME}.so
JNAME=ControllerInfo
JFILE=${JNAME}.java
JCLASS=${JNAME}.class

${JCLASS}: ${JFILE} ${CFILELIB}
	javac ${JFILE}

${CFILELIB}: ${CFILE}
	gcc -o ${CFILELIB} -shared ${INCDIRS} ${CFILE} -lc -fPIC

clean:
	rm -f ${CFILELIB} ${JCLASS} *~ *.class

