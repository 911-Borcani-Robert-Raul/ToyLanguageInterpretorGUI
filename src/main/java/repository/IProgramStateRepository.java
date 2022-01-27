package repository;

import model.MyException;
import model.ProgramState;

import java.util.List;

public interface IProgramStateRepository {
    void addProgramState(ProgramState state);

    void logPrgStateExec(ProgramState program) throws MyException;
    void clearLogFile() throws MyException;

    List<ProgramState> getProgramsList();
    void setProgramsList(List<ProgramState> programs);

    ProgramState getCurrentProgramState();

    ProgramState getProgramById(int parseInt);
}
