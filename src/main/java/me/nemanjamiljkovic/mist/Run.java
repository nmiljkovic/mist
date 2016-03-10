package me.nemanjamiljkovic.mist;

import me.nemanjamiljkovic.mist.parser.mistLexer;
import me.nemanjamiljkovic.mist.parser.mistParser;
import me.nemanjamiljkovic.mist.visitor.CodeFormatterVisitor;
import me.nemanjamiljkovic.mist.visitor.InterpreterVisitor;
import me.nemanjamiljkovic.mist.visitor.SemanticCheckVisitor;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Run {
    public static void main(String args[]) throws IOException {
        ANTLRInputStream input = new ANTLRInputStream(new FileReader(new File("tests/main.mj")));

        mistLexer lexer = new mistLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        mistParser parser = new mistParser(tokens);
        ParseTree tree = parser.program();

        CodeFormatterVisitor formatter = new CodeFormatterVisitor();
        String output = formatter.visit(tree);

        System.out.println("Formatting result:");
        System.out.println(output);

        if (parser.getNumberOfSyntaxErrors() != 0) {
            return;
        }

        System.out.println("\n------");
        System.out.println("Semantic check in progress...");
        SemanticCheckVisitor semanticChecker = new SemanticCheckVisitor();
        semanticChecker.visit(tree);
        System.out.println("Program is semantically valid!");

        System.out.println("\n------");
        System.out.println("Interpreter output:\n");
        InterpreterVisitor interpreter = new InterpreterVisitor();
        interpreter.visit(tree);
    }
}
