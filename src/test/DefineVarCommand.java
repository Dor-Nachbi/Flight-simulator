package test;

public class DefineVarCommand implements Command {
    @Override
    public double doCommand(String[] commands, int index) {
        int counter = 1;
        int temp = index;
        while (!commands[index].equals(";")) {
            counter++;
            index++;
        }
        String var = commands[++temp];
        double val = CalculateExpression.calculate(commands, ++temp);
        CommandHandler.symbolTable.put(var, val);
        return counter;
    }
}
