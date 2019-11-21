grammar interpreter;

start   : p=prog e=expr EOF ;

prog : s=stmt           # Single
     | '{' s+=stmt* '}'  # Sequence
     ;

stmt : x=ID '=' e=expr ';'                        # Assignment
     | 'if' '(' c=cond ')' p1=prog 'else' p2=prog # Conditional
     | 'while' '(' c=cond ')' p=prog              # While
     ;

expr : e1=expr op=OP2 e2=expr # LevelTwo
     | e1=expr op=OP1 e2=expr # LevelOne
     | c=FLOAT                # Constant
     | x=ID                   # Variable
     | '(' e=expr ')'         # Parenthesis
     ;

cond : e1=expr op=CMP e2=expr # Comparison
     | '!' c=cond             # Negation
     | c1=cond '&&' c2=cond   # Conjunction
     | c1=cond '||' c2=cond   # Disjunction
     | '(' c=cond ')'         # ParenthesisCondition
     ;

// Lexer:

OP2  : ('*'|'/') ;
OP1  : ('+'|'-') ;
CMP  : ('=='|'<'|'>'|'<='|'>='|'!=');

ID   : ALPHA (ALPHA|NUM)* ;
FLOAT: NUM+ ('.' NUM+)? ;

ALPHA: [a-zA-Z_ÆØÅæøå] ;
NUM  : [0-9] ;

WHITESPACE : [ \n\t\r]+ -> skip;
COMMENT    : '//'~[\n]*  -> skip;
COMMENT2   : '/*' (~[*] | '*'~[/]  )*   '*/'  -> skip;
