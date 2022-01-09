package model.statements;

import model.MyException;
import model.ProgramState;
import model.types.Type;
import model.utils.MyIDictionary;

public interface IStatement {
    ProgramState execute(ProgramState state) throws MyException;

    MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException;

    IStatement deepCopy();
}
