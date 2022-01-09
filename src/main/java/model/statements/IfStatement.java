package model.statements;

import model.*;
import model.expressions.MyExpression;
import model.types.BoolType;
import model.types.Type;
import model.utils.MyIDictionary;
import model.utils.MyIStack;
import model.values.BoolValue;
import model.values.Value;

public class IfStatement implements IStatement {
    MyExpression exp;
    IStatement thenStatement;
    IStatement elseStatement;
    public IfStatement(MyExpression e, IStatement t, IStatement el) {
        exp = e;
        thenStatement = t;
        elseStatement = el;
    }

    public String toString(){
        return "(IF(" + exp.toString()+") THEN(" + thenStatement.toString()
                + ")ELSE(" + elseStatement.toString() + "))";
    }

    public ProgramState execute(ProgramState state) throws MyException {
        Value result = exp.eval(state.getSymTable(), state.getHeap());
        if (result instanceof BoolValue boolResult) {
            IStatement statement;
            if (boolResult.getValue()) {
                statement = thenStatement;
            }
            else {
                statement = elseStatement;
            }

            MyIStack<IStatement> stk = state.getExecutionStack();
            stk.push(statement);

            return null;
        }
        else
            throw new MyException("please provide a boolean expression in an if statement");
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typeExpr = exp.typecheck(typeEnv);
        if (typeExpr.equals(new BoolType())) {
            thenStatement.typecheck(typeEnv.deepCopy());
            elseStatement.typecheck(typeEnv.deepCopy());
            return typeEnv;
        }
        else {
            throw new MyException("The condition of IF has not the type bool");
        }
    }

    @Override
    public IStatement deepCopy() {
        return new IfStatement(exp.deepCopy(), thenStatement.deepCopy(), elseStatement.deepCopy());
    }
}
