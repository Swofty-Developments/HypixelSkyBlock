package net.swofty.commons.skyblock.item.reforge;

import org.tinylog.Logger;

import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReforgeExpressionEvaluator {
    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{([^}]+)\\}");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("-?\\d+(?:\\.\\d+)?");

    public static double evaluate(String expression, Map<String, Double> variables) {
        try {
            // Replace variables with their values
            String processedExpression = replaceVariables(expression, variables);

            // Evaluate the mathematical expression
            return evaluateMathExpression(processedExpression);
        } catch (Exception e) {
            Logger.error(e, "Error evaluating reforge expression: {}", expression);
            return 0.0;
        }
    }

    private static String replaceVariables(String expression, Map<String, Double> variables) {
        Matcher matcher = VARIABLE_PATTERN.matcher(expression);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String varName = matcher.group(1);
            Double value = variables.get(varName);
            if (value != null) {
                matcher.appendReplacement(result, value.toString());
            } else {
                System.err.println("Variable not found: " + varName);
                matcher.appendReplacement(result, "0");
            }
        }
        matcher.appendTail(result);

        return result.toString();
    }

    private static double evaluateMathExpression(String expression) {
        // Remove whitespace
        expression = expression.replaceAll("\\s+", "");

        // Handle parentheses first
        while (expression.contains("(")) {
            int lastOpen = expression.lastIndexOf('(');
            int firstClose = expression.indexOf(')', lastOpen);

            if (firstClose == -1) {
                throw new IllegalArgumentException("Mismatched parentheses");
            }

            String subExpression = expression.substring(lastOpen + 1, firstClose);
            double result = evaluateSimpleExpression(subExpression);
            expression = expression.substring(0, lastOpen) + result + expression.substring(firstClose + 1);
        }

        return evaluateSimpleExpression(expression);
    }

    private static double evaluateSimpleExpression(String expression) {
        // Stack for numbers
        Stack<Double> numbers = new Stack<>();
        // Stack for operators
        Stack<Character> operators = new Stack<>();

        int i = 0;
        while (i < expression.length()) {
            char c = expression.charAt(i);

            // If current character is a number or decimal point
            if (Character.isDigit(c) || c == '.' || (c == '-' && (i == 0 || isOperator(expression.charAt(i - 1))))) {
                StringBuilder num = new StringBuilder();

                // Handle negative numbers
                if (c == '-') {
                    num.append(c);
                    i++;
                }

                // Read the full number
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    num.append(expression.charAt(i));
                    i++;
                }

                numbers.push(Double.parseDouble(num.toString()));
                continue;
            }

            // If current character is an operator
            if (isOperator(c)) {
                while (!operators.isEmpty() && hasHigherPrecedence(operators.peek(), c)) {
                    double result = applyOperation(operators.pop(), numbers.pop(), numbers.pop());
                    numbers.push(result);
                }
                operators.push(c);
            }

            i++;
        }

        // Apply remaining operations
        while (!operators.isEmpty()) {
            double result = applyOperation(operators.pop(), numbers.pop(), numbers.pop());
            numbers.push(result);
        }

        return numbers.pop();
    }

    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }

    private static boolean hasHigherPrecedence(char op1, char op2) {
        return getPrecedence(op1) >= getPrecedence(op2);
    }

    private static int getPrecedence(char operator) {
        return switch (operator) {
            case '+', '-' -> 1;
            case '*', '/' -> 2;
            case '^' -> 3;
            default -> 0;
        };
    }

    private static double applyOperation(char operator, double b, double a) {
        return switch (operator) {
            case '+' -> a + b;
            case '-' -> a - b;
            case '*' -> a * b;
            case '/' -> {
                if (b == 0) throw new ArithmeticException("Division by zero");
                yield a / b;
            }
            case '^' -> Math.pow(a, b);
            default -> throw new IllegalArgumentException("Unknown operator: " + operator);
        };
    }
}