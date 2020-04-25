package test;

public class DisconnectCommand implements Command {
    @Override
    public double doCommand(String[] commands, int index) {
        try {
            CommandHandler.commandsQueue.put("bye");
            //Thread.sleep(1000);
            //Connect.setStop();
            //DataReaderServer.setStop();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 2;
    }
}
