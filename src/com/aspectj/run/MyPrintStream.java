package com.aspectj.run;
import java.awt.TextArea;
import java.io.OutputStream;
import java.io.PrintStream;

import org.eclipse.swt.widgets.Display;


public class MyPrintStream extends PrintStream {
	private TextArea text;
	public MyPrintStream(OutputStream out, TextArea text) {
		super(out);
		this.text = text;
	}
	/**
	 * �ؽ�write����,���еĴ�ӡ������Ҫ���õķ���
	 */
	public void write(byte[] buf, int off, int len) {
		final String message = new String(buf, off, len);
		if (text != null) {
			/* SWT�ǽ����̷߳�������ķ�ʽ */
			Display.getDefault().syncExec(new Thread() {
				public void run() {
					text.append(message);
					/* ���������Ϣ��ӵ������ */
					
				}
			});
		} 
		else {
			super.write(buf, off, len);
		}
	}
}