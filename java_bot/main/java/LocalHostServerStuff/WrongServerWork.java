package LocalHostServerStuff;

public class WrongServerWork extends Exception{
    private final String message;
    public WrongServerWork(String message){
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
