package model.statements;

import model.*;
import model.expressions.MyExpression;
import model.types.Type;
import model.utils.MyHeap;
import model.utils.MyIDictionary;
import model.utils.MyIStack;
import model.values.Value;

public class AssignStatement implements IStatement {
    String id;
    MyExpression exp;

    public AssignStatement(String id, MyExpression expression) {
        this.id = id;
        this.exp = expression;
    }

    public String toString() {
        return id + "=" + exp.toString();
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        MyIDictionary<String, Value> symTable = state.getSymTable();
        MyHeap heap = state.getHeap();
        if (symTable.isDefined(id)) {
            Value val = exp.eval(symTable, heap);
            Type typId = (symTable.lookup(id)).getType();
            if ((val.getType()).equals(typId)) {
                symTable.update(id, val);
            } else {
                throw new MyException("declared type of variable" + id +
                        " and type of the assigned expression do not match");
            }
        }
        else {
            throw new MyException("the used variable" + id + " was not declared before");
        }
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typeVar = typeEnv.lookup(id);
        Type typeExpr = exp.typecheck(typeEnv);

        if (typeVar.equals(typeExpr)) {
            return typeEnv;
        }
        else {
            throw new MyException("Assignment: right hand side and left hand side have different types");
        }
    }

    @Override
    public IStatement deepCopy() {
        return new AssignStatement(id, exp.deepCopy());
    }
}
