import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.CharStreams;
import java.util.*;
import java.io.IOException;

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
	interpreterLexer lex = new interpreterLexer(input);
	
	// get the stream of tokens from the scanner
	CommonTokenStream tokens = new CommonTokenStream(lex);
	
	// create a parser
	interpreterParser parser = new interpreterParser(tokens);
	
	// and parse anything from the grammar for "start"
	ParseTree parseTree = parser.start();

	// Construct an interpreter and run it on the parse tree
	Interpreter interpreter = new Interpreter();
	start result=(start)interpreter.visit(parseTree);
	
	System.out.println("The result is: "+result.eval(new Environment()));
    }
}

class Interpreter extends AbstractParseTreeVisitor<AST> implements interpreterVisitor<AST> {

    public static Environment env=new Environment();
    
    public AST visitStart(interpreterParser.StartContext ctx){
	return new decl((block) visit(ctx.p), (expr) visit(ctx.e));
    };

    public AST visitAssignment(interpreterParser.AssignmentContext ctx){
	return new Assign(ctx.x.getText(), (expr) visit(ctx.e));
    };
    
    public AST visitParenthesis(interpreterParser.ParenthesisContext ctx){
	return visit(ctx.e);
    };
    
    public AST visitVariable(interpreterParser.VariableContext ctx){
	return new Variable(ctx.x.getText());
    };
    
    public AST visitLevelTwo(interpreterParser.LevelTwoContext ctx){
	if (ctx.op.getText().equals("*"))
	    return new Mult((expr)visit(ctx.e1),(expr)visit(ctx.e2));
	else
	    return new Div((expr)visit(ctx.e1),(expr)visit(ctx.e2));
    };

    public AST visitLevelOne(interpreterParser.LevelOneContext ctx){
	if (ctx.op.getText().equals("+"))
	    return new Add((expr)visit(ctx.e1),(expr)visit(ctx.e2));
	else
	    return new Sub((expr)visit(ctx.e1),(expr)visit(ctx.e2));
    }

    public AST visitConstant(interpreterParser.ConstantContext ctx){
	return new Constant(Double.parseDouble(ctx.c.getText())); 
    };

    public AST visitSingle(interpreterParser.SingleContext ctx){
	List<cmd> s=new ArrayList<cmd>();
	s.add((cmd)visit(ctx.s));
	return new Sequence(s);
    }
    public AST visitSequence(interpreterParser.SequenceContext ctx){
	List<cmd> s=new ArrayList<cmd>();
	for(interpreterParser.StmtContext cmd: ctx.s) s.add((cmd)visit(cmd));
	return new Sequence(s);
    }
    
    public AST visitConditional(interpreterParser.ConditionalContext ctx){
	return new If((cond)visit(ctx.c),(block)visit(ctx.p1),(block)visit(ctx.p2));
    };

    public AST visitWhile(interpreterParser.WhileContext ctx){
	return new While((cond)visit(ctx.c),(block)visit(ctx.p));
    };

    public AST visitConjunction(interpreterParser.ConjunctionContext ctx){
	return new And((cond)visit(ctx.c1),(cond)visit(ctx.c2));
    };

    public AST visitDisjunction(interpreterParser.DisjunctionContext ctx){
	return new Or((cond)visit(ctx.c1),(cond)visit(ctx.c2));
    };

    public AST visitNegation(interpreterParser.NegationContext ctx){
	return new Not((cond)visit(ctx.c));
    };

    public AST visitComparison(interpreterParser.ComparisonContext ctx){
	expr e1=(expr)visit(ctx.e1);
	expr e2=(expr)visit(ctx.e2);
	
	if (ctx.op.getText().equals("=="))
	    return new Equals(e1,e2);
	else if (ctx.op.getText().equals("<"))
	    return new Smaller(e1,e2);
	else if (ctx.op.getText().equals(">"))
	    return new Greater(e1,e2);
	else if (ctx.op.getText().equals("<="))
	    return new SmallerEqual(e1,e2);
	else if (ctx.op.getText().equals(">="))
	    return new GreaterEqual(e1,e2);
	else //if (ctx.op.getText().equals("!="))
	    return new Unequal(e1,e2);
    };

    public AST visitParenthesisCondition(interpreterParser.ParenthesisConditionContext ctx){
	return visit(ctx.c);
    };
}
