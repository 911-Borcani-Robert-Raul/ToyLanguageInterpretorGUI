package model.statements;

import model.*;
import model.types.Type;
import model.utils.MyIDictionary;
import model.values.Value;

public class VariableDeclarationStatement implements IStatement {
    String id;
    Type type;
    public VariableDeclarationStatement(String id, Type type) {
        this.id = id;
        this.type = type;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        MyIDictionary<String, Value> table = state.getSymTable();

        if (table.isDefined(id)) {
            throw new MyException("Variable already defined!");
        }

        table.update(id, type.getDefaultValue());

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        typeEnv.update(id, type);
        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new VariableDeclarationStatement(id, type.deepCopy());
    }

    @Override
    public String toString() {
        return type + " " + id + ";";
    }
}
