package model.expressions;

import model.MyException;
import model.types.Type;
import model.utils.MyHeap;
import model.utils.MyIDictionary;
import model.values.Value;

public interface MyExpression {
    public Value eval(MyIDictionary<String, Value> symTable, MyHeap heapTable) throws MyException;

    Type typecheck(MyIDictionary<String, Type> typeEnv) throws MyException;

    MyExpression deepCopy();
}
