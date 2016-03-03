grammar mist;

program
    :   'program' '{' mainFunction '}'
    ;

mainFunction
    :   'void' 'main' '(' ')' variableDeclarationList* '{' '}'
    ;

variableDeclarationList
    :   typeSpecifier variableNameList ';'
    ;

variableNameList
    :   Identifier (',' Identifier)*
    ;

typeSpecifier
    :   'int'
    ;

Identifier
    :   IdentifierNondigit
        (   IdentifierNondigit
        |   Digit
        )*
    ;

fragment
IdentifierNondigit
    :   Nondigit
    ;

fragment
Nondigit
    :   [a-zA-Z_]
    ;

fragment
Digit
    :   [0-9]
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
