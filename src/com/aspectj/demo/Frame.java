package com.aspectj.demo;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.JButton;

import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.swing.JList;

import java.awt.Component;
import java.awt.List;
import java.awt.Panel;
import java.awt.ScrollPane;
import java.awt.Label;
import java.awt.Button;
import java.awt.Choice;
import java.awt.Canvas;

import javax.swing.Icon;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.JTextArea;
import javax.swing.Box;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JLayeredPane;
import javax.swing.JInternalFrame;
import javax.swing.JDesktopPane;
import javax.swing.JCheckBox;
import javax.swing.UIManager;

import java.awt.TextField;

import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.JProgressBar;

import java.awt.Color;

import javax.swing.JFormattedTextField;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.aspectj.analysis.AnalysisTool;
import com.aspectj.coding.addcode;

import com.aspectj.run.MyPrintStream;
import com.aspectj.run.Run;
import com.aspectj.tree.DrawTree;
import com.aspectj.tree.xmlResultTreeNode;


import java.awt.TextArea;

import javax.xml.parsers.*;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.w3c.dom.*;
import org.xml.sax.*;

import javax.swing.JComboBox;
import java.awt.Scrollbar;
import javax.swing.JTree;

public class Frame extends JFrame {
	Editor edit = null;
	int flagClick ;
	protected static FileSystemView fsv = FileSystemView.getFileSystemView();
	private static class FileTreeCellRenderer extends DefaultTreeCellRenderer {
		private Map<String, Icon> iconCache = new HashMap<String, Icon>();

		private Map<File, String> rootNameCache = new HashMap<File, String>();
		
		
		JMenuItem showTreeItem = null;
		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean sel, boolean expanded, boolean leaf, int row,
				boolean hasFocus) {
			FileTreeNode ftn = (FileTreeNode) value;
			File file = ftn.file;
			String filename = "";
			if (file != null) {
				if (ftn.isFileSystemRoot) {
					filename = this.rootNameCache.get(file);
					if (filename == null) {
						filename = fsv.getSystemDisplayName(file);
						this.rootNameCache.put(file, filename);
					}
					// long end = System.currentTimeMillis();
					// System.out.println(filename + ":" + (end - start));
				} else {
					filename = file.getName();
				}
			}
			JLabel result = (JLabel) super.getTreeCellRendererComponent(tree,
					filename, sel, expanded, leaf, row, hasFocus);
			if (file != null) {
				Icon icon = this.iconCache.get(filename);
				if (icon == null) {
					// System.out.println("Getting icon of " + filename);
					icon = fsv.getSystemIcon(file);
					this.iconCache.put(filename, icon);
				}
				result.setIcon(icon);
			}
			return result;
		}
	}
	private static class FileTreeNode implements TreeNode 
	{
		private File file;

		private File[] children;

		private TreeNode parent;

		private boolean isFileSystemRoot;

		public FileTreeNode(File file, boolean isFileSystemRoot, TreeNode parent) {
			this.file = file;
			this.isFileSystemRoot = isFileSystemRoot;
			this.parent = parent;
			this.children = this.file.listFiles();
			if (this.children == null)
				this.children = new File[0];
		}

		public FileTreeNode(File[] children) {
			this.file = null;
			this.parent = null;
			this.children = children;
		}

		public Enumeration<?> children() {
			final int elementCount = this.children.length;
			return new Enumeration<File>() {
				int count = 0;

				public boolean hasMoreElements() {
					return this.count < elementCount;
				}

				public File nextElement() {
					if (this.count < elementCount) {
						return FileTreeNode.this.children[this.count++];
					}
					throw new NoSuchElementException("Vector Enumeration");
				}
			};

		}

		public boolean getAllowsChildren() {
			return true;
		}

		public TreeNode getChildAt(int childIndex) {
			return new FileTreeNode(this.children[childIndex],
					this.parent == null, this);
		}

		public int getChildCount() {
			return this.children.length;
		}

		public int getIndex(TreeNode node) {
			FileTreeNode ftn = (FileTreeNode) node;
			for (int i = 0; i < this.children.length; i++) {
				if (ftn.file.equals(this.children[i]))
					return i;
			}
			return -1;
		}

		public TreeNode getParent() {
			return this.parent;
		}

		public boolean isLeaf() {
			return (this.getChildCount() == 0);
		}
	}
		
	private JPanel contentPane;
	private Choice choice;
	private String parentpath; //文件路径
	private String javaname;
	Button button_1 = new Button("\u63D0\u4EA4\u4EE3\u7801");
	Button button_2 = new Button("\u7F16\u8BD1\u6267\u884C");
	
	public static String packagename;//保存着包的名字
	private static String list[] = new String[1000]; //保存函数的名字
	private static int functiontime[] = new int[1000]; //保存了对应函数出现的次数
	
	private static int functionlenth = 0;
	ArrayList<xmlResultTreeNode> result = null;
	public TextArea textArea_1 = null;
	JTree tree = null;
	public static String[] getfunctionnamearray(){
		return list;
	} //返回函数的名字
	
	public static int[]  getfunctiontimearray(){
		return functiontime;
	} //返回函数的名字对应出现的次数
	
	public static int getfunctionlenth(){
		return functionlenth;
	} //返回函数的个数
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					Frame frame = new Frame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Frame() {
		flagClick = 0;
		setTitle("Aspectj Plugin");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 849, 520);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		Panel panel = new Panel();
		panel.setBounds(10, 10, 800, 439);
		panel.setForeground(SystemColor.activeCaptionText);
		contentPane.add(panel);
		
		choice = new Choice();
		choice.setBounds(237, 164, 152, 21);
		choice.setFont(new Font("黑体", Font.PLAIN, 12));
		choice.add("before( Formals )");
		choice.add("after( Formals ) returning [ ( Formal ) ]");
		choice.add("after( Formals ) throwing [ ( Formal ) ]");
		choice.add("after( Formals )");
		choice.add("Type around( Formals )");
		panel.setLayout(null);
		panel.add(choice);
		
		final List list_1 = new List();
		list_1.setBounds(490, 10, 300, 176);
		list_1.setFont(new Font("黑体", Font.PLAIN, 12));
		list_1.setMultipleSelections(true);
		list_1.setMultipleMode(true);
		
		
    
		panel.add(list_1);
		
		Label label = new Label("\u51FD\u6570\u5217\u8868");
		label.setBounds(406, 10, 70, 23);
		label.setFont(new Font("黑体", Font.PLAIN, 12));
		label.setAlignment(Label.CENTER);
		panel.add(label);
		
		final TextField textField = new TextField();
		textField.setBounds(282, 64, 107, 23);
		panel.add(textField);
		
		Label label_3 = new Label("Package\u8DEF\u5F84");
		label_3.setFont(new Font("黑体", Font.PLAIN, 12));
		label_3.setAlignment(Label.CENTER);
		label_3.setBounds(186, 64, 92, 23);
		panel.add(label_3);
		
		
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setToolTipText("");
		progressBar.setBounds(173, 10, 227, 122);
		panel.add(progressBar);
		
		final Choice choice_1 = new Choice();
		choice_1.setFont(new Font("黑体", Font.PLAIN, 12));
		choice_1.setBounds(237, 212, 152, 21);
		panel.add(choice_1);
		
		final TextArea textArea = new TextArea();
		textArea.setBounds(199, 255, 190, 115);
		panel.add(textArea);
		
		
		
		button_1.setEnabled(false);
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String active = choice.getSelectedItem();
				String way = choice_1.getSelectedItem();
				String selectname[] = new String[100];
				
				String content = textArea.getText();
				
				selectname = list_1.getSelectedItems();
				try {
					addcode.writeaj(parentpath, active, way, selectname, content);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
//				for(int i = 0; i < selectname.length; i++){
//					System.out.println(selectname[i]);
//				}
			}
		});
		button_1.setFont(new Font("黑体", Font.PLAIN, 12));
		button_1.setBounds(199, 385, 87, 30);
		panel.add(button_1);
		
		Label label_1 = new Label("Active");
		label_1.setFont(new Font("黑体", Font.PLAIN, 12));
		label_1.setAlignment(Label.CENTER);
		label_1.setBounds(186, 164, 45, 23);
		panel.add(label_1);
		
		Label label_2 = new Label("Way");
		label_2.setFont(new Font("黑体", Font.PLAIN, 12));
		label_2.setAlignment(Label.CENTER);
		label_2.setBounds(186, 210, 45, 23);
		panel.add(label_2);
		
		
		button_2.setEnabled(false);
		
		button_2.setFont(new Font("黑体", Font.PLAIN, 12));
		button_2.setBounds(302, 385, 87, 30);
		panel.add(button_2);
		
		JProgressBar progressBar_1 = new JProgressBar();
		progressBar_1.setToolTipText("");
		progressBar_1.setBounds(173, 142, 227, 287);
		panel.add(progressBar_1);
		
		JList jlist = new JList();
		jlist.setBounds(282, 73, 1, 1);
		panel.add(jlist);
		
		textArea_1 = new TextArea();
		textArea_1.setBounds(490, 213, 300, 170);
		panel.add(textArea_1);
		
//		MyPrintStream printStream = new MyPrintStream(System.out, textArea_1);
//		System.setOut(printStream);
		
		Label label_4 = new Label("\u8F93\u51FA");
		label_4.setFont(new Font("黑体", Font.PLAIN, 12));
		label_4.setAlignment(Label.CENTER);
		label_4.setBounds(406, 211, 70, 23);
		panel.add(label_4);
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Run.runAnalysis("F:\\java\\helloworld", "helloworld.java", "add.aj");
				Run.runAnalysis(parentpath, javaname, ".aj");
			}	
		});
		
		
		//File currentFile = new File();
		//File[] roots = currentFile.listFiles();
		JScrollPane treeScrollPane = new JScrollPane();
		treeScrollPane.setBounds(10, 10, 153, 419);
		File[] roots = File.listRoots();
		FileTreeNode rootTreeNode = new FileTreeNode(roots);
		tree = new JTree(rootTreeNode); 
		tree.setCellRenderer(new FileTreeCellRenderer());
		tree.setRootVisible(true);
		
		tree.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e) {
				if(flagClick==0)
				{
					flagClick = 1;
					return;
				}
				flagClick = 0;
				TreePath path = tree.getPathForLocation(e.getX(),e.getY());
				try {
					FileTreeNode node = (FileTreeNode)path.getLastPathComponent();
					if (node.isLeaf()) {
						File file = node.file;
						String fileName = file.getAbsolutePath();
						System.out.println(fileName);
						/**** 此处添加单击文件，可编辑文件。 ****/
						edit = new Editor();
						//edit.edite(file);
					}
				} catch (java.lang.NullPointerException	e2) {
					// TODO: handle exception
					System.out.println("null");
				}

				
			}
		});
		
		treeScrollPane.setViewportView(tree);
		panel.add(treeScrollPane);
		
		JMenu menu = new JMenu("操作");
		JMenuItem menuPackageItem = new JMenuItem("添加package路径");
		final JMenuItem menuMainItem = new JMenuItem("载入主类");
		final JMenuItem showTreeItem = new JMenuItem("显示树状图");
		menu.add(menuPackageItem);
		menu.add(menuMainItem);
		menu.add(showTreeItem);
		menuMainItem.setEnabled(false);
		showTreeItem.setEnabled(false);
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(menu);
		this.setJMenuBar(menuBar);
		showTreeItem.addActionListener(
				new ActionListener() {
				public void actionPerformed(ActionEvent arg0) 
				{
					try {
						result = AnalysisTool.analysisXMLFile(parentpath+"\\analysisResult.xml");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					DrawTree.getInstance(result.get(0));
				}
			});
		menuMainItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				JFileChooser file = new JFileChooser("C:");
//				file.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if(file.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
					File fl = file.getSelectedFile();
					String filepath = fl.getAbsolutePath(); 
					javaname = filepath; 
					System.out.println(filepath); 
//					list_1.add(filepath);
//					choice.add(filepath);
//					choice.add(filepath);
					try {
						AnalysisTool.analysis(filepath);
					} catch (IOException e3) {
						// TODO Auto-generated catch block
						e3.printStackTrace();
					}
					
					File currentFile = new File(filepath.substring(0, filepath.lastIndexOf('\\')));
					File[] roots = currentFile.listFiles();
					FileTreeNode rootTreeNode = new FileTreeNode(roots);
					TreeModel treeModel = new DefaultTreeModel(rootTreeNode);
					tree.setModel(treeModel);
					/***************读取xml信息***************************************/
					File analysisFile = new File(filepath);
					parentpath = analysisFile.getParent();
					String pathname = analysisFile.getParent()+"\\analysisResult.xml";
					
					try {
						Thread.sleep(2500);
					} catch (InterruptedException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					try {
						FileWriter fw = new FileWriter(pathname, true);
						fw.write("</functions>");
						fw.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					
					//System.out.println(pathname);
					DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
					try
					{
						DocumentBuilder db = dbf.newDocumentBuilder();
						Document doc = db.parse(pathname);

						NodeList startList = doc.getElementsByTagName("start");
						System.out.println("共有" + startList.getLength() + "个start节点");
						
						int j =0 , k = 0;
						for (int i = 0; i < startList.getLength(); i++)
						{
							Node start = startList.item(i);
							String name = start.getTextContent(); 
							System.out.println(name);
							for(j = 0; j < k; j++){
								if(name.equals(list[j])==true){
									functiontime[j]++;
									break;
								}
							}
							if(j == k){
								functiontime[k] = 1;
								list[k++] = name;
							}
						}//去重（去除文件名中重复的部分）
						for(int i = 0; i < k; i++){
							list_1.add(list[i]);
						}
						functionlenth = k;
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
					
				}
				//MyPrintStream printStream = new MyPrintStream(System.out, textArea_1);
				//System.setOut(printStream);
				//button.enable(true);
				button_1.enable(true);
				button_2.enable(true);
				showTreeItem.setEnabled(true);
			}
		});
		menuPackageItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser file = new JFileChooser("C:");
				file.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if(file.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
					File fl = file.getSelectedFile();
					String filepath = fl.getAbsolutePath(); 
					if(packagename == null)
						packagename = filepath + "\\*.java ";
					else
						packagename += " "+ filepath + "\\*.java";
					System.out.println(packagename); 
					filepath = (textField.getText() + ";") + filepath;
					textField.setText(filepath);
					}
				menuMainItem.setEnabled(true);
			}
		});
		choice_1.add("execution");
		choice_1.add("call");
		
	}
	
}

