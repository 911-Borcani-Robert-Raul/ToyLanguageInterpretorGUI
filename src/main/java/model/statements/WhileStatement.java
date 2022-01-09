package model.statements;

import model.MyException;
import model.ProgramState;
import model.expressions.MyExpression;
import model.types.BoolType;
import model.types.Type;
import model.utils.MyIDictionary;
import model.values.BoolValue;
import model.values.Value;

public class WhileStatement implements IStatement {
    MyExpression expression;
    IStatement statement;

    public WhileStatement(MyExpression expression, IStatement statement) {
        this.expression = expression;
        this.statement = statement;
    }

    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        Value expressionResult = expression.eval(state.getSymTable(), state.getHeap());

        if (!expressionResult.getType().equals(new BoolType())) {
            throw new MyException("While expression must evaluate to boolean value");
        }

        BoolValue condition = (BoolValue)expressionResult;
        if (condition.getValue()) {
            state.getExecutionStack().push(deepCopy());
            state.getExecutionStack().push(statement);
        }

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typeExpr = expression.typecheck(typeEnv);
        if (typeExpr.equals(new BoolType())) {
            statement.typecheck(typeEnv.deepCopy());
            return typeEnv;
        }
        else {
            throw new MyException("The condition of WHILE has not the type bool");
        }
    }

    @Override
    public IStatement deepCopy() {
        return new WhileStatement(expression, statement);
    }

    @Override
    public String toString() {
        return "WHILE (" + expression + ") { " + statement + "} ";
    }
}
