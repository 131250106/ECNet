package cn.edu.nju.ecm.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.dom4j.DocumentException;

import cn.edu.nju.ecm.canvas.model.CanvasElement;
import cn.edu.nju.ecm.entity.Element;
import cn.edu.nju.ecm.utility.Undotooler;
import cn.edu.nju.ecm.utility.UndoCommand;
import cn.edu.nju.ecm.view.ECMMainFrame.FileType;
import cn.edu.nju.ecm.view.entity.ElementDialog;
import cn.edu.nju.ecm.view.entity.ElementDialog.ElementType;

public class MyFileBar extends JMenuBar {								//最顶层菜单面板以及对应的操作

	private static final long serialVersionUID = 1L;

	private JFrame frmEcm;
	private JTabbedPane tabbedCanvasPanel;

	// 记录新建文件的个数和打开的文件个数
	private int newNum = 0;

	// private int openNum = 0;

	public MyFileBar(JTabbedPane tabbedCanvasPane,JFrame frmEcm) {
		super();
		this.frmEcm = frmEcm;
		this.tabbedCanvasPanel = tabbedCanvasPane;
		initialMenu();
	}

	private void initialMenu() {
		JMenu fileMenu = new JMenu("文件");
		fileMenu.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(fileMenu);

		JMenuItem newFileMenuItem = new JMenuItem("新建");
		// 增加快捷键（CTRL+N）
		newFileMenuItem.setAccelerator(KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_N, java.awt.Event.CTRL_MASK));
		fileMenu.add(newFileMenuItem);
		newFileMenuItem.addActionListener(new CreateCanvasAction(FileType.New));

		JMenuItem openMenuItem = new JMenuItem("打开");
		// 增加快捷键（CTRL+O）
		openMenuItem.setAccelerator(KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_O, java.awt.Event.CTRL_MASK));
		fileMenu.add(openMenuItem);
		openMenuItem.addActionListener(new CreateCanvasAction(FileType.Open));

		JSeparator separator = new JSeparator();
		fileMenu.add(separator);

		JMenuItem closeMenuItem = new JMenuItem("关闭");
		closeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (tabbedCanvasPanel.getSelectedComponent() != null) {
					CanvasPanel currentCanvas = (CanvasPanel) tabbedCanvasPanel
							.getSelectedComponent();
					closeHandler(currentCanvas);
				}
			}
		});
		fileMenu.add(closeMenuItem);

		JMenuItem closeAllMenuItem = new JMenuItem("关闭所有");
		closeAllMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				while (tabbedCanvasPanel.getSelectedComponent() != null) {
					CanvasPanel currentCanvas = (CanvasPanel) tabbedCanvasPanel
							.getSelectedComponent();
					closeHandler(currentCanvas);
				}
			}
		});
		fileMenu.add(closeAllMenuItem);

		JSeparator separator_1 = new JSeparator();
		fileMenu.add(separator_1);

		JMenuItem saveMenuItem = new JMenuItem("保存");
		// 增加快捷键（CTRL+S）
		saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_S, java.awt.Event.CTRL_MASK));
		saveMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tabbedCanvasPanel.getSelectedComponent() != null) {
					CanvasPanel currentCanvas = (CanvasPanel) tabbedCanvasPanel
							.getSelectedComponent();
					saveHandler(currentCanvas);
				}
			}
		});
		fileMenu.add(saveMenuItem);

		JMenuItem saveAsMenuItem = new JMenuItem("另存为");
		saveAsMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (tabbedCanvasPanel.getSelectedComponent() != null) {
					CanvasPanel currentCanvas = (CanvasPanel) tabbedCanvasPanel
							.getSelectedComponent();
					currentCanvas.type = FileType.New;
					saveHandler(currentCanvas);
				}
			}
		});
		fileMenu.add(saveAsMenuItem);

		JMenuItem saveAllMenuItem = new JMenuItem("保存所有");
		saveAllMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < tabbedCanvasPanel.getTabCount(); i++) {
					CanvasPanel currentCanvas = (CanvasPanel) tabbedCanvasPanel
							.getComponentAt(i);
					saveHandler(currentCanvas);
				}
			}
		});
		fileMenu.add(saveAllMenuItem);

		JMenu editMenu = new JMenu("编辑");
		this.add(editMenu);

		JMenuItem copyMenuItem = new JMenuItem("复制");
		// 增加快捷键（CTRL+C）
		copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_C, java.awt.Event.CTRL_MASK));
		copyMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CanvasPanel canvasPanel = (CanvasPanel) tabbedCanvasPanel
						.getSelectedComponent();
				if (canvasPanel != null)
					canvasPanel.copyElement();
			}
		});
		editMenu.add(copyMenuItem);

		JMenuItem pasteMenuItem = new JMenuItem("粘贴");
		// 增加快捷键（CTRL+V）
		pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_V, java.awt.Event.CTRL_MASK));
		pasteMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				CanvasPanel canvasPanel = (CanvasPanel) tabbedCanvasPanel
						.getSelectedComponent();
				if (canvasPanel != null)
					canvasPanel.pasteElement();
			}
		});
		editMenu.add(pasteMenuItem);

		JMenuItem deleteMenuItem = new JMenuItem("删除所选");
		// 增加快捷键（CTRL+D）
		deleteMenuItem.setAccelerator(KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_D, java.awt.Event.CTRL_MASK));
		deleteMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				CanvasPanel canvasPanel = (CanvasPanel) tabbedCanvasPanel
						.getSelectedComponent();
				if (canvasPanel != null)
					canvasPanel.deleteCurrentElement();
			}
		});
		editMenu.add(deleteMenuItem);

		JMenuItem undoMenuItem = new JMenuItem("撤销");
		// 增加快捷键（CTRL+Z）
		undoMenuItem.setAccelerator(KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_Z, java.awt.Event.CTRL_MASK));
		undoMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				UndoCommand undo = Undotooler.popUndoCommand();
				if (undo != null) {
					if (undo.getType() == UndoCommand.ActionType.Delete) {
						CanvasPanel canvasPanel = (CanvasPanel) tabbedCanvasPanel
								.getSelectedComponent();
						if (canvasPanel != null)
							canvasPanel.recoverElement(undo.getElement());
					} else if (undo.getType() == UndoCommand.ActionType.New) {
						CanvasPanel canvasPanel = (CanvasPanel) tabbedCanvasPanel
								.getSelectedComponent();
						if (canvasPanel != null)
							canvasPanel.deleteElementById(undo.getElement()
									.getID(), true);
					} else if (undo.getType() == UndoCommand.ActionType.Move) {
						CanvasPanel canvasPanel = (CanvasPanel) tabbedCanvasPanel
								.getSelectedComponent();
						if (canvasPanel != null) {
							canvasPanel.removeToBefore(undo.getElement());
						}
					} else if (undo.getType() == UndoCommand.ActionType.Format) {
						CanvasPanel canvasPanel = (CanvasPanel) tabbedCanvasPanel
								.getSelectedComponent();
						if (canvasPanel != null&&undo.getElementlist()!=null) {
							for(CanvasElement ce:undo.getElementlist())
								canvasPanel.removeToBefore(ce);
						}
					}
				}
			}
		});
		editMenu.add(undoMenuItem);

		JMenuItem formatMenuItem = new JMenuItem("自动排版");
		// 增加快捷键（CTRL+F）
		formatMenuItem.setAccelerator(KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_F, java.awt.Event.CTRL_MASK));
		formatMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CanvasPanel canvasPanel = (CanvasPanel) tabbedCanvasPanel
						.getSelectedComponent();
				if (canvasPanel != null)
					canvasPanel.autoFormat();
			}
		});
		editMenu.add(formatMenuItem);

		JMenu setMenu = new JMenu("设置");
		this.add(setMenu);
		JMenuItem keyMenuItem = new JMenuItem("快捷键设置");
		setMenu.add(keyMenuItem);

		JMenuItem helpMenu = new JMenu("帮助(H)");
		this.add(helpMenu);
		JMenuItem aboutusMenuItem = new JMenuItem("关于我们");
		helpMenu.add(aboutusMenuItem);
		JMenuItem updateMenuItem = new JMenuItem("检查更新");
		helpMenu.add(updateMenuItem);
	}

	/*
	 * 新建文件和打开文件的动作处理函数
	 */
	class CreateCanvasAction implements ActionListener {

		private File file;
		private FileType fileType;

		public CreateCanvasAction(FileType fileType) {
			this.fileType = fileType;
			this.file = null;
		}

		public void actionPerformed(ActionEvent e) {
			Element newModel = null;
			if (fileType == FileType.Open) {
				this.file = ViewHelper.showOpenFileDialog(frmEcm,
						new FileNameExtensionFilter("Evidence Chain: .ecm",
								"ecm"), null);
				if (this.file == null) {
					return;
				}

				int tabCount = tabbedCanvasPanel.getTabCount();
				for (int canvasIndex = 0; canvasIndex < tabCount; canvasIndex++) {
					File tmpFile = null;
					if ((tmpFile = ((CanvasPanel) tabbedCanvasPanel
							.getComponentAt(canvasIndex)).model.getFile()) != null
							&& tmpFile.getAbsolutePath().equals(
									this.file.getAbsolutePath())) {
						tabbedCanvasPanel
								.setSelectedIndex(canvasIndex);
						return;
					}
				}
			} else {
				ElementDialog dialog = new ElementDialog(frmEcm,
						ElementType.Model);
				newModel = dialog.showCreateDialog();
				if (newModel == null) {
					return;
				} else {

				}
			}

			JPanel componentPanel = new JPanel();
			componentPanel.setLayout(new BorderLayout());

			JLabel title = new JLabel();
			title.setOpaque(true);
			try {
				CanvasPanel canvasPanel = new CanvasPanel(
						 fileType, title, file, newModel,
						frmEcm);

				tabbedCanvasPanel.add(canvasPanel);
				canvasPanel.setVisible(true);

				if (file == null) {
					title.setText("new " + newNum);
					newNum++;
				} else {
					String fileName = file.getName();
					ViewHelper.fileTitleDisplay(fileName, title);
				}
				JLabel closeIcon = new JLabel();
				closeIcon.setOpaque(true);
				closeIcon.setIcon(new ImageIcon("resources/close-out.png",
						"Close"));
				closeIcon.setToolTipText("关闭");
				componentPanel.add(title,BorderLayout.WEST);
				componentPanel.add(closeIcon,BorderLayout.EAST);

				componentPanel.setOpaque(false);
				title.setOpaque(false);
				closeIcon.setOpaque(false);
				
				tabbedCanvasPanel.setTabComponentAt(
						tabbedCanvasPanel
								.indexOfComponent(canvasPanel), componentPanel);

				tabbedCanvasPanel.setSelectedComponent(canvasPanel);

				closeIcon.addMouseListener(new MouseAdapter() {

					@Override
					public void mouseClicked(MouseEvent arg0) {
						// TODO Auto-generated method stub
						closeHandler(canvasPanel);
					}

					@Override
					public void mouseEntered(MouseEvent e) {
						// TODO Auto-generated method stub
						closeIcon.setIcon(new ImageIcon(
								"resources/close-in.png", "Close"));
					}

					@Override
					public void mouseExited(MouseEvent e) {
						// TODO Auto-generated method stub
						closeIcon.setIcon(new ImageIcon(
								"resources/close-out.png", "Close"));
					}

				});
			} catch (DocumentException e1) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(frmEcm,
						e1.getLocalizedMessage(), "无法打开或者新建文件",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	// 检查是否需要保存修改
	private void closeHandler(CanvasPanel canvasPanel) {
		if (canvasPanel.isChanged() || canvasPanel.type == FileType.New) {
			Object[] options = { "保存", "取消" };
			int returnVal = JOptionPane.showOptionDialog(frmEcm,
					"您要关闭的文件没有保存，需要保存吗？", "保存文件", JOptionPane.DEFAULT_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			if (returnVal == JOptionPane.YES_OPTION) {
				saveHandler(canvasPanel);
			}
		}
		tabbedCanvasPanel.remove(canvasPanel);
	}

	// 选择保存文件路径
	private void saveHandler(CanvasPanel canvasPanel) {
		try {
			if (canvasPanel.type == FileType.New) {
				ViewHelper.showSaveFileDialog(frmEcm, canvasPanel,
						tabbedCanvasPanel);
			} else {
				if (canvasPanel.isChanged()) {
					canvasPanel.saveModel(null);
				}
			}
		} catch (IOException exception) {
			JOptionPane.showMessageDialog(frmEcm,
					exception.getLocalizedMessage(), "文件无法保存",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
