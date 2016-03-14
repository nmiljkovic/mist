package me.nemanjamiljkovic.mist.visitor;

import me.nemanjamiljkovic.mist.parser.mistBaseVisitor;
import me.nemanjamiljkovic.mist.parser.mistParser;

import java.util.HashMap;
import java.util.Map;

public class InterpreterVisitor extends mistBaseVisitor<Object> {
    private Map<String, Integer> variables;

    public InterpreterVisitor() {
        this.variables = new HashMap<String, Integer>();
    }

    @Override
    public Object visitAssignStatement(mistParser.AssignStatementContext ctx) {
        Integer rhsValue = (Integer) ctx.expression().accept(this);
        String lhsDesignator = (String) ctx.designator().accept(this);

        variables.put(lhsDesignator, rhsValue);
        System.out.println(lhsDesignator + " = " + rhsValue.toString());
        return null;
    }

    @Override
    public Object visitVariableIdentifier(mistParser.VariableIdentifierContext ctx) {
        return ctx.Identifier().toString();
    }

    @Override
    public Object visitVariableAccessExpression(mistParser.VariableAccessExpressionContext ctx) {
        String variableName = ctx.designator().getText();
        if (!variables.containsKey(variableName)) {
            throw new RuntimeException(String.format("Variable '%s' not initialized!", variableName));
        }

        return variables.get(variableName);
    }

    @Override
    public Object visitStatementList(mistParser.StatementListContext ctx) {
        for (mistParser.StatementContext statement : ctx.statement()) {
            statement.accept(this);
        }

        return null;
    }

    @Override
    public Object visitIfStatement(mistParser.IfStatementContext ctx) {
        Boolean condition = (Boolean) ctx.expression().accept(this);

        if (condition) {
            return ctx.thenStatements.accept(this);
        }

        if (ctx.elseStatements != null) {
            return ctx.elseStatements.accept(this);
        }

        return null;
    }

    @Override
    public Object visitBinaryExpression(mistParser.BinaryExpressionContext ctx) {
        String operand = ctx.operand.getText();
        Integer lhs = (Integer) ctx.lhs.accept(this);
        Integer rhs = (Integer) ctx.rhs.accept(this);

        if (operand.equals("+")) {
            return lhs + rhs;
        } else if (operand.equals("-")) {
            return lhs - rhs;
        } else if (operand.equals("*")) {
            return lhs * rhs;
        } else if (operand.equals("/")) {
            return lhs / rhs;
        } else {
            throw new RuntimeException(String.format("Invalid binary expression operand '%s'", operand));
        }
    }

    @Override
    public Object visitLogicalExpression(mistParser.LogicalExpressionContext ctx) {
        String operand = ctx.operand.getText();
        Boolean lhs = (Boolean) ctx.lhs.accept(this);
        Boolean rhs = (Boolean) ctx.rhs.accept(this);

        if (operand.equals("&&")) {
            return lhs && rhs;
        } else if (operand.equals("||")) {
            return lhs || rhs;
        } else {
            throw new RuntimeException(String.format("Invalid logical expression operand '%s'", operand));
        }
    }

    @Override
    public Object visitComparisonExpression(mistParser.ComparisonExpressionContext ctx) {
        String operand = ctx.operand.getText();
        Object lhs = ctx.lhs.accept(this);
        Object rhs = ctx.rhs.accept(this);

        if (operand.equals("==")) {
            return lhs.equals(rhs);
        } else if (operand.equals("<=")) {
            return (Integer) lhs <= (Integer) rhs;
        } else if (operand.equals(">=")) {
            return (Integer) lhs >= (Integer) rhs;
        } else if (operand.equals("<")) {
            return (Integer) lhs < (Integer) rhs;
        } else if (operand.equals(">")) {
            return (Integer) lhs > (Integer) rhs;
        } else {
            throw new RuntimeException(String.format("Invalid comparison expression operand '%s'", operand));
        }
    }

    @Override
    public Object visitMinusExpression(mistParser.MinusExpressionContext ctx) {
        return -(Integer) ctx.expression().accept(this);
    }

    @Override
    public Object visitParenExpression(mistParser.ParenExpressionContext ctx) {
        return ctx.expression().accept(this);
    }

    @Override
    public Object visitConstantExpression(mistParser.ConstantExpressionContext ctx) {
        return Integer.parseInt(ctx.constant().getText());
    }
}
