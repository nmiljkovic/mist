package me.nemanjamiljkovic.mist.visitor;

import me.nemanjamiljkovic.mist.parser.mistBaseVisitor;
import me.nemanjamiljkovic.mist.parser.mistParser;
import org.antlr.v4.runtime.tree.TerminalNode;

public class CodeFormatterVisitor extends mistBaseVisitor<String> {
    private int indentationLevel = 0;

    @Override
    public String visitProgram(mistParser.ProgramContext ctx) {
        String output = this.indent() + "program {\n";
        this.indentationLevel++;
        output += ctx.mainFunction().accept(this);
        this.indentationLevel--;
        output += this.indent() + "}\n";

        return output;
    }

    @Override
    public String visitMainFunction(mistParser.MainFunctionContext ctx) {
        String output = this.indent() + "void main()\n";
        this.indentationLevel++;
        for (mistParser.VariableDeclarationListContext vctx : ctx.variableDeclarationList()) {
            output += vctx.accept(this);
        }
        this.indentationLevel--;
        output += this.indent() + "{\n";
        this.indentationLevel++;
        output += ctx.statementList().accept(this);
        this.indentationLevel--;
        output += this.indent() + "}\n";

        return output;
    }

    @Override
    public String visitVariableDeclarationList(mistParser.VariableDeclarationListContext ctx) {
        return this.indent() +
            ctx.typeSpecifier().accept(this) + " " +
            ctx.variableNameList().accept(this) + ";\n";
    }

    @Override
    public String visitVariableNameList(mistParser.VariableNameListContext ctx) {
        String output = "";

        for (TerminalNode identifier : ctx.Identifier()) {
            output += ", " + identifier.getText();
        }

        return output.substring(2);
    }

    @Override
    public String visitTypeSpecifier(mistParser.TypeSpecifierContext ctx) {
        return ctx.getText();
    }

    @Override
    public String visitStatementList(mistParser.StatementListContext ctx) {
        String output = "";

        for (mistParser.StatementContext statementContext : ctx.statement()) {
            output += this.indent() + statementContext.accept(this);
        }

        return output;
    }

    @Override
    public String visitAssignStatement(mistParser.AssignStatementContext ctx) {
        return ctx.designator().accept(this) + " = " + ctx.expression().accept(this) + ";\n";
    }

    @Override
    public String visitVariableIdentifier(mistParser.VariableIdentifierContext ctx) {
        return ctx.Identifier().getText();
    }

    @Override
    public String visitBinaryExpression(mistParser.BinaryExpressionContext ctx) {
        return ctx.lhs.accept(this) + " " + ctx.operand.getText() + " " + ctx.rhs.accept(this);
    }

    @Override
    public String visitMinusExpression(mistParser.MinusExpressionContext ctx) {
        return "-" + ctx.expression().accept(this);
    }

    @Override
    public String visitParenExpression(mistParser.ParenExpressionContext ctx) {
        return "(" + ctx.expression().accept(this) + ")";
    }

    @Override
    public String visitConstantExpression(mistParser.ConstantExpressionContext ctx) {
        return ctx.constant().getText();
    }

    private String indent() {
        String indentation = "    ";
        String spaces = "";
        for (int i = 0; i < this.indentationLevel; i++) {
            spaces += indentation;
        }
        return spaces;
    }
}
