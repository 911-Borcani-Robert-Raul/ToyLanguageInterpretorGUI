package com.example.toylanguageinterpretorgui;

import controller.Controller;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.ProgramState;
import model.expressions.*;
import model.statements.*;
import model.types.BoolType;
import model.types.IntType;
import model.types.RefType;
import model.types.StringType;
import model.utils.MyDictionary;
import model.values.BoolValue;
import model.values.IntValue;
import model.values.StringValue;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ChooseProgramController implements Initializable {

    private final List<IStatement> programs = new ArrayList<>();
    private final ReadOnlyObjectWrapper<IStatement> chosenStatement = new ReadOnlyObjectWrapper<>();
    private final ReadOnlyObjectWrapper<Integer> programStateCount = new ReadOnlyObjectWrapper<>();

    public ReadOnlyObjectProperty<IStatement> getChosenProgram() {
        return chosenStatement.getReadOnlyProperty();
    }

    public ReadOnlyObjectProperty<Integer> getProgramStateCount() {
        return programStateCount.getReadOnlyProperty();
    }

    @FXML
    private ListView<String> programListView;

    @FXML
    private Button chooseButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillProgramsList();
        for (IStatement statement : programs)
            programListView.getItems().add(statement.toString());
    }

    public void onChooseClicked(ActionEvent actionEvent) {
        int index = programListView.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            chosenStatement.set(programs.get(index));
            programStateCount.set(programs.size());
            Stage stage = (Stage) chooseButton.getScene().getWindow();
            stage.close();
        }
    }

    private void fillProgramsList() {
        IStatement ex1 = new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
                new CompoundStatement(new AssignStatement("v", new ValueExpression(new IntValue(2))),
                        new PrintStatement(new VariableExpression("v"))));
        ex1.typecheck(new MyDictionary<>());

        IStatement ex2 = new CompoundStatement( new VariableDeclarationStatement("a",new IntType()),
                new CompoundStatement(new VariableDeclarationStatement("b",new IntType()),
                        new CompoundStatement(new AssignStatement("a",
                                new ArithmeticExpression('+', new ValueExpression(new IntValue(2)),
                                        new ArithmeticExpression('*',new ValueExpression(new IntValue(3)),
                                                new ValueExpression(new IntValue(5))))),
                                new CompoundStatement(new AssignStatement("b",
                                        new ArithmeticExpression('+',new VariableExpression("a"),
                                                new ValueExpression(new IntValue(1)))),
                                        new PrintStatement(new VariableExpression("b"))))));
        ex2.typecheck(new MyDictionary<>());

        IStatement ex3 = new CompoundStatement(new VariableDeclarationStatement("a",new BoolType()),
                new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
                        new CompoundStatement(new AssignStatement("a", new ValueExpression(new BoolValue(true))),
                                new CompoundStatement(new IfStatement(new VariableExpression("a"),new AssignStatement("v",
                                        new ValueExpression(new IntValue(2))), new AssignStatement("v",
                                        new ValueExpression(new IntValue(3)))), new PrintStatement(new VariableExpression("v"))))));
        ex3.typecheck(new MyDictionary<>());

        IStatement ex4 = new CompoundStatement(new VariableDeclarationStatement("fileName", new StringType()),
                new CompoundStatement(new AssignStatement("fileName", new ValueExpression(new StringValue("test.txt"))),
                        new CompoundStatement(new OpenFileRead(new VariableExpression("fileName")),
                                new CompoundStatement(new VariableDeclarationStatement("varc", new IntType()),
                                        new CompoundStatement(new ReadStatement(new VariableExpression("fileName"), "varc"),
                                                new CompoundStatement(new PrintStatement(new VariableExpression("varc")),
                                                        new CompoundStatement(new ReadStatement(new VariableExpression("fileName"),
                                                                "varc"), new CompoundStatement(new PrintStatement(new VariableExpression("varc")),
                                                                new CompoundStatement( new CloseFileRead(new VariableExpression("fileName")),
                                                                        new CloseFileRead(new VariableExpression("fileName")) )))))))));
        ex4.typecheck(new MyDictionary<>());

        IStatement ex5 = new IfStatement(new RelationalExpression(new ValueExpression(new IntValue(2)),
                new ValueExpression(new IntValue(5)), RelationalExpressionOperator.GreaterThanOrEqual),
                new PrintStatement(new ValueExpression(new StringValue("conditia e adevarata"))),
                new PrintStatement(new ValueExpression(new StringValue("conditia nu e adevarata"))));
        ex5.typecheck(new MyDictionary<>());

        IStatement ex6 = new CompoundStatement(new VariableDeclarationStatement("v", new RefType(new IntType())),
                new CompoundStatement(new NewStatement("v", new ValueExpression(new IntValue(20))),
                        new CompoundStatement(new VariableDeclarationStatement("a", new RefType(new RefType(new IntType()))),
                                new CompoundStatement(new NewStatement("a", new VariableExpression("v")),
                                        new CompoundStatement(new NewStatement("v", new ValueExpression(new IntValue(30))),
                                                new PrintStatement(new HeapReadingExpression(new HeapReadingExpression(new VariableExpression("a")))))))));
        ex6.typecheck(new MyDictionary<>());

        IStatement ex7 = new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
                new CompoundStatement(new AssignStatement("v", new ValueExpression(new IntValue(4))),
                        new WhileStatement(new RelationalExpression(new VariableExpression("v"), new ValueExpression(new IntValue(0)),
                                RelationalExpressionOperator.GreaterThan), new CompoundStatement(new PrintStatement((new VariableExpression("v"))),
                                new AssignStatement("v", new ArithmeticExpression(ArithmeticExpressionOperator.MINUS, new VariableExpression("v"),
                                        new ValueExpression(new IntValue(1))))))));
        ex7.typecheck(new MyDictionary<>());

        IStatement ex8 = new CompoundStatement(new VariableDeclarationStatement("v", new RefType(new IntType())),
                new CompoundStatement(new NewStatement("v", new ValueExpression(new IntValue(20))),
                        new CompoundStatement(new VariableDeclarationStatement("a", new RefType(new RefType(new IntType()))),
                                new CompoundStatement(new NewStatement("a", new VariableExpression("v")),
                                        new CompoundStatement(new PrintStatement(new HeapReadingExpression(new VariableExpression("v"))),
                                                new PrintStatement(new ArithmeticExpression('+',
                                                        new HeapReadingExpression(new HeapReadingExpression(new VariableExpression("a"))),
                                                        new ValueExpression(new IntValue(5))))
                                        )))));
        ex8.typecheck(new MyDictionary<>());

        IStatement ex9 = new CompoundStatement(new VariableDeclarationStatement("v", new RefType(new IntType())),
                new CompoundStatement(new NewStatement("v", new ValueExpression(new IntValue(20))),
                        new CompoundStatement(new NewStatement("v", new ValueExpression(new IntValue(30))),
                                new PrintStatement(new HeapReadingExpression((new VariableExpression("v")))))));
        ex9.typecheck(new MyDictionary<>());

        IStatement ex10 = new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
                new CompoundStatement(new VariableDeclarationStatement("a", new RefType(new IntType())),
                        new CompoundStatement(new AssignStatement("v", new ValueExpression(new IntValue(10))),
                                new CompoundStatement(new NewStatement("a", new ValueExpression(new IntValue(22))),
                                        new CompoundStatement(new ForkStatement(
                                                new CompoundStatement(new HeapWritingStatement("a", new ValueExpression(new IntValue(30))),
                                                        new CompoundStatement(new AssignStatement("v", new ValueExpression(new IntValue(32))),
                                                                new CompoundStatement(new PrintStatement(new VariableExpression("v")),
                                                                        new PrintStatement(new HeapReadingExpression(new VariableExpression("a"))))))
                                        ),
                                                new CompoundStatement(
                                                        new PrintStatement(new VariableExpression("v")),
                                                        new PrintStatement(new HeapReadingExpression(new VariableExpression("a"))))
                                        )))));
        ex10.typecheck(new MyDictionary<>());

        IStatement ex11 = new CompoundStatement(new CompoundStatement(new CompoundStatement(
                new VariableDeclarationStatement("a", new StringType()),
                new VariableDeclarationStatement("b", new IntType())),
                new AssignStatement("a", new ValueExpression(new StringValue("abdc")))),
                new AssignStatement("b", new ValueExpression(new IntValue(55))));
        ex11.typecheck(new MyDictionary<>());

        programs.add(ex1); programs.add(ex2);
        programs.add(ex3); programs.add(ex4);
        programs.add(ex5); programs.add(ex5);
        programs.add(ex6); programs.add(ex6);
        programs.add(ex7); programs.add(ex8);
        programs.add(ex9); programs.add(ex10);
        programs.add(ex11);
    }
}
