package model.expressions;

import model.*;
import model.types.BoolType;
import model.types.IntType;
import model.types.Type;
import model.utils.MyHeap;
import model.utils.MyIDictionary;
import model.values.BoolValue;
import model.values.Value;



public class LogicExpression implements MyExpression {
    MyExpression expression1, expression2;
    LogicExpressionOperator operator;     // 1 - AND, 2 - OR

    LogicExpression(MyExpression expression1, MyExpression expression2, LogicExpressionOperator operator) {
        this.expression1 = expression1;
        this.expression2 = expression2;
        this.operator = operator;
    }

    @Override
    public Value eval(MyIDictionary<String, Value> symTable, MyHeap heap) throws MyException {
        Value v1, v2;
        v1 = expression1.eval(symTable, heap);
        if (v1.getType().equals(new BoolType())) {
            v2 = expression2.eval(symTable, heap);

            if (v2.getType().equals(new BoolType())) {
                BoolValue b1 = (BoolValue)v1;
                BoolValue b2 = (BoolValue)v2;

                if (operator == LogicExpressionOperator.AND) {
                    boolean result = b1.getValue() && b2.getValue();
                    return new BoolValue(result);
                } else if (operator == LogicExpressionOperator.OR) {
                    boolean result = b1.getValue() || b2.getValue();
                    return new BoolValue(result);
                } else {
                    throw new MyException("Invalid operator for logic expression!");
                }
            }
            else {
                throw new MyException("Second expression not boolean!");
            }
        } else {
            throw new MyException("First expression not boolean!");
        }
    }

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type type1, type2;
        type1 = expression1.typecheck(typeEnv);
        type2 = expression2.typecheck(typeEnv);

        if (type1.equals(new BoolType())) {
            if (type2.equals(new BoolType())) {
                return new BoolType();
            }
            else {
                throw new MyException("Second operand not bool");
            }
        }
        else {
            throw new MyException("Second operand not bool");
        }
    }

    @Override
    public MyExpression deepCopy() {
        return new LogicExpression(expression1.deepCopy(), expression2.deepCopy(), operator);
    }

    @Override
    public String toString() {
        String result = expression1.toString();

        if (operator == LogicExpressionOperator.AND)
            result += " AND ";
        else
            result += " OR ";

        result += expression2.toString();
        return result;
    }
}
