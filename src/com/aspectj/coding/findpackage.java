package com.aspectj.coding;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Stack;

public class findpackage {
	
	public static ArrayList<String> getPackageStrings(String srcPathString) throws FileNotFoundException {
		
		ArrayList<String> packageStrings = new ArrayList<String>();  // ���ڴ洢���е� package ·��
		Stack<File> dirStack = new Stack<File>();  // ���ڴ洢���������ջ
		
		File srcFile = new File(srcPathString);  // ����һ�� src ���ļ����ļ�
		
		// �ж� src �ļ��Ƿ���ڣ��������򱨴�
		if (!srcFile.exists()) {
			throw new FileNotFoundException();
		}
		
		/*-----------���Ǽ��� src �ļ����²����� .java �ļ�------------*/
		/*-----------Ҳ����˵�����ǲ����� (default package) �ĳ���------------*/
		
		// ���� src �ļ����������ļ��������е�Ŀ¼���� Stack ��
		for (File file : srcFile.listFiles()) {
			// ����ļ���Ŀ¼������뵽 dirStack ��ȥ
			if (file.isDirectory()) {
				dirStack.push(file);
			}
		}
		
		// ���� Stack �е�Ŀ¼���ж�������û�� .java
		// ����У���ô����Ϊ��һ�� package������ packageStrings
		// Ȼ�������ļ��е�Ŀ¼���� Stack
		// ֱ�� Stack Ϊ��Ϊֹ
		while (!dirStack.isEmpty()) {
			File dir = dirStack.pop();  // �� dirStack ��ȡ��һ��Ŀ¼
			
			// ������Ŀ¼�������ļ�
			for (File file : dir.listFiles()) {
				// �������Ŀ¼������Ŀ¼����ջ��
				if (file.isDirectory()) {
					dirStack.push(file);
				}
			}
			
			// ����Ŀ¼�������ļ�
			for (File file : dir.listFiles()) {
				// ������� .java �ļ�����Ŀ¼Ϊ package, ���� packageStrings Ȼ�����ѭ��
				if ( file.getName().endsWith(".java") && (!file.isDirectory()) ) {
					// ��ȡ���·��
					String tmpString = dir.getAbsolutePath().replace(srcFile.getAbsolutePath() + File.separator, "");
					// �����·���е� File.separator �滻�� . Ȼ����� packageStrings
					tmpString = tmpString.replace(File.separator, "\\");
					packageStrings.add(tmpString);
					
					break;  // ����ѭ��
				}
			}
		}
		
		return packageStrings;	
	}
}
