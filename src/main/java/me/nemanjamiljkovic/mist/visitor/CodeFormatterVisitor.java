package me.nemanjamiljkovic.mist.visitor;

import me.nemanjamiljkovic.mist.parser.mistBaseVisitor;
import me.nemanjamiljkovic.mist.parser.mistParser;

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
        String output = this.indent() + "void main() {\n";
        this.indentationLevel++;
        output += ctx.printStatement().accept(this);
        this.indentationLevel--;
        output += this.indent() + "}\n";

        return output;
    }

    @Override
    public String visitPrintStatement(mistParser.PrintStatementContext ctx) {
        return this.indent() + "print(" + ctx.expression.getText() + ");\n";
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
