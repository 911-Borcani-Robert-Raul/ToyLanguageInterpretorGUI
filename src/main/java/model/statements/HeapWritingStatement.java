package model.statements;

import model.MyException;
import model.ProgramState;
import model.expressions.MyExpression;
import model.types.IntType;
import model.types.RefType;
import model.types.StringType;
import model.types.Type;
import model.utils.MyHeap;
import model.utils.MyIDictionary;
import model.values.RefValue;
import model.values.Value;

public class HeapWritingStatement implements IStatement {
    String variableName;
    MyExpression expression;

    public HeapWritingStatement(String variableName, MyExpression expression) {
        this.variableName = variableName;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        MyIDictionary<String, Value> symTable = state.getSymTable();
        MyHeap heapTable = state.getHeap();

        if (!symTable.isDefined(variableName)) {
            throw new MyException("Variable name for heap not defined");
        }

        Value variableValue = symTable.lookup(variableName);
        if (!(variableValue.getType() instanceof RefType)) {
            throw new MyException("Variable value not a ref type for head writing");
        }

        int address = ((RefValue)variableValue).getAddress();
        if (!heapTable.isDefined(address))
            throw new MyException("Address not defined");

        Value result = expression.eval(symTable, heapTable);
        if (!result.getType().equals(heapTable.lookup(address).getType())) {
            throw new MyException("Types of heap location and expression do not match");
        }

        heapTable.update(address, result);
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typeExpr = expression.typecheck(typeEnv);
        Type varType = typeEnv.lookup(variableName);

        if (!varType.equals(new RefType(typeExpr))) {
            throw new MyException("heapWritingStatement: expression not of inner type of varType or varType not a ref");
        }

        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return null;
    }

    @Override
    public String toString() {
        return "hW(" + variableName + ", " + expression + ")";
    }
}
