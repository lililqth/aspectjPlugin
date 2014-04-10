package com.aspectj.run;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import com.aspectj.coding.addcode;
import com.aspectj.demo.Frame;


public class Run {
		public static void runAnalysis(String filepath, String filename, String ajFileName) {
		
		System.setProperty("user.dir", filepath);
		
		String command = "cmd.exe /c ajc " + "*.java" + " " + Frame.packagename;
		for(int i = 0;i < addcode.count; i++){
			command += " add" + i +".aj";
		}
		try {
			Runtime.getRuntime().exec(command, null, new File(filepath));
			System.out.println(command);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//command = "ping www.baidu.com";
		command = "cmd.exe /c java " 
				+ filename.substring(0, filename.indexOf(".java"));
		try {
			Process p = Runtime.getRuntime().exec(command);
			p.getInputStream();
			BufferedReader in = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
			String line = null;
			while ((line = in.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			 System.out.println("Run this command:" + command
				     + "cause Exception ,and the message is " + e.getMessage());
			e.printStackTrace();
		}
	}
}
