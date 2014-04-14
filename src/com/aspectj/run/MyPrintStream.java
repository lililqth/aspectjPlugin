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
	 * 重截write方法,所有的打印方法都要调用的方法
	 */
	public void write(byte[] buf, int off, int len) {
		final String message = new String(buf, off, len);
		if (text != null) {
			/* SWT非界面线程访问组件的方式 */
			Display.getDefault().syncExec(new Thread() {
				public void run() {
					text.append(message);
					/* 在这里把信息添加到组件中 */
					
				}
			});
		} 
		else {
			super.write(buf, off, len);
		}
	}
}