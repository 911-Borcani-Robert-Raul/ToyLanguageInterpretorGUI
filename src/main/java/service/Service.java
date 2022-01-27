package service;

import controller.Controller;
import repository.IProgramStateRepository;

public class Service {
    private final Controller controller;
    private final IProgramStateRepository repository;

    public Service(Controller controller) {
        this.controller = controller;
        repository = controller.getRepository();
    }

    public void runOneStep() {
        controller.runOneStep();
    }

    public IProgramStateRepository getRepository() {
        return repository;
    }
}
