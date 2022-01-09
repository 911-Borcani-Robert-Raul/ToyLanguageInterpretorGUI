package model.statements;

import model.MyException;
import model.ProgramState;
import model.expressions.MyExpression;
import model.types.StringType;
import model.types.Type;
import model.utils.MyIDictionary;
import model.values.StringValue;
import model.values.Value;

import java.io.BufferedReader;
import java.io.IOException;

public class CloseFileRead implements IStatement {
    MyExpression expression;

    public CloseFileRead(MyExpression expression) {
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        Value value = expression.eval(state.getSymTable(), state.getHeap());
        if (!value.getType().equals(new StringType())) {
            throw new MyException("Expression result for file to be closed is not of string type");
        }

        String fileName = ((StringValue)value).getValue();
        MyIDictionary<String, BufferedReader> fileTable = state.getFileTable();
        if (!fileTable.isDefined(fileName)) {
            throw new MyException("File to be closed is not opened!");
        }

        BufferedReader reader = fileTable.lookup(fileName);
        try {
            reader.close();
        }
        catch (IOException error) {
            throw new MyException(error.toString());
        }

        fileTable.delete(fileName);
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type exprType = expression.typecheck(typeEnv);
        if (exprType.equals(new StringType())) {
            return typeEnv;
        }
        else {
            throw new MyException("Provided expression for CloseFileRead is not a string");
        }
    }

    @Override
    public IStatement deepCopy() {
        return null;
    }

    @Override
    public String toString() {
        return "closeReadFile(" + expression.toString() + ")";
    }
}
