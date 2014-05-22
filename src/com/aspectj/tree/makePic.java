package com.aspectj.tree;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.internal.win32.SIZE;
import org.eclipse.swt.widgets.TreeItem;

import com.aspectj.demo.Editor;

public class makePic {
	String functionName[] = Editor.getfunctionnamearray();
	int functionTime[] = Editor.getfunctiontimearray();
	int functionLength = Editor.getfunctionlenth();
	DrawTree drawTree;
	GraphViz gv = null;

	public makePic(DrawTree drawTre) {
		// TODO Auto-generated constructor stub
		this.drawTree = drawTre;
		this.start();
	}

	String getline(TreeItem item, int num) {
		String fullName = item.getText();
		String acccessLevel = null;
		String returnValue = null;
		String className = null;
		String funcName = null;
		String argsString = null;
		Pattern p = Pattern
				.compile("^(public|private|protected) (static )+(.*) (.*)\\.(.*)\\((.*)\\)$");
		Matcher m1 = p.matcher(fullName);
		while (m1.find()) {
			acccessLevel = m1.group(1);
			returnValue = m1.group(3);
			className = m1.group(4);
			funcName = m1.group(5);
			argsString = m1.group(6);
		}
		int functionNum = 0;
		for (int i = 0; i < functionLength; i++) {
			if (fullName.equals(functionName[i])) {
				functionNum = functionTime[i];
				break;
			}
		}
		switch (num) {
		case 1:
			return acccessLevel;
		case 2:
			return returnValue;
		case 3:
			return className;
		case 4:
			return funcName;
		case 5:
			return argsString;
		case 6:
			return String.valueOf(functionNum);
		default:
			return "";
		}
	}

	void traverse(TreeItem item) {
		TreeItem[] childrenItems = item.getItems();
		if (childrenItems.length == 0) {
			return;
		}
		for (TreeItem childItem : childrenItems) {
//			String leftName = getline(item, 4);
//			String rightName = getline(childItem, 4);
			String leftName = "\""+item.getText()+"\"";
			String rightName = "\""+childItem.getText()+"\"";
			String num = getline(childItem, 6);
			String s = Integer.valueOf(num) > 1 ? "time" : "times";
			String line = leftName + " -> " + rightName + "[label=\"" + num
					+ " " + s + "\"]" + ";";
			gv.addln(line);
			traverse(childItem);
		}
	}

	private void start() {
		gv = new GraphViz();
		gv.addln(gv.start_graph());
		gv.addln("bgcolor = \"#F0F0F0\";");
		traverse(drawTree.root);
		gv.addln(gv.end_graph());
		System.out.println(gv.getDotSource());
		// String type = "gif";
		// String type = "dot";
		// String type = "fig"; // open with xfig
		// String type = "pdf";
		// String type = "ps";
		// String type = "svg"; // open with inkscape
		//String type = "png";
		String type = "jpg";
		// File out = new File("/tmp/out." + type); // Linux
		File out = new File("example." + type); // Windows
		gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type), out);
	}

	private void start2() {
		String dir = "/home/jabba/eclipse2/laszlo.sajat/graphviz-java-api"; // Linux
		String input = dir + "/sample/simple.dot";
		// String input = "c:/eclipse.ws/graphviz-java-api/sample/simple.dot";
		// // Windows

		GraphViz gv = new GraphViz();
		gv.readSource(input);
		System.out.println(gv.getDotSource());

		String type = "gif";
		// String type = "dot";
		// String type = "fig"; // open with xfig
		// String type = "pdf";
		// String type = "ps";
		// String type = "svg"; // open with inkscape
		// String type = "png";
		// String type = "plain";
		File out = new File("/tmp/simple." + type); // Linux
		// File out = new File("c:/eclipse.ws/graphviz-java-api/tmp/simple." +
		// type); // Windows
		gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type), out);
	}
}
