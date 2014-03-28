package com.aspectj.tree;


import java.util.ArrayList;

public class xmlResultTreeNode {
	private String nameString = null;
	public ArrayList<xmlResultTreeNode> childArrayList = new ArrayList<xmlResultTreeNode>();
	public xmlResultTreeNode(String nameString) {
		// TODO Auto-generated constructor stub
		this.nameString = nameString;
	}
	
	public String getName() {
		return nameString;
	}
	
	public void setName(String nameString) {
		this.nameString = nameString;
	}
	
	public void addChild(xmlResultTreeNode eNode) {
		childArrayList.add(eNode);
	}
	
	public xmlResultTreeNode getChild(int index) {
		return childArrayList.get(index);
	}
	
	public xmlResultTreeNode popChild() {
		if (childArrayList.size() == 0) {
			return null;
		}
		else {
			xmlResultTreeNode tmpNode = childArrayList.get(0);
			childArrayList.remove(0);
			return tmpNode;
		}
	}
	
	public xmlResultTreeNode popChild(int index) {
		xmlResultTreeNode tmpNode = childArrayList.get(index);
		childArrayList.remove(index);
		return tmpNode;
	}
	
	public int getNumOfChild() {
		return childArrayList.size();
	}
	
	public int getIndexOfChild(xmlResultTreeNode eNode) {
		return childArrayList.indexOf(eNode);
	}
	
	
	public boolean isContainNode(xmlResultTreeNode eNode) {
		return childArrayList.contains(eNode);
	}
}
