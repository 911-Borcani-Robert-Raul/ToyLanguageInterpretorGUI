package model.expressions;

import model.MyException;
import model.types.Type;
import model.utils.MyHeap;
import model.utils.MyIDictionary;
import model.values.Value;

public class ValueExpression implements MyExpression {
    Value value;

    public ValueExpression(Value value) {
        this.value = value;
    }

    @Override
    public Value eval(MyIDictionary<String, Value> symTable, MyHeap heap) throws MyException {
        return value;
    }

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        return value.getType();
    }

    @Override
    public MyExpression deepCopy() {
        return new ValueExpression(value.deepCopy());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
