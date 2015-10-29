package Compiler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

public class Token {

	public int gotoreff;
	public ArrayList<String> lineargs;

	public Token(ArrayList<String> lineparts) {
		// TODO Auto-generated constructor stub
		this.lineargs = lineparts;
		this.gotoreff = -1;
	}

	public Token() {
		// TODO Auto-generated constructor stub
		this.lineargs = new ArrayList<String>();
		this.gotoreff = -1;
	}

	public Token(String[] lineparts) {
		// TODO Auto-generated constructor stub
		this.lineargs = new ArrayList<String>(Arrays.asList(lineparts));
		this.gotoreff = -1;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
		HashMap<String, Integer> currentEnviroment = new HashMap<String, Integer>();
		Integer programcounter = 0;
		/*
		 * test incrementing a variable, should make a value of one and move to
		 * the next command;
		 */
		Token testincr = new Token(new String[] { "incr", "X" });
		programcounter = testincr.executetoken(currentEnviroment,
				programcounter);
		System.out.println(programcounter);
		assert programcounter == 1;
		assert currentEnviroment.containsKey("X");
		assert currentEnviroment.get("X") == 1;
		System.out.println("test incr on new");
		/*
		 * test decrementing a variable, should make a value of minus one and
		 * move to the next command;
		 */
		testincr = new Token(new String[] { "decr", "Y" });
		programcounter = testincr.executetoken(currentEnviroment,
				programcounter);
		assert programcounter == 2;
		assert currentEnviroment.containsKey("Y");
		assert currentEnviroment.get("Y") == -1;
		System.out.println("test decr on new");
		/*
		 * test copying a variable to another variable x should have its value
		 * copyed to z and move to the next command
		 */
		testincr = new Token(new String[] { "copy", "X","to", "Z" });
		programcounter = testincr.executetoken(currentEnviroment,
				programcounter);
		assert programcounter == 3;
		assert currentEnviroment.containsKey("Z");
		assert currentEnviroment.get("Z") == 1;
		System.out.println("test copy x>z");
		/*
		 * test incrementing exsisting var
		 */
		testincr = new Token(new String[] { "incr", "X" });
		programcounter = testincr.executetoken(currentEnviroment,
				programcounter);
		assert programcounter == 4;
		assert currentEnviroment.containsKey("X");
		assert currentEnviroment.get("X") == 2;
		System.out.println("test incr on new");
		/*
		 * test while true should move to next command
		 */
		testincr = new Token(new String[] { "while", "X", "not", "0" });
		testincr.gotoreff = 10;
		programcounter = testincr.executetoken(currentEnviroment,
				programcounter);
		System.out.println(programcounter);
		assert programcounter == 5;
		System.out.println("test while true");
		/*
		 * test while false should move program counter to its end statement
		 */
		testincr = new Token(new String[] { "while", "X", "not", "2" });
		testincr.gotoreff = 10;
		programcounter = testincr.executetoken(currentEnviroment,
				programcounter);
		assert currentEnviroment.get("X") == 2;
		assert programcounter == 11;
		System.out.println("test while false");
		/*
		 * test end should just redirect to gotoreff
		 */
		testincr = new Token(new String[] { "end" });
		testincr.gotoreff = 0;
		programcounter = testincr.executetoken(currentEnviroment,
				programcounter);
		assert programcounter == 0;
		System.out.println("test end");
		/*
		 * test clear should init and set to 0 a var
		 */
		testincr = new Token(new String[] { "clear", "X" });
		programcounter = testincr.executetoken(currentEnviroment,
				programcounter);
		assert programcounter == 1;
		assert currentEnviroment.get("X") == 0;
		System.out.println("test clear exsisting");
		/*
		 * test clear should init and set to 0 a var
		 */
		testincr = new Token(new String[] { "clear", "P" });
		programcounter = testincr.executetoken(currentEnviroment,
				programcounter);
		assert programcounter == 2;
		assert currentEnviroment.get("P") == 0;
		System.out.println("test clear new");
		}
		catch (Exception e){
			System.out.println("tests failed");
			e.printStackTrace();
		}
	}

	public Integer executetoken(HashMap<String, Integer> currentEnviroment,
			Integer programcounter) throws Exception {
		switch (lineargs.get(0)) {
		case "copy":
			return cmdcopy(currentEnviroment, programcounter);
		case "while":
			return cmdwhile(currentEnviroment, programcounter);
		case "end":
			return cmdend(currentEnviroment, programcounter);
		case "clear":
			return cmdclear(currentEnviroment, programcounter);
		case "incr":
			return cmdincr(currentEnviroment, programcounter);
		case "decr":
			return cmddecr(currentEnviroment, programcounter);
		default:
			throw new Exception("token type not found");
		}

	}

	private Integer cmdcopy(HashMap<String, Integer> currentEnviroment,
			Integer programcounter) {
		String term1 = this.lineargs.get(1);
		String oporater = this.lineargs.get(2);
		if (oporater.equals("to")) {
			String term2 = this.lineargs.get(3);
			if (currentEnviroment.containsKey(term1)) {
				currentEnviroment.put(term2, currentEnviroment.get(term1));
			}
			return programcounter + 1;
		} else
			return -1;
	}

	private Integer cmdwhile(HashMap<String, Integer> currentEnviroment,
			Integer programcounter) {
		boolean valid = false;
		String term1 = this.lineargs.get(1);
		String oporator = this.lineargs.get(2);
		String term2 = this.lineargs.get(3);
		Pattern numberpatten = Pattern.compile("^\\d+$");
		Integer value1, value2;
		if (numberpatten.matcher(term1).matches()) {
			value1 = Integer.valueOf(term1);
		} else {
			value1 = currentEnviroment.get(term1);
		}
		if (numberpatten.matcher(term2).matches()) {
			value2 = Integer.valueOf(term2);
		} else {
			value2 = currentEnviroment.get(term2);
		}
		valid |= oporator.equals("not") && !value1.equals(value2);
		if (valid)
			return programcounter + 1;
		else
			return this.gotoreff + 1;
	}

	private Integer cmdend(HashMap<String, Integer> currentEnviroment,
			Integer programcounter) {
		return this.gotoreff;
	}

	private Integer cmdclear(HashMap<String, Integer> currentEnviroment,
			Integer programcounter) {
		for (int i = 1; i < this.lineargs.size(); i++) {
			currentEnviroment.put(this.lineargs.get(i), 0);
		}
		return programcounter + 1;

	}

	private Integer cmddecr(HashMap<String, Integer> currentEnviroment,
			Integer programcounter) {
		for (int i = 1; i < this.lineargs.size(); i++) {
			String key = this.lineargs.get(i);
			if (currentEnviroment.containsKey(key)) {
				currentEnviroment.put(key, currentEnviroment.get(key) - 1);
			} else
				currentEnviroment.put(key, -1);
		}
		return programcounter + 1;
	}

	private Integer cmdincr(HashMap<String, Integer> currentEnviroment,
			Integer programcounter) {
		for (int i = 1; i < this.lineargs.size(); i++) {
			String key = this.lineargs.get(i);
			if (currentEnviroment.containsKey(key)) {
				currentEnviroment.put(key, currentEnviroment.get(key) + 1);
			} else
				currentEnviroment.put(key, 1);
		}
		return programcounter + 1;
	}

	@Override
	public String toString() {
		String rval = "";
		for (String s : this.lineargs) {
			rval += s;
			rval += ", ";

		}
		rval += "greff: " + this.gotoreff;
		return rval;

	}

	public String getjava() throws Exception {
		// TODO Auto-generated method stub
		switch (lineargs.get(0)) {
		case "copy":
			String cpterm1 = this.lineargs.get(1);
			String cpterm2 = this.lineargs.get(3);
			return cpterm2+" = "+cpterm1+";";
		case "while":
			String whterm1 = this.lineargs.get(1);
			String whoporator = this.lineargs.get(2);
			String whterm2 = this.lineargs.get(3);	
			if (whoporator.equals("not"))
			return "while ("+whterm1+"!="+whterm2+"){";
			return "while (false){";
		case "end":
			return "}";
		case "clear":
			String clterm1 = this.lineargs.get(1);
			return clterm1+"= 0;";
		case "incr":
			String interm1 = this.lineargs.get(1);
			return interm1+"+= 1;";
		case "decr":
			String determ1 = this.lineargs.get(1);
			return determ1+"-= 1;";
		default:
			throw new Exception("token type not found");
		}
	}

}
