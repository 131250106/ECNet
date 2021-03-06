package cn.edu.nju.ecm.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
import cn.edu.nju.ecm.entity.detail.EHeader;
import cn.edu.nju.ecm.entity.detail.HConnector;
import cn.edu.nju.ecm.entity.detail.HRelation;
import cn.edu.nju.ecm.file.ECMFileManage;
import cn.edu.nju.ecm.utility.Undotooler;
import cn.edu.nju.ecm.utility.UndoCommand;
import cn.edu.nju.ecm.view.ECMMainFrame.Command;
import cn.edu.nju.ecm.view.ECMMainFrame.FileType;
import cn.edu.nju.ecm.view.MyStatusPanel.Model;
import cn.edu.nju.ecm.view.entity.ElementDialog;
import cn.edu.nju.ecm.view.entity.ElementDialog.ElementType;
import cn.edu.nju.ecm.view.table.MyJScrollTable;

public class CanvasPanel extends JScrollPane {

	/**
	 * 这个类就是主要的画布类，主要由鼠标事件的监听器组成； 主要操作的对象为：当前选中的图元，和整个模型。
	 */
	private static final long serialVersionUID = 1L;

	// 这两个个是主要操作的对象；根据当前选中的图元的操作情况更新整个模型
	public ECModel model;
	private CanvasElement currentChoosed;

	public FileType type;
	private JLabel title;

	// 画布
	private JPanel canvasPanel;
	// 表格
	private MyJScrollTable mytable;

	// 从工具栏拖拽时使用，当前所拖拽的label
	private JLabel currentLabel = new JLabel();

	private boolean changed;
	private boolean fromDragging = false;
	private boolean rotate = false;

	JScrollPane mainPanel;

	JFrame mainFrame;

	// 右键选择菜单
	private JPopupMenu popupMenu = new JPopupMenu();
	private JMenuItem deleteItem = new JMenuItem("删除");
	private JMenuItem copyItem = new JMenuItem("复制");
	private JMenuItem pasteItem = new JMenuItem("粘贴");
	private JMenuItem undoItem = new JMenuItem("撤销");
	
	private JMenuItem newEBody = new JMenuItem("新增链体");
	private JMenuItem newEHeader = new JMenuItem("新增链头");
	private JMenuItem newHRelation = new JMenuItem("新增箭头");
	private JMenuItem newConnctor = new JMenuItem("新增联结");

	// 当前被复制的图元
	private CanvasElement currentCopyElement = null;
	private CanvasElement beforeMovedElement = null;

	// 鼠标当前位置
	private int currentX;
	private int currentY;

	// 鼠标点击时位置
	private int pressX;
	private int pressY;
	private boolean isChoosingNow; // 是否正在选择
	private ArrayList<CanvasElement> ChoosedList = new ArrayList<CanvasElement>();

	private Undotooler Undotooler = new Undotooler();

	/**
	 * Create the panel.
	 * 
	 * @throws DocumentException
	 * @throws IOException 
	 */
	public CanvasPanel(FileType type, JLabel title, File file,
			Element newModel, JFrame mainFrame) throws Exception {
		mainPanel = this;
		this.model = new ECModel(Undotooler);
		this.model.setFile(file);
		this.type = type;
		this.title = title;
		this.mainFrame = mainFrame;

		this.changed = false;

		if (type == FileType.OpenECM) {
			ECMFileManage.readModelFromECMFile(model);
		}else if(type == FileType.OpenXLS){
			ECMFileManage.readModelFromXLSFile(model);
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

				if (currentChoosed == null && isChoosingNow) {
					Graphics2D g2d = (Graphics2D) g;
					Stroke dash = new BasicStroke(0.5f, BasicStroke.CAP_BUTT,
							BasicStroke.JOIN_ROUND, 0.5f,
							new float[] { 15, 10, }, 0f);
					g2d.setStroke(dash);
					g2d.setPaint(Color.gray);
					int x = pressX;
					int y = pressY;
					int weight = Math.abs(currentX - pressX);
					int height = Math.abs(currentY - pressY);
					if (currentX < pressX)
						x = currentX;
					if (currentY < pressY)
						y = currentY;
					g2d.drawRect(x, y, weight, height);
				}

				for (CanvasElement ce : model.getElements()) {
					ce.draw((Graphics2D) g);
				}
			}
		};
		this.canvasPanel.setBackground(Color.WHITE);
		this.canvasPanel.addMouseListener(new MouseAction());
		this.canvasPanel.addMouseMotionListener(new MouseMoveAction());

		currentLabel.setVisible(false);
		this.canvasPanel.add(currentLabel);

		// 新增弹出式菜单
		popupMenu.add(deleteItem);
		deleteItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (currentChoosed != null) {
					deleteElementById(currentChoosed.getID(), false);
				}
			}
		});
		popupMenu.add(copyItem);
		copyItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				copyElement();
			}
		});
		popupMenu.add(pasteItem);
		pasteItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pasteElement();
			}
		});
		popupMenu.add(undoItem);
		undoItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				undo();
			}
		});
		
		popupMenu.add(newEBody);
		newEBody.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ECMMainFrame.command = Command.EBody;
				drawElement(currentX, currentY);
			}
		});
		
		popupMenu.add(newEHeader);
		newEHeader.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ECMMainFrame.command = Command.EHeader;
				drawElement(currentX, currentY);
			}
		});
		
		popupMenu.add(newHRelation);
		newHRelation.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ECMMainFrame.command = Command.ERelation;
				drawElement(currentX, currentY);
			}
		});
		
		popupMenu.add(newConnctor);
		newConnctor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ECMMainFrame.command = Command.EConnector;
				drawElement(currentX, currentY);
			}
		});

		mytable = new MyJScrollTable(this);

		changeModel();
	}

	private void setcanvasPanelPreferredSize() { // 设置画布的最佳大小
		canvasPanel.setPreferredSize(new Dimension(model.getMaxWidth() + 100,
				model.getMaxHeight() + 100));
		this.setViewportView(this.canvasPanel);
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
		} else {
			if(type==FileType.OpenXLS && MyStatusPanel.CurrentModel==Model.Table){				//如果是xls，导出成xls
				mytable.exportExcle();
				this.setChanged(false);
				return ;
			}
			filePath = this.model.getFile().getAbsolutePath();
		}
		ECMFileManage.saveModelToFile(this.model, filePath);
		this.model.setFile(new File(filePath));
		this.setChanged(false);
	}

	public void deleteElementById(int ElementId, boolean isUndo) { // 根据Id删除图元，若是由于撤销操作删除的，isUndo=true
		if (model.deleteElement(ElementId, isUndo)) {
			refresh();
			ECMMainFrame.resetElementInfo();
		}
	}

	public void deleteCurrentElement() { // 删除当前选中的图元
		if (currentChoosed != null
				&& model.deleteElement(currentChoosed.getID(), false)) {
			refresh();
			ECMMainFrame.resetElementInfo();
		}
	}

	public void copyElement() { // 复制当前选中图元
		currentCopyElement = currentChoosed;
	}
	
	public void pasteElement() { // 粘贴当前选中图元
		if (currentCopyElement != null) {
			if (currentCopyElement.getElementType() == CanvasElement.ElementType.Body) {
				EBodyModel eBodyModel = new EBodyModel(currentX, currentY, 2);
				eBodyModel.seteBody(((EBodyModel) currentCopyElement)
						.geteBody());
				model.insertNewWElement(eBodyModel, false);
			} else if (currentCopyElement.getElementType() == CanvasElement.ElementType.Header) {
				EHeaderModel eHeader = new EHeaderModel(currentX, currentY, 2);
				eHeader.seteHeader(((EHeaderModel) currentCopyElement)
						.geteHeader());
				model.insertNewWElement(eHeader, false);
			} else if (currentCopyElement.getElementType() == CanvasElement.ElementType.Connector) {
				ConnectorModel connector = new ConnectorModel(currentX,
						currentY, 2);
				connector.sethConnector(((ConnectorModel) currentCopyElement)
						.gethConnector());
				model.insertNewWElement(connector, false);
			} else if (currentCopyElement.getElementType() == CanvasElement.ElementType.Relation) {
				HRelationModel hRelation = new HRelationModel(currentX,
						currentY, 2);
				hRelation.sethRelation(((HRelationModel) currentCopyElement)
						.gethRelation());
				model.insertNewWElement(hRelation, false);
			}
			ECMMainFrame.resetButton();
			refresh();
		}
	}

	public void recoverElement(CanvasElement element) { // 删除后undo 的恢复操作
		model.insertNewWElement(element, true);
		model.updateConnectable(element);
		model.reSetAllElements();
		refresh();
	}

	public void removeToBefore(CanvasElement copyOne) { // 移动后undo的恢复操作
		CanvasElement beforeOne = model.getElementByID(copyOne.getID());
		if (beforeOne != null) {
			beforeOne.setX1Y1(copyOne.getX1(), copyOne.getY1());
			beforeOne.setX2Y2(copyOne.getX2(), copyOne.getY2());
			model.reSetAllElements();
			refresh();
		}

	}

	public void removeToBefore(ArrayList<CanvasElement> elementlist) {
		for (CanvasElement copyOne : elementlist) {
			CanvasElement beforeOne = model.getElementByID(copyOne.getID());
			if (beforeOne != null) {
				beforeOne.setX1Y1(copyOne.getX1(), copyOne.getY1());
				beforeOne.setX2Y2(copyOne.getX2(), copyOne.getY2());

			}
		}
		model.reSetAllElements();
		refresh();
	}

	public void refresh() { // 修改后刷新界面
		setChanged(true);
		canvasPanel.repaint();
		mytable.ResetTableView();
		showChoosedElements();
	}

	public void autoFormat() { // 自动化排版
		ArrayList<CanvasElement> copylist = new ArrayList<CanvasElement>();
		for (CanvasElement ce : model.getElements()) {
			copylist.add(ce.copyLocation());
		}
		Undotooler.pushUndoCommand(new UndoCommand(copylist,
				UndoCommand.ActionType.Format));
		model.autoFormat();
		setcanvasPanelPreferredSize();
		refresh();
	}

	public void showCurrentlabel(int x, int y, String name) { // 显示正在拖拽的图元
		Point point = getLocationOnScreen();
		x -= point.x;
		y -= point.y;
		if (x > 5 && y > 5) {
			ImageIcon icon = new ImageIcon("resources/ebody.png");
			if (name.equals("链体")) {
				icon = new ImageIcon("resources/ebody.png");
			} else if (name.equals("链头")) {
				icon = new ImageIcon("resources/eheader.png");
			} else if (name.equals("箭头")) {
				icon = new ImageIcon("resources/hrelation.png");
			} else if (name.equals("联结")) {
				icon = new ImageIcon("resources/hconnector.png");
			}
			currentLabel.setIcon(icon);
			currentLabel.setLocation(
					x + this.getHorizontalScrollBar().getValue()
							- currentLabel.getWidth() / 2, y
							+ this.getVerticalScrollBar().getValue()
							- currentLabel.getHeight() / 2);
			currentLabel.setVisible(true);
		} else
			currentLabel.setVisible(false);
	}

	public void drawCurrentlabel(int x, int y) { // 先拖拽的图元画出来
		Point point = getLocationOnScreen();
		x -= point.x;
		y -= point.y;
		currentLabel.setText("");
		currentLabel.setVisible(false);
		if (x > 5 && y > 5) {
			x += this.getHorizontalScrollBar().getValue();
			y += this.getVerticalScrollBar().getValue();
			if (ECMMainFrame.command == Command.EHeader)
				y -= currentLabel.getHeight() / 2;
			else if (ECMMainFrame.command == Command.ERelation)
				x -= currentLabel.getWidth() / 2;
			drawElement(x, y);
		}
	}

	private void drawElement(int x, int y) { // 画图元逻辑
		if (ECMMainFrame.command == Command.EBody) {
			ElementDialog dialog = new ElementDialog(mainFrame,
					ElementType.EBody);
			EBody eBodyEntity = (EBody) dialog.showCreateDialog();
			if (eBodyEntity == null) {
				return;
			}
			EBodyModel eBodyModel = new EBodyModel(x, y, 2);
			eBodyModel.seteBody(eBodyEntity);
			model.insertNewWElement(eBodyModel, false);

		} else if (ECMMainFrame.command == Command.EHeader) {
			ElementDialog dialog = new ElementDialog(mainFrame,
					ElementType.EHeader);
			EHeader EHeaderEntity = (EHeader) dialog.showCreateDialog();
			if (EHeaderEntity == null) {
				return;
			}
			EHeaderModel eHeader = new EHeaderModel(x, y, 2);
			eHeader.seteHeader(EHeaderEntity);
			model.insertNewWElement(eHeader, false);
		} else if (ECMMainFrame.command == Command.EConnector) {
			ElementDialog dialog = new ElementDialog(mainFrame,
					ElementType.Connector);
			HConnector hConnectorEntity = (HConnector) dialog
					.showCreateDialog();
			if (hConnectorEntity == null) {
				return;
			}
			ConnectorModel connector = new ConnectorModel(x, y, 2);
			connector.sethConnector(hConnectorEntity);
			model.insertNewWElement(connector, false);
		} else if (ECMMainFrame.command == Command.ERelation) {
			ElementDialog dialog = new ElementDialog(mainFrame,
					ElementType.HRelation);
			HRelation hRealationEntity = (HRelation) dialog.showCreateDialog();
			if (hRealationEntity == null) {
				return;
			}
			HRelationModel hRelation = new HRelationModel(x, y, 2);
			hRelation.sethRelation(hRealationEntity);
			model.insertNewWElement(hRelation, false);
		}
		ECMMainFrame.resetButton();
		refresh();
	}

	class MouseAction extends MouseAdapter {

		public void mouseEntered(MouseEvent me) {
			// cursorStatus.setText("鼠标进入位置: x-" + me.getX() + ", y-" +
			// me.getY());
		}

		public void mouseExited(MouseEvent me) {
			// cursorStatus.setText("鼠标退出位置: x-" + me.getX() + ", y-" +
			// me.getY());
		}

		/*
		 * 监听鼠标按下的动作：根据不同的命令进行处理
		 */
		public void mousePressed(MouseEvent me) {
			pressX = me.getX();
			pressY = me.getY();
			isChoosingNow = true;

			requestFocus();
			if (ECMMainFrame.command == Command.Choose) {
				CanvasElement choosed = model.getChoosedElement(me.getX(),
						me.getY());
				if (currentChoosed != null &&(ChoosedList.size()<0||!ChoosedList.contains(choosed))) {
					currentChoosed.setChoosed(false);
				}
				if (choosed != null) {
					if (ChoosedList.size() > 0 && ChoosedList.contains(choosed)) {
						for (CanvasElement ce : ChoosedList) {
							ce.setOffset(me.getX(), me.getY());
							ce.setConnectedOffset(me.getX(), me.getY());
						}
						currentChoosed = choosed;

						ArrayList<CanvasElement> copylist = new ArrayList<CanvasElement>();
						for (CanvasElement ce : ChoosedList) {
							copylist.add(ce.copyLocation());
						}
						Undotooler.pushUndoCommand(new UndoCommand(copylist,
								UndoCommand.ActionType.MoveAll));

					} else {
						clearChoosedList();
						
						beforeMovedElement = choosed.copyLocation();

						currentChoosed = choosed;
						currentChoosed.setChoosed(true);
						ECMMainFrame.showElementInfo(choosed);
						if (me.getButton() == MouseEvent.BUTTON1
								&& me.getClickCount() == 2) {
							System.out.println("双击");
							// TODO

						} else if (me.getButton() == 3) {
							deleteItem.setEnabled(true);
							copyItem.setEnabled(true);
							pasteItem.setEnabled(false);
							undoItem.setEnabled(false);
							popupMenu.show(me.getComponent(), me.getX(),
									me.getY());
						} else if (currentChoosed.getWithInRotate()) { // 如果选择了旋转点
							rotate = true;
						} else {
							rotate = false;
						}
					}
				} else {
					// 未选中目标时，右键菜单显示粘贴和撤销
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
					ECMMainFrame.resetElementInfo();
					currentChoosed = null;

					clearChoosedList();
				}
			} else {
				if (me.getButton() == MouseEvent.BUTTON1) // 必须要是左键点击才画图，默认右键点击和滑轮为取消
					drawElement(me.getX(), me.getY());
				ECMMainFrame.resetButton();
			}

			canvasPanel.repaint();
		}

		/*
		 * 释放鼠标后的处理，判断操作的图元是否可以链接，如果可以则进行关系链接
		 */
		public void mouseReleased(MouseEvent me) {
			isChoosingNow = false;

			if (ECMMainFrame.command == Command.Choose && fromDragging) {

				if (currentChoosed != null) {
					if (ChoosedList.size() > 0
							&& ChoosedList.contains(currentChoosed)) {
						setcanvasPanelPreferredSize();
					} else {

						Undotooler
								.pushUndoCommand(new UndoCommand(
										beforeMovedElement,
										UndoCommand.ActionType.Move));

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
						canvasPanel.setPreferredSize(new Dimension(
								potentialWidth, potentialHeight));
						mainPanel.updateUI();
						canvasPanel.scrollRectToVisible(currentChoosed
								.getRectangles()[0]);
						canvasPanel.scrollRectToVisible(currentChoosed
								.getRectangles()[1]);
						mainPanel.updateUI();
					}
				}
				fromDragging = false;
			} else {
				checkChooseList();
			}
			canvasPanel.repaint();
		}

		public void mouseClicked(MouseEvent me) {
		}
	}

	class MouseMoveAction extends MouseMotionAdapter {

		public void mouseMoved(MouseEvent me) {
			// cursorStatus.setText("鼠标当前位置: x-" + me.getX() + ", y-" +
			// me.getY());
			currentX = me.getX();
			currentY = me.getY();
		}

		// 鼠标拖动的处理，分为旋转和平移；旋转只需要动一个点，平移需要动所有点；但是都需要更新与操作图元链接的其他图元
		public void mouseDragged(MouseEvent me) {
			currentX = me.getX();
			currentY = me.getY();
			if (currentChoosed != null) {
				setChanged(true);
				if (ChoosedList.size() > 0
						&& ChoosedList.contains(currentChoosed)) {
					for (CanvasElement ce : ChoosedList) {
						if (ce.getElementType() == CanvasElement.ElementType.Header) {
							if (ce.isConnectedOwner()
									&& ChoosedList.contains(ce
											.getConnectedOwner())) {
								ce.resetPoints(me.getX(), me.getY());
							} else {
								ce.setRotateOwner(false);
								ce.resetPointsRotate(me.getX(), me.getY());
							}

						} else if (ce.getElementType() == CanvasElement.ElementType.Body
								|| ce.getElementType() == CanvasElement.ElementType.Connector) {
							ce.resetPoints(me.getX(), me.getY());
						} else if (ce.getElementType() == CanvasElement.ElementType.Relation) {
							if (ce.isConnectedOwner()
									&& ChoosedList.contains(ce
											.getConnectedOwner())
									&& ce.isConnectedSon()
									&& ChoosedList.contains(ce
											.getConnectedSon())) {
								ce.resetPoints(me.getX(), me.getY());
							} else if (ce.isConnectedOwner()
									&& ChoosedList.contains(ce
											.getConnectedOwner())) {
								ce.setRotateOwner(true);
								ce.resetPointsRotate(me.getX(), me.getY());
							} else if (ce.isConnectedSon()
									&& ChoosedList.contains(ce
											.getConnectedSon())) {
								ce.setRotateOwner(false);
								ce.resetPointsRotate(me.getX(), me.getY());
							}
						}
					}
				} else {
					currentChoosed.resetConnectedOwner();
					currentChoosed.resetConnectedSon();
					if (rotate) {
						currentChoosed.resetPointsRotate(me.getX(), me.getY());
					} else {
						currentChoosed.resetPoints(me.getX(), me.getY());
					}
					model.updateConnectable(currentChoosed);
				}
				fromDragging = true;
			}
			canvasPanel.repaint();
		}
	}

	// 改变当前模式
	public void changeModel() {
		ECMMainFrame.resetButton();
		ECMMainFrame.resetElementInfo();
		if (MyStatusPanel.CurrentModel == Model.Image) {
			ECMMainFrame.setInfoVisible(true);
			setcanvasPanelPreferredSize();
		} else if (MyStatusPanel.CurrentModel == Model.Table) {
			mytable.ResetTableView();
			ECMMainFrame.setInfoVisible(false);
			this.setViewportView(mytable);
		}
	}

	public void checkChooseList() {
		ChoosedList = model.chooseElementList(pressX, pressY, currentX,
				currentY);
		showChoosedElements();
	}
	
	private void showChoosedElements(){
		for (CanvasElement ce : ChoosedList) {
			ce.setChoosed(true);
		}
	}

	public void clearChoosedList() {
		for (CanvasElement ce : ChoosedList) {
			ce.setChoosed(false);
		}
		ChoosedList.clear();
	}

	public MyJScrollTable getMytable() {
		return mytable;
	}

	public void undo() {
		UndoCommand undo = Undotooler.popUndoCommand();
		if (undo != null) {
			if (undo.getType() == UndoCommand.ActionType.Delete) {
				recoverElement(undo.getElement());
			} else if (undo.getType() == UndoCommand.ActionType.New) {
				deleteElementById(undo.getElement().getID(), true);
			} else if (undo.getType() == UndoCommand.ActionType.Move) {
				removeToBefore(undo.getElement());
			} else if (undo.getType() == UndoCommand.ActionType.Format
					|| undo.getType() == UndoCommand.ActionType.MoveAll) {
				if (undo.getElementlist() != null)
					removeToBefore(undo.getElementlist());
			}
		}

	}

	public void selectAll() {
		ChoosedList.clear();
		for(CanvasElement ce:model.getElements()){
			ChoosedList.add(ce);
		}
		canvasPanel.repaint();
		showChoosedElements();
	}

}