package cn.edu.nju.ecm.canvas.model.entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Set;

import cn.edu.nju.ecm.canvas.model.CanvasElement;
import cn.edu.nju.ecm.entity.detail.EHeader;
import cn.edu.nju.ecm.file.ECMFileManage;

public class EHeaderModel extends CanvasElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EHeader eHeader;

	public EHeaderModel(int x1, int y1, int x2, int y2, int id, EHeader eheader) {
		super();
		width = 15;
		height = 60;
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.setID(id);

		this.eHeader = eheader;
		this.elementType = ElementType.Header;
	}

	public EHeaderModel(int x1, int y1, int distance) {
		super();
		width = 13;
		height = 60;

		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x1;
		this.y2 = y1 + height;
		this.distance = distance;

		this.eHeader = new EHeader();
		this.elementType = ElementType.Header;
	}

	public void draw(Graphics2D g2d) {
		if (this.isConnectedOwner()) {
			g2d.setPaint(Color.BLACK);
			g2d.setStroke(new BasicStroke(2));

			g2d.fillOval(this.x1 - 3, this.y1 - 3, 6, 6);
		}
		if (this.choosed) {
			g2d.setPaint(Color.RED);
			g2d.setStroke(new BasicStroke(1));

			g2d.drawOval(this.x1 - this.distance * 2, this.y1 - this.distance * 2, this.distance * 4,
					this.distance * 4);
			g2d.drawOval(this.x2 - this.width - this.distance, this.y2 - this.distance - this.width,
					(this.width + this.distance) * 2, (this.width + this.distance) * 2);
		}
		g2d.setPaint(Color.gray);
		g2d.setStroke(new BasicStroke(1));
		g2d.drawLine(x1, y1, x2, y2);
		g2d.setPaint(Color.lightGray);
		g2d.fillOval(x2 - this.width, y2 - this.width, width + this.width, width + this.width);

		if (!this.geteHeader().getName().equals("")) {
			drawName(g2d, this.geteHeader().getName());
		}
	}

	public boolean pointWithInMe(int x, int y) {
		if ((x - this.x2) * (x - this.x2) + (y - this.y2) * (y - this.y2) <= this.width * this.width) {
			return true;
		}
		return false;
	}

	public boolean withInMe(int x, int y) {
		if (this.withInRotate(x, y) || this.withInLine(x, y)) {
			setConnectedOffset(x, y);
			return true;
		} else {
			this.withInRotate = false;
			return false;
		}
	}

	public boolean withInRotate(int x, int y) {
		if ((x - this.x2) * (x - this.x2) + (y - this.y2) * (y - this.y2) <= this.width * this.width) {
			this.setRotateOwner(false);
			setRotateOffset(x, y);
			this.withInRotate = true;
			return true;
		} else {
			if ((x - this.x1) * (x - this.x1) + (y - this.y1) * (y - this.y1) < this.distance * this.distance * 4) {
				this.setRotateOwner(true);
				setRotateOffset(x, y);
				this.withInRotate = true;
				return true;
			}
		}
		return false;
	}

	public boolean withInLine(int x, int y) {
		if ((Math.sqrt((y - y1) * (y - y1) + (x - x1) * (x - x1))
				+ Math.sqrt((y - y2) * (y - y2) + (x - x2) * (x - x2))) <= Math
						.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1)) + this.distance * 1) {
			this.withInRotate = false;
			setOffset(x, y);
			return true;
		}
		return false;
	}

	public void setOffset(int x, int y) {
		this.offsetX1 = x - this.x1;
		this.offsetY1 = y - this.y1;
		this.offsetX2 = x - this.x2;
		this.offsetY2 = y - this.y2;
	}

	public void setRotateOffset(int x, int y) {
		if (this.getRotateOwner()) {
			this.offsetX1 = x - this.x1;
			this.offsetY1 = y - this.y1;
		} else {
			this.offsetX2 = x - this.x2;
			this.offsetY2 = y - this.y2;
		}
	}

	private void setConnectedOffset(int x, int y) {
		for (CanvasElement ce : this.getConnectedOutputs()) {
			ce.setRotateOwner(true);
			ce.setRotateOffset(x, y);
		}
		for (CanvasElement ce : this.getConnectedInputs()) {
			ce.setRotateOwner(false);
			ce.setRotateOffset(x, y);
		}
	}

	public void resetPoints(int x, int y) {
		if ((x - this.offsetX1) > 0 && (y - this.offsetY1) > 0 && (x - this.offsetX2) > 0 && (y - this.offsetY2) > 0) {
			this.x1 = x - this.offsetX1;
			this.y1 = y - this.offsetY1;
			this.x2 = x - this.offsetX2;
			this.y2 = y - this.offsetY2;
			resetConnected(x, y);
		}
	}

	public void resetPointsRotate(int x, int y) {
		if (this.getRotateOwner()) {
			this.x1 = x - this.offsetX1;
			this.y1 = y - this.offsetY1;
		} else {
			if ((x - this.offsetX2) > 0 && (y - this.offsetY2) > 0) {
				this.x2 = x - this.offsetX2;
				this.y2 = y - this.offsetY2;
				resetConnected(x, y);
			}
		}
	}

	public void resetConnected(int x, int y) {
		for (CanvasElement ce : this.getConnectedOutputs()) {
			ce.resetPointsRotate(x, y);
		}
		for (CanvasElement ce : this.getConnectedInputs()) {
			ce.resetPointsRotate(x, y);
		}
	}

	public void resetConnectedPointOwner(CanvasElement ce) {
		int[] newXY1 = CanvasElement.calculateNewConnectedPointRectangle(ce, this.getX2(), this.getY2());
		this.setX1Y1(newXY1[0], newXY1[1]);
	}

	public void resetConnectedOwner() {
		if (this.getConnectedOwner() != null) {
			this.getConnectedOwner().getConnectedOutputs().remove(this);
			this.setConnectedOwner(null);
		}
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public Rectangle[] getRectangles() {
		return new Rectangle[] { new Rectangle(this.x1, this.y1, 2, 2),
				new Rectangle(this.x2 + width - 2, this.y2 + width - 2, 2, 2) };
	}

	public void drawChoosed(Graphics2D g2d) {
		int x11, y11 = y1, x12 = x1, y12, x21 = x2, y21, x22, y22 = y2;
		if (y1 == y2) {
			x11 = x1;
			y11 = y1 + this.distance;
			y12 = y1 - this.distance;
			y21 = y2 + this.distance;
			x22 = x2;
			y22 = y2 - this.distance;
		} else if (x1 == x2) {
			x11 = x1 - this.distance;
			x12 = x1 + this.distance;
			y12 = y1;
			x21 = x2 - this.distance;
			y21 = y2;
			x22 = x2 + this.distance;
		} else {
			if (x1 < x2 && y1 < y2) {
				x11 = x1 + this.distance;
				y12 = y1 + this.distance;
				y21 = y2 - this.distance;
				x22 = x2 - this.distance;
			} else if (x1 > x2 && y1 < y2) {
				y21 = y2 + this.distance;
				x11 = x1 + this.distance;
				y12 = y1 - this.distance;
				x22 = x2 - this.distance;
			} else if (x1 < x2 && y1 > y2) {
				y21 = y2 - this.distance;
				x11 = x1 - this.distance;
				y12 = y1 + this.distance;
				x22 = x2 + this.distance;
			} else {
				y21 = y2 + this.distance;
				x11 = x1 - this.distance;
				y12 = y1 - this.distance;
				x22 = x2 + this.distance;
			}
		}

		g2d.drawLine(x21, y21, x11, y11);
		g2d.drawLine(x11, y11, x12, y12);
		g2d.drawLine(x12, y12, x22, y22);
		g2d.drawOval(this.x2 - this.width - this.distance, this.y2 - this.distance - this.width,
				(this.width + this.distance) * 2, (this.width + this.distance) * 2);
	}

	public static double[] calculateParallelLine(double k, double b, double d) {
		double tmpNewBSqrt = (k * k * k * k + 1 + 2 * (k * k)) / (k * k + 1);
		double tmpNewB = Math.sqrt(tmpNewBSqrt) * d;
		double newB1 = b + tmpNewB;
		double newB2 = b - tmpNewB;

		return new double[] { newB1, newB2 };
	}

	public static int[] calculateCrossPoint(double k1, double b1, double k2, double b2) {
		int x = (int) ((b2 - b1) / (k1 - k2));
		int y = (int) (k1 * x + b1);
		return new int[] { x, y };
	}

	public static double[] calculateVerticalOfLine(double k, double b, double x, double y) {
		return new double[] { -1 / k, 1 / k * x + y };
	}

	public Set<CanvasElement> getConnectedInputs() {
		return this.connectedInputs;
	}

	public Set<CanvasElement> getConnectedOutputs() {
		return this.connectedOutputs;
	}

	public CanvasElement getConnectedOwner() {
		return this.connectedInput;
	}

	public void setConnectedOwner(CanvasElement owner) {
		if(owner!=null){
			resetConnectedOwner();
			owner.getConnectedOutputs().add(this);
			setConnectedOwner(true);
		}else{
			setConnectedOwner(false);
		}
		this.connectedInput = owner;
	}

	public EHeader geteHeader() {
		return eHeader;
	}

	public void seteHeader(EHeader eHeader) {
		this.eHeader = eHeader;
	}

	private void drawName(Graphics2D g2d, String name) {
		g2d.setPaint(Color.black);
		int x = this.x2 - name.length() * 4;
		int y = this.y2 - this.width - this.distance;
		g2d.drawString(name, x, y);
	}

	public String toXMLString() {
		String result = "\t<eheader x1=\"" + this.x1 + "\" y1=\"" + this.y1 + "\" x2=\"" + this.x2 + "\" y2=\""
				+ this.y2 + "\" id=\"" + this.getID() + "\">" + ECMFileManage.NEW_LINE;

		if (this.getConnectedOwner() != null) {
			result += "\t\t<ownerID>" + this.getConnectedOwner().getID() + "</ownerID>" + ECMFileManage.NEW_LINE;
		}
		result += "\t\t<connected>" + this.isConnectedOwner() + "</connected>" + ECMFileManage.NEW_LINE;

		result += this.eHeader.toXMLString();
		result += "\t</eheader>" + ECMFileManage.NEW_LINE;
		return result;
	}
	
}
