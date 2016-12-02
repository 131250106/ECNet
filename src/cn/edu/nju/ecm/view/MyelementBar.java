package cn.edu.nju.ecm.view;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

import cn.edu.nju.ecm.view.ECMMainFrame.Command;

public class MyelementBar extends JToolBar{											//�ϲ���ٹ��������

	private static final long serialVersionUID = 1L;

	private JTabbedPane tabbedCanvasPanel;
	
	// ��ǰѡ�е����ť
	private JButton currentCommandButton;
	//Ĭ�ϱ���ɫ
	private Color defaultColor = new Color(240, 240, 240);
		
	private JButton elementBody;
	private JButton elementHead;
	private JButton elementDirectedLine;
	private JButton elementConnector;
	
	public MyelementBar(JTabbedPane tabbedCanvasPanel) {
		super();
		this.tabbedCanvasPanel = tabbedCanvasPanel;
		initialElement();
	}

	private void initialElement() {
		
		elementBody = new JButton("����");
		elementHead = new JButton("��ͷ");
		elementDirectedLine = new JButton("��ͷ");
		elementConnector = new JButton("����");
		
		elementBody.addMouseListener(new MouseAction());
		elementBody.addMouseMotionListener(new MouseMotionAction());
		add(elementBody);

		elementHead.addMouseListener(new MouseAction());
		elementHead.addMouseMotionListener(new MouseMotionAction());
		add(elementHead);

		elementDirectedLine.addMouseListener(new MouseAction());
		elementDirectedLine.addMouseMotionListener(new MouseMotionAction());
		add(elementDirectedLine);

		elementConnector.addMouseListener(new MouseAction());
		elementConnector.addMouseMotionListener(new MouseMotionAction());
		add(elementConnector);
	}
	
	class MouseAction implements MouseListener {

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			CanvasPanel canvasPanel = (CanvasPanel) tabbedCanvasPanel
					.getSelectedComponent();
			int x = e.getX() + ECMMainFrame.ScreenCenterX / 30;
			int y = e.getY() - ECMMainFrame.ScreenCenterY / 7;
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
			int y = e.getY() - ECMMainFrame.ScreenCenterY / 6;
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

	public void resetButton() {
		if(currentCommandButton!=null){
			currentCommandButton.setBackground(defaultColor);
			ECMMainFrame.command = Command.Choose;
			currentCommandButton = null;
		}
	}
	
}
