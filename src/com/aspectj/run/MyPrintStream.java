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
	 * �ؽ�write����,���еĴ�ӡ������Ҫ���õķ���
	 */
	public void write(byte[] buf, int off, int len) {
		final String message = new String(buf, off, len);
		if (text != null && !message.equals("\r\n")) {
//			/* SWT�ǽ����̷߳�������ķ�ʽ */
//			//Display.getDefault().syncExec(new Thread() {
//				public void run() {
//					text.append(message);
//					/* ���������Ϣ��ӵ������ */	
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