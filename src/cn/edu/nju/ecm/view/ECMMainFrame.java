package cn.edu.nju.ecm.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import cn.edu.nju.ecm.canvas.model.CanvasElement;

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

	// ��Ҫ����
	private JFrame frmEcm = new JFrame();

	// �ײ����λ����Ϣ
	private JLabel lblPosition = new JLabel("Position:");

	// �������ڵ����
	private JTabbedPane tabbedCanvasPanel = new JTabbedPane();

	// �ϲ�ͼԪѡ����
	private static MyelementBar elementBar;

	// �Ҳ���Ϣ��
	private static InfoPanel infoPanel;

	public ECMMainFrame() {
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screensize.getWidth();
		int height = (int) screensize.getHeight();
		ECMMainFrame.ScreenCenterX = width / 2;
		ECMMainFrame.ScreenCenterY = height / 2;

		int frameWidth = 1200, frameHeight = 760;
		int x = ECMMainFrame.ScreenCenterX - frameWidth / 2;
		int y = ECMMainFrame.ScreenCenterY - frameHeight / 2;
		Rectangle bounds = new Rectangle(x, y, frameWidth, frameHeight);

		frmEcm.setTitle("ECM");
		frmEcm.setBounds(bounds);
		frmEcm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmEcm.getContentPane().setLayout(new BorderLayout());

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
		commandPanel.setLayout(new BorderLayout());

		// �˵���
		JMenuBar filePanel = new MyFileBar(tabbedCanvasPanel,frmEcm,lblPosition);
		frmEcm.setJMenuBar(filePanel);

		// ���ٲ�����
		JPanel editPanel = new JPanel();
		editPanel.setLayout(new BorderLayout());
		
		commandPanel.add(editPanel,BorderLayout.SOUTH);
		// ͼԪ��������
		
		elementBar = new MyelementBar(tabbedCanvasPanel);
		editPanel.add(elementBar);

		/*
		 * ��Ҫ���壬�����������Ҳ���Ϣ��
		 */
		JPanel mainPanel = new JPanel();
		frmEcm.getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(new BorderLayout());

		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.8);
		mainPanel.add(splitPane, BorderLayout.CENTER);

		// ��ȅ^����Ϣ
		JPanel modelPanel = new JPanel();
		splitPane.setLeftComponent(modelPanel);
		modelPanel.setLayout(new BorderLayout());

		// ���������ײ���һ����Tab��panel
		tabbedCanvasPanel.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		tabbedCanvasPanel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				CanvasPanel canvasPanel = (CanvasPanel) tabbedCanvasPanel
						.getSelectedComponent();
				if (canvasPanel != null) {
					infoPanel.setCanvasPanel(canvasPanel);
				} else {
					infoPanel.reSetModel();
				}
			}
		});
		
		
		modelPanel.add(tabbedCanvasPanel, BorderLayout.CENTER);

		// ��ײ���״̬��
		JPanel statusPanel = new JPanel();
		modelPanel.add(statusPanel, BorderLayout.SOUTH);
		statusPanel.setLayout(new BorderLayout(0, 0));

		JPanel positionPanel = new JPanel();
		statusPanel.add(positionPanel, BorderLayout.WEST);
		positionPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

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

		// �Ҳ���Ϣ��
		infoPanel = new InfoPanel();
		splitPane.setRightComponent(infoPanel);
	}

	public static void resetButton() {
		if (elementBar != null) {
			elementBar.resetButton();
		}
	}

	public static void showElementInfo(CanvasElement element) {
		infoPanel.setInfo(element);
	}

	public static void resetElementInfo() {
		infoPanel.reSetInfo();
	}
}
