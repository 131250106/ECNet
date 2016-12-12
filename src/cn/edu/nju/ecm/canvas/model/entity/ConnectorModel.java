package cn.edu.nju.ecm.canvas.model.entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Set;

import cn.edu.nju.ecm.canvas.model.CanvasElement;
import cn.edu.nju.ecm.entity.detail.HConnector;
import cn.edu.nju.ecm.file.ECMFileManage;

public class ConnectorModel extends CanvasElement {

	private static final long serialVersionUID = 1L;
	private HConnector hConnector;

	public ConnectorModel(int x, int y, int distance) {
		super();
		this.height = 30;
		this.width = 30;

		int halfHeight = this.height / 2;
		int halfWidth = this.width / 2;

		this.x1 = x - halfWidth;
		this.y1 = y - halfHeight;
		this.distance = distance;

		this.sethConnector(new HConnector());
		this.elementType = ElementType.Connector;
	}

	public ConnectorModel(int x1, int y1, int height, int width, int id,
			HConnector hConnector) {
		super();
		this.x1 = x1;
		this.y1 = y1;
		this.height = height;
		this.width = width;
		this.setID(id);

		this.sethConnector(hConnector);
		this.elementType = ElementType.Connector;
	}

	public void draw(Graphics2D g2d) {
		if (this.choosed) {
			g2d.setPaint(Color.RED);
			g2d.setStroke(new BasicStroke(1));

			g2d.drawRect(x1 - 3, y1 - 3, width + 5, height + 5);
		}
		g2d.setPaint(Color.lightGray);
		g2d.setStroke(new BasicStroke(1));

		g2d.fillRect(x1, y1, width, height);

		if (!this.gethConnector().getName().equals("")) {
			g2d.setPaint(Color.BLACK);
			g2d.setStroke(new BasicStroke(1));
			drawName(g2d, this.gethConnector().getName());
		}
	}

	public boolean pointWithInMe(int x, int y) {
		if (this.x1 <= x && this.y1 <= y && x <= this.x1 + this.width
				&& y <= this.y1 + this.height) {
			return true;
		}
		return false;
	}

	public boolean withInMe(int x, int y) {
		if (this.x1 <= x && this.y1 <= y && x <= this.x1 + this.width
				&& y <= this.y1 + this.height) {
			this.setOffset(x, y);
			setConnectedOffset(x, y);
			return true;
		} else {
			return false;
		}
	}

	public void setOffset(int x, int y) {
		this.offsetX1 = x - this.x1;
		this.offsetY1 = y - this.y1;
	}

	private void drawName(Graphics2D g2d, String name) {
		int x = this.x1+this.width/2 - name.length() * 4;
		int y = this.y1 - 4;
		g2d.drawString(name, x, y);
	}

	public void setConnectedOffset(int x, int y) {
		for (CanvasElement ce : this.getConnectedOutputs()) {
			ce.setRotateOwner(true);
			ce.setOffset(x, y);
		}
		for (CanvasElement ce : this.getConnectedInputs()) {
			ce.setRotateOwner(false);
			ce.setOffset(x, y);
		}
	}

	public void resetPoints(int x, int y) {
		if ((x - this.offsetX1) > 0 && (y - this.offsetY1) > 0) {
			this.x1 = x - this.offsetX1;
			this.y1 = y - this.offsetY1;
			resetConnected(x, y);
		}
	}

	// 重新设定链接它的图元的旋转的点
	public void resetConnected(int x, int y) {
		for (CanvasElement ce : this.getConnectedOutputs()) {
			ce.resetPointsRotate(x, y);
		}
		for (CanvasElement ce : this.getConnectedInputs()) {
			ce.resetPointsRotate(x, y);
		}
	}

	// getters and setters
	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public Set<CanvasElement> getConnectedInputs() {
		return this.connectedInputs;
	}

	public Set<CanvasElement> getConnectedOutputs() {
		return this.connectedOutputs;
	}

	public HConnector gethConnector() {
		return hConnector;
	}

	public void sethConnector(HConnector hConnector) {
		this.hConnector = hConnector;
	}

	public Rectangle[] getRectangles() {
		return new Rectangle[] { new Rectangle(this.x1, this.y1, 2, 2),
				new Rectangle(this.x1 + width - 2, this.y1 + height - 2, 2, 2) };
	}

	@Override
	public String toXMLString() {
		String result = "\t<connector x=\"" + this.x1 + "\" y=\"" + this.y1
				+ "\" height=\"" + this.height + "\" width=\"" + this.width
				+ "\" id=\"" + this.getID() + "\">" + ECMFileManage.NEW_LINE;
		result += this.hConnector.toXMLString();
		result += "\t</connector>" + ECMFileManage.NEW_LINE;
		return result;
	}

	public int getDegree(){
		return this.connectedInputs.size();
	}

}
