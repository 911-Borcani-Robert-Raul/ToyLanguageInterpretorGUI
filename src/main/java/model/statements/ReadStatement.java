package model.statements;

import model.MyException;
import model.ProgramState;
import model.expressions.MyExpression;
import model.types.IntType;
import model.types.StringType;
import model.types.Type;
import model.utils.MyIDictionary;
import model.values.IntValue;
import model.values.StringValue;
import model.values.Value;

import java.io.BufferedReader;
import java.io.IOException;

public class ReadStatement implements IStatement {
    MyExpression expression;
    String variable;

    public ReadStatement(MyExpression expression, String variableName) {
        this.expression = expression;
        this.variable = variableName;
    }
    
    @Override
    public ProgramState execute(ProgramState state) throws MyException {
        MyIDictionary<String, Value> symbolTable = state.getSymTable();
        if (!(symbolTable.isDefined(variable) || symbolTable.lookup(variable).getType().equals(new IntType()))) {
            throw new MyException("Variable not defined or not integer!");
        }

        Value file = expression.eval(symbolTable, state.getHeap());
        if (!file.getType().equals(new StringType())) {
            throw new MyException("Expression for file not string!");
        }

        MyIDictionary<String, BufferedReader> fileTable = state.getFileTable();
        String fileName = ((StringValue)file).getValue();
        if (!fileTable.isDefined(fileName)) {
            throw new MyException("There is no such file open!");
        }

        BufferedReader reader = fileTable.lookup(fileName);
        String line;
        try {
            line = reader.readLine();
        }
        catch (IOException error) {
            throw new MyException("Could not read from file!");
        }

        if (line == null) {
            symbolTable.update(variable, new IntValue(0));
        }
        else {
            int read_value;
            try {
                read_value = Integer.parseInt(line);
            }
            catch (NumberFormatException error) {
                throw new MyException("Error reading number from file: " + error.getMessage());
            }
            symbolTable.update(variable, new IntValue(read_value));
        }

        return null;
    }

    @Override
    public MyIDictionary<String, Type> typecheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typeExpr = expression.typecheck(typeEnv);
        Type varType = typeEnv.lookup(variable);
        
        if (!varType.equals(new IntType())) {
            throw new MyException("Variable type for readStatement not integer");
        }
        if (!typeExpr.equals(new StringType())) {
            throw new MyException("Expression type for readStatement not string");
        }

        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return null;
    }

    @Override
    public String toString() {
        return variable + " = read(" + expression.toString() + ")";
    }
}
