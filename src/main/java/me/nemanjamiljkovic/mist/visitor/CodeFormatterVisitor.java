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

    private String indent() {
        String indentation = "    ";
        String spaces = "";
        for (int i = 0; i < this.indentationLevel; i++) {
            spaces += indentation;
        }
        return spaces;
    }
}
