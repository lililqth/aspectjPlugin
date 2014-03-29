package com.aspectj.analysis;

import java.io.*;
import java.util.ArrayList;
import java.util.Stack;

import com.aspectj.tree.xmlResultTreeNode;

public class AnalysisTool {
	public static void copyFile(File source, File target) {	
		InputStream fis = null;
        OutputStream fos = null;
        try {
            fis = new BufferedInputStream(new FileInputStream(source));
            fos = new BufferedOutputStream(new FileOutputStream(target));
            byte[] buf = new byte[4096];
            int i;
            while ((i = fis.read(buf)) != -1) {
                fos.write(buf, 0, i);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
			try {
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		return;
	}
	
	public static void runAnalysis(String filepath, String filename, String ajFileName) {
		
		System.setProperty("user.dir", filepath);
		
		String command = "cmd.exe /c start ajc " + filename + " " + ajFileName;
		try {
			Runtime.getRuntime().exec(command, null, new File(filepath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		command = "cmd.exe /c start java -cp \".;%CLASSPATH%\" " 
				+ filename.substring(0, filename.indexOf(".java")) + " " 
				+ ajFileName.substring(0, ajFileName.indexOf(".aj"));
		
		try {
			Runtime.getRuntime().exec(command, null, new File(filepath));
			System.out.println(command);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void analysis(String pathname) {
		
		// Get filepath & filename
		File analysisFile = new File(pathname);
		String filepath = analysisFile.getParent();
		String filename = analysisFile.getName();
		
		// Generate ajFileName
		String ajFileName = "aspectjanalysictempfile.aj";
		
		// copy analysis.aj to ajFileName
		copyFile(new File("static/analysis.aj"), new File(filepath + "\\" + ajFileName));
		
		// 删除原本的 XML 文件
		File xmlResultFile = new File(filepath + "\\analysisResult.xml");
		if (xmlResultFile.exists()) {
			xmlResultFile.delete();
		}
		
		// 添加 XML 文件头
		FileWriter fw;
		try {
			fw = new FileWriter(xmlResultFile);
			String xmlHeaderString = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<functions>\n";
			fw.write(xmlHeaderString);
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		// Run analysis
		runAnalysis(filepath, filename, ajFileName);
	}
	
	public static ArrayList<xmlResultTreeNode> analysisXMLFile(String filepath) {

		BufferedReader bReader = new BufferedReader(new FileReader(filepath));
		
		ArrayList<xmlResultTreeNode> resultNodes = new ArrayList<xmlResultTreeNode>();
		Stack<xmlResultTreeNode> tmpStack = new Stack<xmlResultTreeNode>();
		String nodeContent = bReader.readLine();
		while (!nodeContent.equals("")) {
			if (nodeContent.startsWith("<start>")) {
				xmlResultTreeNode tmpNode = new xmlResultTreeNode(nodeContent.replace("<start>", "").replace("</start>", ""));
				resultNodes.add(tmpNode);
				tmpStack.push(tmpNode);
			} 
			else if(nodeContent.startsWith("<end>")){
				xmlResultTreeNode childNode = tmpStack.pop();
				tmpStack.peek().addChild(childNode);
			}
			nodeContent = bReader.readLine();
		}
		
		return resultNodes;  // 返回所有节点，其中第一个为根节点。
	}
}