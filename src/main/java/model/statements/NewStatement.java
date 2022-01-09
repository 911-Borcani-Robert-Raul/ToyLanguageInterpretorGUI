package model.statements;

import model.MyException;
import model.ProgramState;
import model.expressions.MyExpression;
import model.types.RefType;
import model.types.Type;
import model.utils.MyHeap;
import model.utils.MyIDictionary;
import model.values.RefValue;
import model.values.Value;

public class NewStatement implements IStatement {
    String variableName;
    MyExpression expression;

    public NewStatement(String variableName, MyExpression expression) {
        this.variableName = variableName;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        MyIDictionary<String, Value> symbolTable = state.getSymTable();
        if (!symbolTable.isDefined(variableName))
            throw new MyException("Variable name for new statement not defined!");
        Type variableType = symbolTable.lookup(variableName).getType();

        if (!(variableType instanceof RefType))
            throw new MyException("Variable type for new must be a RefType");

        Value expressionResult = expression.eval(symbolTable, state.getHeap());
        Type innerTypeOfReference = ((RefValue)symbolTable.lookup(variableName)).getLocationType();
        if (!expressionResult.getType().equals(innerTypeOfReference))
            throw new MyException("Variable type and expression type must be the same for a new operation");

        MyHeap heap = state.getHeap();
        int address = heap.getNewFreeAddress();
        heap.update(address, expressionResult);

        // ((RefValue)symbolTable.lookup(variableName)).setAdrress(address);
        symbolTable.update(variableName, new RefValue(address, expressionResult.getType()));
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typeVar = typeEnv.lookup(variableName);
        Type typeExpr = expression.typecheck(typeEnv);
        if (typeVar.equals(new RefType(typeExpr))) {
            return typeEnv;
        }
        else {
            throw new MyException("NEW statement: right hand side and left hand side have different types");
        }
    }

    @Override
    public IStatement deepCopy() {
        return new NewStatement(variableName, expression.deepCopy());
    }

    @Override
    public String toString() {
        return "new(" + variableName + ", " + expression + ")";
    }
}
