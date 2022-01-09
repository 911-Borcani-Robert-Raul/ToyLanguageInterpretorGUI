package model.expressions;

import model.MyException;
import model.types.BoolType;
import model.types.IntType;
import model.types.Type;
import model.utils.MyHeap;
import model.utils.MyIDictionary;
import model.values.BoolValue;
import model.values.IntValue;
import model.values.Value;



public class RelationalExpression implements MyExpression {
    MyExpression expression1, expression2;
    RelationalExpressionOperator operator;

    public RelationalExpression(MyExpression expression1,
                                MyExpression expression2,
                                RelationalExpressionOperator operator) {
        this.expression1 = expression1;
        this.expression2 = expression2;
        this.operator = operator;
    }

    @Override
    public Value eval(MyIDictionary<String, Value> symTable, MyHeap heap) throws MyException {
        Value result1 = expression1.eval(symTable, heap);
        Value result2 = expression2.eval(symTable, heap);

        if (!result1.getType().equals(new IntType())
            || !result2.getType().equals(new IntType())) {
            throw new MyException("Relational expression must have int values for expressions");
        }

        int value1 = ((IntValue)result1).getVal();
        int value2 = ((IntValue)result2).getVal();

        BoolValue result;
        switch (operator) {
            case SmallerThan -> result = new BoolValue(value1 < value2);
            case SmallerThanOrEqual -> result = new BoolValue(value1 <= value2);
            case Equal -> result = new BoolValue(value1 == value2);
            case Different -> result = new BoolValue(value1 != value2);
            case GreaterThan -> result = new BoolValue(value1 > value2);
            case GreaterThanOrEqual -> result = new BoolValue(value1 >= value2);
            default -> throw new MyException("Unexpected value for operator");
        }

        return result;
    }

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type type1, type2;
        type1 = expression1.typecheck(typeEnv);
        type2 = expression2.typecheck(typeEnv);

        if (type1.equals(new IntType())) {
            if (type2.equals(new IntType())) {
                return new BoolType();
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
        return new RelationalExpression(
                expression1.deepCopy(),
                expression2.deepCopy(),
                operator
        );
    }

    @Override
    public String toString() {
        String operatorString = "";
        switch (operator) {
            case SmallerThan -> operatorString = "<";
            case SmallerThanOrEqual -> operatorString = "<=";
            case Equal -> operatorString = "==";
            case Different -> operatorString = "!=";
            case GreaterThan -> operatorString = ">";
            case GreaterThanOrEqual -> operatorString = ">=";
        }

        return expression1.toString() + " " + operatorString + " " + expression2.toString();
    }
}
