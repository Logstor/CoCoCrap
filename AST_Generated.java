/*
 * Generated
 * Created by:
 * s160107 - Alfred Röttger Rydahl
 * s185097 - Rasmus Sander Larsen
 */


import java.util.*;

abstract class AST{}

abstract class Expr extends AST
{};

class Start extends Expr
{
    List<DataTypeDef> dataTypeDefs;
    Start(List<DataTypeDef> dataTypeDefs)
    {
        this.dataTypeDefs = dataTypeDefs;
    }
    public  String translate(String ... params)  { 
        StringBuilder sb = new StringBuilder();

        // Header
        sb.append("import java.util.*;\n\n");
        sb.append("abstract class AST{}\n\n");

        // Add all datatype definitions
        for (DataTypeDef def : dataTypeDefs) { sb.append(def.translate()); }

        return sb.toString(); }
}


class DataTypeDef extends Expr
{
    String dataTypeName;
    String functionHead;
    List<Alternative> alternatives;
    DataTypeDef(String dataTypeName, String functionHead, List<Alternative> alternatives)
    {
        this.dataTypeName = dataTypeName;
        this.functionHead = functionHead;
        this.alternatives = alternatives;
    }
    public  String translate(String ... params)  {
        StringBuilder sb = new StringBuilder();

        // Class header
        sb.append( String.format("abstract class %s extends AST\n{", dataTypeName) );

        // Function
        if ( functionHead.replace(" ", "").equals("") )
            sb.append( String.format("\n%spublic abstract %s;\n", "    ", functionHead) );
        sb.append("};\n");

        for (Alternative alt : alternatives) 
        { 
            sb.append("\n");
            sb.append( alt.translate(dataTypeName, functionHead) );
            sb.append("\n");
        }

        return sb.toString(); }
}


class Alternative extends Expr
{
    String constructor;
    List<Argument> arguments;
    String code;
    Alternative(String constructor, List<Argument> arguments, String code)
    {
        this.constructor = constructor;
        this.arguments = arguments;
        this.code = code;
    }
    public  String translate(String ... params)  { 
        StringBuilder sb = new StringBuilder();

        // Class header
        sb.append( String.format("class %s extends %s\n{\n",constructor, params[0]) );

        // Class fields
        for (Argument arg : arguments) { sb.append( String.format("%s%s %s;\n", "    ", arg.type, arg.name) ); }

        // Constructor
        int argLength = arguments.size();
        sb.append( String.format("%s%s(", "    ", constructor) );

        for (int i=0; i<argLength; i++)
        {
            sb.append(arguments.get(i).translate());

            if (i != argLength-1)
                sb.append(", ");
        }

        sb.append( String.format(")\n%s{\n", "    ") );

        for (Argument arg : arguments)
        { sb.append( String.format("%sthis.%s = %s;\n", "        ", arg.name, arg.name) ); }

        sb.append( String.format("%s}\n", "    ") );

        // Function
        sb.append(String.format("%spublic %s %s\n","    ", params[1], code));

        // Ending bracket
        sb.append("}\n");

        return sb.toString(); 
}
}


class Argument extends Expr
{
    String type;
    String name;
    Argument(String type, String name)
    {
        this.type = type;
        this.name = name;
    }
    public  String translate(String ... params)  { return String.format("%s %s", type, name); }
}


