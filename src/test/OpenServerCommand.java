package test;

public class OpenServerCommand implements Command {
    @Override
    public double doCommand(String[] commands, int index) {

        int port = Integer.parseInt(commands[index + 1]);
        int timeOut = 1000 / (int) CalculateExpression.calculate(commands, index + 2);
        Thread serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                DataReaderServer dataReaderServer = new DataReaderServer(port, timeOut);
                dataReaderServer.runServer();
            }
        });
        serverThread.start();
        while(!DataReaderServer.connected) {
            try {
                Thread.sleep(100);
            }
            catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
        int counter = 1;
        int temp = index;
        while (!commands[temp].equals(";")) {
            counter++;
            temp++;
        }
        return counter;
    }
}
