package controller;

import model.MyException;
import model.ProgramState;
import model.statements.IStatement;
import model.utils.ADTException;
import model.utils.MyHeap;
import model.utils.MyIDictionary;
import model.utils.MyIStack;
import model.values.RefValue;
import model.values.Value;
import repository.IProgramStateRepository;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Controller {
    IProgramStateRepository states;
    ExecutorService executor;

    public IProgramStateRepository getRepository() {
        return states;
    }

    public Controller(IProgramStateRepository states) {
        this.states = states;
    }

    public void oneStepForAllPrograms(List<ProgramState> programs) {
        programs.forEach(program -> {
            try {
                states.logPrgStateExec(program);
            } catch (MyException e) {
                e.printStackTrace();
            }
        });

        List<Callable<ProgramState>> callList = programs.stream()
                .map((ProgramState p) -> (Callable<ProgramState>)(p::runOneStep))
                .collect(Collectors.toList());

        try {
            List<ProgramState> newProgramList = executor.invokeAll(callList).stream()
                    .map(future -> {
                        try {
                            return future.get();
                        } catch (InterruptedException | ExecutionException e) {
                            System.out.println(e.toString());
                        }
                        return null;
                    }).filter(Objects::nonNull)
                    .collect(Collectors.toList());
            programs.addAll(newProgramList);
            programs.forEach(program -> {
                try {
                    states.logPrgStateExec(program);
                } catch (MyException e) {
                    e.printStackTrace();
                }
            });
            states.setProgramsList(programs);
        }
        catch (InterruptedException ignored) {
        }
    }

    public void runOneStep() throws MyException {
        executor = Executors.newFixedThreadPool(2);
        // remove the completed programs
        List<ProgramState> programList = removeCompletedPrograms(states.getProgramsList());
        if (programList.size() > 0){
            MyHeap heap = programList.get(0).getHeap();
            List<MyIDictionary<String, Value>> symTables = programList.stream()
                            .map(ProgramState::getSymTable)
                    .collect(Collectors.toList());
            heap.setContent((HashMap<Integer, Value>) garbageCollector(
                    getAddressesFromSymTables(symTables),
                    getAddressesFromValues(heap.getContent().values()),
                    heap
            ));
            oneStepForAllPrograms(programList);
        }

        executor.shutdownNow();
        // HERE the repository still contains at least one Completed Prg
        // and its List<PrgState> is not empty. Note that oneStepForAllPrg calls the method
        // setPrgList of repository in order to change the repository
        // update the repository state
        states.setProgramsList(programList);
    }

    Map<Integer, Value> garbageCollector(Collection<Integer> symTableAddresses,
                                          Collection<Integer> heapAddresses,
                                          MyHeap heap) {
        return heap.keys().stream()
                .filter(e -> symTableAddresses.contains(e) || heapAddresses.contains(e))
                .collect(Collectors.toMap(e -> e,
                        e -> {
                            try {
                                return heap.lookup(e);
                            }
                            catch (ADTException ignored) {
                            }
                            return null;
                        }));
    }

    private Collection<Integer> getAddressesFromSymTables(List<MyIDictionary<String, Value>> symTables) {
        Set<Integer> addresses = new HashSet<Integer>();
        symTables.stream().map(table -> getAddressesFromValues(table.getContent().values()))
                .forEach(addresses::addAll);
        return addresses;
    }

    Collection<Integer> getAddressesFromValues(Collection<Value> symTableValues) {
        return symTableValues.stream()
                .filter(v -> v instanceof RefValue)
                .map(v ->  ((RefValue)v).getAddress())
                .collect(Collectors.toList());
    }

    public List<ProgramState> removeCompletedPrograms(List<ProgramState> programs) {
        return programs.stream()
                .filter(ProgramState::isNotCompleted)
                .collect(Collectors.toList());
    }

    void displayState(ProgramState state) {
        System.out.println("Current program state: " + state.toString() + "\n");
    }
}
