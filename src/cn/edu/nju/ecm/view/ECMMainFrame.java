package cn.edu.nju.ecm.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.dom4j.DocumentException;

import cn.edu.nju.ecm.canvas.model.CanvasElement;
import cn.edu.nju.ecm.entity.Element;
import cn.edu.nju.ecm.view.entity.ElementDialog;
import cn.edu.nju.ecm.view.entity.ElementDialog.ElementType;
import cn.edu.nju.ecm.view.entity.panel.InfoPanel;

public class ECMMainFrame {

	public static int ScreenCenterX, ScreenCenterY;

	// 定义所有可能的操作
	public enum Command {
		Choose, Delete, Edit, EBody, EConnector, EHeader, ERelation
	}

	// 定义新建画板的方式：新建文件、打开文件
	public enum FileType {
		New, Open
	}

	// 当前选中的命令，整个实例中唯一且能够被其他类型直接使用
	public static Command command = Command.Choose;
	public static Color defaultColor = new Color(240, 240, 240);

	// 主要窗体
	private JFrame frmEcm;

	// 底部鼠标位置信息
	private JLabel lblPosition;

	// 画布所在的面板
	public static JTabbedPane tabbedCanvasPanel = null;
	// 当前选中的命令按钮
	public static JButton currentCommandButton;
	// 右侧信息栏
	private static InfoPanel infoPanel;

	// 记录新建文件的个数和打开的文件个数
	public int newNum = 0;
	public int openNum = 0;

	private JButton elementBody = new JButton("链体");
	private JButton elementHead = new JButton("链头");
	private JButton elementDirectedLine = new JButton("箭头");
	private JButton elementConnector = new JButton("联结");

	/**
	 * Create the application.
	 */
	public ECMMainFrame() {
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screensize.getWidth();
		int height = (int) screensize.getHeight();
		ECMMainFrame.ScreenCenterX = width / 2;
		ECMMainFrame.ScreenCenterY = height / 2;

		int frameWidth = 900, frameHeight = 650;
		int x = ECMMainFrame.ScreenCenterX - frameWidth / 2;
		int y = ECMMainFrame.ScreenCenterY - frameHeight / 2;
		Rectangle bounds = new Rectangle(x, y, frameWidth, frameHeight);

		frmEcm = new JFrame();
		frmEcm.setTitle("ECM");
		frmEcm.setBounds(bounds);
		frmEcm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmEcm.getContentPane().setLayout(new BorderLayout(0, 1));

		initialize();
	}

	public JFrame getFrmEcm() {
		return frmEcm;
	}

	public void setFrmEcm(JFrame frmEcm) {
		this.frmEcm = frmEcm;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		JPanel commandPanel = new JPanel();
		frmEcm.getContentPane().add(commandPanel, BorderLayout.NORTH);
		commandPanel.setLayout(new GridLayout(2, 1, 0, 0));

		JPanel filePanel = new JPanel();
		filePanel.setBackground(UIManager.getColor("Panel.background"));
		commandPanel.add(filePanel);
		filePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 2));

		// 菜单栏
		JMenuBar fileMenuBar = new JMenuBar();
		filePanel.add(fileMenuBar);

		JMenu fileMenu = new JMenu("文件");
		fileMenu.setHorizontalAlignment(SwingConstants.CENTER);
		fileMenuBar.add(fileMenu);

		JMenuItem newFileMenuItem = new JMenuItem("新建");
		//增加快捷键（CTRL+N）
		newFileMenuItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N,  
		                java.awt.Event.CTRL_MASK));  
		fileMenu.add(newFileMenuItem);
		newFileMenuItem.addActionListener(new CreateCanvasAction(FileType.New));

		JMenuItem openMenuItem = new JMenuItem("打开");
		//增加快捷键（CTRL+O）
		openMenuItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O,  
                java.awt.Event.CTRL_MASK)); 
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
		fileMenu.add(closeAllMenuItem);

		JSeparator separator_1 = new JSeparator();
		fileMenu.add(separator_1);

		JMenuItem saveMenuItem = new JMenuItem("保存");
		//增加快捷键（CTRL+S）
		saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S,  
                java.awt.Event.CTRL_MASK));  
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
		fileMenu.add(saveAsMenuItem);

		JMenuItem saveAllMenuItem = new JMenuItem("保存所有");
		fileMenu.add(saveAllMenuItem);

		JMenuBar functionMenuBar = new JMenuBar();
		filePanel.add(functionMenuBar);

		JMenu editMenu = new JMenu("编辑");
		functionMenuBar.add(editMenu);
		
		JMenuItem copyMenuItem = new JMenuItem("复制");
		//增加快捷键（CTRL+C）
		copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C,  
		                java.awt.Event.CTRL_MASK)); 
		copyMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				CanvasPanel canvasPanel = (CanvasPanel) tabbedCanvasPanel
						.getSelectedComponent();
				if (canvasPanel != null)
					canvasPanel.copyElement();
			}
		});
		editMenu.add(copyMenuItem);
		
		JMenuItem pasteMenuItem = new JMenuItem("粘贴");
		//增加快捷键（CTRL+V）
		pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V,  
		                java.awt.Event.CTRL_MASK)); 
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
		//增加快捷键（CTRL+D）
		deleteMenuItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D,  
		                java.awt.Event.CTRL_MASK)); 
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
		//增加快捷键（CTRL+Z）
		undoMenuItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z,  
		                java.awt.Event.CTRL_MASK)); 
		editMenu.add(undoMenuItem);

		JMenuItem formatMenuItem = new JMenuItem("自动排版");
		//增加快捷键（CTRL+F）
		formatMenuItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F,  
		                java.awt.Event.CTRL_MASK)); 
		editMenu.add(formatMenuItem);

		JMenuBar setMenuBar = new JMenuBar();
		filePanel.add(setMenuBar);
		JMenu setMenu = new JMenu("设置");
		setMenuBar.add(setMenu);
		JMenuItem keyMenuItem = new JMenuItem("快捷键设置");
		setMenu.add(keyMenuItem);

		JMenuBar helpMenuBar = new JMenuBar();
		filePanel.add(helpMenuBar);
		JMenuItem helpMenu = new JMenu("帮助");
		helpMenuBar.add(helpMenu);
		JMenuItem aboutusMenuItem = new JMenuItem("关于我们");
		helpMenu.add(aboutusMenuItem);
		JMenuItem updateMenuItem = new JMenuItem("检查更新");
		helpMenu.add(updateMenuItem);

		// 快速操作和命令栏
		JPanel editPanel = new JPanel();
		FlowLayout fl_editPanel = (FlowLayout) editPanel.getLayout();
		fl_editPanel.setVgap(2);
		fl_editPanel.setAlignment(FlowLayout.LEFT);
		editPanel.setBackground(UIManager.getColor("PopupMenu.background"));
		commandPanel.add(editPanel);

		// 图元绘制命令
		JToolBar elementBar = new JToolBar();
		editPanel.add(elementBar);

		// ImageIcon icon = new ImageIcon("resources/ebody.png");
		// elementBody.setIcon(icon);
		// elementBody.setBorderPainted(false);
		// elementBody.setPreferredSize(new Dimension(icon.getIconWidth(),
		// icon.getIconHeight()));
		elementBody.addMouseListener(new MouseAction());
		elementBody.addMouseMotionListener(new MouseMotionAction());
		elementBar.add(elementBody);

		elementHead.addMouseListener(new MouseAction());
		elementHead.addMouseMotionListener(new MouseMotionAction());
		elementBar.add(elementHead);

		elementDirectedLine.addMouseListener(new MouseAction());
		elementDirectedLine.addMouseMotionListener(new MouseMotionAction());
		elementBar.add(elementDirectedLine);

		elementConnector.addMouseListener(new MouseAction());
		elementConnector.addMouseMotionListener(new MouseMotionAction());
		elementBar.add(elementConnector);

		/*
		 * 主要窗体，包括画布和右侧信息栏
		 */
		JPanel mainPanel = new JPanel();
		frmEcm.getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(new BorderLayout(0, 0));

		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.8);
		mainPanel.add(splitPane, BorderLayout.CENTER);

		// 左側區域信息
		JPanel modelPanel = new JPanel();
		splitPane.setLeftComponent(modelPanel);
		modelPanel.setLayout(new BorderLayout(0, 3));

		// 画布区，底部是一个多Tab的panel
		JTabbedPane tabbedModelsPanel = new JTabbedPane(JTabbedPane.TOP);
		tabbedModelsPanel.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		modelPanel.add(tabbedModelsPanel, BorderLayout.CENTER);
		tabbedCanvasPanel = tabbedModelsPanel;

		// 最底部的状态栏
		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		statusPanel.setBackground(UIManager.getColor("Panel.background"));
		modelPanel.add(statusPanel, BorderLayout.SOUTH);
		statusPanel.setLayout(new BorderLayout(0, 0));

		JPanel positionPanel = new JPanel();
		statusPanel.add(positionPanel, BorderLayout.WEST);
		positionPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		lblPosition = new JLabel("Position:");
		lblPosition.setHorizontalAlignment(SwingConstants.CENTER);
		positionPanel.add(lblPosition);

		JPanel sliderPanel = new JPanel();
		statusPanel.add(sliderPanel, BorderLayout.EAST);
		sliderPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));

		JLabel canvasSliderValueLabel = new JLabel("New label");
		sliderPanel.add(canvasSliderValueLabel);

		JSlider canvaSlider = new JSlider();
		sliderPanel.add(canvaSlider);
		canvasSliderValueLabel.setText(canvaSlider.getValue() + "%");

		JScrollPane scrollPane = new JScrollPane();
		splitPane.setRightComponent(scrollPane);

		JPanel elementInforPanel = new JPanel();
		scrollPane.setViewportView(elementInforPanel);

		infoPanel = new InfoPanel();
		splitPane.setRightComponent(infoPanel);
	}

	public static void resetButton() {
		if (currentCommandButton != null) {
			currentCommandButton.setBackground(defaultColor);
			ECMMainFrame.command = Command.Choose;
			currentCommandButton = null;
		}
	}

	public static void showElementInfo(CanvasElement element) {
		infoPanel.setInfo(element);
	}

	public static void resetInfo() {
		infoPanel.reSetInfo();
	}

	public static void deleteElement() {
		CanvasPanel canvasPanel = (CanvasPanel) tabbedCanvasPanel
				.getSelectedComponent();
		if (canvasPanel != null)
			canvasPanel.deleteElement(infoPanel.getID());
	}

	public static void reFreshAll() {
		CanvasPanel canvasPanel = (CanvasPanel) tabbedCanvasPanel
				.getSelectedComponent();
		if (canvasPanel != null)
			canvasPanel.refresh();
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
						tabbedCanvasPanel.setSelectedIndex(canvasIndex);
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
			componentPanel.setLayout(new FlowLayout());

			JLabel title = new JLabel();

			try {
				CanvasPanel canvasPanel = new CanvasPanel(lblPosition,
						fileType, title, file, newModel, frmEcm);

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
				closeIcon.setIcon(new ImageIcon("resources/close-out.png",
						"Close"));
				closeIcon.setToolTipText("关闭");
				componentPanel.add(title);
				componentPanel.add(closeIcon);

				tabbedCanvasPanel.setTabComponentAt(
						tabbedCanvasPanel.indexOfComponent(canvasPanel),
						componentPanel);

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
				JOptionPane.showMessageDialog(frmEcm, e1.getLocalizedMessage(),
						"无法打开或者新建文件", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	// 检查是否需要保存修改
	private void closeHandler(CanvasPanel canvasPanel) {
		if (canvasPanel.isChanged()) {
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

	class MouseAction implements MouseListener {

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			CanvasPanel canvasPanel = (CanvasPanel) tabbedCanvasPanel
					.getSelectedComponent();
			int x = e.getX() + ScreenCenterX / 30;
			int y = e.getY() - ScreenCenterY / 7;
			if (((JButton) e.getSource()).getText().equals("链头")) {
				x += ((JButton) e.getSource()).getWidth();
			} else if (((JButton) e.getSource()).getText().equals("箭头")) {
				x += ((JButton) e.getSource()).getWidth() * 2;
			} else if (((JButton) e.getSource()).getText().equals("联结")) {
				x += ((JButton) e.getSource()).getWidth() * 3;
			}
			if (canvasPanel != null) {
				canvasPanel.drawCurrentlabel(x, y);

			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			if (currentCommandButton != null)
				currentCommandButton.setBackground(defaultColor);

			if (((JButton) e.getSource()).getText().equals("链体")) {
				ECMMainFrame.command = Command.EBody;
				currentCommandButton = elementBody;
			} else if (((JButton) e.getSource()).getText().equals("链头")) {
				ECMMainFrame.command = Command.EHeader;
				currentCommandButton = elementHead;
			} else if (((JButton) e.getSource()).getText().equals("箭头")) {
				ECMMainFrame.command = Command.ERelation;
				currentCommandButton = elementDirectedLine;
			} else if (((JButton) e.getSource()).getText().equals("联结")) {
				ECMMainFrame.command = Command.EConnector;
				currentCommandButton = elementConnector;
			}
			currentCommandButton.setBackground(Color.LIGHT_GRAY);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
		}
	}

	class MouseMotionAction implements MouseMotionListener {

		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
			int x = e.getX();
			int y = e.getY() - ScreenCenterY / 6;
			if (((JButton) e.getSource()).getText().equals("链头")) {
				x += ((JButton) e.getSource()).getWidth();
			} else if (((JButton) e.getSource()).getText().equals("箭头")) {
				x += ((JButton) e.getSource()).getWidth() * 2;
			} else if (((JButton) e.getSource()).getText().equals("联结")) {
				x += ((JButton) e.getSource()).getWidth() * 3;
			}
			CanvasPanel canvasPanel = (CanvasPanel) tabbedCanvasPanel
					.getSelectedComponent();
			if (canvasPanel != null)
				canvasPanel.showCurrentlabel(x, y,
						((JButton) e.getSource()).getText());
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	}
}
