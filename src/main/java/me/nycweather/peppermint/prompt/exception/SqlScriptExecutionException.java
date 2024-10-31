package me.nycweather.peppermint.prompt.exception;

public class SqlScriptExecutionException extends Exception {

    public SqlScriptExecutionException(String message) {
        super(message);
    }

    public SqlScriptExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
