package com.calculator.service;

import org.springframework.stereotype.Service;

@Service
public class CalculatorService {

    public double calculate(String expression) {
        if (expression == null || expression.isBlank()) {
            throw new IllegalArgumentException("Expression cannot be empty.");
        }

        Parser parser = new Parser(expression);
        double value = parser.parseExpression();

        if (!parser.isAtEnd()) {
            throw new IllegalArgumentException("Invalid expression.");
        }
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Result is not a finite number.");
        }
        return value;
    }

    /*
     * Small recursive-descent parser.
     * Supports:
     * + - * / % ^, parentheses,
     * sqrt(), sin(), cos(), tan(), log(), ln(), abs()
     *
     * Trigonometric functions use degrees because that is familiar on a calculator.
     */
    private static class Parser {
        private final String input;
        private int pos = 0;

        Parser(String input) {
            this.input = input.replace("×", "*").replace("÷", "/").replace("−", "-")
                    .replaceAll("\s+", "");
        }

        boolean isAtEnd() {
            return pos == input.length();
        }

        double parseExpression() {
            double value = parseTerm();
            while (!isAtEnd()) {
                char c = input.charAt(pos);
                if (c == '+') {
                    pos++;
                    value += parseTerm();
                } else if (c == '-') {
                    pos++;
                    value -= parseTerm();
                } else {
                    break;
                }
            }
            return value;
        }

        double parseTerm() {
            double value = parsePower();
            while (!isAtEnd()) {
                char c = input.charAt(pos);
                if (c == '*') {
                    pos++;
                    value *= parsePower();
                } else if (c == '/') {
                    pos++;
                    double divisor = parsePower();
                    if (divisor == 0) throw new IllegalArgumentException("Cannot divide by zero.");
                    value /= divisor;
                } else if (c == '%') {
                    pos++;
                    value %= parsePower();
                } else {
                    break;
                }
            }
            return value;
        }

        double parsePower() {
            double value = parseUnary();
            if (!isAtEnd() && input.charAt(pos) == '^') {
                pos++;
                value = Math.pow(value, parsePower());
            }
            return value;
        }

        double parseUnary() {
            if (!isAtEnd() && input.charAt(pos) == '+') {
                pos++;
                return parseUnary();
            }
            if (!isAtEnd() && input.charAt(pos) == '-') {
                pos++;
                return -parseUnary();
            }
            return parsePrimary();
        }

        double parsePrimary() {
            if (isAtEnd()) throw new IllegalArgumentException("Incomplete expression.");

            if (input.charAt(pos) == '(') {
                pos++;
                double value = parseExpression();
                expect(')');
                return value;
            }

            if (Character.isLetter(input.charAt(pos))) {
                String name = readName();
                if ("pi".equalsIgnoreCase(name)) return Math.PI;
                if ("e".equalsIgnoreCase(name)) return Math.E;

                expect('(');
                double argument = parseExpression();
                expect(')');

                return switch (name.toLowerCase()) {
                    case "sqrt" -> Math.sqrt(requireNonNegative(argument, "sqrt"));
                    case "sin" -> Math.sin(Math.toRadians(argument));
                    case "cos" -> Math.cos(Math.toRadians(argument));
                    case "tan" -> Math.tan(Math.toRadians(argument));
                    case "log" -> Math.log10(requirePositive(argument, "log"));
                    case "ln" -> Math.log(requirePositive(argument, "ln"));
                    case "abs" -> Math.abs(argument);
                    default -> throw new IllegalArgumentException("Unknown function: " + name);
                };
            }

            return readNumber();
        }

        private double readNumber() {
            int start = pos;
            boolean dotSeen = false;
            boolean digitSeen = false;

            while (!isAtEnd()) {
                char c = input.charAt(pos);
                if (Character.isDigit(c)) {
                    digitSeen = true;
                    pos++;
                } else if (c == '.' && !dotSeen) {
                    dotSeen = true;
                    pos++;
                } else {
                    break;
                }
            }

            if (!digitSeen) throw new IllegalArgumentException("Number expected.");
            return Double.parseDouble(input.substring(start, pos));
        }

        private String readName() {
            int start = pos;
            while (!isAtEnd() && Character.isLetter(input.charAt(pos))) pos++;
            return input.substring(start, pos);
        }

        private void expect(char expected) {
            if (isAtEnd() || input.charAt(pos) != expected) {
                throw new IllegalArgumentException("Expected '" + expected + "'.");
            }
            pos++;
        }

        private double requirePositive(double value, String function) {
            if (value <= 0) throw new IllegalArgumentException(function + " requires a positive value.");
            return value;
        }

        private double requireNonNegative(double value, String function) {
            if (value < 0) throw new IllegalArgumentException(function + " requires a non-negative value.");
            return value;
        }
    }
}
