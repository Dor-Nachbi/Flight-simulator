package test;

public class ReturnCommand implements Command {
    @Override
    public double doCommand(String[] commands, int index) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int val = (int)CalculateExpression.calculate(commands, ++index);
        return val;
    }
}
