package cn.edu.nju.ecm.view;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JToggleButton;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

import cn.edu.nju.ecm.view.ECMMainFrame.Command;

public class MyelementBar extends JToolBar { // 上层快速工具栏面板

	private static final long serialVersionUID = 1L;

	private JTabbedPane tabbedCanvasPanel;

	// 当前选中的命令按钮
	private JToggleButton currentCommandButton;

	private JToggleButton elementBody;
	private JToggleButton elementHead;
	private JToggleButton elementDirectedLine;
	private JToggleButton elementConnector;

	public MyelementBar(JTabbedPane tabbedCanvasPanel) {
		super();
		this.setFloatable(true);
		this.tabbedCanvasPanel = tabbedCanvasPanel;
		initialElement();
	}

	private void initialElement() {

		elementBody = new JToggleButton("链体");
		elementHead = new JToggleButton("链头");
		elementDirectedLine = new JToggleButton("箭头");
		elementConnector = new JToggleButton("联结");

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
			Point point = getLocationOnScreen();
			System.out.print("("+point.x+" , "+point.y+")");
			CanvasPanel canvasPanel = (CanvasPanel) tabbedCanvasPanel
					.getSelectedComponent();
			int x = e.getX() + point.x;
			int y = e.getY() + point.y;
			if (((JToggleButton) e.getSource()).getText().equals("链头")) {
				x += ((JToggleButton) e.getSource()).getWidth();
			} else if (((JToggleButton) e.getSource()).getText().equals("箭头")) {
				x += ((JToggleButton) e.getSource()).getWidth() * 2;
			} else if (((JToggleButton) e.getSource()).getText().equals("联结")) {
				x += ((JToggleButton) e.getSource()).getWidth() * 3;
			}
			if (canvasPanel != null) {
				canvasPanel.drawCurrentlabel(x, y);

			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			if (currentCommandButton != null)
				currentCommandButton.setSelected(false);

			if (((JToggleButton) e.getSource()).getText().equals("链体")) {
				ECMMainFrame.command = Command.EBody;
				currentCommandButton = elementBody;
			} else if (((JToggleButton) e.getSource()).getText().equals("链头")) {
				ECMMainFrame.command = Command.EHeader;
				currentCommandButton = elementHead;
			} else if (((JToggleButton) e.getSource()).getText().equals("箭头")) {
				ECMMainFrame.command = Command.ERelation;
				currentCommandButton = elementDirectedLine;
			} else if (((JToggleButton) e.getSource()).getText().equals("联结")) {
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
			Point point = getLocationOnScreen();
			int x = e.getX() + point.x;
			int y = e.getY() + point.y;
			if (((JToggleButton) e.getSource()).getText().equals("链头")) {
				x += ((JToggleButton) e.getSource()).getWidth();
			} else if (((JToggleButton) e.getSource()).getText().equals("箭头")) {
				x += ((JToggleButton) e.getSource()).getWidth() * 2;
			} else if (((JToggleButton) e.getSource()).getText().equals("联结")) {
				x += ((JToggleButton) e.getSource()).getWidth() * 3;
			}
			CanvasPanel canvasPanel = (CanvasPanel) tabbedCanvasPanel
					.getSelectedComponent();
			if (canvasPanel != null)
				canvasPanel.showCurrentlabel(x, y,
						((JToggleButton) e.getSource()).getText());
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	}

	public void resetButton() {
		if (currentCommandButton != null) {
			currentCommandButton.setSelected(false);
			ECMMainFrame.command = Command.Choose;
			currentCommandButton = null;
		}
	}
}
