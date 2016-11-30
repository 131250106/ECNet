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

	// �������п��ܵĲ���
	public enum Command {
		Choose, Delete, Edit, EBody, EConnector, EHeader, ERelation
	}

	// �����½�����ķ�ʽ���½��ļ������ļ�
	public enum FileType {
		New, Open
	}

	// ��ǰѡ�е��������ʵ����Ψһ���ܹ�����������ֱ��ʹ��
	public static Command command = Command.Choose;
	public static Color defaultColor = new Color(240, 240, 240);

	// ��Ҫ����
	private JFrame frmEcm;

	// �ײ����λ����Ϣ
	private JLabel lblPosition;

	// �������ڵ����
	public static JTabbedPane tabbedCanvasPanel = null;
	// ��ǰѡ�е����ť
	public static JButton currentCommandButton;
	// �Ҳ���Ϣ��
	private static InfoPanel infoPanel;

	// ��¼�½��ļ��ĸ����ʹ򿪵��ļ�����
	public int newNum = 0;
	public int openNum = 0;

	private JButton elementBody = new JButton("����");
	private JButton elementHead = new JButton("��ͷ");
	private JButton elementDirectedLine = new JButton("��ͷ");
	private JButton elementConnector = new JButton("����");

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

		// �˵���
		JMenuBar fileMenuBar = new JMenuBar();
		filePanel.add(fileMenuBar);

		JMenu fileMenu = new JMenu("�ļ�");
		fileMenu.setHorizontalAlignment(SwingConstants.CENTER);
		fileMenuBar.add(fileMenu);

		JMenuItem newFileMenuItem = new JMenuItem("�½�");
		//���ӿ�ݼ���CTRL+N��
		newFileMenuItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N,  
		                java.awt.Event.CTRL_MASK));  
		fileMenu.add(newFileMenuItem);
		newFileMenuItem.addActionListener(new CreateCanvasAction(FileType.New));

		JMenuItem openMenuItem = new JMenuItem("��");
		//���ӿ�ݼ���CTRL+O��
		openMenuItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O,  
                java.awt.Event.CTRL_MASK)); 
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
		fileMenu.add(closeAllMenuItem);

		JSeparator separator_1 = new JSeparator();
		fileMenu.add(separator_1);

		JMenuItem saveMenuItem = new JMenuItem("����");
		//���ӿ�ݼ���CTRL+S��
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

		JMenuItem saveAsMenuItem = new JMenuItem("���Ϊ");
		fileMenu.add(saveAsMenuItem);

		JMenuItem saveAllMenuItem = new JMenuItem("��������");
		fileMenu.add(saveAllMenuItem);

		JMenuBar functionMenuBar = new JMenuBar();
		filePanel.add(functionMenuBar);

		JMenu editMenu = new JMenu("�༭");
		functionMenuBar.add(editMenu);
		
		JMenuItem copyMenuItem = new JMenuItem("����");
		//���ӿ�ݼ���CTRL+C��
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
		
		JMenuItem pasteMenuItem = new JMenuItem("ճ��");
		//���ӿ�ݼ���CTRL+V��
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
		
		JMenuItem deleteMenuItem = new JMenuItem("ɾ����ѡ");
		//���ӿ�ݼ���CTRL+D��
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

		JMenuItem undoMenuItem = new JMenuItem("����");
		//���ӿ�ݼ���CTRL+Z��
		undoMenuItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z,  
		                java.awt.Event.CTRL_MASK)); 
		editMenu.add(undoMenuItem);

		JMenuItem formatMenuItem = new JMenuItem("�Զ��Ű�");
		//���ӿ�ݼ���CTRL+F��
		formatMenuItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F,  
		                java.awt.Event.CTRL_MASK)); 
		editMenu.add(formatMenuItem);

		JMenuBar setMenuBar = new JMenuBar();
		filePanel.add(setMenuBar);
		JMenu setMenu = new JMenu("����");
		setMenuBar.add(setMenu);
		JMenuItem keyMenuItem = new JMenuItem("��ݼ�����");
		setMenu.add(keyMenuItem);

		JMenuBar helpMenuBar = new JMenuBar();
		filePanel.add(helpMenuBar);
		JMenuItem helpMenu = new JMenu("����");
		helpMenuBar.add(helpMenu);
		JMenuItem aboutusMenuItem = new JMenuItem("��������");
		helpMenu.add(aboutusMenuItem);
		JMenuItem updateMenuItem = new JMenuItem("������");
		helpMenu.add(updateMenuItem);

		// ���ٲ�����������
		JPanel editPanel = new JPanel();
		FlowLayout fl_editPanel = (FlowLayout) editPanel.getLayout();
		fl_editPanel.setVgap(2);
		fl_editPanel.setAlignment(FlowLayout.LEFT);
		editPanel.setBackground(UIManager.getColor("PopupMenu.background"));
		commandPanel.add(editPanel);

		// ͼԪ��������
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
		 * ��Ҫ���壬�����������Ҳ���Ϣ��
		 */
		JPanel mainPanel = new JPanel();
		frmEcm.getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(new BorderLayout(0, 0));

		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.8);
		mainPanel.add(splitPane, BorderLayout.CENTER);

		// ��ȅ^����Ϣ
		JPanel modelPanel = new JPanel();
		splitPane.setLeftComponent(modelPanel);
		modelPanel.setLayout(new BorderLayout(0, 3));

		// ���������ײ���һ����Tab��panel
		JTabbedPane tabbedModelsPanel = new JTabbedPane(JTabbedPane.TOP);
		tabbedModelsPanel.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		modelPanel.add(tabbedModelsPanel, BorderLayout.CENTER);
		tabbedCanvasPanel = tabbedModelsPanel;

		// ��ײ���״̬��
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
				closeIcon.setToolTipText("�ر�");
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
						"�޷��򿪻����½��ļ�", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	// ����Ƿ���Ҫ�����޸�
	private void closeHandler(CanvasPanel canvasPanel) {
		if (canvasPanel.isChanged()) {
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

	class MouseAction implements MouseListener {

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			CanvasPanel canvasPanel = (CanvasPanel) tabbedCanvasPanel
					.getSelectedComponent();
			int x = e.getX() + ScreenCenterX / 30;
			int y = e.getY() - ScreenCenterY / 7;
			if (((JButton) e.getSource()).getText().equals("��ͷ")) {
				x += ((JButton) e.getSource()).getWidth();
			} else if (((JButton) e.getSource()).getText().equals("��ͷ")) {
				x += ((JButton) e.getSource()).getWidth() * 2;
			} else if (((JButton) e.getSource()).getText().equals("����")) {
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

			if (((JButton) e.getSource()).getText().equals("����")) {
				ECMMainFrame.command = Command.EBody;
				currentCommandButton = elementBody;
			} else if (((JButton) e.getSource()).getText().equals("��ͷ")) {
				ECMMainFrame.command = Command.EHeader;
				currentCommandButton = elementHead;
			} else if (((JButton) e.getSource()).getText().equals("��ͷ")) {
				ECMMainFrame.command = Command.ERelation;
				currentCommandButton = elementDirectedLine;
			} else if (((JButton) e.getSource()).getText().equals("����")) {
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
			if (((JButton) e.getSource()).getText().equals("��ͷ")) {
				x += ((JButton) e.getSource()).getWidth();
			} else if (((JButton) e.getSource()).getText().equals("��ͷ")) {
				x += ((JButton) e.getSource()).getWidth() * 2;
			} else if (((JButton) e.getSource()).getText().equals("����")) {
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
