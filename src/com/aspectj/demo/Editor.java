package com.aspectj.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
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
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Label;
import com.aspectj.run.MyPrintStream;

public class Editor {
	private static Shell shell = null;
	private static TabFolder tabFolder = null;
	private static TabFolder fileTabFolder = null;
	private static Font font = null;
	private static Color fontColor = null;
	private static File editFile = new File("D://Desktop//session.txt");
	static Composite centerComposite = null;
	static Display display = null;
	static File rootDir = null;
	static Tree tree = null;
	static ImageRegistry imageRegistry;
	static Image iconFolder = null;
	static Image iconFile = null;

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
				String content = getFileContent(editFile);
				addTab(editFile, content);
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
		shell.setText("Now browsing: " + root.getAbsolutePath());
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
		functionTabItem.setText("����");
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

		setRootDir(new File("D:/Desktop"));
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
		
		tab.setText("��Ӵ���");
		Composite functionComposite = new Composite(tabFolder, SWT.NONE);
		functionComposite.setLayout(new FormLayout());
		//  �ύ���밴ť
		Button submitButton = new Button(functionComposite, SWT.NONE);
		submitButton.setText("�ύ����");
		final FormData submitButtonFormData = new FormData();
		submitButtonFormData.top = new FormAttachment(80, -15);
		submitButtonFormData.left = new FormAttachment(10, -40);
		submitButtonFormData.right = new FormAttachment(10, +40);
		submitButtonFormData.bottom = new FormAttachment(80, +15);
		submitButton.setLayoutData(submitButtonFormData);
		Button compilingtButton = new Button(functionComposite, SWT.NONE);
		compilingtButton.setText("����ִ��");
		final FormData compilButtonFormData = new FormData();
		compilButtonFormData.top = new FormAttachment(80, -15);
		compilButtonFormData.left = new FormAttachment(10, +160);
		compilButtonFormData.right = new FormAttachment(10, +240);
		compilButtonFormData.bottom = new FormAttachment(80, +15);
		compilingtButton.setLayoutData(compilButtonFormData);
		
		Text insertText  = new Text(functionComposite, SWT.BORDER);
		final FormData textFormData = new FormData();
		textFormData.top = new FormAttachment(30, 0);
		textFormData.left = new FormAttachment(10, -40);
		textFormData.right = new FormAttachment(10, +240);
		textFormData.bottom = new FormAttachment(70, 0);
		insertText.setLayoutData(textFormData);
		
		Combo activeCombo = new Combo(functionComposite, SWT.DROP_DOWN|SWT.READ_ONLY);
		activeCombo.add("before( Formals )");
		activeCombo.add("after( Formals ) returning [ ( Formal ) ]");
		activeCombo.add("after( Formals ) throwing [ ( Formal ) ]");
		activeCombo.add("after( Formals )");
		activeCombo.add("Type around( Formals )");
		activeCombo.setText("before( Formals )");
		final FormData activeComboFormData = new FormData();
		activeComboFormData.top = new FormAttachment(10, -15);
		activeComboFormData.left = new FormAttachment(10, +20);
		activeComboFormData.right = new FormAttachment(10, +240);
		activeComboFormData.bottom = new FormAttachment(10, +15);
		activeCombo.setLayoutData(activeComboFormData);
		
		Combo wayCombo = new Combo(functionComposite, SWT.DROP_DOWN|SWT.READ_ONLY);
		wayCombo.add("execution");
		wayCombo.add("call");
		wayCombo.setText("execution");
		final FormData wayComboFormData = new FormData();
		wayComboFormData.top = new FormAttachment(22, -15);
		wayComboFormData.left = new FormAttachment(10, +20);
		wayComboFormData.right = new FormAttachment(10, +240);
		wayComboFormData.bottom = new FormAttachment(22, +15);
		wayCombo.setLayoutData(wayComboFormData);
		
		
		Label activeLabel = new Label(functionComposite, SWT.NONE);
		activeLabel.setText("Active");
		final FormData activeLabelFormData = new FormData();
		activeLabelFormData.top = new FormAttachment(10, -15);
		activeLabelFormData.left = new FormAttachment(10, -40);
		activeLabelFormData.right = new FormAttachment(10, +10);
		activeLabelFormData.bottom = new FormAttachment(10, +15);
		activeLabel.setLayoutData(activeLabelFormData);
		
		Label wayLabel = new Label(functionComposite, SWT.NONE);
		wayLabel.setText("Way");
		final FormData wayLabelFormData = new FormData();
		wayLabelFormData.top = new FormAttachment(22, -15);
		wayLabelFormData.left = new FormAttachment(10, -40);
		wayLabelFormData.right = new FormAttachment(10, +10);
		wayLabelFormData.bottom = new FormAttachment(22, +15);
		wayLabel.setLayoutData(wayLabelFormData);
		tab.setControl(functionComposite);
		tabFolder.setSelection(tab);
		
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
