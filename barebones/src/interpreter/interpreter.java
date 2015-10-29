package interpreter;

import java.util.HashMap;

import Compiler.Compiler;

public class interpreter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HashMap<String, Integer> enviroment = new HashMap<String, Integer>();
		Console.writeLine("baire bones interpreter v0.1");

		String userin = "";
		while (!userin.equals("exit")) {
			Console.write("<<<");
			userin = Console.readLine();
			if (userin.length()<=0 || userin.substring(userin.length() - 1).equals(";")) {
				if (userin.contains(" do;")) {
					while (!userin.contains("end;")) {
						Console.write("---");
						userin += "\n" + Console.readLine();
					}
				}
				try {
					if (!userin.equals("exit;")) {
						Compiler.interpret(userin, enviroment);
						for (String k : enviroment.keySet()) {
							Console.write(">>>");
							Console.writeLine(k + ": " + enviroment.get(k));
						}
					} else
						break;
				} catch (Exception e) {
					Console.writeLine("~~~~~~~~~~Error~~~~~~~~~");
					Console.writeLine(e.getMessage());
					for (StackTraceElement ste:e.getStackTrace()){
						Console.writeLine(ste);
						
					}
					
				}
			} else {
				Console.writeLine("~~~~~~~~~~Error~~~~~~~~~");
				Console.writeLine("syntax error, expected ;");				
			}
		}
	}

}
