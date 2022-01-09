package repository;

import model.MyException;
import model.ProgramState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ProgramStateRepository implements IProgramStateRepository {
    private List<ProgramState> states;
    private final String logFilePath;

    public ProgramStateRepository(String logFilePath) {
        this.states = new ArrayList<>();
        this.logFilePath = logFilePath;

        try {
            clearLogFile();
        }
        catch (MyException error) {
            System.err.print(error.getMessage());
        }
    }

    @Override
    public void addProgramState(ProgramState state) {
        this.states.add(state);
    }

    @Override
    public void logPrgStateExec(ProgramState program) throws MyException {
        try {
            PrintWriter logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true)));

            logFile.write(program.getLogInfo() + "\n");
            logFile.close();
        }
        catch (IOException error) {
            throw new MyException("Error while writing log");
        }
    }

    @Override
    public void clearLogFile() throws MyException {
        try {
            PrintWriter logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, false)));

            logFile.write("");
            logFile.close();
        }
        catch (IOException error) {
            throw new MyException("Error while clearing log");
        }
    }

    @Override
    public List<ProgramState> getProgramsList() {
        return states;
    }

    @Override
    public void setProgramsList(List<ProgramState> programs) {
        states = programs;
    }

    @Override
    public ProgramState getCurrentProgramState() {
        return states.get(states.size() - 1);
    }

    @Override
    public ProgramState getProgramById(int programId) {
        for (var program : states) {
            if (program.getProgramId() == programId)
                return program;
        }

        return null;
    }
}
