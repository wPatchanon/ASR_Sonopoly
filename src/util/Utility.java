package util;

public class Utility {
	public static String commaNumber (int number) {
		String numberString;
		String output = "";
		
		while (number > 0) {
			numberString = Integer.toString(number);
			if (output.length() > 0) {
				output = "," + output;
			}
			
			output = numberString.substring(Math.max(0, numberString.length() - 3)) + output;
			
			number /= 1000;
		}
		
		return output;
	}
	
	public static int roundTen (double number) {
		return (int) Math.round(Math.ceil(number / 10)) * 10;
	}
}
