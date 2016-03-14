package me.nemanjamiljkovic.mist.visitor;

import me.nemanjamiljkovic.mist.parser.mistBaseVisitor;
import me.nemanjamiljkovic.mist.parser.mistParser;
import org.antlr.v4.runtime.tree.TerminalNode;

public class CodeFormatterVisitor extends mistBaseVisitor<String> {
    private int indentationLevel = 0;

    @Override
    public String visitProgram(mistParser.ProgramContext ctx) {
        String output = this.indent("program {\n");
        this.indentationLevel++;
        output += ctx.mainFunction().accept(this);
        this.indentationLevel--;
        output += this.indent("}\n");

        return output;
    }

    @Override
    public String visitMainFunction(mistParser.MainFunctionContext ctx) {
        String output = this.indent("void main()\n");
        this.indentationLevel++;
        for (mistParser.VariableDeclarationListContext vctx : ctx.variableDeclarationList()) {
            output += vctx.accept(this);
        }
        this.indentationLevel--;
        output += this.indent("{\n");
        this.indentationLevel++;
        output += ctx.statementList().accept(this);
        this.indentationLevel--;
        output += this.indent("}\n");

        return output;
    }

    @Override
    public String visitVariableDeclarationList(mistParser.VariableDeclarationListContext ctx) {
        return this.indent(
            ctx.typeSpecifier().accept(this) + " " +
                ctx.variableNameList().accept(this) + ";\n"
        );
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
            output += statementContext.accept(this);
        }

        return output;
    }

    @Override
    public String visitSingleIfBodyStatement(mistParser.SingleIfBodyStatementContext ctx) {
        this.indentationLevel++;
        String output = ctx.statement().accept(this);
        this.indentationLevel--;
        return output;
    }

    @Override
    public String visitAssignStatement(mistParser.AssignStatementContext ctx) {
        return this.indent(
            ctx.designator().accept(this) + " = " + ctx.expression().accept(this) + ";\n"
        );
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
    public String visitLogicalExpression(mistParser.LogicalExpressionContext ctx) {
        return ctx.lhs.accept(this) + " " + ctx.operand.getText() + " " + ctx.rhs.accept(this);
    }

    @Override
    public String visitComparisonExpression(mistParser.ComparisonExpressionContext ctx) {
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

    @Override
    public String visitIfStatement(mistParser.IfStatementContext ctx) {
        String output = this.indent(
            "if (" + ctx.expression().accept(this) + ")\n"
        );

        output += ctx.thenStatements.accept(this);
        if (ctx.elseStatements != null) {
            output += this.indent("else\n");
            output += ctx.elseStatements.accept(this);
        }

        return output;
    }

    @Override
    public String visitBlock(mistParser.BlockContext ctx) {
        String output = this.indent("{\n");
        this.indentationLevel++;
        output += ctx.statementList().accept(this);
        this.indentationLevel--;
        output += this.indent("}\n");

        return output;
    }

    private String indent() {
        return indent("");
    }

    private String indent(String line) {
        String indentation = "    ";
        String spaces = "";
        for (int i = 0; i < this.indentationLevel; i++) {
            spaces += indentation;
        }
        return spaces + line;
    }
}
