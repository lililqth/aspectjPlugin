package com.aspectj.tree;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.*;

public class DrawTree {
	private  Display display = new Display();
	Shell shell = new Shell(display, SWT.DIALOG_TRIM
			| SWT.APPLICATION_MODAL | SWT.ON_TOP);
	private  xmlResultTreeNode head = new xmlResultTreeNode("main");
	 Tree tree = new Tree(shell, SWT.VIRTUAL);// 用于存储树的信息

	public  TreeViewer viewer = new TreeViewer(shell,// 用于显示树
			SWT.FULL_SELECTION);
	 Tree ViewTree = viewer.getTree();

	public DrawTree() {
		// DrawTree DrawTre = new DrawTree();
		shell.setText("函数调用关系 ");
		GridLayout gridLayout = new GridLayout(1, true);
		shell.setLayout(gridLayout);
		ViewTree.setLayoutData(new GridData(GridData.FILL_BOTH));
		// 隐藏tree
		tree.setLayoutData(new GridData(GridData.FILL_BOTH));
		((GridData) tree.getLayoutData()).exclude = true;
		tree.setVisible(false);
		tree.getParent().layout();

		// tree.getParent().layout();
		// 测试代码 向arraylist中插入数据
		xmlResultTreeNode node = new xmlResultTreeNode(
				"public static void helloworld.helloworld(java.lang.String[])");
		xmlResultTreeNode node1 = new xmlResultTreeNode(
				"public static void helloworld.function(int)");
		node.childArrayList.add(node1);
		node1 = new xmlResultTreeNode(
				"public static boolean helloworld.setText(java.lang.String)");
		node.childArrayList.add(node1);
		node1 = new xmlResultTreeNode(
				"private static boolean helloworld.setX(int)");
		node.childArrayList.add(node1);
		node1 = new xmlResultTreeNode(
				"protected static boolean helloworld.setValue(double)");
		node.childArrayList.add(node1);
		head.childArrayList.add(node);

		node = new xmlResultTreeNode("public static int helloworld.getX(int)");
		node1 = new xmlResultTreeNode(
				"public static void helloworld.show(java.lang.String[])");
		node.childArrayList.add(node1);
		xmlResultTreeNode node2 = new xmlResultTreeNode(
				"public static int helloworld.getValue(java.lang.String[])");
		node1.childArrayList.add(node2);
		xmlResultTreeNode node3 = new xmlResultTreeNode(
				"public static string helloworld.getText(java.lang.String[])");
		node2.childArrayList.add(node3);
		head.childArrayList.add(node);
		// ////////////////////////////////////////////////////////////////////

		// 创建根节点
		TreeItem rootRoot = new TreeItem(tree, SWT.NONE);
		TreeItem root = new TreeItem(rootRoot, SWT.NONE);
		root.setText("public static string helloworld.main(java.lang.String[])");
		// root.setImage(new Image(display, "F://javaworkspace//src//2.bmp"));
		root.setExpanded(true);

		// 遍历arraylist并生成树
		traverse(head, root);

		shell.setSize(800, 600);
		shell.open();
		// 创建表格
		viewer.getTree().setHeaderVisible(true);
		TreeColumn column = new TreeColumn(viewer.getTree(), SWT.LEFT);
		column.setText("类名");
		column.setWidth(200);
		column = new TreeColumn(viewer.getTree(), SWT.LEFT);
		column.setText("函数名");
		column.setWidth(200);
		column = new TreeColumn(viewer.getTree(), SWT.LEFT);
		column.setText("访问级别");
		column.setWidth(100);
		column = new TreeColumn(viewer.getTree(), SWT.LEFT);
		column.setText("返回值");
		column.setWidth(100);
		column = new TreeColumn(viewer.getTree(), SWT.LEFT);
		column.setText("参数");
		column.setWidth(100);
		viewer.setContentProvider(new MyTreeContenetProvider());
		viewer.setLabelProvider(new MyTableLableProvider());
		viewer.setInput(rootRoot);
		//窗口关闭的响应事件  调试
		shell.addShellListener(new ShellAdapter() {
			public void shellClosed(ShellEvent arg0) {
				//System.out.println("closed");
				//arg0.doit = false;
				//shell.setVisible(false);

			}
		});
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	void traverse(xmlResultTreeNode head, TreeItem item) {
		int childNum = head.getNumOfChild();
		if (childNum == 0) {
			return;
		}
		for (int i = 0; i < childNum; i++) {
			xmlResultTreeNode node = head.popChild();
			TreeItem subItem = new TreeItem(item, SWT.NONE);
			subItem.setText(node.getName());
			subItem.setExpanded(true);
			// subItem.setImage(new Image(display,
			// "F://javaworkspace//src//1.gif"));
			traverse(node, subItem);
		}
	}

}
