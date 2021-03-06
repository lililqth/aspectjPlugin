package com.aspectj.tree;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.action.MenuManager;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;

import org.eclipse.swt.layout.*;
import org.eclipse.ui.dialogs.ContainerCheckedTreeViewer;

import com.aspectj.demo.Editor;
import com.aspectj.demo.Frame;


public class DrawTree {
	private static DrawTree drawTree;
	private Display display = Editor.display;
	Shell shell = new Shell( display, SWT.DIALOG_TRIM /*| SWT.APPLICATION_MODAL
			| SWT.ON_TOP*/);
	Composite composite = null;
	Composite compositeImage = null;
	private xmlResultTreeNode head = null;//new xmlResultTreeNode("main");
	Tree tree = new Tree(shell, SWT.CHECK);// 用于存储树的信息
	public ContainerCheckedTreeViewer viewer = new ContainerCheckedTreeViewer(
			shell,// 用于显示树
			SWT.BORDER);
	Tree ViewTree = viewer.getTree();
	// 创建根节点
	TreeItem rootRoot = new TreeItem(tree, SWT.NONE);
	TreeItem root = new TreeItem(rootRoot, SWT.NONE);

	Text ruleInput = null;
	TreeFilter filter = null;
	ScrolledComposite scrolledComposite = null;
	Composite parentComposite = null;
	int displayWidth ;
	int displayHeight ;
	public void createComposite() {
		// 添加文本编辑区域
		composite = new Composite(shell, SWT.BORDER);
		
		GridData compositeData = new GridData(GridData.FILL_VERTICAL);
		compositeData.widthHint = 110;
		composite.setLayout(new GridLayout());
		composite.setLayoutData(compositeData);

		ruleInput = new Text(composite, SWT.MULTI);
		GridData editData = new GridData(GridData.FILL_VERTICAL);
		editData.widthHint = 95;
		ruleInput.setLayoutData(editData);

		Button okButton = new Button(composite, SWT.PUSH);
		GridData buttonData = new GridData();
		buttonData.widthHint = 95;
		okButton.setLayoutData(buttonData);
		okButton.setText("确认");
		okButton.addSelectionListener(new MakeFilt());
	}

	public static DrawTree getInstance(xmlResultTreeNode rootNode) {
		drawTree = new DrawTree(rootNode);
		return drawTree;
	}

	public void createCompositeImage() {
		//要打开的图片
		final Image img = new Image(this.display,
				"example.jpg");
		final Rectangle bounds = img.getBounds();
		final int picwidth = bounds.width;// 图片宽
		final int picheight = bounds.height;// 图片高
		//父容器  fillLayout
		parentComposite = new Composite(shell, SWT.BORDER);
		parentComposite.setLayout(new FillLayout());
		GridData parentCompositeGridData = new GridData(GridData.FILL_VERTICAL);
		parentCompositeGridData.widthHint = 810;
		parentComposite.setLayoutData(parentCompositeGridData);
		//滚动容器
		scrolledComposite = new ScrolledComposite(parentComposite,  SWT.H_SCROLL|SWT.V_SCROLL);
		scrolledComposite.setLayout(new GridLayout());
		scrolledComposite.setExpandHorizontal(true);
	    scrolledComposite.setExpandVertical(true);
	    scrolledComposite.setMinWidth(picwidth+100);
	    scrolledComposite.setMinHeight((picheight+100)>600 ? (picheight+100):600);
		//图片容器
		compositeImage = new Composite(scrolledComposite, SWT.BORDER);
		//compositeImage.setLayout(new GridLayout());
		compositeImage.setLayout(new FillLayout());
		scrolledComposite.setContent(compositeImage);
		GridData compositeImageData = new GridData(GridData.FILL_VERTICAL);
		compositeImageData.widthHint = 800;
		compositeImage.setLayoutData(compositeImageData);
//		compositeImage.addPaintListener(new PaintListener() {
//			
//			@Override
//			public void paintControl(PaintEvent e) {
//				// TODO Auto-generated method stub
//				e.gc.drawImage(img, 10, 10);
//			}
//		});
//		
		//画布
		final Canvas canvas = new Canvas(compositeImage, SWT.NONE);
//		GridData canvasData = new GridData(GridData.FILL_BOTH);
//		canvasData.widthHint = picwidth+20;
//		canvasData.heightHint = 600;
//		canvas.setLayoutData(canvasData);
		displayWidth = img.getImageData().width;
		displayHeight = img.getImageData().height;
		canvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				e.gc.drawImage(img, 0 , 0, img.getImageData().width,
						img.getImageData().height, 10, 10, displayWidth, displayHeight);}
		});
		canvas.forceFocus();
	}
	private DrawTree(xmlResultTreeNode rootNode) {
		
		head = rootNode;
		viewer.expandAll();
		shell.setText("函数调用关系 ");
		GridLayout gridLayout = new GridLayout(2, false);
		shell.setLayout(gridLayout);
		ViewTree.setTouchEnabled(true);
		ViewTree.setLayoutData(new GridData(GridData.FILL_BOTH));

		// 隐藏tree
		tree.setLayoutData(new GridData(GridData.FILL_BOTH));
		((GridData) tree.getLayoutData()).exclude = true;
		tree.setVisible(false);
		tree.getParent().layout();
		
		/* 测试代码 向arraylist中插入数据
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
		node1 = new xmlResultTreeNode("public static int helloworld.show()");
		node.childArrayList.add(node1);
		xmlResultTreeNode node2 = new xmlResultTreeNode(
				"public static int helloworld.getValue(java.lang.String[])");
		node1.childArrayList.add(node2);
		xmlResultTreeNode node3 = new xmlResultTreeNode(
				"public static string helloworld.getText(java.lang.String[])");
		node2.childArrayList.add(node3);
		head.childArrayList.add(node);
		
		root.setText("public static string helloworld.main(java.lang.String[])");
		 root.setImage(new Image(display, "F://javaworkspace//src//2.bmp"));*/
		root.setExpanded(true);

		// 遍历arraylist并生成树
		traverse(head, root); 
		makePic pic = new makePic(this);
		shell.setSize(920, 600);
		shell.open();
		
		// 创建表格
		viewer.getTree().setHeaderVisible(true);
		viewer.getTree().setLinesVisible(true);
		makeColumn();
		viewer.setContentProvider(new MyTreeContenetProvider());
		viewer.setLabelProvider(new MyTableLableProvider());
		viewer.setInput(rootRoot);
		
		// 创建右键菜单
		MyActionGroup actionGroup = new MyActionGroup(viewer, this);
		actionGroup.fillContextMenu(new MenuManager());
		// 窗口关闭的响应事件 调试
		shell.addShellListener(new ShellAdapter() {
			public void shellClosed(ShellEvent arg0) {
				// System.out.println("closed");
				// arg0.doit = false;
				// shell.setVisible(false);

			}
		});
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	void makeColumn() {
		TreeColumn column = new TreeColumn(viewer.getTree(), SWT.LEFT);
		column.setText("类名");
		column.setWidth(200);
		column = new TreeColumn(viewer.getTree(), SWT.LEFT);
		column.setText("函数名");
		column.setWidth(100);
		column = new TreeColumn(viewer.getTree(), SWT.LEFT);
		column.setText("访问级别");
		column.setWidth(100);
		column = new TreeColumn(viewer.getTree(), SWT.LEFT);
		column.setText("返回值");
		column.setWidth(100);
		column = new TreeColumn(viewer.getTree(), SWT.LEFT);
		column.setText("参数");
		column.setWidth(200);
		column = new TreeColumn(viewer.getTree(), SWT.LEFT);
		column.setText("子节点数量");
		column.setWidth(100);
		column = new TreeColumn(viewer.getTree(), SWT.LEFT);
		column.setText("调用次数");
		column.setWidth(100);
	}

	// 遍历arraylist生成树
	void traverse(xmlResultTreeNode head, TreeItem item) {
		item.setText(head.getName());
		int childNum = head.getNumOfChild();
		System.out.println(head.getName());
		System.out.println("当前节点有孩子："+childNum+"个");
		if (childNum == 0) {
			return;
		}
		for (int i = 0; i < childNum; i++) {
			xmlResultTreeNode node = head.getChild(i);
			System.out.println(i+" =====> "+node.getName());
			TreeItem subItem = new TreeItem(item, SWT.CHECK);
			subItem.setExpanded(true);
			traverse(node, subItem);
		}
		System.out.println();
	}

	class MakeFilt extends SelectionAdapter {
		String lines[];
		boolean flag = true;

		public void widgetSelected(SelectionEvent e) {
			String textString = ruleInput.getText();
			lines = textString.split("\n");
			int numOfLines = lines.length;
			for (int i = 0; i < numOfLines; i++) {
				Pattern p = Pattern.compile("\\s*|\t|\r|\n");
				Matcher m = p.matcher(lines[i]);
				lines[i] = m.replaceAll("");
			}
			if (filter != null) {
				viewer.removeFilter(filter);
			}
			filter = new TreeFilter(lines);
			viewer.addFilter(filter);
		}
	}
}
