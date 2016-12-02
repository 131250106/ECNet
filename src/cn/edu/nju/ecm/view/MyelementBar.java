package cn.edu.nju.ecm.view;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

import cn.edu.nju.ecm.view.ECMMainFrame.Command;

public class MyelementBar extends JToolBar{											//上层快速工具栏面板

	private static final long serialVersionUID = 1L;

	private JTabbedPane tabbedCanvasPanel;
	
	// 当前选中的命令按钮
	private JButton currentCommandButton;
	//默认背景色
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
		
		elementBody = new JButton("链体");
		elementHead = new JButton("链头");
		elementDirectedLine = new JButton("箭头");
		elementConnector = new JButton("联结");
		
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
			int y = e.getY() - ECMMainFrame.ScreenCenterY / 6;
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

	public void resetButton() {
		if(currentCommandButton!=null){
			currentCommandButton.setBackground(defaultColor);
			ECMMainFrame.command = Command.Choose;
			currentCommandButton = null;
		}
	}
	
}
