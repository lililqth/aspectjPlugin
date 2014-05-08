package com.aspectj.run;
import java.awt.TextArea;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.crypto.Data;

import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;


public class MyPrintStream extends PrintStream {
	private Text text;
	public MyPrintStream(OutputStream out, Text text) {
		super(out);
		this.text = text;
	}
	/**
	 * 重截write方法,所有的打印方法都要调用的方法
	 */
	public void write(byte[] buf, int off, int len) {
		final String message = new String(buf, off, len);
		if (text != null && !message.equals("\r\n")) {
//			/* SWT非界面线程访问组件的方式 */
//			//Display.getDefault().syncExec(new Thread() {
//				public void run() {
//					text.append(message);
//					/* 在这里把信息添加到组件中 */	
//				}
//			});
			String timeString = new SimpleDateFormat("[HH:mm:ss]").format(new Date());
			
			text.append(timeString + message + "\r\n");
			
		} 
		else
		{
			super.write(buf, off, len);
		}
	}
}