package com.aspectj.tree;

import org.eclipse.jface.viewers.*;
import java.util.regex.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeItem;

import com.aspectj.demo.Editor;
import com.aspectj.demo.Frame;

class MyTableLableProvider implements ITableLabelProvider{
	String functionName[] = Editor.getfunctionnamearray();;
	int functionTime[] = Editor.getfunctiontimearray();
	int functionLength = Editor.getfunctionlenth();
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
    	int functionNum = 0;
    	Pattern p = Pattern.compile("^(public |private |protected )?(static )?(.*) (.*)\\.(.*)\\((.*)\\)$");
    	Matcher m1 = p.matcher(fullName);
    	while (m1.find()) {
			acccessLevel = m1.group(1);
			returnValue = m1.group(3);
			className = m1.group(4);
			funcName = m1.group(5);
			argsString = m1.group(6);
		}
    	for (int i=0; i<functionLength; i++) {
			if (fullName.equals(functionName[i])) {
				functionNum = functionTime[i];
				break;
			}
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
        case 5:
        	return String.valueOf(item.getItemCount());
        case 6:
        	return String.valueOf(functionNum);
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
