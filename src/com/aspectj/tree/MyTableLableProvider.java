package com.aspectj.tree;

import org.eclipse.jface.viewers.*;
import java.util.regex.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeItem;

class MyTableLableProvider implements ITableLabelProvider{

    public Image getColumnImage(Object element, int columnIndex) {
        return null;
    }

    public String getColumnText(Object element, int columnIndex) {
    	TreeItem item = (TreeItem)element;
    	String fullName = item.getText();
    	
    	String acccessLevel = null;
    	String returnValue = null;
    	String className = null;
    	String funcName = null;
    	String argsString = null;
    	Pattern p = Pattern.compile("^(public|private|protected) (static )+(.*) (.*)\\.(.*)\\((.*)\\)$");
    	Matcher m1 = p.matcher(fullName);
    	while (m1.find()) {
			acccessLevel = m1.group(1);
			returnValue = m1.group(3);
			className = m1.group(4);
			funcName = m1.group(5);
			argsString = m1.group(6);
		}
    	
        switch (columnIndex) {
        case 0:
        	return className;
        case 1:
            return funcName;
        case 2:
            return acccessLevel;
        case 3:
            return returnValue;
        case 4:
        	return argsString;
        default:
            return "";
        }
    }
    public void addListener(ILabelProviderListener listener) {
        
    }

    public void dispose() {
        
    }

    public boolean isLabelProperty(Object element, String property) {
        return false;
    }

    public void removeListener(ILabelProviderListener listener) {
        
    }
}
