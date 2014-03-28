package com.aspectj.tree;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.*;

public class MyTreeContenetProvider implements ITreeContentProvider {
	public Object[] getChildren(Object arg0) {
		// 返回树的下一级节点
		TreeItem[] children = ((TreeItem)arg0).getItems();
		return children;
	}

	public Object getParent(Object arg0) {
		// 返回树的上一级节点
		return ((TreeItem) arg0).getParentItem();
	}

	public Object[] getElements(Object inputElement) {
		TreeItem[] root = ((TreeItem)inputElement).getItems();
		return root;
	}

	public void dispose() {

	}

	public boolean hasChildren(Object element) {
		TreeItem[] children = ((TreeItem) element).getItems();
		return children.length == 0 ? false : true;
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

}
