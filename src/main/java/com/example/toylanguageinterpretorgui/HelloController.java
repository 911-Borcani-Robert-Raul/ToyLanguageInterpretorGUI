package com.example.toylanguageinterpretorgui;

import controller.Controller;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.ProgramState;
import model.statements.IStatement;
import model.utils.*;
import model.values.Value;
import repository.IProgramStateRepository;
import repository.ProgramStateRepository;
import service.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

class TableRow {

    private final StringProperty string1;
    private final StringProperty string2;

    public TableRow(String string1, String string2) {
        this.string1 = new SimpleStringProperty(string1);
        this.string2 = new SimpleStringProperty(string2);

        this.string1.set(string1);
        this.string2.set(string2);
    }

    public String getString1() {
        return string1.get();
    }

    public void setString1(String newString) {
        this.string1.set(newString);
    }

    public String getString2() {
        return string2.get();
    }

    public void setString2(String newString) {
        this.string2.set(newString);
    }

    public StringProperty string1Property() {
        return string1;
    }

    public StringProperty string2Property() {
        return string2;
    }
}

public class HelloController implements Initializable {
    Parent root;
    IStatement currentProgram;
    MyIStack<IStatement> executionStack;
    MyIDictionary<String, Value> symbolTable;
    MyIList<Value> output;
    MyIDictionary<String, BufferedReader> fileTable;
    MyHeap heap;

    IProgramStateRepository repository;
    Service service;

    @FXML
    private Label numberOfStatesLabel;
    @FXML
    private ListView<String> executionStackListView;
    @FXML
    private ListView<String> programStatesIdentifiersListView;
    @FXML
    private ListView<String> outputListView;
    @FXML
    private ListView<String> fileTableListView;
    @FXML
    private TableView<TableRow> heapTableView;
    @FXML
    private TableView<TableRow> symbolTableView;
    @FXML
    private TableColumn<TableRow, String> heapTableAddressColumn;
    @FXML
    private TableColumn<TableRow, String> heapTableValueColumn;
    @FXML
    private TableColumn<TableRow, String> variableNameColumn;
    @FXML
    private TableColumn<TableRow, String> variableValueColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        heapTableAddressColumn.setCellValueFactory(cellData -> cellData.getValue().string1Property());
        heapTableValueColumn.setCellValueFactory(cellData -> cellData.getValue().string2Property());
        variableNameColumn.setCellValueFactory(cellData -> cellData.getValue().string1Property());
        variableValueColumn.setCellValueFactory(cellData -> cellData.getValue().string2Property());
    }

    @FXML
    protected void onChooseProgramState() {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("choose_program.fxml"));

        try {
            root = fxmlLoader.load();

            ChooseProgramController programStateController = fxmlLoader.getController();
            programStateController.getChosenProgram().addListener((obs, oldProgram, newProgram) -> {
                currentProgram = newProgram;
                executionStack = new MyStack<>();
                symbolTable = new MyDictionary<>();
                output = new MyList<>();
                fileTable = new MyDictionary<>();
                heap = new MyHeap();
                ProgramState program = new ProgramState(executionStack, symbolTable, output, fileTable, heap, currentProgram);
                IProgramStateRepository repo = new ProgramStateRepository("log.txt");
                repo.addProgramState(program);
                Controller controller = new Controller(repo);
                service = new Service(controller);
            });

            programStateController.getProgramStateCount().addListener((obs, oldNumber, newNumber) -> {
                numberOfStatesLabel.setText(newNumber.toString());
            });

            Stage stage = new Stage();
            stage.setTitle("Choose program");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onRunOneStepClicked(ActionEvent actionEvent) {
        service.runOneStep();
        repository = service.getRepository();
        updateAll();
    }

    private void updateAll() {
        updateProgramStatesIdentifiers();
        updateExecutionStack();
        updateOutput();
        updateFileTable();
        updateHeapTable();
        updateSymbolTable();
    }

    private void updateExecutionStack() {
        executionStackListView.getItems().clear();
        if (repository.getProgramsList().isEmpty())
            return;

        MyIStack<IStatement> executionStack = getCurrentProgram()
                .getExecutionStack();
        for (var item : executionStack.getReversedElements()) {
            executionStackListView.getItems().add(item);
        }
    }

    private void updateProgramStatesIdentifiers() {
        programStatesIdentifiersListView.getItems().clear();
        if (repository.getProgramsList().isEmpty())
            return;

        List<ProgramState> programs = repository.getProgramsList();
        for (var item : programs) {
            programStatesIdentifiersListView.getItems().add(String.valueOf(item.getProgramId()));
        }
    }

    private void updateOutput() {
        outputListView.getItems().clear();
        if (repository.getProgramsList().isEmpty())
            return;

        MyIList<Value> output = getCurrentProgram()
                .getOutput();
        for (var item : output.getItems()) {
            outputListView.getItems().add(item.toString());
        }
    }

    private void updateFileTable() {
        fileTableListView.getItems().clear();
        if (repository.getProgramsList().isEmpty())
            return;

        MyIDictionary<String, BufferedReader> fileTable = getCurrentProgram()
                .getFileTable();
        for (var fileName : fileTable.keys()) {
            fileTableListView.getItems().add(fileName + " -> " + fileTable.lookup(fileName));
        }
    }

    private void updateHeapTable() {
        heapTableView.getItems().clear();
        if (repository.getProgramsList().isEmpty())
            return;

       MyHeap heapTable = getCurrentProgram()
                .getHeap();
        for (var address : heapTable.keys()) {
            heapTableView.getItems().add(new TableRow(address.toString(), heapTable.get(address).toString()));
        }
    }

    private void updateSymbolTable() {
        symbolTableView.getItems().clear();
        if (repository.getProgramsList().isEmpty())
            return;

        MyIDictionary<String, Value> symbolTable = getCurrentProgram()
                .getSymTable();
        for (var variableName : symbolTable.keys()) {
            symbolTableView.getItems().add(new TableRow(variableName,
                    symbolTable.lookup(variableName).toString()));
        }
    }

    public void handleMouseClick(MouseEvent mouseEvent) {
        updateExecutionStack();
    }

    private ProgramState getCurrentProgram() {
        try {
            return repository.getProgramById(
                    Integer.parseInt(programStatesIdentifiersListView.getSelectionModel().getSelectedItem()));
        }
        catch (NumberFormatException e) {
            return repository.getProgramsList().get(0);
        }
    }
}