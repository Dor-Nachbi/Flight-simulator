package test;

public class ConnectCommand implements Command {
    @Override
    public double doCommand(String[] commands, int index) {
        Thread t = new Thread(new Runnable() {
            public void run() {
                String ip = commands[index + 1];
                int port = (int) CalculateExpression.calculate(commands, index + 2);
                Connect connect = new Connect(ip,port);
                connect.run();
            }
        });
        t.start();
        int counter = 1;
        int temp = index;
        while (!commands[temp].equals(";")) {
            counter++;
            temp++;
        }
        return counter;
    }
}

