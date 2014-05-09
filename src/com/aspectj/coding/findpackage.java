package com.aspectj.coding;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Stack;

public class findpackage {
	
	public static ArrayList<String> getPackageStrings(String srcPathString) throws FileNotFoundException {
		
		ArrayList<String> packageStrings = new ArrayList<String>();  // 用于存储所有的 package 路径
		Stack<File> dirStack = new Stack<File>();  // 用于存储辅助计算的栈
		
		File srcFile = new File(srcPathString);  // 创建一个 src 的文件夹文件
		
		// 判断 src 文件是否存在，不存在则报错
		if (!srcFile.exists()) {
			throw new FileNotFoundException();
		}
		
		/*-----------我们假设 src 文件夹下不含有 .java 文件------------*/
		/*-----------也就是说，我们不允许 (default package) 的出现------------*/
		
		// 遍历 src 文件夹下所有文件，将其中的目录加入 Stack 中
		for (File file : srcFile.listFiles()) {
			// 如果文件是目录，则加入到 dirStack 中去
			if (file.isDirectory()) {
				dirStack.push(file);
			}
		}
		
		// 对于 Stack 中的目录，判断其中有没有 .java
		// 如果有，那么就认为是一个 package，加入 packageStrings
		// 然后将其子文件中的目录加入 Stack
		// 直到 Stack 为空为止
		while (!dirStack.isEmpty()) {
			File dir = dirStack.pop();  // 从 dirStack 中取出一个目录
			
			// 遍历该目录下所有文件
			for (File file : dir.listFiles()) {
				// 如果存在目录，将新目录加入栈中
				if (file.isDirectory()) {
					dirStack.push(file);
				}
			}
			
			// 遍历目录下所有文件
			for (File file : dir.listFiles()) {
				// 如果存在 .java 文件，该目录为 package, 加入 packageStrings 然后结束循环
				if ( file.getName().endsWith(".java") && (!file.isDirectory()) ) {
					// 获取相对路径
					String tmpString = dir.getAbsolutePath().replace(srcFile.getAbsolutePath() + File.separator, "");
					// 将相对路径中的 File.separator 替换成 . 然后加入 packageStrings
					tmpString = tmpString.replace(File.separator, "\\");
					packageStrings.add(tmpString);
					
					break;  // 结束循环
				}
			}
		}
		
		return packageStrings;	
	}
}
