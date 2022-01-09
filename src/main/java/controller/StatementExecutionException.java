package controller;

import model.MyException;

public class StatementExecutionException extends MyException {
    public StatementExecutionException(String message) {
        super(message);
    }
}
