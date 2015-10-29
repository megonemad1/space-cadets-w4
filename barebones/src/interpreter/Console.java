package interpreter;

import java.io.BufferedReader;
import java.io.InputStreamReader;

class Console {
	private static BufferedReader br = new BufferedReader(new InputStreamReader(
			System.in));

	public static boolean writeLine(Object item) {
		try {
			System.out.println(item.toString());
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	public static boolean writeErrLine(Object item) {
		try {
			System.err.println(item.toString());
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	public static boolean write(Object item) {
		try {
			System.out.print(item.toString());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static String readLine() {
		try {
			return Console.br.readLine();
		} catch (Exception e) {
			return null;
		}
	}

}
