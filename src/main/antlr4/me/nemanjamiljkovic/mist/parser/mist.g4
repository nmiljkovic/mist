grammar mist;

program
    :   'program' '{' mainFunction '}'
    ;

mainFunction
    :   'void' 'main' '(' ')' variableDeclarationList* '{' statementList '}'
    ;

variableDeclarationList
    :   typeSpecifier variableNameList ';'
    ;

variableNameList
    :   Identifier (',' Identifier)*
    ;

typeSpecifier
    :   'int'
    |   'bool'
    |   'char'
    ;

statementList
    :   statement*
    ;

statement
    :   designator '=' expression ';'       # assignStatement
    ;

designator
    :   Identifier      # variableIdentifier
    ;

expression
    :   lhs=expression operand='*' rhs=expression   # binaryExpression
    |   lhs=expression operand='/' rhs=expression   # binaryExpression
    |   lhs=expression operand='+' rhs=expression   # binaryExpression
    |   lhs=expression operand='-' rhs=expression   # binaryExpression
    |   '(' expression ')'          # parenExpression
    |   '-' expression              # minusExpression
    |   designator                  # variableAccessExpression
    |   constant                    # constantExpression
    ;

constant
    :   IntegerLiteral      # integerConstant
    |   CharacterLiteral    # characterConstant
    |   BooleanLiteral      # booleanConstant
    ;

BooleanLiteral
    :   'true'
    |   'false'
    ;

CharacterLiteral
    :   '\'' SingleCharacter '\''
    |   '\'' EscapeSequence '\''
    ;

IntegerLiteral
    :   Digit+
    ;

Identifier
    :   IdentifierNondigit
        (   IdentifierNondigit
        |   Digit
        )*
    ;

fragment
SingleCharacter
    :   ~['\\]
    ;

fragment
EscapeSequence
    :   '\\' [btnfr"'\\]
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
