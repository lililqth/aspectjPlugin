package com.aspectj.demo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

public class Editor {
	private static Shell shell = null ;
	private static TabFolder tabFolder = null;
	private static Font font = null;
	private static Color fontColor= null;
	/**
	 * @param f
	 *            文件
	 * @return 文件内容,文件大于1M时返回一句文件太大的字符串。
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
	 * @param f 要打开的文件
	 * @param text 要添加到选项卡的内容
	 */
	static void addTab(File f, String text) {
		TabItem tab = new TabItem(tabFolder, SWT.NONE);
		if(f!=null)tab.setText(f.getName());else tab.setText("new tab");
		Text texts = new Text(tabFolder, SWT.MULTI | SWT.V_SCROLL
				|SWT.WRAP);
		texts.setText(text);
		texts.setFont(font);
		texts.setForeground(fontColor);
		tab.setControl(texts);
		tabFolder.setSelection(tab);
		texts.setFocus();
		texts.setData(f);
	}

	/**保存选项卡的内容到原始文件中，保存前要求确认。
	 * @param tab 要保存的选项卡
	 */
	private static void saveTab(TabItem tab){
		Text texts=(Text) tab.getControl();
		File f=(File) texts.getData();
		if(f==null){
			 saveTabAs(tab);return;
		}
		
		MessageBox ensure=new MessageBox(shell,SWT.OK|SWT.CANCEL|SWT.ICON_QUESTION);
		ensure.setMessage("该文件已经存在，确定覆盖？");
		int result=ensure.open();
		if(result!=SWT.OK)return;
		
		try {
			FileWriter fw=new FileWriter(f, false);
			fw.write(texts.getText());
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** 保存选项卡的内容到新的文件中
	 * @param tab 要保存的选项卡
	 */
	private static void saveTabAs(TabItem tab){
		Text texts=(Text) tab.getControl();
	
	
			FileDialog fd = new FileDialog(shell, SWT.SAVE);//SWT.SAVE可以指定默认名称
			fd.setFileName("新文件"+System.currentTimeMillis()+".txt");//默认保存的名称
			fd.setFilterExtensions(new String[]{"*.txt;*.html;*.htm;*.java"});
			while(fd.open()==null);
			File f=new File(fd.getFilterPath()+File.separator+fd.getFileName());
	
		try {
			FileWriter fw=new FileWriter(f, false);
			fw.write(texts.getText());
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Launch the application.
	 * 
	 * @param editfFile 需要打开的文件
	 * @return 
	 */
	public static void open(File editfFile) {
		String content = getFileContent(editfFile);
		addTab(editfFile, content);
	}
	public static void edite(final File editfFile) {
		final Display display = Display.getDefault();
		font=Display.getDefault().getSystemFont();
		fontColor=Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
		shell = new Shell();
		shell.setSize(653, 414);
		shell.setText("简易文本编辑器");
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));

		tabFolder = new TabFolder(shell, SWT.BORDER | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);

		//		菜单
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);

		MenuItem mntmFile = new MenuItem(menu, SWT.CASCADE);
		mntmFile.setText("File");

		Menu menu_1 = new Menu(mntmFile);
		mntmFile.setMenu(menu_1);
		
		String content = getFileContent(editfFile);
		addTab(editfFile, content);
		
		MenuItem mntmTest = new MenuItem(menu_1, SWT.NONE);
		mntmTest.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TabItem[] items = tabFolder.getSelection();
				if (items.length > 0)
					saveTab( items[0]);
			}
		});
		mntmTest.setText("&Save");
		
		MenuItem mntmSaveAs = new MenuItem(menu_1, SWT.NONE);
		mntmSaveAs.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TabItem[] items = tabFolder.getSelection();
				if (items.length > 0)
					saveTabAs( items[0]);
			}
		});
		mntmSaveAs.setText("Save as");

		//清除当前选项卡内容的菜单项
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
		
		//关闭当前选项卡的菜单项
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
		
		//字体选择选项卡
		MenuItem mntmFont = new MenuItem(menu, SWT.NONE);
		mntmFont.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FontDialog fd=new FontDialog(shell);//字体选择对话框的使用
				FontData fontData=fd.open();
				RGB fontColor=fd.getRGB();
				if(fontData!=null){
					font=new Font(display, fontData);
					TabItem[] items = tabFolder.getSelection();
					if (items.length > 0)
						 ((Text)items[0].getControl()).setFont(font);
				}
			}
		});
		mntmFont.setText("Font");
		
		//颜色选项卡
		MenuItem mntmFontcolor = new MenuItem(menu, SWT.NONE);
		mntmFontcolor.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ColorDialog colorDialog=new ColorDialog(shell);//颜色选择对话框的使用
				RGB rgb=colorDialog.open();
				if(rgb!=null){
					fontColor=new Color(display, rgb);
					TabItem[] items = tabFolder.getSelection();
					if (items.length > 0)
						 ((Text)items[0].getControl()).setForeground(fontColor);
				}
				
			}
		});
		mntmFontcolor.setText("FontColor");

		
		//addTab(null, "欢迎使用简易文本编辑器;本编辑器可以对小于1M的txt或html文件进行编辑\n\n该程序旨在演示打开文件对话框、menu、目录对话框、颜色对话框、字体对话框的使用方法\n\nFile菜单下有新建、打开、打开多个文件的功能，以及保存及另存为功能。\nclean为清除当前选项卡的内容，close为关闭当前选项卡，Font为选择字体（忽略颜色），FontColor为选择字体颜色（只对当前选项卡内容及之后打开的有效）");
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

