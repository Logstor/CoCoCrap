#antlr4 = java -jar /usr/local/lib/antlr-4.7-complete.jar
antlr4 = java -Xmx500M -cp "/usr/local/lib/antlr-4.7.2-complete.jar:$$CLASSPATH" org.antlr.v4.Tool #antlr4
SRCFILES = main.java AST.java
GENERATED = cocoListener.java cocoBaseListener.java cocoParser.java cocoBaseVisitor.java cocoVisitor.java cocoLexer.java

all:
	make main.class

main.class:	$(SRCFILES) $(GENERATED) coco.g4
	javac  -Xlint:unchecked  $(SRCFILES) $(GENERATED)

cocoListener.java:	coco.g4
	$(antlr4) -visitor coco.g4

test:	main.class
	java main coco_input.txt > coco_output.java
	cat coco_output.java

test2:	main.class
	java main coco.coco > coco_new.java
	cat coco_new.java

bigtest:	main.class
	java main example/interpreter.coco > example/interpreter.java
	cd example ; make test

clean:
	rm -f *.class
	rm -f $(GENERATED)
	rm -f *.tokens
	cd example ; make clean
