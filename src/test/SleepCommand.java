package test;

public class SleepCommand implements Command {
    @Override
    public double doCommand(String[] commands, int index) {
        int counter = 1;
        int temp = index;
        while (!commands[index].equals(";")) {
            counter++;
            index++;
        }
        double timeout = CalculateExpression.calculate(commands,++temp);
        try {
            Thread.sleep((int) timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return counter;
    }
}
