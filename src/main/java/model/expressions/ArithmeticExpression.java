package model.expressions;

import model.*;
import model.types.IntType;
import model.types.Type;
import model.utils.MyHeap;
import model.utils.MyIDictionary;
import model.values.IntValue;
import model.values.Value;



public class ArithmeticExpression implements MyExpression {
    MyExpression expression1;
    MyExpression expression2;
    ArithmeticExpressionOperator operator;

    public ArithmeticExpression(ArithmeticExpressionOperator operator, MyExpression expression1, MyExpression expression2) {
        this.expression1 = expression1;
        this.expression2 = expression2;
        this.operator = operator;
    }

    public ArithmeticExpression(char operator, MyExpression expression1, MyExpression expression2) {
        this.expression1 = expression1;
        this.expression2 = expression2;

        switch (operator) {
            case '+' -> this.operator = ArithmeticExpressionOperator.PLUS;
            case '-' -> this.operator = ArithmeticExpressionOperator.MINUS;
            case '*' -> this.operator = ArithmeticExpressionOperator.STAR;
            case '/' -> this.operator = ArithmeticExpressionOperator.DIVIDE;
        }
    }

    public Value eval(MyIDictionary<String, Value> tbl, MyHeap heap) throws MyException {
        Value v1,v2;
        v1= expression1.eval(tbl, heap);
        if (v1.getType().equals(new IntType())) {
            v2 = expression2.eval(tbl, heap);
            if (v2.getType().equals(new IntType())) {
                IntValue i1 = (IntValue)v1;
                IntValue i2 = (IntValue)v2;
                int n1,n2;
                n1= i1.getVal();
                n2 = i2.getVal();
                if (operator == ArithmeticExpressionOperator.PLUS)
                    return new IntValue(n1 + n2);
                if (operator == ArithmeticExpressionOperator.MINUS)
                    return new IntValue(n1 - n2);
                if (operator == ArithmeticExpressionOperator.STAR)
                    return new IntValue(n1 * n2);
                if (operator == ArithmeticExpressionOperator.DIVIDE)
                    if(n2 == 0)
                        throw new ExpressionException("division by zero");
                    else return new IntValue(n1 / n2);
            }
            else
                throw new MyException("second operand is not an integer");
        }
        else
            throw new MyException("first operand is not an integer");
        return null;
    }

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type type1, type2;
        type1 = expression1.typecheck(typeEnv);
        type2 = expression2.typecheck(typeEnv);

        if (type1.equals(new IntType())) {
            if (type2.equals(new IntType())) {
                return new IntType();
            }
            else {
                throw new MyException("Second operand not integer");
            }
        }
        else {
            throw new MyException("Second operand not integer");
        }
    }

    @Override
    public MyExpression deepCopy() {
        return new ArithmeticExpression(operator, expression1.deepCopy(), expression2.deepCopy());
    }

    @Override
    public String toString() {
        String result = expression1.toString();

        String sign = " ";
        switch (operator) {
            case PLUS -> sign = "+";
            case MINUS -> sign = "-";
            case STAR -> sign = "*";
            case DIVIDE -> sign = "/";
        }

        result += sign;
        result += expression2.toString();
        return result;
    }
}
