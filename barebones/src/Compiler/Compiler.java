package Compiler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;
import java.util.regex.Pattern;

public class Compiler {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			String code = "clear X;" + "incr X;" + "incr X;" + "clear Y;"
					+ "incr Y;" + "incr Y;" + "incr Y;" + "clear Z;"
					+ "while X not 0 do;" + "   clear W;"
					+ "   while Y not 0 do;" + "      incr Z;"
					+ "      incr W;" + "      decr Y;" + "   end;"
					+ "   while W not 0 do;" + "      incr Y;"
					+ "      decr W;" + "   end;" + "   decr X;" + "end;"
					+ "copy X to Y";
			HashMap<String, Integer> testrun = Compiler.compile(code);
			HashMap<String, Integer> fixed = new HashMap<String, Integer>();
			fixed.put("W", 0);
			fixed.put("X", 0);
			fixed.put("Y", 0);
			fixed.put("Z", 6);
			assert testrun.equals(fixed);
			System.out.println("test Sample code");
			for (String key : testrun.keySet()) {
				System.out.println(key + " : " + testrun.get(key).toString());
			}
		} catch (Exception e) {
			System.out.println("test failed");
			e.printStackTrace();

		}
	}

	public static HashMap<String, Integer> compile(String code,
			HashMap<String, Integer> currentEnviroment) throws Exception {
		ArrayList<Token> tokens = interpret(code, currentEnviroment);
		String filename="BaireBones";
		String comp="//baire bones v0.1\npublic class "+filename+" {\npublic static void main(String[] args) {";
		for(String k:currentEnviroment.keySet()){
			comp+="\nint "+k+"= 0;";
		}
		for (Token t:tokens){
			comp+="\n"+t.getjava();
		}
		for(String k:currentEnviroment.keySet()){
			comp+="\nSystem.out.println(\""+k+" : \"+"+k+");";
		}
		comp+="}\n}";
		//System.out.println(comp);
		PrintWriter writer = new PrintWriter(filename+".java", "UTF-8");
		writer.println(comp);
		writer.close();
		return currentEnviroment;
	}

	public static ArrayList<Token> interpret(String code,
			HashMap<String, Integer> currentEnviroment) throws Exception {
		ArrayList<Token> tokens = new ArrayList<Token>();
		String[] lines = code.split(";");
		Pattern blankline = Pattern.compile("^\\s*$");
		for (String line : lines) {
			if (!blankline.matcher(line).matches()) {
				ArrayList<String> lineparts = new ArrayList<String>(
						Arrays.asList(line.split("\\W")));
				Token t = new Token();
				for (Iterator<String> iterator = lineparts.iterator(); iterator
						.hasNext();) {
					String part = iterator.next();
					// System.out.println(part);
					if (blankline.matcher(part).matches() || part.equals("")) {
						iterator.remove();
					} else {
						t.lineargs.add(part);
					}
				}

				tokens.add(t);
			}
		}

		Stack<Integer> dotokens = new Stack<Integer>();
		for (int i = 0; i < tokens.size(); i++) {
			if (tokens.get(i).lineargs.contains("do")) {
				dotokens.push(i);
			}
			if (tokens.get(i).lineargs.contains("end")) {
				Integer doindex = dotokens.pop();
				tokens.get(doindex).gotoreff = i;
				tokens.get(i).gotoreff = doindex;
			}
		}

		Integer programcounter = 0;
		while (programcounter < tokens.size()) {
			// System.out.println(programcounter);
			programcounter = tokens.get(programcounter).executetoken(
					currentEnviroment, programcounter);
		}
		return tokens;
	}

	public static HashMap<String, Integer> interpret(String code)
			throws Exception {
		HashMap<String, Integer> currentEnviroment = new HashMap<String, Integer>();
		interpret(code, currentEnviroment);
		return currentEnviroment;

	}
	public static HashMap<String, Integer> compile(String code)
			throws Exception {
		HashMap<String, Integer> currentEnviroment = new HashMap<String, Integer>();
		return compile(code, currentEnviroment);

	}
}
