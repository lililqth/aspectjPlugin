package com.aspectj.demo;
import java.io.File;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class FileBrowser {

	Display display = new Display();
	Shell shell = new Shell(display);
	Tree tree;
	File rootDir;

	public FileBrowser() {

		Action actionSetRootDir = new Action("Set Root Dir") {
			public void run() {
				DirectoryDialog dialog = new DirectoryDialog(shell);
				String path = dialog.open();
				if (path != null) {
					setRootDir(new File(path));
				}
			}
		};
		ToolBar toolBar = new ToolBar(shell, SWT.FLAT);
		ToolBarManager manager = new ToolBarManager(toolBar);
		manager.add(actionSetRootDir);
		manager.update(true);
		shell.setLayout(new GridLayout());
		tree = new Tree(shell, SWT.BORDER);
		tree.setLayoutData(new GridData(GridData.FILL_BOTH));
		setRootDir(new File("C:/temp"));
		tree.addTreeListener(new TreeListener() {
			public void treeCollapsed(TreeEvent e) {
			}

			public void treeExpanded(TreeEvent e) {
				TreeItem item = (TreeItem) e.item;
				TreeItem[] children = item.getItems();
				for (int i = 0; i < children.length; i++)
					if (children[i].getData() == null)
						children[i].dispose();
					else
						return;
				File[] files = ((File) item.getData()).listFiles();
				for (int i = 0; files != null && i < files.length; i++)
					addFileToTree(item, files[i]);
			}
		});
		tree.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				TreeItem item = (TreeItem) e.item;
				File file = (File) item.getData();
				if (Program.launch(file.getAbsolutePath())) {
					System.out.println("File has been launched: " + file);
				} else {
					System.out.println("Unable to launch file: " + file);
				}
			}
		});
		final TreeEditor editor = new TreeEditor(tree);
		tree.addListener(SWT.MouseDown, new Listener() {
			public void handleEvent(Event event) {
				Point point = new Point(event.x, event.y);
				final TreeItem item = tree.getItem(point);
				if (item == null)
					return;
				final Text text = new Text(tree, SWT.NONE);
				text.setText(item.getText());
				text.setBackground(shell.getDisplay().getSystemColor(
						SWT.COLOR_YELLOW));
				editor.horizontalAlignment = SWT.LEFT;
				editor.grabHorizontal = true;
				editor.setEditor(text, item);
				Listener textListener = new Listener() {
					public void handleEvent(final Event e) {
						switch (e.type) {
						case SWT.FocusOut:
							File renamed = renameFile((File) item.getData(),
									text.getText());
							if (renamed != null) {
								item.setText(text.getText());
								item.setData(renamed);
							}
							text.dispose();
							break;
						case SWT.Traverse:
							switch (e.detail) {
							case SWT.TRAVERSE_RETURN:
								renamed = renameFile((File) item.getData(),
										text.getText());
								if (renamed != null) {
									item.setText(text.getText());
									item.setData(renamed);
								}
							case SWT.TRAVERSE_ESCAPE:
								text.dispose();
								e.doit = false;
							}
							break;
						}
					}
				};
				text.addListener(SWT.FocusOut, textListener);
				text.addListener(SWT.Traverse, textListener);
				text.setFocus();
			}
		});

		shell.setSize(400, 260);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	private File renameFile(File file, String newName) {
		File dest = new File(file.getParentFile(), newName);
		if (file.renameTo(dest)) {
			return dest;
		} else {
			return null;
		}
	}

	private void setRootDir(File root) {
		if ((!root.isDirectory()) || (!root.exists()))
			throw new IllegalArgumentException("Invalid root: " + root);
		this.rootDir = root;
		shell.setText("Now browsing: " + root.getAbsolutePath());
		if (tree.getItemCount() > 0) {
			TreeItem[] items = tree.getItems();
			for (int i = 0; i < items.length; i++) {
				items[i].dispose();
			}
		}
		File[] files = root.listFiles();
		for (int i = 0; files != null && i < files.length; i++)
			addFileToTree(tree, files[i]);
	}

	private void addFileToTree(Object parent, File file) {
		TreeItem item = null;
		if (parent instanceof Tree)
			item = new TreeItem((Tree) parent, SWT.NULL);
		else if (parent instanceof TreeItem)
			item = new TreeItem((TreeItem) parent, SWT.NULL);
		else
			throw new IllegalArgumentException(
					"parent should be a tree or a tree item: " + parent);
		item.setText(file.getName());
		item.setImage(getIcon(file));
		item.setData(file);
		if (file.isDirectory()) {
			// // recursively adds all the children of this file.
			// File[] files = file.listFiles();
			// for(int i=0; i<files.length; i++)
			// buildTree(item, files[i]);
			if (file.list() != null && file.list().length > 0)
				new TreeItem(item, SWT.NULL);
		}
	}

	private ImageRegistry imageRegistry;
	Image iconFolder = new Image(shell.getDisplay(), "D:/Desktop/folder.gif");
	Image iconFile = new Image(shell.getDisplay(), "D:/Desktop/file.gif");

	private Image getIcon(File file) {
		if (file.isDirectory())
			return iconFolder;
		int lastDotPos = file.getName().indexOf('.');
		if (lastDotPos == -1)
			return iconFile;
		Image image = getIcon(file.getName().substring(lastDotPos + 1));
		return image == null ? iconFile : image;
	}

	private Image getIcon(String extension) {
		if (imageRegistry == null)
			imageRegistry = new ImageRegistry();
		Image image = imageRegistry.get(extension);
		if (image != null)
			return image;
		Program program = Program.findProgram(extension);
		ImageData imageData = (program == null ? null : program.getImageData());
		if (imageData != null) {
			image = new Image(shell.getDisplay(), imageData);
			imageRegistry.put(extension, image);
		} else {
			image = iconFile;
		}
		return image;
	}

	public static void main(String[] args) {
		new FileBrowser();
	}
}
