package com.example.calculator;
public class Expression {
    String expression;
    public Expression(){
        expression = "";

    }
    public String render(){
        return expression;
    }
    public void remove(){
        if (expression.length() > 0) {
            expression = expression.substring(0, expression.length() - 1);
        }
    }
    public void clear(){
        expression = "";
    }
    public void add(String tokenChar){
        this.expression += tokenChar;
    }

    public String Calculate() throws Exception {
        double value = Calculator.GetValue(expression);
        clear();
        if (value == (int) value){
            return String.valueOf((int) value);
        }
        return String.valueOf(value);
    }
}
