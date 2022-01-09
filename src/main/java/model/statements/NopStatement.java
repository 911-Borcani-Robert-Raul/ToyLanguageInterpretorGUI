package model.statements;

import model.MyException;
import model.ProgramState;
import model.types.Type;
import model.utils.MyIDictionary;

public class NopStatement implements IStatement {
    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        return state;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return null;
    }

    @Override
    public String toString() {
        return "nop()";
    }
}
