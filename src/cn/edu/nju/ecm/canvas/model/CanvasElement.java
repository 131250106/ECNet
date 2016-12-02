package cn.edu.nju.ecm.canvas.model;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class CanvasElement implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum ElementType {
		Body, Header, Relation, Connector
	}

	// 图元的类型
	protected ElementType elementType;

	private int ID = 0;

	protected float stroke = 1;

	// 所有图元的顶点坐标，有些只用了x1,y1,有些都用了；不同点在不同图元中代表不同意义的顶点
	protected int x1, x2, y1, y2;
	protected int offsetX1, offsetY1, offsetX2, offsetY2;

	// 这三个量在不同的图元中代表不同的含义，具体看子类里面的定义
	protected int height;
	protected int width;
	protected int distance = 2;

	// 标记该图元是否被选中
	protected boolean choosed = false;
	// 标记选中该图元中的点是否在旋转区域
	protected boolean withInRotate = false;
	// 该图元是否链接了输入图元和输出图元，主要用于链头和关系
	protected boolean connectedOwner = false;
	protected boolean connectedSon = false;
	// 标记该图元是否需要旋转链接输入图元的那个顶点
	protected boolean rotateOwner = false;

	protected CanvasElement connectedInput;
	protected CanvasElement connectedOutput;

	protected Set<CanvasElement> connectedInputs;
	protected Set<CanvasElement> connectedOutputs;
	
	//自动化排版时使用，记录该图元是否被访问过：  0 未被访问  1 访问过
	protected int flag;

	public CanvasElement() {
		connectedInputs = new HashSet<CanvasElement>();
		connectedOutputs = new HashSet<CanvasElement>();
	}

	// 获取ID，保存文件时需要保存它，用来标记图元的关系
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	// 绘制这个图元
	public void draw(Graphics2D g2d) {

	}

	// 仅仅判断点是否在该图元中
	public boolean pointWithInMe(int x, int y) {
		return false;
	}

	// 判断点击的点是否在图元中
	public boolean withInMe(int x, int y) {
		return false;
	}

	// 判断with in me的时候同时设定是否选择了旋转的点
	public boolean getWithInRotate() {
		return this.withInRotate;
	}

	// 图元是否被选择的设定和判断
	public void setChoosed(boolean choosed) {
		this.choosed = choosed;
	}

	public boolean isChoosed() {
		return this.choosed;
	}

	// 设定所有顶点到当前点击点的位移
	public void setOffset(int x, int y) {

	}

	// 只设定旋转顶点到当前点击点的位移
	public void setRotateOffset(int x, int y) {

	}

	// 平移拖拽动作结束后按照位移设定新顶点的坐标
	public void resetPoints(int x, int y) {

	}

	// 旋转拖拽动作结束后只设定旋转顶点的坐标
	public void resetPointsRotate(int x, int y) {

	}

	// 有些图元的旋转点分为两种，一种是链接输入的顶点，一个是链接输出的顶点，用这个区分
	public void setRotateOwner(boolean rotateOwner) {
		this.rotateOwner = rotateOwner;
	}

	public boolean getRotateOwner() {
		return this.rotateOwner;
	}

	// 设定顶点1的坐标
	public void setX1Y1(int x, int y) {
		this.x1 = x;
		this.y1 = y;
	}

	// 设定顶点2的坐标
	public void setX2Y2(int x, int y) {
		this.x2 = x;
		this.y2 = y;
	}

	public int getX1() {
		return this.x1;
	}

	public int getY1() {
		return this.y1;
	}

	public int getX2() {
		return this.x2;
	}

	public int getY2() {
		return this.y2;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	// 获取图元边界矩形，用来判断画布是否要扩大，同时添加画布的滚动条
	public Rectangle[] getRectangles() {
		return null;
	}

	public ElementType getElementType() {
		return elementType;
	}

	public void setElementType(ElementType elementType) {
		this.elementType = elementType;
	}

	public String toXMLString() {
		return "";
	}

	/*
	 * 以上的内容都是关于图元操作的，下面的内容就是关于图元的输入和输出的设定了
	 */
	// owner代表输入，son代表输出；这两个量用来表示是否链接了输入和输出图元
	public boolean isConnectedOwner() {
		return connectedOwner;
	}

	public void setConnectedOwner(boolean connected) {
		this.connectedOwner = connected;
	}

	public boolean isConnectedSon() {
		return connectedSon;
	}

	public void setConnectedSon(boolean connected) {
		this.connectedSon = connected;
	}

	// 设定和获取具体的单个输入和输出图元
	public CanvasElement getConnectedOwner() {
		return null;
	}

	public void setConnectedOwner(CanvasElement owner) {

	}

	public CanvasElement getConnectedSon() {
		return null;
	}

	public void setConnectedSon(CanvasElement son) {

	}

	// 将该图元从其输入或输出图元的输出或输入列表中删去
	public void resetConnectedOwner() {

	}

	public void resetConnectedSon() {

	}

	// 这两个分别是将该图元从其输入和输出图元的输出和输入列表中删去
	public void resetConnectedPointOwner(CanvasElement ce) {

	}

	public void resetConnectedPointSon(CanvasElement ce) {

	}

	// 获取图元的输入和输出图元列表
	public Set<CanvasElement> getConnectedInputs() {
		return this.connectedInputs;
	}

	public Set<CanvasElement> getConnectedOutputs() {
		return this.connectedOutputs;
	}

	public CanvasElement getConnectedOutput() {
		return connectedOutput;
	}

	public void setConnectedInputs(Set<CanvasElement> connectedInputs) {		
		this.connectedInputs = connectedInputs;
	}

	public void setConnectedOutputs(Set<CanvasElement> connectedOutputs) {
		this.connectedOutputs = connectedOutputs;
	}

	// 用来计算图元和图元间的链接点位置
	protected static int[] calculateNewConnectedPointRectangle(CanvasElement ce, int outX, int outY) {
		// TODO Auto-generated method stub
		int[] newXY = new int[2];
		int ceCenterX = ce.getX1() + ce.getWidth() / 2;
		int ceCenterY = ce.getY1() + ce.getHeight() / 2;
		if (outX == ceCenterX) {
			newXY[0] = outX;
			if (outY > ceCenterY) {
				newXY[1] = ce.getY1() + ce.getHeight();
			} else {
				newXY[1] = ce.getY1();
			}
		} else if (outY == ceCenterY) {
			newXY[1] = outY;
			if (outX > ceCenterX) {
				newXY[0] = ce.getX1() + ce.getWidth();
			} else {
				newXY[0] = ce.getX1();
			}
		} else {
			if ((Math.abs(outY - ceCenterY) / Math.abs(outX - ceCenterX)) > (ce.getHeight() / ce.getWidth())) {
				newXY[0] = ceCenterX;
				if (outY > ceCenterY) {
					newXY[1] = ce.getY1() + ce.getHeight();
				} else {
					newXY[1] = ce.getY1();
				}
			} else {
				newXY[1] = ceCenterY;
				if (outX > ceCenterX) {
					newXY[0] = ce.getX1() + ce.getWidth();
				} else {
					newXY[0] = ce.getX1();
				}
			}
		}
		return newXY;
	}
	
	public CanvasElement copy(){
		CanvasElement copy = new CanvasElement();
		copy.setChoosed(choosed);
		copy.setConnectedOwner(connectedOwner);
		copy.setConnectedSon(connectedSon);
		copy.setRotateOwner(rotateOwner);
		copy.setElementType(elementType);
		copy.setHeight(height);
		copy.setID(ID);
		copy.setWidth(width);
		copy.setX1Y1(x1, y1);
		copy.setX2Y2(x2, y2);
		return copy;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getDegree() {
		return -1;
	}
	
	private int relativeX=-1;
	private int relativeY=-1;
	private double startAngle = 0;
	
	public double getStartAngle() {
		return startAngle;
	}

	public void setStartAngle(double startAngle) {
		this.startAngle = startAngle;
	}

	public void setRelativeXY(int x,int y){
		this.flag=1;
//		if(this.relativeX!=-1)
//			this.relativeX=(this.relativeX+x)/2;
//		else
			this.relativeX = x;
//		if(this.relativeY!=-1)
//			this.relativeY=(this.relativeY+y)/2;
//		else
			this.relativeY = y;
	}
	public int getRelativeX(){
		return this.relativeX;
	}
	public int getRelativeY(){
		return this.relativeY;
	}
	public void setRelativeX(int relativeX) {
		this.relativeX = relativeX;
	}
	public void setRelativeY(int relativeY) {
		this.relativeY = relativeY;
	}
}
