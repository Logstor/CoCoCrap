import java.util.*;

public class Environment{
	HashMap<String,Double> env;
	
	Environment() { env=new HashMap<String,Double>(); }
	
    public Double getVariable(String name){
		Double d=env.get(name);
		if (d==null){
			System.err.println("Variable "+name+" undefined\n");
			System.exit(-1);
		}
		return d;
    }
}
