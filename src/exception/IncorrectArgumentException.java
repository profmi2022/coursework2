package exception;

public class IncorrectArgumentException extends Exception {

    private final String argument;

    public IncorrectArgumentException(String argument) {
        this.argument = argument;
    }

    public String getArgument() {
        return argument;
    }

    @Override
    public String getMessage() {
        return toString();
    }

    @Override
    public String toString() {
        return "Параметр " + argument + " задан неверно";
    }
}
