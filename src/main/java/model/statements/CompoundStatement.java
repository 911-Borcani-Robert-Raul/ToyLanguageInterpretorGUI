package model.statements;

import model.MyException;
import model.types.Type;
import model.utils.MyIDictionary;
import model.utils.MyIStack;
import model.ProgramState;

public class CompoundStatement implements IStatement {
    IStatement first;
    IStatement second;

    public CompoundStatement(IStatement first, IStatement second) {
        this.first = first;
        this.second = second;
    }

    public String toString() {
        return "(" + first.toString() + ", " + second.toString() + ")";
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        MyIStack<IStatement> stk = state.getExecutionStack();

        stk.push(second);
        stk.push(first);
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        MyIDictionary<String, Type> typeEnv1 = first.typecheck(typeEnv);
        return second.typecheck(typeEnv1);
    }

    @Override
    public IStatement deepCopy() {
        return new CompoundStatement(first.deepCopy(), second.deepCopy());
    }
}
