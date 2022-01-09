package model;

import model.statements.IStatement;
import model.utils.MyHeap;
import model.utils.MyIDictionary;
import model.utils.MyIList;
import model.utils.MyIStack;
import model.values.Value;

import java.io.BufferedReader;

public class ProgramState {
    static int idGenerator = 0;

    MyIStack<IStatement> executionStack;
    MyIDictionary<String, Value> symTable;
    MyIDictionary<String, BufferedReader> fileTable;
    MyIList<Value> output;
    MyHeap heapTable;
    IStatement originalProgram; //optional field, but good to have
    int id;

    public static synchronized int getId() {
        idGenerator += 1;
        return idGenerator;
    }

    public ProgramState(MyIStack<IStatement> stack, MyIDictionary<String, Value> symTable,
                        MyIList<Value> output, MyIDictionary<String, BufferedReader> fileTable,
                        MyHeap heapTable, IStatement program) {
        this.executionStack = stack;
        this.symTable = symTable;
        this.output = output;
        this.fileTable = fileTable;
        this.heapTable = heapTable;
        this.originalProgram = program.deepCopy();    //recreate the entire original prg
        this.executionStack.push(program);
        this.id = getId();
    }

    public MyIStack<IStatement> getExecutionStack() {
        return executionStack;
    }

    public MyIDictionary<String, Value> getSymTable() {
        return symTable;
    }

    public MyIList<Value> getOutput() {
        return output;
    }

    public MyIDictionary<String, BufferedReader> getFileTable() {
        return fileTable;
    }

    public boolean isNotCompleted() {
        return !executionStack.isEmpty();
    }

    public int getProgramId() {
        return id;
    }

    public ProgramState runOneStep() throws MyException {
        if (executionStack.isEmpty())
            throw new MyException("Program stack is empty!");
        IStatement currentStatement = executionStack.pop();
        return currentStatement.execute(this);
    }

    @Override
    public String toString() {
        return getLogInfo();
    }

    public String getLogInfo() {
        String result = "Program id: " + id + "\n";
        result += "ExeStack:\n";
        result += executionStack.getReversedString() + "\n";
        result += "Symtable:\n";
        result += symTable.toString() + "\n";
        result += "Out\n";
        result += output.toString() + "\n";
        result += "FileTable:\n";
        result += fileTable.toString() + "\n";
        result += "HeapTable:\n";
        result += heapTable.toString() + "\n";

        return result;
    }

    public MyHeap getHeap() {
        return heapTable;
    }
}
