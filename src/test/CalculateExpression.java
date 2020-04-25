package test;

public class CalculateExpression {//Equal Command
    public static double calculate(String[] expression,int index){
        StringBuilder sb = new StringBuilder();
        if(expression[index].equals("="))
            index++;
        if(expression[index].equals("bind"))
        {
            int temp = index +1;
            String toBind = "";
            while(!expression[temp].equals(";")) {
                toBind += expression[temp];
                temp++;
            }
            bind(expression[index-2],toBind);
        }
        else {
            while (!expression[index].equals(";")
                    && !expression[index].equals("<")
                    && !expression[index].equals(">")
                    && !expression[index].equals("=")
                    && !expression[index].equals("!")
                    && !expression[index].equals("{")){
                if (CommandHandler.symbolTable.containsKey(expression[index]))
                    if(CommandHandler.binded.containsKey(expression[index])) {
                        sb.append(CommandHandler.symbolTable.get(CommandHandler.binded.get(expression[index])));
                    }
                    else
                        sb.append(CommandHandler.symbolTable.get(expression[index]));
                else
                    sb.append(expression[index]);
                index++;
            }
        }
        if(sb.toString().equals(""))
            sb.append("0");
        return Calculator.calculate(sb.toString());
    }
    public static void bind(String x, String y){
        CommandHandler.binded.put(x,y);
    }
}
