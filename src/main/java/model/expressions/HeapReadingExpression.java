package model.expressions;

import model.MyException;
import model.types.RefType;
import model.types.Type;
import model.utils.MyDictionary;
import model.utils.MyHeap;
import model.utils.MyIDictionary;
import model.values.RefValue;
import model.values.Value;

public class HeapReadingExpression implements MyExpression {
    MyExpression expression;

    public HeapReadingExpression(MyExpression expression) {
        this.expression = expression;
    }

    @Override
    public Value eval(MyIDictionary<String, Value> symTable, MyHeap heap) throws MyException {
        Value result = expression.eval(symTable, heap);
        if (!(result instanceof RefValue))
            throw new MyException("Result of expression for heap reading must be a RefValue");

        int address = ((RefValue)result).getAddress();
        if (!heap.isDefined(address))
            throw new MyException("Address not defined");

        return heap.lookup(address);
    }

    @Override
    public Type typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type type = expression.typecheck(typeEnv);
        if (type instanceof RefType ref) {
            return ref.getInner();
        }
        else {
            throw new MyException("The HeapReadingExpression argument not a Ref Type");
        }
    }

    @Override
    public MyExpression deepCopy() {
        return new HeapReadingExpression(expression);
    }

    @Override
    public String toString() {
        return "heapRead(" + expression + ")";
    }
}
