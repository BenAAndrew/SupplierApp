package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class ReadWrite {
	/*
	 * Get data file finds the working directory and appends the fileName to it to get the final address of the data file
	 * @param String fileName
	 * @return String
	 */
	public static String getDataFile(String fileName) {
		String location = Controller.getLocation();
		String[] locationSplit = location.split("/");
		int currentWindowLength = (locationSplit[locationSplit.length - 1]).length();
		//Starting at index 6 removes 'file:/' at start of location
		return location.substring(6, location.length() - currentWindowLength) + fileName;
	}

	/*
	 * Reads text from data.txt and formats it in a way to define where a new supplier is defined
	 * @return String
	 */
	public static String reader() {
		String data = null;
		try (BufferedReader br = new BufferedReader(new FileReader(getDataFile("data.txt")))) {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			sb.append(line);
			line = br.readLine();
			while (line != null) {
				if (line.contains("$")) {
					//&& is a reserved character set used to define the start of a new supplier when the output string is split
					sb.append("&&");
				} else {
					sb.append(",");
				}
				sb.append(line);
				line = br.readLine();
			}
			data = sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

	/*
	 * Writes product update by identifying the line of the chosen product and overwriting it
	 * @param String proDetails, String supCode, String proCode, boolean overwrite
	 */
	public static void proUpdateWriter(String proDetails, String supCode, String proCode, boolean overwrite) {
		try (BufferedReader br = new BufferedReader(new FileReader(getDataFile("data.txt")))) {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				if (line.contains("$" + supCode)) {
					sb.append(line + "\n");
					line = br.readLine();
					sb.append(line + "\n");
					line = br.readLine();
					//First write Supplier info and address before checking products
					if (line == null && !(overwrite)) {
						//Product add in empty supplier
						sb.append(proDetails + "\n");
					} else if (line.substring(0, line.indexOf(",")).equals(proCode) && overwrite) {
						//Checks the product code (this is for product edit)
						sb.append(proDetails + "\n");
					} else if (!(overwrite)) {
						//Pushes the new product into the file (product add in supplier with exsiting products)
						sb.append(proDetails + "\n");
						sb.append(line + "\n");
					} else {
						sb.append(line + "\n");
					}
					line = br.readLine();
				}
				sb.append(line + "\n");
				line = br.readLine();
			}
			try (BufferedWriter out = new BufferedWriter(new FileWriter(getDataFile("data.txt"), false))) {
				out.write(sb.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Writes supplier update by identifying the line of the chosen supplier and overwriting it
	 * @param String supCode, String newLine, boolean codeChange
	 */
	public static void supUpdateWriter(String supCode, String newVal, boolean codeChange, int pos) {
		try (BufferedReader br = new BufferedReader(new FileReader(getDataFile("data.txt")))) {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				if (line.contains("$" + supCode)) {
					if(!(codeChange)) {
						//Section designed to target Supplier name & address line
						sb.append(line + "\n");
						line = br.readLine();
						String[] editedLine = line.split(",");
						editedLine[pos] = newVal;
						//Splits line on comma and replaces the selected value
						String finalLine = Arrays.toString(editedLine);
						finalLine = finalLine.substring(1, finalLine.length()-1); 
						for(int i =0; i < finalLine.length()-1; i++) {
							if(finalLine.charAt(i) == ',') {
								finalLine = finalLine.substring(0, i+1) + finalLine.substring(i+2);
								//Removing default spacing found after commas when an array is converted to string
							}
						}
						System.out.println(finalLine);
						sb.append(finalLine + "\n");
						line = br.readLine();
					} else {
						sb.append("$" + newVal + "\n");
						line = br.readLine();
					}
				}
				sb.append(line + "\n");
				line = br.readLine();
			}
			try (BufferedWriter out = new BufferedWriter(new FileWriter(getDataFile("data.txt"), false))) {
				out.write(sb.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Deletes suppliers/products by searching the text file and rewriting the data without the chosen items
	 * @param String supCode, String proCode, boolean product
	 */
	public static void deleteWriter(String supCode, String proCode, boolean product) {
		try (BufferedReader br = new BufferedReader(new FileReader(getDataFile("data.txt")))) {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				if (line.equals("$" + supCode)) {
					//Finds supplier
					if (!(product)) {
						line = br.readLine();
						while (line != null) {
							if (!(line.contains("$"))) {
								//Skipping through lines until next supplier
								line = br.readLine();
							} else {
								break;
							}
						}
					} else {
						sb.append(line + "&&");
						line = br.readLine();
						sb.append(line + "&&");
						line = br.readLine();
						while (!(line.contains("$"))) {
							if (line.split(",")[0].equals(proCode)) {
								//Ignores (deletes) product if code matches
								line = br.readLine();
								break;
							}
							sb.append(line + "&&");
							line = br.readLine();
						}
					}
				}
				sb.append(line + "&&");
				line = br.readLine();
			}
			String[] data = sb.toString().split("&&");
			try (BufferedWriter out = new BufferedWriter(new FileWriter(getDataFile("data.txt"), false))) {
				for(String i : data) {
					out.write(i);
					out.newLine();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Writes a new supplier by getting all the information and appending to data.txt
	public static void newSupplierWriter(String newSup) {
		String[] newSupplier = newSup.split("&&");
		try (BufferedWriter out = new BufferedWriter(new FileWriter(getDataFile("data.txt"), true))) {
			out.newLine();
			out.write(newSupplier[0]);
			out.newLine();
			out.write(newSupplier[1]);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
