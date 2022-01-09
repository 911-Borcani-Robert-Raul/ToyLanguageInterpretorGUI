package service;

import controller.Controller;
import javafx.beans.property.SimpleObjectProperty;
import repository.IProgramStateRepository;

public class Service {
    private Controller controller;
    private IProgramStateRepository repository;

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
