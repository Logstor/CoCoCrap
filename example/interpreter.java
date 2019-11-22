import java.util.*;

abstract class AST{}

abstract class start extends AST
{};

class decl extends start
{
    block b;
    expr e;
    decl(block b, expr e)
    {
        this.b = b;
        this.e = e;
    }
    public  Double eval(Environment env)  { b.eval(env); return e.eval(env); }
}

abstract class block extends AST
{};

class Sequence extends block
{
    List<cmd> l;
    Sequence(List<cmd> l)
    {
        this.l = l;
    }
    public  void eval(Environment env)  { for(cmd c:l) c.eval(env); }
}

abstract class cmd extends AST
{};

class Assign extends cmd
{
    String x;
    expr e;
    Assign(String x, expr e)
    {
        this.x = x;
        this.e = e;
    }
    public  void eval(Environment env)  { Double d=e.eval(env); env.setVariable(x,d); }
}


class If extends cmd
{
    cond c;
    block b1;
    block b2;
    If(cond c, block b1, block b2)
    {
        this.c = c;
        this.b1 = b1;
        this.b2 = b2;
    }
    public  void eval(Environment env)  { if (c.eval(env)) b1.eval(env); else b2.eval(env); }
}


class While extends cmd
{
    cond c;
    block b;
    While(cond c, block b)
    {
        this.c = c;
        this.b = b;
    }
    public  void eval(Environment env)  { while (c.eval(env)) b.eval(env); }
}

abstract class cond extends AST
{};

class Equals extends cond
{
    expr e1;
    expr e2;
    Equals(expr e1, expr e2)
    {
        this.e1 = e1;
        this.e2 = e2;
    }
    public  boolean eval(Environment env) { Double d1=e1.eval(env);
                            Double d2=e2.eval(env);
                            return d1.equals(d2); }
}


class Greater extends cond
{
    expr e1;
    expr e2;
    Greater(expr e1, expr e2)
    {
        this.e1 = e1;
        this.e2 = e2;
    }
    public  boolean eval(Environment env) {Double d1=e1.eval(env);
                            Double d2=e2.eval(env);
                            return d1>d2; }
}


class Smaller extends cond
{
    expr e1;
    expr e2;
    Smaller(expr e1, expr e2)
    {
        this.e1 = e1;
        this.e2 = e2;
    }
    public  boolean eval(Environment env) {Double d1=e1.eval(env);
                            Double d2=e2.eval(env);
                            return d1<d2; }
}


class GreaterEqual extends cond
{
    expr e1;
    expr e2;
    GreaterEqual(expr e1, expr e2)
    {
        this.e1 = e1;
        this.e2 = e2;
    }
    public  boolean eval(Environment env) { Double d1=e1.eval(env);
                            Double d2=e2.eval(env);
                            return d1>=d2; }
}


class SmallerEqual extends cond
{
    expr e1;
    expr e2;
    SmallerEqual(expr e1, expr e2)
    {
        this.e1 = e1;
        this.e2 = e2;
    }
    public  boolean eval(Environment env) { Double d1=e1.eval(env);
                            Double d2=e2.eval(env);
                            return d1<=d2; }
}


class Unequal extends cond
{
    expr e1;
    expr e2;
    Unequal(expr e1, expr e2)
    {
        this.e1 = e1;
        this.e2 = e2;
    }
    public  boolean eval(Environment env) {Double d1=e1.eval(env);
                            Double d2=e2.eval(env);
                            return d1!=d2; }
}


class And extends cond
{
    cond c1;
    cond c2;
    And(cond c1, cond c2)
    {
        this.c1 = c1;
        this.c2 = c2;
    }
    public  boolean eval(Environment env) { return c1.eval(env) && c2.eval(env); }
}


class Or extends cond
{
    cond c1;
    cond c2;
    Or(cond c1, cond c2)
    {
        this.c1 = c1;
        this.c2 = c2;
    }
    public  boolean eval(Environment env) { return c1.eval(env) || c2.eval(env); }
}


class Not extends cond
{
    cond c;
    Not(cond c)
    {
        this.c = c;
    }
    public  boolean eval(Environment env) { return !c.eval(env); }
}

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


class Div extends expr
{
    expr e1;
    expr e2;
    Div(expr e1, expr e2)
    {
        this.e1 = e1;
        this.e2 = e2;
    }
    public  Double eval(Environment env)  { return e1.eval(env) / e2.eval(env); }
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


class Sub extends expr
{
    expr e1;
    expr e2;
    Sub(expr e1, expr e2)
    {
        this.e1 = e1;
        this.e2 = e2;
    }
    public  Double eval(Environment env)  { return e1.eval(env) - e2.eval(env); }
}


