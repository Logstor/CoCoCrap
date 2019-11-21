grammar coco;

/* Compiler-compiler */

start   : dtd+=dataTypeDef+ EOF ;

// definition of an algebraic data type
dataTypeDef : 'DATA'
 name=ID 'WITH' fun=JAVACODE '=' as=alternatives ';' ; 

// alternatives of an algebraic data type and concrete syntax for it. 
alternatives : as+=alternative ('|' as+=alternative)* ;
alternative  : cons=ID '(' as=arguments ')' code=JAVACODE ;

// an argument consists of a type and a name (both are IDs for the lexer)
arguments : as+=argument (',' as+=argument)* ;
argument  : type=ID name=ID ;

ID	   : ('A'..'Z'|'a'..'z'|'_')('A'..'Z'|'a'..'z'|'_'|'0'..'9'|'<'|'>')* ;
WHITESPACE : [ \n\t\r]+ -> skip;
COMMENT    : '//'(~[\n])* -> skip;
JAVACODE   : '{' ~[{}]* '}';  // allowing no further braces in code.
