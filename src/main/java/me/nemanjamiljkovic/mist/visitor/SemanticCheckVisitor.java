package me.nemanjamiljkovic.mist.visitor;

import me.nemanjamiljkovic.mist.parser.mistBaseVisitor;
import me.nemanjamiljkovic.mist.parser.mistParser;
import me.nemanjamiljkovic.mist.semantic.Symbol;
import me.nemanjamiljkovic.mist.semantic.Type;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.HashMap;
import java.util.Map;

public class SemanticCheckVisitor extends mistBaseVisitor<Object> {
    private final Type voidType = new Type("void");
    private final Type intType = new Type("int");
    private final Type charType = new Type("char");
    private final Type boolType = new Type("bool");

    private final Map<String, Symbol> symbols;

    public SemanticCheckVisitor() {
        this.symbols = new HashMap<String, Symbol>();
    }

    @Override
    public Object visitProgram(mistParser.ProgramContext ctx) {
        // Initialize global symbols (type definitions)
        symbols.put("void", new Symbol("void", voidType, Symbol.Kind.Type));
        symbols.put("int", new Symbol("int", intType, Symbol.Kind.Type));
        symbols.put("char", new Symbol("char", charType, Symbol.Kind.Type));
        symbols.put("bool", new Symbol("bool", boolType, Symbol.Kind.Type));

        return super.visitProgram(ctx);
    }

    @Override
    public Object visitMainFunction(mistParser.MainFunctionContext ctx) {
        for (mistParser.VariableDeclarationListContext vctx : ctx.variableDeclarationList()) {
            Symbol typeSymbol = (Symbol) vctx.typeSpecifier().accept(this);
            for (TerminalNode variable : vctx.variableNameList().Identifier()) {
                String name = variable.getText();
                if (symbols.containsKey(name)) {
                    throw new RuntimeException(String.format("Cannot define variable '%s' as it already exists", name));
                }

                symbols.put(name, new Symbol(name, typeSymbol.getType(), Symbol.Kind.Variable));
            }
        }

        return super.visitMainFunction(ctx);
    }

    @Override
    public Object visitTypeSpecifier(mistParser.TypeSpecifierContext ctx) {
        String typeName = ctx.getText();
        if (!symbols.containsKey(typeName)) {
            throw new RuntimeException(String.format("Type '%s' does not exist in current scope.", typeName));
        }

        Symbol symbol = symbols.get(typeName);

        if (symbol.getKind() != Symbol.Kind.Type) {
            throw new RuntimeException(String.format("Identifier '%s' used as type is not a type", typeName));
        }

        return symbol;
    }

    @Override
    public Object visitAssignStatement(mistParser.AssignStatementContext ctx) {
        Type lhsType = (Type) ctx.designator().accept(this);
        Type rhsType = (Type) ctx.expression().accept(this);

        if (!lhsType.equals(rhsType)) {
            throw new RuntimeException(
                String.format(
                    "Type mismatch at line %d:\n%s\nExpected '%s', got '%s'",
                    ctx.start.getLine(), ctx.getText(), lhsType.toString(), rhsType.toString()
                )
            );
        }

        return null;
    }

    @Override
    public Object visitBinaryExpression(mistParser.BinaryExpressionContext ctx) {
        Type lhsType = (Type) ctx.lhs.accept(this);
        Type rhsType = (Type) ctx.rhs.accept(this);

        if (lhsType != intType && rhsType != intType) {
            throw new RuntimeException(
                String.format(
                    "Type mismatch for binary expression at line %d:\nBoth operands expected to be int",
                    ctx.start.getLine()
                )
            );
        }

        return lhsType;
    }

    @Override
    public Object visitLogicalExpression(mistParser.LogicalExpressionContext ctx) {
        Type lhsType = (Type) ctx.lhs.accept(this);
        Type rhsType = (Type) ctx.rhs.accept(this);

        if (lhsType != boolType && rhsType != boolType) {
            throw new RuntimeException(
                String.format(
                    "Type mismatch for logical expression at line %d:\nBoth operands expected to be bool\n" +
                        "Lhs operand typeof: %s\nRhs operand typeof: %s",
                    ctx.start.getLine(), lhsType.toString(), rhsType.toString()
                )
            );
        }

        return boolType;
    }

    @Override
    public Object visitComparisonExpression(mistParser.ComparisonExpressionContext ctx) {
        Type lhsType = (Type) ctx.lhs.accept(this);
        Type rhsType = (Type) ctx.rhs.accept(this);
        String operand = ctx.operand.getText();

        if (operand.equals("==")) {
            if (!lhsType.equals(rhsType)) {
                throw new RuntimeException(
                    String.format(
                        "Type mismatch for comparison expression at line %d:\n" +
                            "Lhs operand typeof: %s\nRhs operand typeof: %s",
                        ctx.start.getLine(), lhsType.toString(), rhsType.toString()
                    )
                );
            }

            return boolType;
        }

        if (operand.equals("<=") || operand.equals(">=") || operand.equals("<") || operand.equals(">")) {
            if (lhsType != intType && rhsType != intType) {
                throw new RuntimeException(
                    String.format(
                        "Type mismatch for binary expression at line %d:\nBoth operands expected to be int\n" +
                            "Lhs operand typeof: %s\nRhs operand typeof: %s",
                        ctx.start.getLine(), lhsType.toString(), rhsType.toString()
                    )
                );
            }

            return boolType;
        }

        throw new RuntimeException(String.format("Unhandled comparison operator '%s'", operand));
    }

    @Override
    public Object visitIfStatement(mistParser.IfStatementContext ctx) {
        Type conditionType = (Type) ctx.expression().accept(this);
        if (conditionType != boolType) {
            throw new RuntimeException(String.format(
                "Expected condition expression to be typeof bool; provided typeof: %s",
                conditionType.toString()
            ));
        }

        return null;
    }

    @Override
    public Object visitMinusExpression(mistParser.MinusExpressionContext ctx) {
        Type type = (Type) ctx.expression().accept(this);
        if (type != intType) {
            throw new RuntimeException(
                String.format(
                    "Type mismatch for minus expression at line %d:\nExpected operand to be int",
                    ctx.start.getLine()
                )
            );
        }

        return type;
    }

    @Override
    public Object visitParenExpression(mistParser.ParenExpressionContext ctx) {
        return ctx.expression().accept(this);
    }

    @Override
    public Object visitVariableIdentifier(mistParser.VariableIdentifierContext ctx) {
        String variableName = ctx.Identifier().getText();

        if (!symbols.containsKey(variableName)) {
            throw new RuntimeException(String.format("Variable '%s' does not exist in this context.", variableName));
        }

        Symbol symbol = symbols.get(variableName);
        if (symbol.getKind() != Symbol.Kind.Variable) {
            throw new RuntimeException(String.format("Identifier '%s' is not a variable", variableName));
        }

        return symbol.getType();
    }

    @Override
    public Object visitIntegerConstant(mistParser.IntegerConstantContext ctx) {
        return intType;
    }

    @Override
    public Object visitBooleanConstant(mistParser.BooleanConstantContext ctx) {
        return boolType;
    }

    @Override
    public Object visitCharacterConstant(mistParser.CharacterConstantContext ctx) {
        return charType;
    }
}
