package com.aspectj.analysis;

import java.io.*;
import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.*;

import com.aspectj.demo.Editor;
import com.aspectj.demo.Frame;
import com.aspectj.tree.xmlResultTreeNode;

public class AnalysisTool {
    /** copyFile
     *  复制文件
     * @param source 源文件路径（包含文件名）
     * @param target 目标文件路径（包含文件名）
     */
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
				e.printStackTrace();
			}
            try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
		return;
	}
	
	/** _runAnalysis
	 * 内部函数，运行分析功能
	 * @param filepath 要分析的文件地址
	 * @param filename 要分析的文件名
	 * @param ajFileName 使用的 aj 文件
	 */
	public static void _runAnalysis(String filepath, String filename, String ajFileName) {
		
		System.setProperty("user.dir", filepath);
		
		//String command = "cmd.exe /c  ajc " + filename + " " + ajFileName +" "+ Editor.getpackagename();
		String command = "cmd.exe /c  ajc " + filename + " " + ajFileName +" "+ Editor.getpackagename();
		try {
			Runtime.getRuntime().exec(command, null, new File(filepath));
			System.out.println(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		System.out.println(filename.substring(0, filename.indexOf(".java")));
		command = "cmd.exe /c  java -cp \".;%CLASSPATH%\" " + filename.substring(0, filename.indexOf(".java"));
		//command = "cmd.exe /c  java -cp \".;%CLASSPATH%\" " + "HelloWorld";
		try {
			Runtime.getRuntime().exec(command, null, new File(filepath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** analysis
	 * 分析用函数
	 * @param pathname 要分析的目标文件
	 */
	public static void analysis(String pathname) {
		
		// Get filepath & filename
		String filepath = Editor.getparentpath();
		String filename = Editor.getjavaname();
		
		// Generate ajFileName
		String ajFileName = "aspectjanalysictempfile.aj";
		
		// copy analysis.aj to ajFileName
		copyFile(new File("static/analysis.aj"), new File(filepath + "\\" + ajFileName));
		
		// 判断 XML 是否存在，如果存在则删除。
		File xmlResultFile = new File(filepath + "\\analysisResult.xml");
		if (xmlResultFile.exists()) {
			xmlResultFile.delete();
		}
		
		// 生成  XML 文件
		FileWriter fw;
		try {
			fw = new FileWriter(xmlResultFile);
			String xmlHeaderString = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<functions>\n";
			fw.write(xmlHeaderString);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		// Run analysis
		_runAnalysis(filepath, filename, ajFileName);
	}
	
	/** analysisXMLFile
	 * 分析 XML 文件并返回结果集，其中第一个 xmlResultTreeNode 是根节点
	 * @param filepath xml 文件的地址
	 * @return 返回结果集，其中第一个 xmlResultTreeNode 是根节点
	 * @throws IOException 文件不存在则报错
	 */
	public static ArrayList<xmlResultTreeNode> analysisXMLFile(String filepath) throws IOException {


		BufferedReader bReader = new BufferedReader(new FileReader(filepath));
		
		ArrayList<xmlResultTreeNode> resultNodes = new ArrayList<xmlResultTreeNode>();
		Stack<xmlResultTreeNode> tmpStack = new Stack<xmlResultTreeNode>();
		String nodeContent = bReader.readLine();
		// while ((nodeContent != null)&&(!nodeContent.equals(""))) {
		while (nodeContent != null) {
			if (nodeContent.startsWith("<start>")) {
				xmlResultTreeNode tmpNode = new xmlResultTreeNode(nodeContent.replace("<start>", "").replace("</start>", ""));
				resultNodes.add(tmpNode);
				tmpStack.push(tmpNode);
			} 
			else if(nodeContent.startsWith("<end>")){
				xmlResultTreeNode childNode = tmpStack.pop();
				if (!tmpStack.empty()) {
					tmpStack.peek().addChild(childNode);
				}
			}
			nodeContent = bReader.readLine();
		}
		bReader.close();
		return resultNodes;  
	}

	/** _getAllFunctions
	 * 获取某个 java 文件中所有的函数名(内部函数)
	 * @param pathname java 文件的路径
	 * @return 所有的函数名的数组，类型为 ArrayList<String>
	 */
	public static ArrayList<String> _getAllFunctions(String pathname)
	{
		// 编写正则表达式并编译
		String regex = "(public|private|protected)+\\s*(static)?\\s*\\S+\\s*\\S+\\s*\\(.*?\\)\\s*(\\{|extend|throws)+?";
		Pattern re = Pattern.compile(regex);
		
		// 读取 java 文件内容
		BufferedReader bReader = null;
		try {
			bReader = new BufferedReader(new FileReader(pathname));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String fileContent = "";
		String tmpString = "";
		try {
			do {
				tmpString = bReader.readLine();
				fileContent += tmpString;
			} while (tmpString != null);
			bReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ArrayList<String> result = new ArrayList<String>();
		
		// 将匹配方法加入 ArrayList
		Matcher matcher = re.matcher(fileContent);
		while (matcher.find()) {
			result.add(matcher.group().replace("{", "").replace("extend", "").replace("throws", "").trim());
		}
		// 返回结果
		return result;
	}

	/** getAllFunctions
	 * 获取某个目录下所有 java 文件中的函数
	 * @param path 目录路径
	 * @return 所有的函数名的数组，类型为 ArrayList<String>，文件名和函数名用 "||" 分割
	 * @throws Exception 传入的不是目录而是文件
	 */
	public static ArrayList<String> getAllFunctions(String path) throws Exception 
	{
		// 判断传入参数是否为目录地址
		File dir = new File(path);
		if (!dir.isDirectory()) {
			throw new Exception("传入的应当是一个目录");
		}
		
		ArrayList<String> funcArrayList = new ArrayList<String>();
		
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				// 如果是目录，则递归
				funcArrayList.addAll(getAllFunctions(file.getAbsolutePath()));
			} else if (file.getAbsolutePath().endsWith(".java")) {
				// 如果是是文件，则判断是否为 .java 结尾，是就分析
				ArrayList<String> tmpArrayList = _getAllFunctions(file.getAbsolutePath());
				String fileName = file.getName();
				for (String string : tmpArrayList) {
					// 将每个文件的函数加上类名
					funcArrayList.add(fileName + "||" + string);
				}
			}
		}
		return funcArrayList;
	}
}