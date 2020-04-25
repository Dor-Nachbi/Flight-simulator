package test;
import java.util.*;
public class MyInterpreter {

	public static String[] interpret(String[] lines)  {
		return lexer(lines);
	}
	public static String[] lexer(String[] lines) {
		ArrayList<String> arr = new ArrayList<>();
		for(int i = 0;i < lines.length;i++) {
			String[] words = lines[i]
					.replace("="," = ")
					.replace("+", " + ")
					.replace("-"," - ")
					.replace("*"," * ")
					.replace(")"," ) ")
					.replace("("," ( ")
					.trim()
					.split("\\s+");
			for(String s : words)
				arr.add(s);
			arr.add(";");
		}
		String result[] = new String[arr.size()];
		for (int i = 0; i < arr.size(); i++)
			result[i] = arr.get(i);
		return result;
	}
}
