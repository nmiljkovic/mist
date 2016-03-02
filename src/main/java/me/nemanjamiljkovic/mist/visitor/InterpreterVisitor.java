package me.nemanjamiljkovic.mist.visitor;

import me.nemanjamiljkovic.mist.parser.mistBaseVisitor;
import me.nemanjamiljkovic.mist.parser.mistParser;

public class InterpreterVisitor extends mistBaseVisitor<Void> {
    @Override
    public Void visitPrintStatement(mistParser.PrintStatementContext ctx) {
        String text = ctx.expression.getText();

        // get rid of the start/end quote
        System.out.println(text.substring(1, text.length() - 1));

        return null;
    }
}
