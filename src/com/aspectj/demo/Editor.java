package com.aspectj.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.NoSuchElementException;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Group;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.aspectj.analysis.AnalysisTool;
import com.aspectj.analysis.ValueChangePoint;
import com.aspectj.analysis.ValueTracker;
import com.aspectj.coding.addcode;
import com.aspectj.coding.findpackage;
import com.aspectj.run.MyPrintStream;
import com.aspectj.run.Run;
import com.aspectj.tree.DrawTree;
import com.aspectj.tree.xmlResultTreeNode;

public class Editor {
	
	//�ؼ������б�
	public static Shell shell = null;
	private static TabFolder tabFolder = null;
	private static TabFolder fileTabFolder = null;
	private static Font font = null;
	private static Color fontColor = null;
	private static File editFile = new File("D://Desktop//session.txt");
	static List functionList = null; //�����б�
	static Composite centerComposite = null;
	public static Display display = null;
	static File rootDir = null;
	static Tree tree = null;
	static ImageRegistry imageRegistry;
	static Image iconFolder = null;
	static Image iconFile = null;

	//�ļ������б�
	private static String packagename;//�����Ű�������
	private static String parentpath = "E:"; //�ļ�·��
	private static String javaname = null;
	private static String list[] = new String[1000]; //���溯��������
	private static int functiontime[] = new int[1000]; //�����˶�Ӧ�������ֵĴ���
	private static int functionlenth = 0;              //�����ź����ĸ���
	static ArrayList<xmlResultTreeNode> result = null;
	static ArrayList<ValueChangePoint> variatelog = null;
	public static String[] getfunctionnamearray(){
		return list;
	} //���غ���������
	
	public static int[]  getfunctiontimearray(){
		return functiontime;
	} //���غ��������ֶ�Ӧ���ֵĴ���
	
	public static int getfunctionlenth(){
		return functionlenth;
	} //���غ����ĸ���
	
	public static String getparentpath(){
		return parentpath;
	} //���ع���src
	
	public static String getjavaname(){
		return javaname;
	} //����main�������Ե�ַ
	
	public static String getpackagename(){
		return packagename;
	} //����main�������Ե�ַ
	/**
	 * @param f
	 *            �ļ�
	 * @return �ļ�����,�ļ�����1Mʱ����һ���ļ�̫����ַ�����
	 */
	private static String getFileContent(File f) {
		StringBuilder sb = new StringBuilder();
		try {
			if (f.length() > 1024 * 1024)
				return f.getName() + " is too big to open";

			FileReader fileReader = new FileReader(f);
			BufferedReader bf = new BufferedReader(fileReader);
			do {
				String temp = bf.readLine();
				if (temp != null)
					sb.append(temp + "\n");
				else
					break;
			} while (true);
			bf.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return sb.toString();

	}

	/**
	 * @param f
	 *            Ҫ�򿪵��ļ�
	 * @param text
	 *            Ҫ��ӵ�ѡ�������
	 */
	static void addTab(File f, String text) {
		TabItem tab = new TabItem(tabFolder, SWT.NONE);
		if (f != null)
			tab.setText(f.getName());
		else
			tab.setText("new tab");
		Text texts = new Text(tabFolder, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
		texts.setText(text);
		texts.setFont(font);
		texts.setForeground(fontColor);
		tab.setControl(texts);
		tabFolder.setSelection(tab);
		texts.setFocus();
		texts.setData(f);
	}

	/**
	 * ����ѡ������ݵ�ԭʼ�ļ��У�����ǰҪ��ȷ�ϡ�
	 * 
	 * @param tab
	 *            Ҫ�����ѡ�
	 */
	private static void saveTab(TabItem tab) {
		Text texts = (Text) tab.getControl();
		File f = (File) texts.getData();
		if (f == null) {
			saveTabAs(tab);
			return;
		}

		MessageBox ensure = new MessageBox(shell, SWT.OK | SWT.CANCEL
				| SWT.ICON_QUESTION);
		ensure.setMessage("���ļ��Ѿ����ڣ�ȷ�����ǣ�");
		int result = ensure.open();
		if (result != SWT.OK)
			return;

		try {
			FileWriter fw = new FileWriter(f, false);
			fw.write(texts.getText());
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ����ѡ������ݵ��µ��ļ���
	 * 
	 * @param tab
	 *            Ҫ�����ѡ�
	 */
	private static void saveTabAs(TabItem tab) {
		Text texts = (Text) tab.getControl();

		FileDialog fd = new FileDialog(shell, SWT.SAVE);// SWT.SAVE����ָ��Ĭ������
		fd.setFileName("���ļ�" + System.currentTimeMillis() + ".txt");// Ĭ�ϱ��������
		fd.setFilterExtensions(new String[] { "*.txt;*.html;*.htm;*.java" });
		while (fd.open() == null)
			;
		File f = new File(fd.getFilterPath() + File.separator
				+ fd.getFileName());

		try {
			FileWriter fw = new FileWriter(f, false);
			fw.write(texts.getText());
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Launch the application.
	 * 
	 * @param editfFile
	 *            ��Ҫ�򿪵��ļ�
	 * @return
	 */
	public static void open(File editfFile) {
		String content = getFileContent(editfFile);
		addTab(editfFile, content);
	}

	protected static void folderDig(Shell parent) throws FileNotFoundException{
		//�½��ļ��У�Ŀ¼���Ի���
		DirectoryDialog folderdlg=new DirectoryDialog(parent);
		//�����ļ��Ի���ı���
		folderdlg.setText("�ļ�ѡ��");
		//���ó�ʼ·��
		folderdlg.setFilterPath("SystemDrive");
		//���öԻ�����ʾ�ı���Ϣ
		folderdlg.setMessage("��ѡ����Ӧ���ļ���");
		//���ļ��Ի��򣬷���ѡ���ļ���Ŀ¼
		String selecteddir=folderdlg.open();
		if(selecteddir==null){
		return ;
		}
		else{
		parentpath = selecteddir;
		setRootDir(new File(selecteddir));
		System.out.println("��ѡ�е��ļ���Ŀ¼Ϊ��"+selecteddir);
		ArrayList <String> temp = findpackage.getPackageStrings(selecteddir);
		
		packagename = parentpath + "\\*.java ";
		
		for(int i = 0; i < temp.size();i++){
				packagename += " "+ parentpath +"\\"+ temp.get(i) + "\\*.java";
		}
		
		System.out.println(parentpath + "  "+ temp.size());
		}
		}

	
	public static void createConsole() {

		// ���Console
		Text consoleText = new Text(centerComposite, SWT.BORDER | SWT.WRAP
				| SWT.V_SCROLL | SWT.MULTI);
		consoleText.setSize(300, 100);
		final FormData textFormData = new FormData();
		textFormData.top = new FormAttachment(75, 0);
		textFormData.left = new FormAttachment(0, 5);
		textFormData.right = new FormAttachment(100, 5);
		textFormData.bottom = new FormAttachment(100, -5);
		consoleText.setLayoutData(textFormData);
		MyPrintStream printStream = new MyPrintStream(System.out, consoleText);
		System.setOut(printStream);
		System.out.println("��������ض���");
	}

	public static void createMenu() {
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);

		MenuItem mntmFile = new MenuItem(menu, SWT.CASCADE);
		mntmFile.setText("File");

		Menu menu_1 = new Menu(mntmFile);
		mntmFile.setMenu(menu_1);

		MenuItem mntmOpenItem = new MenuItem(menu_1, SWT.NONE);
		mntmOpenItem.setText("&Open");
		mntmOpenItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				try {
					folderDig(shell);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		MenuItem mntmanalysisItem = new MenuItem(menu_1, SWT.NONE);
		mntmanalysisItem.setText("&analysis");
		mntmanalysisItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if(javaname != null){
					System.out.println(javaname);
					AnalysisTool.analysis(javaname);
				}
				else {
					MessageBox messageBox = 
							   new MessageBox(shell, 
							    SWT.OK| 
							    SWT.CANCEL| 
							    SWT.ICON_WARNING); 
							 messageBox.setMessage("�Ҳ������࣡"); 
							 messageBox.open(); 
				}
				setRootDir(new File(parentpath));
				
				/***************��ȡxml��Ϣ***************************************/
				String pathname = parentpath+"\\analysisResult.xml";
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
					org.w3c.dom.Document doc = db.parse(pathname);

					NodeList startList = doc.getElementsByTagName("start");
					System.out.println("����" + startList.getLength() + "��start�ڵ�");
					
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
					}//ȥ�أ�ȥ���ļ������ظ��Ĳ��֣�
					for(int i = 0; i < k; i++){
						functionList.add(list[i]);
					}
					functionlenth = k;
				}
				catch (Exception e1)
				{
					e1.printStackTrace();
				}
				
			}
		});
		
		MenuItem mntmTest = new MenuItem(menu_1, SWT.NONE);
		mntmTest.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TabItem[] items = tabFolder.getSelection();
				if (items.length > 0)
					saveTab(items[0]);
			}
		});
		mntmTest.setText("&Save");

		MenuItem mntmSaveAs = new MenuItem(menu_1, SWT.NONE);
		mntmSaveAs.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TabItem[] items = tabFolder.getSelection();
				if (items.length > 0)
					saveTabAs(items[0]);
			}
		});
		mntmSaveAs.setText("Save as");

		// �����ǰѡ����ݵĲ˵���
		MenuItem mntmCleancurrentfile = new MenuItem(menu, SWT.NONE);
		mntmCleancurrentfile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TabItem[] items = tabFolder.getSelection();
				if (items.length > 0)
					((Text) items[0].getControl()).setText("");
			}
		});
		mntmCleancurrentfile.setText("&Clean");

		// �رյ�ǰѡ��Ĳ˵���
		MenuItem mntmClose = new MenuItem(menu, SWT.NONE);
		mntmClose.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TabItem[] items = tabFolder.getSelection();
				if (items.length > 0)
					items[0].dispose();
			}
		});
		mntmClose.setText("Close");

		// ����ѡ��˵�
		MenuItem mntmFont = new MenuItem(menu, SWT.NONE);
		mntmFont.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FontDialog fd = new FontDialog(shell);// ����ѡ��Ի����ʹ��
				FontData fontData = fd.open();
				RGB fontColor = fd.getRGB();
				if (fontData != null) {
					font = new Font(display, fontData);
					TabItem[] items = tabFolder.getSelection();
					if (items.length > 0)
						((Text) items[0].getControl()).setFont(font);
				}
			}
		});
		mntmFont.setText("Font");

		// ��ɫ�˵�
		MenuItem mntmFontcolor = new MenuItem(menu, SWT.NONE);
		mntmFontcolor.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ColorDialog colorDialog = new ColorDialog(shell);// ��ɫѡ��Ի����ʹ��
				RGB rgb = colorDialog.open();
				if (rgb != null) {
					fontColor = new Color(display, rgb);
					TabItem[] items = tabFolder.getSelection();
					if (items.length > 0)
						((Text) items[0].getControl()).setForeground(fontColor);
				}

			}
		});
		mntmFontcolor.setText("FontColor");

		
		//��ʾ��״ͼ�Ĳ˵���
				MenuItem openTreeItem = new MenuItem(menu, SWT.NONE);
				openTreeItem.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						try {
							result = AnalysisTool.analysisXMLFile(parentpath
									+ "\\analysisResult.xml");
							showtree();
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							
							e1.printStackTrace();
						}
						// DrawTree.getInstance(result.get(0));
						
					}
				});
				openTreeItem.setText("tree");
	}
	
	public static void showtree()
	{
		if (!display.isDisposed()) {

			Runnable runnable = new Runnable() {

				public void run() {
					DrawTree.getInstance(result.get(0));
				}
			};
			display.syncExec(runnable); // �ؼ�����һ����
			

		}
	}
	

	public static void createEditTab() {
		tabFolder = new TabFolder(centerComposite, SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.CANCEL | SWT.MULTI);
		final FormData tabFormData = new FormData();
		tabFormData.top = new FormAttachment(0, 5);
		tabFormData.left = new FormAttachment(0, 5);
		tabFormData.right = new FormAttachment(100, 5);
		tabFormData.bottom = new FormAttachment(75, -5);
		tabFolder.setLayoutData(tabFormData);
		}

	private static File renameFile(File file, String newName) {
		File dest = new File(file.getParentFile(), newName);
		if (file.renameTo(dest)) {
			return dest;
		} else {
			return null;
		}
	}

	private static void setRootDir(File root) {
		if ((!root.isDirectory()) || (!root.exists()))
			throw new IllegalArgumentException("Invalid root: " + root);
		rootDir = root;
		shell.setText("Aspectj Plugin");
		if (tree.getItemCount() > 0) {
			TreeItem[] items = tree.getItems();
			for (int i = 0; i < items.length; i++) {
				items[i].dispose();
			}
		}
		File[] files = root.listFiles();
		for (int i = 0; files != null && i < files.length; i++)
			addFileToTree(tree, files[i]);
	}

	private static Image getIcon(File file) {
		if (file.isDirectory())
			return iconFolder;
		int lastDotPos = file.getName().indexOf('.');
		if (lastDotPos == -1)
			return iconFile;
		Image image = getIcon(file.getName().substring(lastDotPos + 1));
		return image == null ? iconFile : image;
	}

	private static Image getIcon(String extension) {
		if (imageRegistry == null)
			imageRegistry = new ImageRegistry();
		Image image = imageRegistry.get(extension);
		if (image != null)
			return image;
		Program program = Program.findProgram(extension);
		ImageData imageData = (program == null ? null : program.getImageData());
		if (imageData != null) {
			image = new Image(shell.getDisplay(), imageData);
			imageRegistry.put(extension, image);
		} else {
			image = iconFile;
		}
		return image;
	}

	private static void addFileToTree(Object parent, File file) {
		TreeItem item = null;
		if (parent instanceof Tree)
			item = new TreeItem((Tree) parent, SWT.NULL);
		else if (parent instanceof TreeItem)
			item = new TreeItem((TreeItem) parent, SWT.NULL);
		else
			throw new IllegalArgumentException(
					"parent should be a tree or a tree item: " + parent);
		item.setText(file.getName());
		item.setImage(getIcon(file));
		item.setData(file);
		if (file.isDirectory()) {
			// // recursively adds all the children of this file.
			// File[] files = file.listFiles();
			// for(int i=0; i<files.length; i++)
			// buildTree(item, files[i]);
			if (file.list() != null && file.list().length > 0)
				new TreeItem(item, SWT.NULL);
		}
	}

	public static void createFileTree() {
		iconFolder = new Image(shell.getDisplay(), "src/com/aspectj/demo/folder.gif");
		iconFile = new Image(shell.getDisplay(), "src/com/aspectj/demo/file.gif");
		fileTabFolder = new TabFolder(shell, SWT.H_SCROLL | SWT.V_SCROLL
				| SWT.CANCEL | SWT.MULTI);
		//�����tab����ʾ�����б�
		TabItem functionTabItem = new TabItem(fileTabFolder, SWT.NONE);
		functionList = new List(fileTabFolder,SWT.MULTI|SWT.BORDER);
		functionTabItem.setText("����");
		//functionList.add("Functin 1");
		functionTabItem.setControl(functionList);
		
		//�ļ�Ŀ¼����״ͼ
		TabItem tab = new TabItem(fileTabFolder, SWT.NONE);
		tab.setText("�ļ�Ŀ¼");
		tree = new Tree(fileTabFolder, SWT.BORDER);
		tab.setControl(tree);
		fileTabFolder.setSelection(tab);
		final FormData fileFormData = new FormData();
		fileFormData.top = new FormAttachment(0, 5);
		fileFormData.left = new FormAttachment(0, 5);
		fileFormData.right = new FormAttachment(20, 5);
		fileFormData.bottom = new FormAttachment(100, -5);
		fileTabFolder.setLayoutData(fileFormData);

		setRootDir(new File("C:"));
		tree.addTreeListener(new TreeListener() {
			public void treeCollapsed(TreeEvent e) {
			}

			public void treeExpanded(TreeEvent e) {
				TreeItem item = (TreeItem) e.item;
				TreeItem[] children = item.getItems();
				for (int i = 0; i < children.length; i++)
					if (children[i].getData() == null)
						children[i].dispose();
					else
						return;
				File[] files = ((File) item.getData()).listFiles();
				for (int i = 0; files != null && i < files.length; i++)
					addFileToTree(item, files[i]);
			}
		});
		tree.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {

			}

			public void widgetDefaultSelected(SelectionEvent e) {
				TreeItem item = (TreeItem) e.item;
				File file = (File) item.getData();
				if(javaname == null)
					javaname = file.getAbsolutePath();
				System.out.println(javaname);
				addTab(file, getFileContent(file));
//				if (Program.launch(file.getAbsolutePath())) {
//					System.out.println("File has been launched: " + file);
//				} else {
//					System.out.println("Unable to launch file: " + file);
//				}
				
			}
		});
		final TreeEditor editor = new TreeEditor(tree);
		tree.addListener(SWT.MouseDown, new Listener() {
			public void handleEvent(Event event) {
				Point point = new Point(event.x, event.y);
				final TreeItem item = tree.getItem(point);
				if (item == null)
					return;
				final Text text = new Text(tree, SWT.NONE);
				text.setText(item.getText());
				text.setBackground(shell.getDisplay().getSystemColor(
						SWT.COLOR_YELLOW));
				editor.horizontalAlignment = SWT.LEFT;
				editor.grabHorizontal = true;
				editor.setEditor(text, item);
				Listener textListener = new Listener() {
					public void handleEvent(final Event e) {
						switch (e.type) {
						case SWT.FocusOut:
							File renamed = renameFile((File) item.getData(),
									text.getText());
							if (renamed != null) {
								item.setText(text.getText());
								item.setData(renamed);
							}
							text.dispose();
							break;
						case SWT.Traverse:
							switch (e.detail) {
							case SWT.TRAVERSE_RETURN:
								renamed = renameFile((File) item.getData(),
										text.getText());
								if (renamed != null) {
									item.setText(text.getText());
									item.setData(renamed);
								}
							case SWT.TRAVERSE_ESCAPE:
								text.dispose();
								e.doit = false;
							}
							break;
						}
					}
				};
				text.addListener(SWT.FocusOut, textListener);
				text.addListener(SWT.Traverse, textListener);
				text.setFocus();
			}
		});
	}
	
	public static void createFunctionTab()
	{
		TabItem tab = new TabItem(tabFolder, SWT.NONE);
		
		tab.setText("AJ�������");
		Composite functionComposite = new Composite(tabFolder, SWT.NONE);
		functionComposite.setLayout(new FormLayout());
		Group insertGroup = new Group(functionComposite,SWT.SHADOW_IN);
		insertGroup.setText("�����е�");
		insertGroup.setLayout(new FormLayout());
		final FormData insertGroupFormData = new FormData();
		insertGroupFormData.top = new FormAttachment(5, 0);
		insertGroupFormData.left = new FormAttachment(5, 0);
		insertGroupFormData.right = new FormAttachment(35, 0);
		insertGroupFormData.bottom = new FormAttachment(95, 0);
		insertGroup.setLayoutData(insertGroupFormData);
		
		// �ı������
		final Text insertText  = new Text(insertGroup, SWT.V_SCROLL | SWT.BORDER | SWT.H_SCROLL);
		final FormData textFormData = new FormData();
		textFormData.top = new FormAttachment(35, 0);
		textFormData.left = new FormAttachment(5, 0);
		textFormData.right = new FormAttachment(95, 0);
		textFormData.bottom = new FormAttachment(75, 0);
		insertText.setLayoutData(textFormData);
		//System.out.println(insertText.getMessage());
		//�е�λ��������
		final Combo activeCombo = new Combo(insertGroup, SWT.DROP_DOWN|SWT.READ_ONLY);
		activeCombo.add("before( Formals )");
		activeCombo.add("after( Formals ) returning [ ( Formal ) ]");
		activeCombo.add("after( Formals ) throwing [ ( Formal ) ]");
		activeCombo.add("after( Formals )");
		activeCombo.add("Type around( Formals )");
		activeCombo.setText("before( Formals )");
		final FormData activeComboFormData = new FormData();
		activeComboFormData.top = new FormAttachment(5, 0);
		activeComboFormData.left = new FormAttachment(30, 0);
		activeComboFormData.right = new FormAttachment(95, 0);
		activeComboFormData.bottom = new FormAttachment(15, 0);
		activeCombo.setLayoutData(activeComboFormData);
		//�е�����������
		final Combo wayCombo = new Combo(insertGroup, SWT.DROP_DOWN|SWT.READ_ONLY);
		wayCombo.add("execution");
		wayCombo.add("call");
		wayCombo.setText("execution");
		final FormData wayComboFormData = new FormData();
		wayComboFormData.top = new FormAttachment(20, 0);
		wayComboFormData.left = new FormAttachment(30, 0);
		wayComboFormData.right = new FormAttachment(95, 0);
		wayComboFormData.bottom = new FormAttachment(30, 0);
		wayCombo.setLayoutData(wayComboFormData);
				
		Label activeLabel = new Label(insertGroup, SWT.NONE);
		activeLabel.setText("Active");
		final FormData activeLabelFormData = new FormData();
		activeLabelFormData.top = new FormAttachment(5, 0);
		activeLabelFormData.left = new FormAttachment(5, 0);
		activeLabelFormData.right = new FormAttachment(25, 0);
		activeLabelFormData.bottom = new FormAttachment(15, 0);
		activeLabel.setLayoutData(activeLabelFormData);
				
		Label wayLabel = new Label(insertGroup, SWT.NONE);
		wayLabel.setText("Way");
		final FormData wayLabelFormData = new FormData();
		wayLabelFormData.top = new FormAttachment(20, 0);
		wayLabelFormData.left = new FormAttachment(5, 0);
		wayLabelFormData.right = new FormAttachment(25, 0);
		wayLabelFormData.bottom = new FormAttachment(30, 0);
		wayLabel.setLayoutData(wayLabelFormData);
				
		Group variateGroup = new Group(functionComposite, SWT.SHADOW_IN);  
		variateGroup.setText("��������");
		variateGroup.setLayout(new FormLayout());
		final FormData variateGroupFormData = new FormData();
		variateGroupFormData.top = new FormAttachment(5, 0);
		variateGroupFormData.left = new FormAttachment(45, 0);
		variateGroupFormData.right = new FormAttachment(80, 0);
		variateGroupFormData.bottom = new FormAttachment(95, 0);
		variateGroup.setLayoutData(variateGroupFormData);
		Label variateLabel = new Label(variateGroup, SWT.NONE);
		variateLabel.setText("�����������");
		final FormData variateLabelFormData = new FormData();
		variateLabelFormData.top = new FormAttachment(5, 0);
		variateLabelFormData.left = new FormAttachment(5, 0);
		variateLabelFormData.right = new FormAttachment(25, 0);
		variateLabelFormData.bottom = new FormAttachment(15, 0);
		variateLabel.setLayoutData(variateLabelFormData);
		//����������
		final Text variateText  = new Text(variateGroup, SWT.BORDER);
		final FormData variateTextFormData = new FormData();
		variateTextFormData.top = new FormAttachment(5, 0);
		variateTextFormData.left = new FormAttachment(30, 0);
		variateTextFormData.right = new FormAttachment(95, 0);
		variateTextFormData.bottom = new FormAttachment(11, 0);
		variateText.setLayoutData(variateTextFormData);
		
		Label variateLogLabel = new Label(variateGroup, SWT.NONE);
		variateLogLabel.setText("����ֵ���٣�");
		final FormData variateLogLabelFormData = new FormData();
		variateLogLabelFormData.top = new FormAttachment(25, 0);
		variateLogLabelFormData.left = new FormAttachment(5, 0);
		variateLogLabelFormData.right = new FormAttachment(25, 0);
		variateLogLabelFormData.bottom = new FormAttachment(35, 0);
		variateLogLabel.setLayoutData(variateLogLabelFormData);
				
		final Table variateTable = new Table(variateGroup, SWT.MULTI | SWT.BORDER);
		final FormData variateTableFormData = new FormData();
		variateTableFormData.top = new FormAttachment(40, 0);
		variateTableFormData.left = new FormAttachment(5, 0);
		variateTableFormData.right = new FormAttachment(95, 0);
		variateTableFormData.bottom = new FormAttachment(95, 0);
		variateTable.setLayoutData(variateTableFormData);
		variateTable.setHeaderVisible(true);
		variateTable.setLinesVisible(true);
		TableColumn column1 = new TableColumn(variateTable, SWT.NONE);
		column1.setText("����");
		column1.setWidth(150);
		TableColumn column2 = new TableColumn(variateTable, SWT.NONE);
		column2.setText("��ֵ");
		column2.setWidth(90);
		TableColumn column3 = new TableColumn(variateTable, SWT.NONE);
		column3.setText("��ֵ");
		column3.setWidth(90);
		
		//��ʼ���ٰ�ť
		Button startAnalyseButton = new Button(variateGroup, SWT.NONE);
		startAnalyseButton.setText("��ʼ����");
		final FormData startAnalyseButtonFormData = new FormData();
		startAnalyseButtonFormData.top = new FormAttachment(25, 0);
		startAnalyseButtonFormData.left = new FormAttachment(30, 0);
		startAnalyseButtonFormData.right = new FormAttachment(95, 0);
		startAnalyseButtonFormData.bottom = new FormAttachment(33, 0);
		startAnalyseButton.setLayoutData(startAnalyseButtonFormData);
		startAnalyseButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				try {
					ValueTracker.analysis("test.xml");
					String nameOfVariate = variateText.getText();
					variatelog = ValueTracker.getValueList(nameOfVariate);
					for (ValueChangePoint vcp : variatelog) {
						TableItem item = new TableItem(variateTable, SWT.NONE);
						if (vcp.oldValue != null) {
							item.setText(new String[]{vcp.methodName, vcp.oldValue, vcp.newValue});
						}
						else {
							item.setText(new String[]{vcp.methodName, "��δ��ʼ��", vcp.newValue});
						}
						
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block			
					MessageBox messageBox = 
							   new MessageBox(shell, 
							    SWT.OK| 
							    SWT.CANCEL| 
							    SWT.ICON_WARNING); 
							 messageBox.setMessage("�Ҳ�������׷���ļ���"); 
							 messageBox.open();			 
				}
				catch (NullPointerException e2) {
					// TODO: handle exception
					MessageBox messageBox = 
							   new MessageBox(shell, 
							    SWT.OK| 
							    SWT.CANCEL| 
							    SWT.ICON_WARNING); 
							 messageBox.setMessage("�Ҳ���������"); 
							 messageBox.open();
					
				}
			}
		});
		tab.setControl(functionComposite);
		tabFolder.setSelection(tab);
		
		//�ύ���밴ť
		Button submitButton = new Button(insertGroup, SWT.NONE);
		submitButton.setText("�ύ����");
		final FormData submitButtonFormData = new FormData();
		submitButtonFormData.top = new FormAttachment(80, 0);
		submitButtonFormData.left = new FormAttachment(5, 0);
		submitButtonFormData.right = new FormAttachment(30, 0);
		submitButtonFormData.bottom = new FormAttachment(88, 0);
		submitButton.setLayoutData(submitButtonFormData);
		submitButton.addSelectionListener(new SelectionAdapter() {
			   public void widgetSelected(SelectionEvent e) {
				    // ����aj�ļ�
				   //variateList.add(insertText.getText());
				   String active = activeCombo.getText();
				   String way = wayCombo.getText();
				   String selectname[] = new String[100];
				   String content = insertText.getText();
					
				   selectname = functionList.getSelection();
					try {
						addcode.writeaj(parentpath, active, way, selectname, content);
						setRootDir(new File(parentpath));

					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}				   
				   }
				  });
		//����ִ�а�ť
		Button compilingtButton = new Button(insertGroup, SWT.NONE);
		compilingtButton.setText("����ִ��");
		final FormData compilButtonFormData = new FormData();
		compilButtonFormData.top = new FormAttachment(80, 0);
		compilButtonFormData.left = new FormAttachment(70, 0);
		compilButtonFormData.right = new FormAttachment(95, 0);
		compilButtonFormData.bottom = new FormAttachment(88, 0);
		compilingtButton.setLayoutData(compilButtonFormData);
		compilingtButton.addSelectionListener(new SelectionAdapter() {
			   public void widgetSelected(SelectionEvent e) {
				   if(javaname == null){
					   MessageBox messageBox = 
							   new MessageBox(shell, 
							    SWT.OK| 
							    SWT.CANCEL| 
							    SWT.ICON_WARNING); 
							 messageBox.setMessage("�Ҳ������࣡"); 
							 messageBox.open(); 
				   }
				   else{
				   Run.runAnalysis(parentpath, javaname, ".aj");
				   System.out.println(javaname);
				   setRootDir(new File(parentpath));
				   }
			   }
		 });
		
		
		
		Button undoButton = new Button(insertGroup, SWT.NONE);
		undoButton.setText("����");
		final FormData undoFormData = new FormData();
		undoFormData.top = new FormAttachment(92, 0);
		undoFormData.left = new FormAttachment(5, 0);
		undoFormData.right = new FormAttachment(30, 0);
		undoFormData.bottom = new FormAttachment(100, 0);
		undoButton.setLayoutData(undoFormData);
		undoButton.addSelectionListener(new SelectionAdapter() {
			   public void widgetSelected(SelectionEvent e) {
				   int num = addcode.getcount();
				   if(num >= 1){
					   num--;
					   addcode.setCount(num);
					   delete("add"+num+".aj");
					   delete("add"+num+".class");
					   MessageBox messageBox = 
							   new MessageBox(shell, 
							    SWT.OK| 
							    SWT.CANCEL| 
							    SWT.ICON_WARNING); 
							 messageBox.setMessage("������ɣ�"); 
							 messageBox.open(); 
						setRootDir(new File(parentpath));
				   }
				   else{
					   MessageBox messageBox = 
							   new MessageBox(shell, 
							    SWT.OK| 
							    SWT.CANCEL| 
							    SWT.ICON_WARNING); 
							 messageBox.setMessage("����δִ���κβ�����"); 
							 messageBox.open(); 
				   }
				   
			   }
		});
		
		Button redoButton = new Button(insertGroup, SWT.NONE);
		redoButton.setText("����");
		final FormData redoFormData = new FormData();
		redoFormData.top = new FormAttachment(92, 0);
		redoFormData.left = new FormAttachment(70, 0);
		redoFormData.right = new FormAttachment(95, 0);
		redoFormData.bottom = new FormAttachment(100, 0);
		redoButton.setLayoutData(redoFormData);
		redoButton.addSelectionListener(new SelectionAdapter() {
			   public void widgetSelected(SelectionEvent e) {
				   int num = addcode.getcount();
				   if(num >= 1){
					   addcode.setCount(0);
					   for(int i = 0; i < num; i++){
						   delete("add"+i+".aj");     //�������
					   		delete("add"+i+".class");     //�������
					   }
					   setRootDir(new File(parentpath));
					   MessageBox messageBox = 
							   new MessageBox(shell, 
							    SWT.OK| 
							    SWT.CANCEL| 
							    SWT.ICON_WARNING); 
							 messageBox.setMessage("������ɣ�"); 
							 messageBox.open(); 
				   }
				   else{
					   MessageBox messageBox = 
							   new MessageBox(shell, 
							    SWT.OK| 
							    SWT.CANCEL| 
							    SWT.ICON_WARNING); 
							 messageBox.setMessage("����δִ���κβ�����"); 
							 messageBox.open(); 
				   }
			   }
		});
		
	}
	
	public static void delete(String ajname){
		String command = "cmd.exe /c del " + ajname;
		try {
			Runtime.getRuntime().exec(command, null, new File(parentpath));
			System.out.println(command);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		display = Display.getDefault();
		font = Display.getDefault().getSystemFont();
		fontColor = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
		shell = new Shell();
		shell.setSize(653, 414);
		shell.setText("�༭");
		shell.setLayout(new FormLayout());
		
		// Ŀ¼��
		createFileTree();
		
		centerComposite = new Composite(shell, SWT.NONE);
		centerComposite.setLayout(new FormLayout());
		final FormData centerFormData = new FormData();
		centerFormData.top = new FormAttachment(0, 5);
		centerFormData.left = new FormAttachment(20, 5);
		centerFormData.right = new FormAttachment(100, -5);
		centerFormData.bottom = new FormAttachment(100, -5);
		centerComposite.setLayoutData(centerFormData);
		// ��ӱ༭����
		createEditTab();
		// ���console
		createConsole();
		// �˵�
		createMenu();
		// ��Ӵ���ѡ�
		createFunctionTab();
//		addTab(null,
//				"��ӭʹ�ü����ı��༭��;���༭�����Զ�С��1M��txt��html�ļ����б༭\n\n�ó���ּ����ʾ���ļ��Ի���menu��Ŀ¼�Ի�����ɫ�Ի�������Ի����ʹ�÷���\n\nFile�˵������½����򿪡��򿪶���ļ��Ĺ��ܣ��Լ����漰���Ϊ���ܡ�\ncleanΪ�����ǰѡ������ݣ�closeΪ�رյ�ǰѡ���FontΪѡ�����壨������ɫ����FontColorΪѡ��������ɫ��ֻ�Ե�ǰѡ����ݼ�֮��򿪵���Ч��");
		shell.open();
		shell.layout();
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
	
}

