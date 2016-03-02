grammar mist;

program
    :   'program' '{' mainFunction '}'
    ;

mainFunction
    :   'void' 'main' '(' ')' '{' printStatement '}'
    ;

printStatement
    :   'print' '(' expression=HelloWorld ')' ';'
    ;

HelloWorld
    :   '"hello world"'
    ;

Whitespace
    :   [ \t]+
        -> skip
    ;

Newline
    :   (   '\r' '\n'?
        |   '\n'
        )
        -> skip
    ;
