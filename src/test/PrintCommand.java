package test;

public class PrintCommand implements Command {
    @Override
    public double doCommand(String[] commands, int index) {
        int counter = 1;
        int temp = index;
        while (!commands[index].equals(";")) {
            counter++;
            index++;
        }
        if(commands[temp +1].startsWith("\""))
            System.out.println(commands[temp + 1].substring(1, commands[temp + 1].length() - 1));
        else
            System.out.println(CalculateExpression.calculate(commands, ++temp));
        return counter;
    }
}
