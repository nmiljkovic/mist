package me.nemanjamiljkovic.mist;

import me.nemanjamiljkovic.mist.parser.mistLexer;
import me.nemanjamiljkovic.mist.parser.mistParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.IOException;

public class Run {
    public static void main(String args[]) throws IOException {
        ANTLRInputStream input = new ANTLRInputStream("hello");

        mistLexer lexer = new mistLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        mistParser parser = new mistParser(tokens);
        ParseTree tree = parser.program();
        System.out.println(tree.getText());
    }
}
