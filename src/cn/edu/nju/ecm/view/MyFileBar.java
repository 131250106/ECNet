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

public class MyFileBar extends JMenuBar {								//���˵�����Լ���Ӧ�Ĳ���

	private static final long serialVersionUID = 1L;

	private JFrame frmEcm;
	private JTabbedPane tabbedCanvasPanel;

	// ��¼�½��ļ��ĸ����ʹ򿪵��ļ�����
	private int newNum = 0;

	// private int openNum = 0;

	public MyFileBar(JTabbedPane tabbedCanvasPane,JFrame frmEcm) {
		super();
		this.frmEcm = frmEcm;
		this.tabbedCanvasPanel = tabbedCanvasPane;
		initialMenu();
	}

	private void initialMenu() {
		JMenu fileMenu = new JMenu("�ļ�");
		fileMenu.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(fileMenu);

		JMenuItem newFileMenuItem = new JMenuItem("�½�");
		// ���ӿ�ݼ���CTRL+N��
		newFileMenuItem.setAccelerator(KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_N, java.awt.Event.CTRL_MASK));
		fileMenu.add(newFileMenuItem);
		newFileMenuItem.addActionListener(new CreateCanvasAction(FileType.New));

		JMenuItem openMenuItem = new JMenuItem("��");
		// ���ӿ�ݼ���CTRL+O��
		openMenuItem.setAccelerator(KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_O, java.awt.Event.CTRL_MASK));
		fileMenu.add(openMenuItem);
		openMenuItem.addActionListener(new CreateCanvasAction(FileType.Open));

		JSeparator separator = new JSeparator();
		fileMenu.add(separator);

		JMenuItem closeMenuItem = new JMenuItem("�ر�");
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

		JMenuItem closeAllMenuItem = new JMenuItem("�ر�����");
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

		JMenuItem saveMenuItem = new JMenuItem("����");
		// ���ӿ�ݼ���CTRL+S��
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

		JMenuItem saveAsMenuItem = new JMenuItem("���Ϊ");
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

		JMenuItem saveAllMenuItem = new JMenuItem("��������");
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

		JMenu editMenu = new JMenu("�༭");
		this.add(editMenu);

		JMenuItem copyMenuItem = new JMenuItem("����");
		// ���ӿ�ݼ���CTRL+C��
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

		JMenuItem pasteMenuItem = new JMenuItem("ճ��");
		// ���ӿ�ݼ���CTRL+V��
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

		JMenuItem deleteMenuItem = new JMenuItem("ɾ����ѡ");
		// ���ӿ�ݼ���CTRL+D��
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

		JMenuItem undoMenuItem = new JMenuItem("����");
		// ���ӿ�ݼ���CTRL+Z��
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

		JMenuItem formatMenuItem = new JMenuItem("�Զ��Ű�");
		// ���ӿ�ݼ���CTRL+F��
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

		JMenu setMenu = new JMenu("����");
		this.add(setMenu);
		JMenuItem keyMenuItem = new JMenuItem("��ݼ�����");
		setMenu.add(keyMenuItem);

		JMenuItem helpMenu = new JMenu("����(H)");
		this.add(helpMenu);
		JMenuItem aboutusMenuItem = new JMenuItem("��������");
		helpMenu.add(aboutusMenuItem);
		JMenuItem updateMenuItem = new JMenuItem("������");
		helpMenu.add(updateMenuItem);
	}

	/*
	 * �½��ļ��ʹ��ļ��Ķ���������
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
				closeIcon.setToolTipText("�ر�");
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
						e1.getLocalizedMessage(), "�޷��򿪻����½��ļ�",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	// ����Ƿ���Ҫ�����޸�
	private void closeHandler(CanvasPanel canvasPanel) {
		if (canvasPanel.isChanged() || canvasPanel.type == FileType.New) {
			Object[] options = { "����", "ȡ��" };
			int returnVal = JOptionPane.showOptionDialog(frmEcm,
					"��Ҫ�رյ��ļ�û�б��棬��Ҫ������", "�����ļ�", JOptionPane.DEFAULT_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			if (returnVal == JOptionPane.YES_OPTION) {
				saveHandler(canvasPanel);
			}
		}
		tabbedCanvasPanel.remove(canvasPanel);
	}

	// ѡ�񱣴��ļ�·��
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
					exception.getLocalizedMessage(), "�ļ��޷�����",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
