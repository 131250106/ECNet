package cn.edu.nju.ecm.canvas.model.entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;

import cn.edu.nju.ecm.canvas.model.CanvasElement;
import cn.edu.nju.ecm.entity.detail.HRelation;
import cn.edu.nju.ecm.file.ECMFileManage;

public class HRelationModel extends CanvasElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	double height = 12;
	double arrayWidth = 5;
	private HRelation hRelation;

	public HRelationModel(int x1, int y1, int x2, int y2, int id, HRelation hRelation) {
		super();
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.setID(id);

		this.hRelation = hRelation;
		this.elementType = ElementType.Relation;
	}

	public HRelationModel(int x1, int y1, int distance) {
		super();
		width = 50;
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = this.x1 + this.width;
		this.y2 = this.y1;
		this.distance = distance;

		this.hRelation = new HRelation();
		this.elementType = ElementType.Relation;
	}

	public void draw(Graphics2D g2d) {
		if (this.isConnectedOwner()) {
			g2d.setPaint(Color.black);
			g2d.setStroke(new BasicStroke(1));

			g2d.fillOval(this.x1 - 3, this.y1 - 3, 6, 6);
		}
		if (this.isConnectedSon()) {
			g2d.setPaint(Color.black);
			g2d.setStroke(new BasicStroke(1));

			g2d.fillOval(this.x2 - 3, this.y2 - 3, 6, 6);
		}
		if (this.choosed) {
			g2d.setPaint(Color.RED);
			g2d.setStroke(new BasicStroke(1));

			g2d.drawOval(this.x1 - this.distance * 2, this.y1 - this.distance * 2, this.distance * 4,
					this.distance * 4);
			g2d.drawOval(this.x2 - this.distance * 2, this.y2 - this.distance * 2, this.distance * 4,
					this.distance * 4);
		}
		g2d.setPaint(Color.BLACK);
		g2d.setStroke(new BasicStroke(1));
		drawAL(g2d);
		if (!this.gethRelation().getName().equals("")) {
			drawName(g2d, this.gethRelation().getName());
		}
	}

	public boolean pointWithInMe(int x, int y) {
		if (((x - this.x1) * (x - this.x1) + (y - this.y1) * (y - this.y1) < this.distance * this.distance * 4)
				|| ((x - this.x2) * (x - this.x2) + (y - this.y2) * (y - this.y2) < this.distance * this.distance * 4)
				|| ((Math.sqrt((y - y1) * (y - y1) + (x - x1) * (x - x1))
						+ Math.sqrt((y - y2) * (y - y2) + (x - x2) * (x - x2))) <= Math
								.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1)) + this.distance * 1)) {
			return true;
		}
		return false;
	}

	public boolean withInMe(int x, int y) {
		if (this.withInRotate(x, y) || this.withInLine(x, y)) {
			return true;
		} else {
			this.withInRotate = false;
			return false;
		}
	}

	public boolean withInRotate(int x, int y) {
		if ((x - this.x1) * (x - this.x1) + (y - this.y1) * (y - this.y1) < this.distance * this.distance * 4) {
			this.setRotateOwner(true);
			setRotateOffset(x, y);
			this.withInRotate = true;
			return true;
		} else {
			if ((x - this.x2) * (x - this.x2) + (y - this.y2) * (y - this.y2) < this.distance * this.distance * 4) {
				this.setRotateOwner(false);
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
			setOffset(x, y);
			this.withInRotate = false;
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

	public void resetPoints(int x, int y) {
		if ((x - this.offsetX1) > 0 && (y - this.offsetY1) > 0 && (x - this.offsetX2) > 0 && (y - this.offsetY2) > 0) {
			this.x1 = x - this.offsetX1;
			this.y1 = y - this.offsetY1;
			this.x2 = x - this.offsetX2;
			this.y2 = y - this.offsetY2;
		}
	}

	public void resetPointsRotate(int x, int y) {
		if (this.getRotateOwner()) {
			if ((x - this.offsetX1) > 0 && (y - this.offsetY1) > 0) {
				this.x1 = x - this.offsetX1;
				this.y1 = y - this.offsetY1;
			}
		} else {
			if ((x - this.offsetX2) > 0 && (y - this.offsetY2) > 0) {
				this.x2 = x - this.offsetX2;
				this.y2 = y - this.offsetY2;
			}
		}
	}

	public void resetConnectedPointOwner(CanvasElement ce) {
		int[] newXY1;
		if (ce.getElementType() == ElementType.Header) {
			CanvasElement ceTmp = new EBodyModel();
			ceTmp.setX1Y1(ce.getX2() - ce.getWidth(), ce.getY2() - ce.getWidth());
			ceTmp.setHeight(ce.getWidth() * 2);
			ceTmp.setWidth(ce.getWidth() * 2);
			newXY1 = CanvasElement.calculateNewConnectedPointRectangle(ceTmp, this.x2, this.y2);
		} else {
			newXY1 = CanvasElement.calculateNewConnectedPointRectangle(ce, this.x2, this.y2);
		}
		this.x1 = newXY1[0];
		this.y1 = newXY1[1];
	}

	public void resetConnectedPointSon(CanvasElement ce) {
		int[] newXY2;
		newXY2 = CanvasElement.calculateNewConnectedPointRectangle(ce, this.x1, this.y1);
		this.x2 = newXY2[0];
		this.y2 = newXY2[1];
	}

	public void resetConnectedOwner() {
		if (this.getConnectedOwner() != null) {
			this.getConnectedOwner().getConnectedOutputs().remove(this);
			this.setConnectedOwner(null);
		}
	}

	public void resetConnectedSon() {
		if (this.getConnectedSon() != null) {
			this.getConnectedSon().getConnectedInputs().remove(this);
			this.setConnectedSon(null);
		}
	}

	public Rectangle[] getRectangles() {
		return new Rectangle[] { new Rectangle(this.x1, this.y1, 2, 2), new Rectangle(this.x2, this.y2, 2, 2) };
	}

	public CanvasElement getConnectedOwner() {
		return this.connectedInput;
	}

	public void setConnectedOwner(CanvasElement owner) {
		if(owner!=null){
			resetConnectedOwner();
			owner.getConnectedOutputs().add(this);
		}else{
			setConnectedOwner(false);
		}
		this.connectedInput = owner;
	}

	public CanvasElement getConnectedSon() {
		return this.connectedOutput;
	}

	public void setConnectedSon(CanvasElement son) {
		if(son!=null){
			resetConnectedSon();
			son.getConnectedInputs().add(this);
		}else{
			setConnectedSon(false);
		}
		this.connectedOutput = son;
	}

	public HRelation gethRelation() {
		return hRelation;
	}

	public void sethRelation(HRelation hRelation) {
		this.hRelation = hRelation;
	}

	public void drawAL(Graphics2D g2) {
		int x3 = 0, y3 = 0, x4 = 0, y4 = 0;
		double awrad = Math.atan(this.arrayWidth / this.height); // 箭头角度
		double arraow_len = Math.sqrt(this.arrayWidth * this.arrayWidth + this.height * this.height); // 箭头的长度
		double[] arrXY_1 = rotateVec(this.x2 - this.x1, this.y2 - this.y1, awrad, true, arraow_len);
		double[] arrXY_2 = rotateVec(this.x2 - this.x1, this.y2 - this.y1, -awrad, true, arraow_len);
		double x_3 = this.x2 - arrXY_1[0]; // (x3,y3)是第一端点
		double y_3 = this.y2 - arrXY_1[1];
		double x_4 = this.x2 - arrXY_2[0]; // (x4,y4)是第二端点
		double y_4 = this.y2 - arrXY_2[1];

		Double X3 = new Double(x_3);
		x3 = X3.intValue();
		Double Y3 = new Double(y_3);
		y3 = Y3.intValue();
		Double X4 = new Double(x_4);
		x4 = X4.intValue();
		Double Y4 = new Double(y_4);
		y4 = Y4.intValue();
		// 画线
		g2.drawLine(this.x1, this.y1, this.x2, this.y2);
		//
		GeneralPath triangle = new GeneralPath();
		triangle.moveTo(this.x2, this.y2);
		triangle.lineTo(x3, y3);
		triangle.lineTo(x4, y4);
		triangle.closePath();
		g2.setColor(Color.gray);
		g2.fill(triangle);

	}

	// 计算
	public static double[] rotateVec(int px, int py, double ang, boolean isChLen, double newLen) {

		double mathstr[] = new double[2];
		// 矢量旋转函数，参数含义分别是x分量、y分量、旋转角、是否改变长度、新长度
		double vx = px * Math.cos(ang) - py * Math.sin(ang);
		double vy = px * Math.sin(ang) + py * Math.cos(ang);
		if (isChLen) {
			double d = Math.sqrt(vx * vx + vy * vy);
			vx = vx / d * newLen;
			vy = vy / d * newLen;
			mathstr[0] = vx;
			mathstr[1] = vy;
		}
		return mathstr;
	}

	private void drawName(Graphics2D g2d, String name) {
		g2d.setPaint(Color.black);
		int x = (this.x1 + this.x2) / 2 - name.length() * 4;
		int y = (this.y1 + this.y2) / 2 - 4;
		g2d.drawString(name, x, y);
	}

	@Override
	public String toXMLString() {
		// TODO Auto-generated method stub
		String result = "\t<hrelation x1=\"" + this.x1 + "\" y1=\"" + this.y1 + "\" x2=\"" + this.x2 + "\" y2=\""
				+ this.y2 + "\" id=\"" + this.getID() + "\">" + ECMFileManage.NEW_LINE;

		if (this.getConnectedOwner() != null) {
			result += "\t\t<ownerID>" + this.getConnectedOwner().getID() + "</ownerID>" + ECMFileManage.NEW_LINE;
		}
		if (this.getConnectedSon() != null) {
			result += "\t\t<sonID>" + this.getConnectedSon().getID() + "</sonID>" + ECMFileManage.NEW_LINE;
		}
		result += "\t\t<connected>" + this.isConnectedOwner() + "</connected>" + ECMFileManage.NEW_LINE;
		result += "\t\t<connected>" + this.isConnectedSon() + "</connected>" + ECMFileManage.NEW_LINE;

		result += this.hRelation.toXMLString();
		result += "\t</hrelation>" + ECMFileManage.NEW_LINE;
		return result;
	}
	
}
