package com.aspectj.analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ValueTracker {
	
		/** getCode
		 *  get the aj code to track value's change
		 * @param ValueName value's name
		 * @return aj code
		 * @throws IOException 
		 */
		public static String getCode(String valueName) throws IOException 
		{
			BufferedReader fReader = new BufferedReader(
					new FileReader(
							new File("static\\VTprint.aj")));
			String resultFormat = "";
			String tmpString;
			tmpString = fReader.readLine();
			
			while (tmpString != null) {
				resultFormat = resultFormat + tmpString + "\n";
				tmpString = fReader.readLine();
			}
			
			return String.format(resultFormat, valueName);
		}
		/** getCode
		 *  get the aj code to track value's change
		 * @param ValueName value's name
		 * @param filepath xml file's path
		 * @return aj code
		 * @throws IOException 
		 */
		public static String getCode(String valueName, String filepath) throws IOException
		{
			BufferedReader fReader = new BufferedReader(
					new FileReader(
							new File("static\\VTfile.aj")));
			String resultFormat = "";
			String tmpString;
			tmpString = fReader.readLine();
			
			while (tmpString != null) {
				resultFormat = resultFormat + tmpString + "\n";
				tmpString = fReader.readLine();
			}
			
			return String.format(resultFormat, valueName, filepath);
		}
		/** analysis
		 *  analysis the xml file to get all ValueChangePoint
		 * @param filepath the xml file's path
		 * @return Dictionary<ValueName, ArrayList<ValueChangepoint>>
		 * @throws IOException 
		 */
		public static Map<String, ArrayList<ValueChangePoint>> analysis(String filepath) throws IOException {
			Map<String, ArrayList<ValueChangePoint>> result = new HashMap<>();
			BufferedReader fReader = new BufferedReader(
														new FileReader(
																new File(filepath)));
			String tmpString;
			tmpString = fReader.readLine();
			while (tmpString != null) {
				String[] tmpParse = tmpString.split(",");
				
				if (tmpParse.length == 5 && tmpParse[4].equals("set")) {
					ValueChangePoint tmpVCP = new ValueChangePoint(tmpParse[1], tmpParse[2], tmpParse[3]);
					
					// add a new key-value map
					if (!result.containsKey(tmpParse[0])) 
						result.put(tmpParse[0], new ArrayList<ValueChangePoint>());
					
					result.get(tmpParse[0]).add(tmpVCP);
				}
				
				tmpString = fReader.readLine();
			}
			return result;
		}
}
