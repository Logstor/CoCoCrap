import java.util.*;

abstract class AST{}

abstract class expr extends AST
{};

class Constant extends expr
{
    Double v;
    Constant(Double v)
    {
        this.v = v;
    }
    public  Double eval(Environment env)  { return v; }
}


class Variable extends expr
{
    String name;
    Variable(String name)
    {
        this.name = name;
    }
    public  Double eval(Environment env)  { return env.getVariable(name); }
}


class Mult extends expr
{
    expr e1;
    expr e2;
    Mult(expr e1, expr e2)
    {
        this.e1 = e1;
        this.e2 = e2;
    }
    public  Double eval(Environment env)  { return e1.eval(env) * e2.eval(env); }
}


class Add extends expr
{
    expr e1;
    expr e2;
    Add(expr e1, expr e2)
    {
        this.e1 = e1;
        this.e2 = e2;
    }
    public  Double eval(Environment env)  { return e1.eval(env) + e2.eval(env); }
}


