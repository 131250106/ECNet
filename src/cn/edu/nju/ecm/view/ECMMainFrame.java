package cn.edu.nju.ecm.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import cn.edu.nju.ecm.canvas.model.CanvasElement;

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
	JFrame frmEcm;

	// 底部鼠标位置信息
	JLabel lblPosition;

	// 画布所在的面板
	JTabbedPane tabbedCanvasPanel = null;
	// 当前选中的命令按钮
	public static JButton currentCommandButton;
	// 右侧信息栏
	private static InfoPanel infoPanel;

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

		int frameWidth = 1200, frameHeight = 750;
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

		//菜单栏
		JPanel filePanel = new myFilePanel(this);
		filePanel.setBackground(UIManager.getColor("Panel.background"));
		commandPanel.add(filePanel);
		filePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 2));

		
		// 快速操作
		JPanel editPanel = new JPanel();
		FlowLayout fl_editPanel = (FlowLayout) editPanel.getLayout();
		fl_editPanel.setVgap(2);
		fl_editPanel.setAlignment(FlowLayout.LEFT);
		editPanel.setBackground(UIManager.getColor("PopupMenu.background"));
		commandPanel.add(editPanel);

		// 图元绘制命令
		JToolBar elementBar = new JToolBar();
		editPanel.add(elementBar);

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
		tabbedModelsPanel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				CanvasPanel canvasPanel = (CanvasPanel) tabbedCanvasPanel
						.getSelectedComponent();
				if (canvasPanel != null){
					infoPanel.setCanvasPanel(canvasPanel);
				}else{
					infoPanel.reSetModel();
				}
			}
		});
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
