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
import java.io.FileNotFoundException;
import java.io.FileReader;

public class OpenFileRead implements IStatement {
    MyExpression expression;

    public OpenFileRead(MyExpression expression) {
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        Value file = expression.eval(state.getSymTable(), state.getHeap());
        if (file.getType().equals(new StringType())) {
            MyIDictionary<String, BufferedReader> fileTable = state.getFileTable();
            String fileName = ((StringValue)file).getValue();

            if (!fileTable.isDefined(fileName)) {
                try {
                    BufferedReader buffer = new BufferedReader(new FileReader(fileName));
                    fileTable.update(fileName, buffer);
                } catch (FileNotFoundException e) {
                    throw new MyException(e.toString());
                }
            }
            else {
                throw new MyException("File already opened!");
            }
        }
        else {
            throw new MyException("Expression for file name is not a string!");
        }

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type exprType = expression.typecheck(typeEnv);
        if (exprType.equals(new StringType())) {
            return typeEnv;
        }
        else {
            throw new MyException("Provided expression for OpenFileRead is not a string");
        }
    }

    @Override
    public IStatement deepCopy() {
        return null;
    }

    @Override
    public String toString() {
        return "openReadFile(" + expression.toString() + ")";
    }
}
