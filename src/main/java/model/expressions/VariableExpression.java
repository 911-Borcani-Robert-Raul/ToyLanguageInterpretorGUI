package model.expressions;

import model.MyException;
import model.types.Type;
import model.utils.ADTException;
import model.utils.MyHeap;
import model.utils.MyIDictionary;
import model.values.Value;

public class VariableExpression implements MyExpression {
    String id;

    public VariableExpression(String variableId) {
        id = variableId;
    }

    @Override
    public Value eval(MyIDictionary<String, Value> symTable, MyHeap heap) throws MyException {
        try {
            return symTable.lookup(id);
        }
        catch (ADTException error) {
            throw new ExpressionException("Symtable error: " + error);
        }
    }

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        return typeEnv.lookup(id);
    }

    @Override
    public MyExpression deepCopy() {
        return new VariableExpression(id);
    }

    @Override
    public String toString() {
        return id;
    }
}
