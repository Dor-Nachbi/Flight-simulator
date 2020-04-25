package test;

public class SetVarCommand implements Command {
    @Override
    public double doCommand(String[] commands, int index) {
        int counter = 1;
        int temp = index;
        while (!commands[index].equals(";")) {
            counter++;
            index++;
        }
        String var = commands[temp];
        double val = CalculateExpression.calculate(commands, ++temp);
        CommandHandler.symbolTable.put(var, val);
        if(CommandHandler.binded.containsKey(var)) {
            try {
                CommandHandler.commandsQueue.put("set " + CommandHandler.binded.get(var) + " " + val);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return counter;
    }
}
