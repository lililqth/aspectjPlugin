package com.aspectj.demo;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JList;

import java.awt.List;
import java.awt.ScrollPane;
import java.awt.Label;
import java.awt.Button;
import java.awt.Choice;
import java.awt.Canvas;

import javax.swing.JEditorPane;
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
import com.aspectj.run.Run;
import com.aspectj.tree.DrawTree;
import com.aspectj.tree.xmlResultTreeNode;


import java.awt.TextArea;

import javax.xml.parsers.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Listener;
import org.w3c.dom.*;
import org.xml.sax.*;

import javax.swing.JComboBox;
import java.awt.Scrollbar;

public class Frame extends JFrame {

	private JPanel contentPane;
	private Choice choice;
	private String parentpath; //文件路径
	private String javaname;

	public static String packagename;//保存着包的名字
	private static String list[] = new String[1000]; //保存函数的名字
	private static int functiontime[] = new int[1000]; //保存了对应函数出现的次数
	

	private static int functionlenth = 0;
	ArrayList<xmlResultTreeNode> result = null;
	TextArea textArea_1 = null;
	/**
	 * Launch the application.
	 */
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
		
		setTitle("Aspectj Plugin");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 661, 488);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 10, 635, 439);
		panel.setForeground(SystemColor.activeCaptionText);
		contentPane.add(panel);
		
		choice = new Choice();
		choice.setBounds(90, 165, 152, 21);
		choice.setFont(new Font("黑体", Font.PLAIN, 12));
		choice.add("before( Formals )");
		choice.add("after( Formals ) returning [ ( Formal ) ]");
		choice.add("after( Formals ) throwing [ ( Formal ) ]");
		choice.add("after( Formals )");
		choice.add("Type around( Formals )");
		panel.setLayout(null);
		panel.add(choice);
		
		final List list_1 = new List();
		list_1.setBounds(342, 10, 283, 176);
		list_1.setFont(new Font("黑体", Font.PLAIN, 12));
		list_1.setMultipleSelections(true);
		list_1.setMultipleMode(true);
		
		
    
		panel.add(list_1);
		
		Label label = new Label("\u51FD\u6570\u5217\u8868");
		label.setBounds(258, 10, 70, 23);
		label.setFont(new Font("黑体", Font.PLAIN, 12));
		label.setAlignment(Label.CENTER);
		panel.add(label);
		
		Button openbutton = new Button("\u8F7D\u5165\u4E3B\u7C7B");
		openbutton.setBounds(150, 33, 92, 30);
		openbutton.addActionListener(new ActionListener() {
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
					AnalysisTool.analysis(filepath);
					
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
			}
		});
		openbutton.setFont(new Font("黑体", Font.PLAIN, 12));
		panel.add(openbutton);
		
		Button button = new Button("\u751F\u6210\u6811\u72B6\u56FE");
		button.setBounds(459, 395, 87, 30);
		button.setFont(new Font("黑体", Font.PLAIN, 12));
		 
		button.addActionListener(
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
		
		final TextField textField = new TextField();
		textField.setBounds(135, 86, 107, 23);
		panel.add(textField); 
		Button button_3 = new Button("\u6DFB\u52A0package\u8DEF\u5F84");
		button_3.addActionListener(new ActionListener() {
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
			}
		});
		button_3.setFont(new Font("黑体", Font.PLAIN, 12));
		button_3.setBounds(39, 33, 107, 30);
		panel.add(button_3);
		panel.add(button);
		
		Label label_3 = new Label("Package\u8DEF\u5F84");
		label_3.setFont(new Font("黑体", Font.PLAIN, 12));
		label_3.setAlignment(Label.CENTER);
		label_3.setBounds(39, 86, 92, 23);
		panel.add(label_3);
		
		
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setToolTipText("");
		progressBar.setBounds(30, 10, 222, 122);
		panel.add(progressBar);
		
		final Choice choice_1 = new Choice();
		choice_1.setFont(new Font("黑体", Font.PLAIN, 12));
		choice_1.setBounds(90, 213, 152, 21);
		panel.add(choice_1);
		
		final TextArea textArea = new TextArea();
		textArea.setBounds(52, 256, 190, 115);
		panel.add(textArea);
		
		Button button_1 = new Button("\u63D0\u4EA4\u4EE3\u7801");
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
		button_1.setBounds(52, 386, 87, 30);
		panel.add(button_1);
		
		Label label_1 = new Label("Active");
		label_1.setFont(new Font("黑体", Font.PLAIN, 12));
		label_1.setAlignment(Label.CENTER);
		label_1.setBounds(39, 165, 45, 23);
		panel.add(label_1);
		
		Label label_2 = new Label("Way");
		label_2.setFont(new Font("黑体", Font.PLAIN, 12));
		label_2.setAlignment(Label.CENTER);
		label_2.setBounds(39, 211, 45, 23);
		panel.add(label_2);
		
		Button button_2 = new Button("\u7F16\u8BD1\u6267\u884C");
		
		button_2.setFont(new Font("黑体", Font.PLAIN, 12));
		button_2.setBounds(155, 386, 87, 30);
		panel.add(button_2);
		
		JProgressBar progressBar_1 = new JProgressBar();
		progressBar_1.setToolTipText("");
		progressBar_1.setBounds(30, 142, 227, 287);
		panel.add(progressBar_1);
		
		JList list = new JList();
		list.setBounds(282, 73, 1, 1);
		panel.add(list);
		
		textArea_1 = new TextArea();
		textArea_1.setBounds(342, 201, 283, 170);
		panel.add(textArea_1);
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Run.runAnalysis("F:\\java\\helloworld", "helloworld.java", "add.aj");
				Run.runAnalysis(parentpath, javaname, ".aj", textArea_1);
			}	
		});
		
		choice_1.add("execution");
		choice_1.add("call");
		
	}
}
