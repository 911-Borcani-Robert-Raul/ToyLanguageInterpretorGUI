package model.statements;

import model.MyException;
import model.ProgramState;
import model.types.Type;
import model.utils.MyIDictionary;
import model.utils.MyIStack;
import model.utils.MyStack;

public class ForkStatement implements IStatement {
    IStatement statement;

    public ForkStatement(IStatement statement) {
        this.statement = statement;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        MyIStack<IStatement> newStack = new MyStack<IStatement>();
        return new ProgramState(newStack, state.getSymTable().deepCopy(), state.getOutput(), state.getFileTable(), state.getHeap(), statement);
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        statement.typecheck(typeEnv.deepCopy());
        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return null;
    }

    @Override
    public String toString() {
        return "fork(" + statement + ")";
    }
}
