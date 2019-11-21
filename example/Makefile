ANTLRJAR = /usr/local/lib/antlr-4.7-complete.jar

export CLASSPATH := .:$(ANTLRJAR):${CLASSPATH}
antlr4 = java -jar $(ANTLRJAR)
grun   = java org.antlr.v4.gui.TestRig

SRCFILES = main.java
GENERATED = interpreterListener.java interpreterBaseListener.java interpreterParser.java interpreterBaseVisitor.java interpreterVisitor.java interpreterLexer.java interpreter.java

all:
	make main.class

main.class:	$(SRCFILES) $(GENERATED) interpreter.g4
	javac  $(SRCFILES) $(GENERATED)

interpreterListener.java:	interpreter.g4
	$(antlr4) -visitor interpreter.g4

test:	main.class
	java main interpreter_input.txt

tree:	$(GENERATED) interpreter.g4
	javac $(GENERATED)
	$(grun) interpreter start -tree -gui < interpreter_input.txt

clean:
	rm -f *.class
	rm -f *.tokens
	rm -f $(GENERATED)
