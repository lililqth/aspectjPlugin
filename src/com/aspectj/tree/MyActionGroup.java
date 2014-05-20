package com.aspectj.tree;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.actions.ActionGroup;
import org.eclipse.ui.dialogs.ContainerCheckedTreeViewer;

public class MyActionGroup extends ActionGroup {
	private ContainerCheckedTreeViewer tv;
	DrawTree drawTre;

	public MyActionGroup(ContainerCheckedTreeViewer treeViewer, DrawTree tree) {
		this.tv = treeViewer;
		drawTre = tree;
	}

	public void fillContextMenu(IMenuManager mgr) {
		MenuManager menuManager = (MenuManager) mgr;
		menuManager.add(new RefreshAction());
		menuManager.add(new ResetAction());
		menuManager.add(new RuleEditOpenAction());
		menuManager.add(new RuleEditCloseAction());
		menuManager.add(new ImageOpenAction());
		menuManager.add(new ImageCloseAction());
		Tree tree = tv.getTree();
		Menu menu = menuManager.createContextMenu(tree);
		tree.setMenu(menu);

	}

	class RefreshAction extends Action {
		public RefreshAction() {
			setText("����");
		}

		public void run() {
			Object[] checks = tv.getCheckedElements();
			if (checks.length == 0) {
				MessageDialog.openInformation(null, "��ʾ", "���ȹ�ѡ��¼");
			}
			for (Object object : checks) {
				if (tv.getGrayed(object)) {
					continue;
				}
				tv.remove(object);
			}
			// tv.remove(checks);
			// tv.addFilter(new TreeFilter());

		}
	}

	class ResetAction extends Action {
		public ResetAction() {
			setText("����");
		}

		public void run() {
			if (drawTre.filter != null) {
				tv.removeFilter(drawTre.filter);
				drawTre.filter = null;
			}
			tv.setInput(drawTre.rootRoot);
		}
	}

	class RuleEditOpenAction extends Action {
		public RuleEditOpenAction() {
			setText("չ���༭��");
		}

		public void run() {
			if (drawTre.composite == null) {
				drawTre.createComposite();
				drawTre.shell.setSize(drawTre.shell.getSize().x+100, drawTre.shell.getSize().y);
				drawTre.shell.layout(true);
				
			}
		}
	}

	class RuleEditCloseAction extends Action {
		public RuleEditCloseAction() {
			setText("�رձ༭��");
		}

		public void run() {
			if (drawTre.composite != null) {
				drawTre.composite.dispose();
				drawTre.composite = null;
				drawTre.shell.setSize(drawTre.shell.getSize().x-100, drawTre.shell.getSize().y);
				drawTre.shell.layout(true);
			}
		}
	}

	class ImageOpenAction extends Action {
		public ImageOpenAction() {
			setText("չ��ͼ����ʾ");
		}
		public void run() {
			if(drawTre.parentComposite == null) {
				drawTre.createCompositeImage();
				//drawTre.shell.setSize(drawTre.shell.getSize().x+400, drawTre.shell.getSize().y);
				drawTre.shell.layout(true);
			}
		}
	}
	class ImageCloseAction extends Action {
		public ImageCloseAction() {
			setText("�ر�ͼ����ʾ");
		}
		public void run() {
			if(drawTre.parentComposite != null) {
				drawTre.parentComposite.dispose();
				drawTre.parentComposite = null;
				//drawTre.shell.setSize(drawTre.shell.getSize().x-400, drawTre.shell.getSize().y);
				drawTre.shell.layout(true);
			}
		}

	}
}
