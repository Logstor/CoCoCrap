import java.util.HashMap;
import java.util.Map.Entry;
import java.util.List;
import java.util.ArrayList;

class faux{ // collection of non-OO auxiliary functions (currently just error)
    public static void error(String msg) 
    {
        System.err.println("Interpreter error: "+msg);
        System.exit(-1);
    }
}

abstract class AST 
{ 
    String tab(int noOfTabs){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i<noOfTabs; i++){
            sb.append("    ");
        }
        return sb.toString();
    }
}

class Start extends AST {

    public List<DataTypeDef> datatypedefs;

    Start(List<DataTypeDef> datatypedefs) {
	    this.datatypedefs=datatypedefs;
    }

    public String translate()
    {
        StringBuilder sb = new StringBuilder();

        // Header
        sb.append("import java.util.*;\n\n");
        sb.append("abstract class AST{}\n\n");

        // Add all datatype definitions
        for (DataTypeDef def : datatypedefs) { sb.append(def.translate()); }

        return sb.toString();
    }
}

class DataTypeDef extends AST 
{
    public String dataTypeName;
    public String functionHead;
    public List<Alternative> alternatives;

    DataTypeDef(String dataTypeName, String functionHead, List<Alternative> alternatives)
    {
        this.dataTypeName=dataTypeName;
        this.functionHead=functionHead;
        this.alternatives=alternatives;
    }

    public String translate()
    {
        StringBuilder sb = new StringBuilder();

        // Class header
        sb.append( String.format("abstract class %s extends AST\n{\n", dataTypeName) );

        // Function
        sb.append( String.format("%spublic abstract %s;\n", tab(1), functionHead) );
        sb.append("};\n");

        for (Alternative alt : alternatives) 
        { 
            sb.append("\n");
            sb.append( alt.translate(dataTypeName, functionHead) );
            sb.append("\n");
        }

        return sb.toString();
    }

}

class Alternative extends AST 
{
    public String constructor;
    public List<Argument> arguments;
    public String code;
    
    Alternative(String constructor, List<Argument> arguments,  String code)
    {
        this.constructor=constructor;
        this.arguments=arguments;
        this.code=code;
    }

    public String translate(String dataTypeDef, String functionHead)
    {
        StringBuilder sb = new StringBuilder();

        // Class header
        sb.append( String.format("class %s extends %s\n{\n",constructor, dataTypeDef) );

        // Class fields
        for (Argument arg : arguments) { sb.append( String.format("%s%s %s;\n", tab(1), arg.type, arg.name) ); }

        // Constructor
        int argLength = arguments.size();
        sb.append( String.format("%s%s(", tab(1), constructor) );

        for (int i=0; i<argLength; i++)
        {
            sb.append(arguments.get(i).toString());

            if (i != argLength-1)
                sb.append(", ");
        }

        sb.append( String.format(")\n%s{\n", tab(1)) );

        for (Argument arg : arguments)
        { sb.append( String.format("%sthis.%s = %s;\n", tab(2), arg.name, arg.name) ); }

        sb.append( String.format("%s}\n", tab(1)) );

        sb.append(String.format("%spublic %s %s\n",tab(1),functionHead,code));

        // TODO: FJERN!
        //System.out.println(String.format("Constructor: %s\nCode: %s\nDataTypeDef: %s\nFunctionHead: %s", constructor, code, dataTypeDef, functionHead));
        //for (Argument arg : arguments) { System.out.println(String.format("Type: %s\nName: %s\n\n", arg.type, arg.name)); }
        // 

        // Ending bracket
        sb.append("}\n");

        return sb.toString(); 
    }
}

class Argument extends AST 
{
    public String type;
    public String name;
    Argument(String type, String name) { this.type=type; this.name=name; }

    @Override
    public String toString() {
        return String.format("%s %s", type, name);
    }
}