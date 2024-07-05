package com.example.calculator;

import android.graphics.Path;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

public class Calculator {
    abstract static class CalcToken {
        abstract void print();
    }
    static class NumToken extends CalcToken {
        static HashSet<String> digits = new HashSet<>(Arrays.asList("0","1","2","3","4","5","6","7","8","9"));
        double number;
        public NumToken(double number){
            this.number = number;
        }
        void print(){
            System.out.println(number);
        }
    }
    static class OperatorToken extends CalcToken {
        static HashSet<String> Operators = new HashSet<>(Arrays.asList("/", "*", "+", "-", "^"));
        String operator;
        public OperatorToken(String operator) throws Exception {
            if(Operators.contains(operator)){
                this.operator = operator;
            }
            else{
                throw new Exception(operator + " is not a valid operator");
            }
        }
        void print(){
            System.out.println(operator);
        }
    }

    static class StartBracketToken extends CalcToken {
        public StartBracketToken(){

        }
        void print(){
            System.out.println("(");
        }
    }

    static class EndBracketToken extends CalcToken {
        public EndBracketToken(){
        }
        void print(){
            System.out.println(")");
        }
    }

    static public ArrayList<CalcToken> Tokenise(String inString) throws Exception {
        inString = inString.replace('f','/');
        inString = inString.replace('÷','/');
        inString = inString.replace('π','p');
        inString = inString.replace('✕','*');
        inString = inString.replace('−','-');
        ArrayList<CalcToken> expression = new ArrayList<>();
        String currentNumber = "";
        int stringLength = inString.length();

        for (int i = 0; i < stringLength; i++){
            String currentCharacter = inString.substring(i,i+1);

            if (OperatorToken.Operators.contains(currentCharacter)) { // Handling adding operators
                if (currentNumber.endsWith(".")) {
                    throw new Exception("Expression invalid - invalid number, cannot end with decimal point");
                } else {
                    if (!currentNumber.isEmpty()){
                        expression.add(new NumToken(Double.parseDouble(currentNumber)));
                        currentNumber = "";
                    }
                    expression.add(new OperatorToken(currentCharacter));
                }
            }
            else if ("()".contains(currentCharacter)) {
                if (currentNumber.endsWith(".")) {
                    throw new Exception("Expression invalid - invalid number, cannot end with decimal point");
                } else {
                    if (!currentNumber.isEmpty()){
                        expression.add(new NumToken(Double.parseDouble(currentNumber)));
                        currentNumber = "";
                    }
                    if (currentCharacter.equals("(")){
                        expression.add(new StartBracketToken());
                    } else{
                        expression.add(new EndBracketToken());
                    }
                }
            }

            else if(NumToken.digits.contains(currentCharacter)){ // Handling adding digits
                currentNumber += currentCharacter;
            }

            else if(currentCharacter.equals(".")) {// Handling adding decimal points
                if (currentNumber.contains(".")) {
                    throw new Exception("Expression invalid - invalid number, cannot have more than one decimal point");
                } else {
                    currentNumber += currentCharacter;
                }
            }
            else if(currentCharacter.equals("e")){
                if (currentNumber.endsWith(".")) {
                    throw new Exception("Expression invalid - invalid number, cannot end with decimal point");
                } else if (!currentNumber.isEmpty()){
                    expression.add(new NumToken(Double.parseDouble(currentNumber)));
                    currentNumber = "";
                }
                expression.add(new NumToken(Math.E));
            }
            else if (currentCharacter.equals("p")){
                if (currentNumber.endsWith(".")) {
                    throw new Exception("Expression invalid - invalid number, cannot end with decimal point");
                } else if (!currentNumber.isEmpty()){
                    expression.add(new NumToken(Double.parseDouble(currentNumber)));
                    currentNumber = "";
                }
                expression.add(new NumToken(Math.PI));
            }
        }

        if (!currentNumber.isEmpty()){
            expression.add(new NumToken(Double.parseDouble(currentNumber)));
        }
        if(expression.get(expression.size() - 1) instanceof OperatorToken){
            throw new Exception("Expression invalid - cannot end with operator");
        }
        if (currentNumber.endsWith(".")) {
            throw new Exception("Expression invalid - invalid number, cannot end with decimal point");
        }
        return expression;
    }
    static public ArrayList<CalcToken> Simplify(ArrayList<CalcToken> expression) throws Exception {
        ArrayList<CalcToken> simplified = new ArrayList<>();
        int expressionLength = expression.size();
        for (int i = 0; i < expressionLength; i++){
            CalcToken currentToken = expression.get(i);
            if (currentToken instanceof OperatorToken) { // Handling adding operators
                if (endsWithNumber(simplified)) {
                    simplified.add(currentToken);
                } else {
                    if (isSignModifier(currentToken)){
                        int sign = 1;
                        while (i < expressionLength){
                            CalcToken token = expression.get(i);
                            if(token instanceof OperatorToken){
                                OperatorToken opToken = (OperatorToken) token;
                                if (opToken.operator.equals("-")) {
                                    sign *= -1;
                                }
                                else if(!opToken.operator.equals("+")){
                                    throw new Exception("Expression invalid - cannot have this operator after others");
                                }
                                i++;
                            }
                            else{
                                if(sign == -1){
                                    simplified.add(new NumToken(-1));
                                    simplified.add(new OperatorToken("*"));
                                    i--;
                                    break;
                                }
                            }
                        }
                        if (i >= expressionLength){
                            throw new Exception("Expression invalid - cannot end with operator");
                        }
                    }
                    else {
                        throw new Exception("Expression invalid, double operator");
                    }
                }
            }

            else if (currentToken instanceof NumToken){
                if(endsWithNumber(simplified)){
                    simplified.add(new OperatorToken("*"));
                }
                simplified.add(currentToken);
            }
            else if (currentToken instanceof StartBracketToken) {
                int endBracket = i+1;
                int bracketDiff = 1;
                ArrayList<CalcToken> subExpression = new ArrayList<>();
                while (endBracket < expressionLength){
                    if (expression.get(endBracket) instanceof EndBracketToken) {
                        bracketDiff --;
                    } else if (expression.get(endBracket) instanceof StartBracketToken) {
                        bracketDiff ++;
                    }
                    if(bracketDiff == 0){
                        if (endsWithNumber(simplified)){
                            simplified.add(new OperatorToken("*"));
                        }
                        simplified.add(new NumToken(Calculate(subExpression)));
                        i = endBracket;
                        break;
                    }
                    else {
                        subExpression.add(expression.get(endBracket));
                    }
                    endBracket++;
                }

                if(bracketDiff > 0) {
                    throw new Exception("Expression invalid - unclosed bracket");
                }
            }
        }
        if(endsWithOperator(simplified)){
            throw new Exception("Expression invalid - cannot end with operator");
        }
        return simplified;
    }
    static public Double Calculate(ArrayList<CalcToken> expression) throws Exception {
        ArrayList<CalcToken> infix = Simplify(expression);
        Stack<String> operatorStack = new Stack<>();
        Stack<Double> operandStack = new Stack<>();
        HashMap<String, Integer> precedence = new HashMap<>();
        precedence.put("^", 3);
        precedence.put("/", 2);
        precedence.put("*", 2);
        precedence.put("+", 1);
        precedence.put("-", 1);
        for (int i = 0; i < infix.size(); i++){
            CalcToken currentToken = infix.get(i);
            if (currentToken instanceof OperatorToken){
                String operator = ((OperatorToken) currentToken).operator;
                if (operatorStack.isEmpty()){
                    operatorStack.push(operator);
                }
                else {
                    while (precedence.get(operator) <= precedence.get(operatorStack.peek())) {
                        String newOperator = operatorStack.pop();
                        Double operand2 = operandStack.pop();
                        Double operand1 = operandStack.pop();
                        operandStack.push(applyOperator(operand1, newOperator, operand2));
                        if (operatorStack.isEmpty()){
                            break;
                        }
                    }
                    operatorStack.push(operator);
                }
            }
            else {
                operandStack.add(((NumToken) currentToken).number);
            }
        }
        while (!operatorStack.isEmpty()) {
            String newOperator = operatorStack.pop();
            Double operand2 = operandStack.pop();
            Double operand1 = operandStack.pop();
            operandStack.push(applyOperator(operand1, newOperator, operand2));
        }
        return operandStack.pop();
    }

    static public double GetValue(String inString) throws Exception {
        ArrayList<CalcToken> expression = Tokenise(inString);
        return Calculate(expression);
    }
    static void printOperators(Stack<String> stack){
        String[] array = stack.toArray(new String[0]);
        System.out.println("Operators");
        for (int i = array.length -1; i >= 0; i--) {
            System.out.println(array[i]);
        }
    }
    static void printOperands(Stack<Float> stack){
        Float[] array = stack.toArray(new Float[0]);
        System.out.println("Operands");
        for (int i = array.length -1; i >= 0; i--) {
            System.out.println(array[i]);
        }
    }
    static public double applyOperator(Double a, String operator, Double b){
        switch (operator){
            case("^"):
                return (float) Math.pow(a,b);
            case("/"):
                return a/b;
            case("*"):
                return a*b;
            case("+"):
                return a+b;
            default:
                return a-b;
        }
    }
    static public boolean endsWithOperator(ArrayList<CalcToken> expression){
        if(expression.isEmpty()){
            return false;
        }
        return expression.get(expression.size() - 1) instanceof OperatorToken;
    }
    static public boolean endsWithNumber(ArrayList<CalcToken> expression){
        if(expression.isEmpty()){
            return false;
        }
        return !(expression.get(expression.size() - 1) instanceof OperatorToken);
    }
    private static boolean isSignModifier(CalcToken token) {
        if (!(token instanceof OperatorToken)){
            return false;
        }
        OperatorToken opToken = (OperatorToken) token;
        return (opToken.operator.equals("+") || opToken.operator.equals("-"));
    }
}

