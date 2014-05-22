package com.aspectj.run;

import java.awt.TextArea;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;












import javax.sql.CommonDataSource;

import org.eclipse.core.commands.Command;

import com.aspectj.coding.addcode;
import com.aspectj.demo.Editor;
import com.aspectj.demo.Frame;

public class Run {
	public static void runAnalysis(String filepath, String filename,
			String ajFileName) {

//		System.setProperty("user.dir", filepath);
//		System.out.println("current path:"+filepath);
		String command = "cmd.exe /c ajc -d bin " + Editor.getpackagename();
		for (int i = 0; i < addcode.getcount(); i++)
			command += " " + filepath + "\\addaj\\add"+i+".aj ";
		command += "-cp " +"C:/aspectj1.7/lib/aspectjrt.jar -1.7";
		try {
			Process pcProcess = Runtime.getRuntime().exec(command, null, new File(filepath));
			pcProcess.waitFor();
			System.out.println(command);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		System.setProperty("user.dir", filepath+"\\temp");
//		System.out.println("user: "+System.getProperty("user.dir"));
		command = "cmd.exe /c java -cp bin;C:/aspectj1.7/lib/aspectjrt.jar " 
				+ Editor.getmainjava().substring(0, Editor.getmainjava().indexOf(".java"));//+ filename.substring(0, filename.indexOf(".java"));
		try {
			/*
			 * int b = Runtime.getRuntime().exec(command, null, new
			 * File(filepath)).waitFor(); System.out.println(command);
			 * System.out.println("run: "+b);
			 */
			Process p = Runtime.getRuntime().exec(command, null,
					new File(filepath));
			p.waitFor();
			InputStream is = p.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while ((line = in.readLine()) != null) {
				System.out.println(line);
			}

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
