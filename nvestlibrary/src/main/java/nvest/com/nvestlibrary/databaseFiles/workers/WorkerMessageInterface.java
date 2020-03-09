package nvest.com.nvestlibrary.databaseFiles.workers;

public interface WorkerMessageInterface {
    void sendSuccessMessage(String message);
    void sendFailureMessage(String message);

}
