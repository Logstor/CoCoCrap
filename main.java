import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.CharStreams;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class main {
    public static void main(String[] args) throws IOException{

	// we expect exactly one argument: the name of the input file
	if (args.length!=1) {
	    System.err.println("\n");
	    System.err.println("Simple calculator\n");
	    System.err.println("=================\n\n");
	    System.err.println("Please give as input argument a filename\n");
	    System.exit(-1);
	}
	String filename=args[0];

	// open the input file
	CharStream input = CharStreams.fromFileName(filename);
	    //new ANTLRFileStream (filename); // depricated
	
	// create a lexer/scanner
	cocoLexer lex = new cocoLexer(input);
	
	// get the stream of tokens from the scanner
	CommonTokenStream tokens = new CommonTokenStream(lex);
	
	// create a parser
	cocoParser parser = new cocoParser(tokens);
	
	// and parse anything from the grammar for "start"
	ParseTree parseTree = parser.start();

	// Instead of the interpreter, we have now a maker for an
	// Abstract Syntax Tree (AST) that we define right after this class.
	ASTMaker astmaker = new ASTMaker();
	AST ast=astmaker.visit(parseTree);

	String s=((Start) ast).translate();
	if (s.equals(""))
	    System.out.println("This is where the output is supposed to be\n");
	else
	    System.out.println(s);
	/* Suggested way to do it: implement the translate function in ast to produce a more meaningful string than "", so it is output here instead. */

    }
}


class ASTMaker extends AbstractParseTreeVisitor<AST> implements cocoVisitor<AST> {
	public AST visitStart(cocoParser.StartContext ctx)
	{
		List<DataTypeDef> typedefs = new ArrayList<DataTypeDef>();
		for ( cocoParser.DataTypeDefContext c : ctx.dtd)
			typedefs.add((DataTypeDef) visit(c));
		return new Start(typedefs);
	}
	
	public AST visitDataTypeDef(cocoParser.DataTypeDefContext ctx)
	{
		List<Alternative> list=new ArrayList<Alternative>();
		for (cocoParser.AlternativeContext c: ctx.as.as)
			list.add((Alternative)visit(c));
		String s=ctx.fun.getText();
		return new DataTypeDef(ctx.name.getText(),s.substring(1,s.length()-1),list);
	}
	
	public AST visitAlternative(cocoParser.AlternativeContext ctx)
	{
		List<Argument> list=new ArrayList<Argument>();
		for(cocoParser.ArgumentContext c: ctx.as.as)
			list.add((Argument)visit(c));
		return new Alternative(ctx.cons.getText(),list,ctx.code.getText());
	}
	
	public AST visitAlternatives(cocoParser.AlternativesContext ctx)
	{
		return null;//shouldn't be called directly
	}
	
	public AST visitArguments(cocoParser.ArgumentsContext ctx)
	{
		return null;//shouldn't be called really
	}
	
	public AST visitArgument(cocoParser.ArgumentContext ctx)
	{
		return new Argument(ctx.type.getText(),ctx.name.getText());
    }
}
