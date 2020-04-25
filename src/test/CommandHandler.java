package test;

import java.sql.Statement;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class CommandHandler {
    public static HashMap<String, Double> symbolTable = new HashMap<>();
    public static BlockingQueue<String> commandsQueue = new LinkedBlockingDeque();
    public static HashMap<String,Command> hash = new HashMap<>();
    public static HashMap<String,String> binded = new HashMap<>();
    public static boolean run;
    public CommandHandler(){
        hash.put("var",new DefineVarCommand());
        hash.put("return", new ReturnCommand());
        hash.put("connect", new ConnectCommand());
        hash.put("disconnect", new DisconnectCommand());
        hash.put("openDataServer", new OpenServerCommand());
        hash.put("while",new LoopCommand());
        hash.put("print",new PrintCommand());
        hash.put("sleep",new SleepCommand());
    }
    public double parser(String[] commands) {
        double returnVal = 0;
        for(int index = 0; index<commands.length;){
            Command c = hash.get(commands[index]);
            if (c != null)
                if(c.getClass().getSimpleName().equals("ReturnCommand")) {
                    returnVal = c.doCommand(commands,index);
                    break;
                }
                else
                    index += c.doCommand(commands,index);
            else if(symbolTable.containsKey(commands[index])){
                index+= new SetVarCommand().doCommand(commands,index);
            }
            if(!run) {
                break;
            }
        }
        return returnVal;
    }
    public static void setRun(boolean runValue)
    {
        run = runValue;
    }
}
