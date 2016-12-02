package cn.edu.nju.ecm.canvas.model.entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.HashSet;
import java.util.Set;

import cn.edu.nju.ecm.canvas.model.CanvasElement;
import cn.edu.nju.ecm.entity.detail.EBody;
import cn.edu.nju.ecm.file.ECMFileManage;

public class EBodyModel extends CanvasElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EBody eBody;

	public EBodyModel() {
		this.connectedOutputs = new HashSet<CanvasElement>();
	}

	public EBodyModel(int x1, int y1, int height, int width, int id, EBody eBody) {
		this();
		this.x1 = x1;
		this.y1 = y1;
		this.height = height;
		this.width = width;
		this.setID(id);

		this.eBody = eBody;
		this.elementType = ElementType.Body;
	}

	public EBodyModel(int x, int y, int distance) {
		this();
		this.height = 30;
		this.width = 80;

		int halfHeight = this.height / 2;
		int halfWidth = this.width / 2;

		this.x1 = x - halfWidth;
		this.y1 = y - halfHeight;
		this.distance = distance;

		this.eBody = new EBody();
		this.elementType = ElementType.Body;
	}

	public void draw(Graphics2D g2d) {
		if (this.choosed) {
			g2d.setPaint(Color.RED);
			g2d.setStroke(new BasicStroke(1));

			g2d.drawRect(x1 - 3, y1 - 3, width + 5, height + 5);
		}
		g2d.setPaint(Color.BLACK);
		g2d.setStroke(new BasicStroke(2));

		g2d.drawRect(x1, y1, width, height);
		if (!this.geteBody().getName().equals("")) {
			g2d.setPaint(Color.BLACK);
			g2d.setStroke(new BasicStroke(1));
			drawName(g2d);
		}
	}

	public boolean pointWithInMe(int x, int y) {
		if (this.x1 <= x && this.y1 <= y && x <= this.x1 + this.width && y <= this.y1 + this.height) {
			return true;
		}
		return false;
	}

	public boolean withInMe(int x, int y) {
		if (this.x1 <= x && this.y1 <= y && x <= this.x1 + this.width && y <= this.y1 + this.height) {
			this.setOffset(x, y);
			for (CanvasElement connected : this.getConnectedOutputs()) {
				connected.setRotateOwner(true);
				connected.setRotateOffset(x, y);
			}
			return true;
		} else {
			return false;
		}
	}

	public void setOffset(int x, int y) {
		this.offsetX1 = x - this.x1;
		this.offsetY1 = y - this.y1;
	}

	public void resetPoints(int x, int y) {
		if ((x - this.offsetX1) > 0 && (y - this.offsetY1) > 0) {
			this.x1 = x - this.offsetX1;
			this.y1 = y - this.offsetY1;
			resetConnected(x, y);
		}
	}

	public void resetConnected(int x, int y) {
		for (CanvasElement ce : this.getConnectedOutputs()) {
			ce.resetPointsRotate(x, y);
		}
	}

	public Rectangle[] getRectangles() {
		return new Rectangle[] { new Rectangle(this.x1, this.y1, 2, 2),
				new Rectangle(this.x1 + width - 2, this.y1 + height - 2, 2, 2) };
	}

	public EBody geteBody() {
		return eBody;
	}

	public void seteBody(EBody eBody) {
		this.eBody = eBody;
	}

	public Set<CanvasElement> getConnectedOutputs() {
		return this.connectedOutputs;
	}

	private void drawName(Graphics2D g2d) {
		int x = (this.x1 + this.width / 2) - (this.geteBody().getName().length() * 6);
		int y = this.y1 - this.distance;
		g2d.drawString(this.geteBody().getName(), x, y);
	}

	public String toXMLString() {
		String result = "\t<ebody x=\"" + this.x1 + "\" y=\"" + this.y1 + "\" height=\"" + this.height + "\" width=\""
				+ this.width + "\" id=\"" + this.getID() + "\">" + ECMFileManage.NEW_LINE;
		result += this.eBody.toXMLString();
		result += "\t</ebody>" + ECMFileManage.NEW_LINE;
		return result;
	}
	
	public CanvasElement copy() {
		CanvasElement copy = new EBodyModel(getX1(), getY1(), getHeight(),
				getWidth(), getID(), geteBody());
		copy.setConnectedOutputs(getConnectedOutputs());
		return copy;
	}

	public int getDegree(){
		return this.connectedOutputs.size();
	}
}
