package com.aspectj.tree;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.widgets.TreeItem;

public class TreeFilter extends ViewerFilter {
	String[] lines;

	TreeFilter(String[] str) {
		lines = (String[]) str.clone();
	}

	private boolean judge(TreeItem currentItem) {
		
		for (String line : lines) {
			/*if (currentItem.getText().contains(line)) {
				return true;
			}// false表示不显示*/
			Pattern p=Pattern.compile(line);
			Matcher m=p.matcher(currentItem.getText()); 
			if(m.find()){
				return true;
			}
		}
		TreeItem[] childrenItems = currentItem.getItems();
		boolean flag = false;
		for (TreeItem item : childrenItems) {
			if (judge(item)) {
				flag = true;
				break;
			}
		}
		return flag;// false表示不显示
	}

	public boolean select(Viewer viewer, Object parentElement, Object element) {
		TreeItem item = (TreeItem) element;
		return judge(item);
	}
}
