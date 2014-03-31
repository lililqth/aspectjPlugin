package com.aspectj.tree;
import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TreeItem;

public class Proba
{
	DrawTree drawTree;
	GraphViz gv = null;
   public static void main(String[] args)
   {
	  
      Proba p = new Proba();
      p.start();
//      p.start2();
   }

   private void start()
   {
      gv = new GraphViz();
      
      //this.traverse(drawTree.rootRoot);
      gv.addln(gv.start_graph());
      gv.addln("main -> helloworld;");
      gv.addln("main -> getX;");
      gv.addln("helloworld -> functioz4n;");
      gv.addln("helloworld -> setX;");
      gv.addln("helloworld -> setValue;");
      gv.addln("getX -> getalue;");
      
      gv.addln(gv.end_graph());
      System.out.println(gv.getDotSource());
      String type = "gif";
//      String type = "dot";
//      String type = "fig";    // open with xfig
//      String type = "pdf";
//      String type = "ps";
//      String type = "svg";    // open with inkscape
//      String type = "png";
//      String type = "plain";
      //File out = new File("/tmp/out." + type);   // Linux
      File out = new File("c:/Temp/out." + type);    // Windows
      gv.writeGraphToFile( gv.getGraph( gv.getDotSource(), type ), out );
   }
   
   /**
    * Read the DOT source from a file,
    * convert to image and store the image in the file system.
    */
   private void start2()
   {
      String dir = "/home/jabba/eclipse2/laszlo.sajat/graphviz-java-api";     // Linux
      String input = dir + "/sample/simple.dot";
//	   String input = "c:/eclipse.ws/graphviz-java-api/sample/simple.dot";    // Windows
	   
	   GraphViz gv = new GraphViz();
	   gv.readSource(input);
	   System.out.println(gv.getDotSource());
   		
      String type = "gif";
//    String type = "dot";
//    String type = "fig";    // open with xfig
//    String type = "pdf";
//    String type = "ps";
//    String type = "svg";    // open with inkscape
//    String type = "png";
//      String type = "plain";
	   File out = new File("/tmp/simple." + type);   // Linux
//	   File out = new File("c:/eclipse.ws/graphviz-java-api/tmp/simple." + type);   // Windows
	   gv.writeGraphToFile( gv.getGraph( gv.getDotSource(), type ), out );
   }
}
