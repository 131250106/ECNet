package cn.edu.nju.ecm.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import org.dom4j.DocumentException;

import cn.edu.nju.ecm.canvas.model.CanvasElement;
import cn.edu.nju.ecm.canvas.model.ECModel;
import cn.edu.nju.ecm.canvas.model.entity.ConnectorModel;
import cn.edu.nju.ecm.canvas.model.entity.EBodyModel;
import cn.edu.nju.ecm.canvas.model.entity.EHeaderModel;
import cn.edu.nju.ecm.canvas.model.entity.HRelationModel;
import cn.edu.nju.ecm.entity.Element;
import cn.edu.nju.ecm.entity.detail.EBody;
import cn.edu.nju.ecm.file.ECMFileManage;
import cn.edu.nju.ecm.view.ECMMainFrame.Command;
import cn.edu.nju.ecm.view.ECMMainFrame.FileType;
import cn.edu.nju.ecm.view.entity.ElementDialog;
import cn.edu.nju.ecm.view.entity.ElementDialog.ElementType;

public class CanvasPanel extends JScrollPane {

	/**
	 * ����������Ҫ�Ļ����࣬��Ҫ������¼��ļ�������ɣ� ��Ҫ�����Ķ���Ϊ����ǰѡ�е�ͼԪ��������ģ�͡�
	 */
	private static final long serialVersionUID = 1L;

	// ������������Ҫ�����Ķ��󣻸��ݵ�ǰѡ�е�ͼԪ�Ĳ��������������ģ��
	public ECModel model;
	private CanvasElement currentChoosed;

	private JLabel cursorStatus;
	public FileType type;
	private JLabel title;

	// ����
	private JPanel canvasPanel;

	// �ӹ�������קʱʹ�ã���ǰ����ק��label
	private JLabel currentLabel = new JLabel();

	private boolean changed;
	private boolean fromDragging = false;
	private boolean rotate = false;

	JScrollPane mainPanel;

	JFrame mainFrame;

	// �Ҽ�ѡ��˵�
	private JPopupMenu popupMenu = new JPopupMenu();
	private JMenuItem deleteItem = new JMenuItem("ɾ��");
	private JMenuItem copyItem = new JMenuItem("����");
	private JMenuItem pasteItem = new JMenuItem("ճ��");
	private JMenuItem undoItem = new JMenuItem("����");

	// ��ǰ�����Ƶ�ͼԪ
	private CanvasElement currentCopyElement = null;

	// ��굱ǰλ��
	private int currentX;
	private int currentY;

	/**
	 * Create the panel.
	 * 
	 * @throws DocumentException
	 */
	public CanvasPanel(JLabel cursorStatus, FileType type, JLabel title,
			File file, Element newModel, JFrame mainFrame)
			throws DocumentException {
		mainPanel = this;
		this.model = new ECModel();
		this.model.setFile(file);
		this.cursorStatus = cursorStatus;
		this.type = type;
		this.title = title;
		this.mainFrame = mainFrame;

		this.changed = false;

		if (type == FileType.Open) {
			ECMFileManage.readModelFromFile(model);
		} else {
			model.setDescription(newModel.getContent());
			model.setTitle(newModel.getName());
		}

		this.canvasPanel = new JPanel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void paintComponent(Graphics g) {
				super.paintComponent(g);

				for (CanvasElement ce : model.getElements()) {
					ce.draw((Graphics2D) g);
				}
			}
		};
		this.canvasPanel.setBackground(Color.WHITE);
		this.canvasPanel.addMouseListener(new MouseAction());
		this.canvasPanel.addMouseMotionListener(new MouseMoveAction());

		this.setViewportView(this.canvasPanel);

		currentLabel.setVisible(false);
		this.canvasPanel.add(currentLabel);

		// ��������ʽ�˵�
		popupMenu.add(deleteItem);
		deleteItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if (currentChoosed != null) {
					deleteElement(currentChoosed.getID());
				}
			}
		});
		popupMenu.add(copyItem);
		copyItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				copyElement();
			}
		});
		popupMenu.add(pasteItem);
		pasteItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				pasteElement();
			}
		});
		popupMenu.add(undoItem);

	}

	public boolean isChanged() {
		return changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
		if (changed) {
			if (title.getText().charAt(0) != '+') {
				ViewHelper.fileTitleDisplay("+" + title.getText(), title);
			}
		} else {
			ViewHelper.fileTitleDisplay(model.getFile().getName(), title);
		}
	}

	public void saveModel(String filePath) throws IOException {
		if (filePath != null) {
			this.type = FileType.Open;
		} else {
			filePath = this.model.getFile().getAbsolutePath();
		}
		ECMFileManage.saveModelToFile(this.model, filePath);
		this.model.setFile(new File(filePath));
		this.setChanged(false);
	}

	public void deleteElement(int ElementId) {
		model.deleteElement(ElementId);
		refresh();
	}

	public void deleteCurrentElement() {
		if (currentChoosed != null) {
			model.deleteElement(currentChoosed.getID());
			refresh();
		}
	}

	public void copyElement() {
		currentCopyElement = currentChoosed;
	}

	public void pasteElement() {
		if (currentCopyElement.getElementType() == CanvasElement.ElementType.Body) {
			EBodyModel eBodyModel = new EBodyModel(currentX, currentY, 2);
			eBodyModel.seteBody(((EBodyModel) currentCopyElement).geteBody());
			model.insertNewWElement(eBodyModel);
		} else if (currentCopyElement.getElementType() == CanvasElement.ElementType.Header) {
			EHeaderModel eHeader = new EHeaderModel(currentX, currentY, 2);
			eHeader.seteHeader(((EHeaderModel) currentCopyElement).geteHeader());
			model.insertNewWElement(eHeader);
		} else if (currentCopyElement.getElementType() == CanvasElement.ElementType.Connector) {
			ConnectorModel connector = new ConnectorModel(currentX, currentY, 2);
			connector.sethConnector(((ConnectorModel) currentCopyElement)
					.gethConnector());
			model.insertNewWElement(connector);
		} else if (currentCopyElement.getElementType() == CanvasElement.ElementType.Relation) {
			HRelationModel hRelation = new HRelationModel(currentX, currentY, 2);
			hRelation.sethRelation(((HRelationModel) currentCopyElement)
					.gethRelation());
			model.insertNewWElement(hRelation);
		}
		ECMMainFrame.resetButton();
		refresh();
	}

	public void refresh() {
		setChanged(true);
		canvasPanel.repaint();
	}

	public void showCurrentlabel(int x, int y, String name) {
		if (x > 5 && y > 8) {
			currentLabel.setLocation(x, y);
			currentLabel.setVisible(true);

			ImageIcon icon = new ImageIcon("resources/ebody.png");
			if (name.equals("����")) {
				icon = new ImageIcon("resources/ebody.png");
			} else if (name.equals("��ͷ")) {
				icon = new ImageIcon("resources/eheader.png");
			} else if (name.equals("��ͷ")) {
				icon = new ImageIcon("resources/hconnector.png");
			} else if (name.equals("����")) {
				icon = new ImageIcon("resources/hrelation.png");
			}
			currentLabel.setIcon(icon);
		} else
			currentLabel.setVisible(false);
	}

	public void drawCurrentlabel(int x, int y) {
		currentLabel.setText("");
		currentLabel.setVisible(false);
		if (x > 5 && y > 5)
			drawElement(x, y);
	}

	private void drawElement(int x, int y) {
		// TODO Auto-generated method stub
		if (ECMMainFrame.command == Command.EBody) {
			ElementDialog dialog = new ElementDialog(mainFrame,
					ElementType.EBody);
			EBody eBodyEntity = (EBody) dialog.showCreateDialog();
			if (eBodyEntity == null) {
				return;
			}
			EBodyModel eBodyModel = new EBodyModel(x, y, 2);
			eBodyModel.seteBody(eBodyEntity);
			model.insertNewWElement(eBodyModel);
		} else if (ECMMainFrame.command == Command.EHeader) {
			EHeaderModel eHeader = new EHeaderModel(x, y, 2);
			model.insertNewWElement(eHeader);
		} else if (ECMMainFrame.command == Command.EConnector) {
			ConnectorModel connector = new ConnectorModel(x, y, 2);
			model.insertNewWElement(connector);
		} else if (ECMMainFrame.command == Command.ERelation) {
			CanvasElement hRelation = new HRelationModel(x, y, 2);
			model.insertNewWElement(hRelation);
		}
		ECMMainFrame.resetButton();
		refresh();
	}

	class MouseAction extends MouseAdapter {

		public void mouseEntered(MouseEvent me) {
			cursorStatus.setText("������λ��: x-" + me.getX() + ", y-" + me.getY());
		}

		public void mouseExited(MouseEvent me) {
			cursorStatus.setText("����˳�λ��: x-" + me.getX() + ", y-" + me.getY());
		}

		/*
		 * ������갴�µĶ��������ݲ�ͬ��������д���
		 */
		public void mousePressed(MouseEvent me) {
			if (ECMMainFrame.command == Command.Choose) {
				CanvasElement choosed = model.getChoosedElement(me.getX(),
						me.getY());
				if (currentChoosed != null) {
					currentChoosed.setChoosed(false);
				}
				if (choosed != null) {
					currentChoosed = choosed;
					currentChoosed.setChoosed(true);
					ECMMainFrame.showElementInfo(choosed);
					if (me.getButton() == MouseEvent.BUTTON1
							&& me.getClickCount() == 2) {
						System.out.println("˫��");
						// todo

					} else if (me.getButton() == 3) {
						deleteItem.setEnabled(true);
						copyItem.setEnabled(true);
						pasteItem.setEnabled(false);
						undoItem.setEnabled(false);
						popupMenu.show(me.getComponent(), me.getX(), me.getY());
					} else if (currentChoosed.getWithInRotate()) { // ���ѡ������ת��
						rotate = true;
					} else {
						rotate = false;
					}
				} else {
					// δѡ��Ŀ��ʱ���Ҽ��˵���ʾճ���ͳ���
					if (me.getButton() == 3) {
						deleteItem.setEnabled(false);
						copyItem.setEnabled(false);
						if (currentCopyElement != null)
							pasteItem.setEnabled(true);
						else
							pasteItem.setEnabled(false);
						undoItem.setEnabled(true);
						popupMenu.show(me.getComponent(), me.getX(), me.getY());
					}
					ECMMainFrame.resetInfo();
					currentChoosed = null;
				}
			} else {
				if (me.getButton() == MouseEvent.BUTTON1) // ����Ҫ���������Ż�ͼ��Ĭ���Ҽ�����ͻ���Ϊȡ��
					drawElement(me.getX(), me.getY());
				ECMMainFrame.resetButton();
			}

			canvasPanel.repaint();
		}

		/*
		 * �ͷ�����Ĵ����жϲ�����ͼԪ�Ƿ�������ӣ������������й�ϵ����
		 */
		public void mouseReleased(MouseEvent me) {
			if (ECMMainFrame.command == Command.Choose && fromDragging) {
				if (currentChoosed != null) {
					if (!currentChoosed.isConnectedOwner()
							|| !currentChoosed.isConnectedSon()) {
						model.reSetConnected(currentChoosed);
					}
					int currentWidth = canvasPanel.getWidth();
					int currentHeight = canvasPanel.getHeight();
					int potentialWidth = currentChoosed.getX1()
							+ currentChoosed.getWidth();
					int potentialHeight = currentChoosed.getY1()
							+ currentChoosed.getHeight();
					if (potentialWidth < currentWidth) {
						potentialWidth = currentWidth;
					}
					if (potentialHeight < currentHeight) {
						potentialHeight = currentHeight;
					}
					canvasPanel.setPreferredSize(new Dimension(potentialWidth,
							potentialHeight));
					mainPanel.updateUI();
					canvasPanel.scrollRectToVisible(currentChoosed
							.getRectangles()[0]);
					canvasPanel.scrollRectToVisible(currentChoosed
							.getRectangles()[1]);
					mainPanel.updateUI();
				}
				fromDragging = false;
			}
		}

		public void mouseClicked(MouseEvent me) {
		}
	}

	class MouseMoveAction extends MouseMotionAdapter {

		public void mouseMoved(MouseEvent me) {
			cursorStatus.setText("��굱ǰλ��: x-" + me.getX() + ", y-" + me.getY());
			currentX = me.getX();
			currentY = me.getY();
		}

		// ����϶��Ĵ�����Ϊ��ת��ƽ�ƣ���תֻ��Ҫ��һ���㣬ƽ����Ҫ�����е㣻���Ƕ���Ҫ���������ͼԪ���ӵ�����ͼԪ
		public void mouseDragged(MouseEvent me) {
			if (currentChoosed != null) {
				setChanged(true);
				currentChoosed.setConnectedOwner(false);
				currentChoosed.resetConnectedOwner();
				currentChoosed.setConnectedSon(false);
				currentChoosed.resetConnectedSon();
				if (rotate) {
					currentChoosed.resetPointsRotate(me.getX(), me.getY());
				} else {
					currentChoosed.resetPoints(me.getX(), me.getY());
				}
				model.updateConnectable(currentChoosed);
			}
			fromDragging = true;
			canvasPanel.repaint();
		}
	}
}