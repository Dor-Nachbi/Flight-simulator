package test;

public class LoopCommand implements Command {
    @Override
    public double doCommand(String[] commands, int index) {
        int counter = 1;
        int temp = index;
        int loopExpressionLength=0;
        int loopStartIndex = index;
        boolean isInBlock = false;
        while (!commands[index].equals("}")) {
            if(isInBlock)
                loopExpressionLength++;
            if(commands[index].equals("{")) {
                isInBlock = true;
                loopStartIndex += counter+1;
            }
            counter++;
            index++;
        }
        index = temp;

        String[] loopCommands = new String[loopExpressionLength-1];
        for(int i = 0; i<loopCommands.length;i++)
        {
            loopCommands[i] = commands[loopStartIndex+i];
        }
        String operator = commands[index+2];
        double left = CalculateExpression.calculate(commands,index+1);
        double right = CalculateExpression.calculate(commands,index+3);
        switch (operator) {
            case "<":
                while (left < right && CommandHandler.run) {
                    new CommandHandler().parser(loopCommands);
                    left = CalculateExpression.calculate(commands, index + 1);
                    right = CalculateExpression.calculate(commands, index + 3);
                }break;
            case ">":
                while (left > right && CommandHandler.run) {
                    new CommandHandler().parser(loopCommands);
                    left = CalculateExpression.calculate(commands, index + 1);
                    right = CalculateExpression.calculate(commands, index + 3);
                }break;
            case "==":
                while (left == right && CommandHandler.run) {
                    new CommandHandler().parser(loopCommands);
                    left = CalculateExpression.calculate(commands, index + 1);
                    right = CalculateExpression.calculate(commands, index + 3);
                }break;
            case "!=":
                while (left != right && CommandHandler.run) {
                    new CommandHandler().parser(loopCommands);
                    left = CalculateExpression.calculate(commands, index + 1);
                    right = CalculateExpression.calculate(commands, index + 3);
                }break;
            case ">=":
                while (left >= right && CommandHandler.run) {
                    new CommandHandler().parser(loopCommands);
                    left = CalculateExpression.calculate(commands, index + 1);
                    right = CalculateExpression.calculate(commands, index + 3);
                }break;
            case "<=":
                while (left <= right && CommandHandler.run) {
                    new CommandHandler().parser(loopCommands);
                    left = CalculateExpression.calculate(commands, index + 1);
                    right = CalculateExpression.calculate(commands, index + 3);
                }break;
        }
        return counter+1;
    }
}
