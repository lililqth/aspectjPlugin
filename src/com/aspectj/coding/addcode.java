package com.aspectj.coding;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

public class addcode {
	public static int count = 0;
	public static void writeaj(String pathname, String Active, String Way, String selectfuction[], String content) throws IOException{
		String ajFilename = "\\add"+(count++)+".aj";
		 BufferedWriter out = new BufferedWriter(new FileWriter(pathname + ajFilename));
         out.write("public aspect add"+(count - 1)+"{\n");
         out.write("	pointcut pcut():"+ Way +"("+ selectfuction[0]+")");
         for(int i=1; i < selectfuction.length; i++){
        	 out.write("|| "+Way+"(" + selectfuction[i]+")");
         }
         out.write(";\n");
         if(Active == "after( Formals )")
        	 out.write("	after(): pcut(){\n");
         if(Active == "before( Formals )")
        	 out.write("	before(): pcut(){\n");
         if(Active == "after( Formals ) returning [ ( Formal ) ]")
        	 out.write("	after() returning (Object o): pcut(){\n");
         if(Active == "after( Formals ) throwing [ ( Formal ) ]")
        	 out.write("	after() throwing (Exception e): pcut(){\n");
         out.write("	"+content+"\n}\n");
         out.write("}");
         out.close();
         JOptionPane.showMessageDialog(
        		    null,"aj文件生成成功");
	}
}
