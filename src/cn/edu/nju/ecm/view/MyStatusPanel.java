package cn.edu.nju.ecm.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;

public class MyStatusPanel extends JPanel { // �ײ�״̬�������ģʽ��ѡ��

	private static final long serialVersionUID = 1L;

	// �������п��ܵ�ģʽ
	public enum Model {
		Image, Table
	}
	
	public static Model CurrentModel = Model.Image;
	
	private JTabbedPane tabbedCanvasPanel;

	public MyStatusPanel(JTabbedPane tabbedCanvasPanel) {
		this.setTabbedCanvasPanel(tabbedCanvasPanel);
		
		setLayout(new BorderLayout(0, 0));

		JPanel modelSelect = new JPanel();
		add(modelSelect, BorderLayout.WEST);
		modelSelect.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

		JButton imageModel = new JButton("ͼģʽ");
		JButton tableModel = new JButton("���ģʽ");
		
		imageModel.setSelected(true);
		imageModel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				tableModel.setSelected(false);
				imageModel.setSelected(true);
				CurrentModel = Model.Image;
				for (int i = 0; i < tabbedCanvasPanel.getTabCount(); i++) {
					CanvasPanel currentCanvas = (CanvasPanel) tabbedCanvasPanel
							.getComponentAt(i);
					currentCanvas.changeModel();
				}
			}
		});
		tableModel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tableModel.setSelected(true);
				imageModel.setSelected(false);
				CurrentModel = Model.Table;
				CanvasPanel canvasPanel = (CanvasPanel) tabbedCanvasPanel
						.getSelectedComponent();
				if (canvasPanel != null)
					canvasPanel.changeModel();
			}
		});
		
		modelSelect.add(imageModel);
		modelSelect.add(tableModel);

		JPanel sliderPanel = new JPanel();
		add(sliderPanel, BorderLayout.EAST);
		sliderPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));

		JLabel canvasSliderValueLabel = new JLabel("New label");
		sliderPanel.add(canvasSliderValueLabel);

		JSlider canvaSlider = new JSlider();
		sliderPanel.add(canvaSlider);
		canvasSliderValueLabel.setText(canvaSlider.getValue() + "%");

	}

	public JTabbedPane getTabbedCanvasPanel() {
		return tabbedCanvasPanel;
	}

	public void setTabbedCanvasPanel(JTabbedPane tabbedCanvasPanel) {
		this.tabbedCanvasPanel = tabbedCanvasPanel;
	}
}
