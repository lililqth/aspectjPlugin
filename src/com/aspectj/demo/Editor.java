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
	 * @param f Ҫ�򿪵��ļ�
	 * @param text Ҫ��ӵ�ѡ�������
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

	/**����ѡ������ݵ�ԭʼ�ļ��У�����ǰҪ��ȷ�ϡ�
	 * @param tab Ҫ�����ѡ�
	 */
	private static void saveTab(TabItem tab){
		Text texts=(Text) tab.getControl();
		File f=(File) texts.getData();
		if(f==null){
			 saveTabAs(tab);return;
		}
		
		MessageBox ensure=new MessageBox(shell,SWT.OK|SWT.CANCEL|SWT.ICON_QUESTION);
		ensure.setMessage("���ļ��Ѿ����ڣ�ȷ�����ǣ�");
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
	
	/** ����ѡ������ݵ��µ��ļ���
	 * @param tab Ҫ�����ѡ�
	 */
	private static void saveTabAs(TabItem tab){
		Text texts=(Text) tab.getControl();
	
	
			FileDialog fd = new FileDialog(shell, SWT.SAVE);//SWT.SAVE����ָ��Ĭ������
			fd.setFileName("���ļ�"+System.currentTimeMillis()+".txt");//Ĭ�ϱ��������
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
	 * @param editfFile ��Ҫ�򿪵��ļ�
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
		shell.setText("�����ı��༭��");
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));

		tabFolder = new TabFolder(shell, SWT.BORDER | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);

		//		�˵�
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

		//�����ǰѡ����ݵĲ˵���
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
		
		//�رյ�ǰѡ��Ĳ˵���
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
		
		//����ѡ��ѡ�
		MenuItem mntmFont = new MenuItem(menu, SWT.NONE);
		mntmFont.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FontDialog fd=new FontDialog(shell);//����ѡ��Ի����ʹ��
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
		
		//��ɫѡ�
		MenuItem mntmFontcolor = new MenuItem(menu, SWT.NONE);
		mntmFontcolor.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ColorDialog colorDialog=new ColorDialog(shell);//��ɫѡ��Ի����ʹ��
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

		
		//addTab(null, "��ӭʹ�ü����ı��༭��;���༭�����Զ�С��1M��txt��html�ļ����б༭\n\n�ó���ּ����ʾ���ļ��Ի���menu��Ŀ¼�Ի�����ɫ�Ի�������Ի����ʹ�÷���\n\nFile�˵������½����򿪡��򿪶���ļ��Ĺ��ܣ��Լ����漰���Ϊ���ܡ�\ncleanΪ�����ǰѡ������ݣ�closeΪ�رյ�ǰѡ���FontΪѡ�����壨������ɫ����FontColorΪѡ��������ɫ��ֻ�Ե�ǰѡ����ݼ�֮��򿪵���Ч��");
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

