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

	// ͼԪ������
	protected ElementType elementType;

	private int ID = 0;

	protected float stroke = 1;

	// ����ͼԪ�Ķ������꣬��Щֻ����x1,y1,��Щ�����ˣ���ͬ���ڲ�ͬͼԪ�д���ͬ����Ķ���
	protected int x1, x2, y1, y2;
	protected int offsetX1, offsetY1, offsetX2, offsetY2;

	// ���������ڲ�ͬ��ͼԪ�д���ͬ�ĺ��壬���忴��������Ķ���
	protected int height;
	protected int width;
	protected int distance = 2;

	// ��Ǹ�ͼԪ�Ƿ�ѡ��
	protected boolean choosed = false;
	// ���ѡ�и�ͼԪ�еĵ��Ƿ�����ת����
	protected boolean withInRotate = false;
	// ��ͼԪ�Ƿ�����������ͼԪ�����ͼԪ����Ҫ������ͷ�͹�ϵ
	protected boolean connectedOwner = false;
	protected boolean connectedSon = false;
	// ��Ǹ�ͼԪ�Ƿ���Ҫ��ת��������ͼԪ���Ǹ�����
	protected boolean rotateOwner = false;

	protected CanvasElement connectedInput;
	protected CanvasElement connectedOutput;

	protected Set<CanvasElement> connectedInputs;
	protected Set<CanvasElement> connectedOutputs;
	
	//�Զ����Ű�ʱʹ�ã���¼��ͼԪ�Ƿ񱻷��ʹ���  0 δ������  1 ���ʹ�
	protected int flag;

	public CanvasElement() {
		connectedInputs = new HashSet<CanvasElement>();
		connectedOutputs = new HashSet<CanvasElement>();
	}

	// ��ȡID�������ļ�ʱ��Ҫ���������������ͼԪ�Ĺ�ϵ
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	// �������ͼԪ
	public void draw(Graphics2D g2d) {

	}

	// �����жϵ��Ƿ��ڸ�ͼԪ��
	public boolean pointWithInMe(int x, int y) {
		return false;
	}

	// �жϵ���ĵ��Ƿ���ͼԪ��
	public boolean withInMe(int x, int y) {
		return false;
	}

	// �ж�with in me��ʱ��ͬʱ�趨�Ƿ�ѡ������ת�ĵ�
	public boolean getWithInRotate() {
		return this.withInRotate;
	}

	// ͼԪ�Ƿ�ѡ����趨���ж�
	public void setChoosed(boolean choosed) {
		this.choosed = choosed;
	}

	public boolean isChoosed() {
		return this.choosed;
	}

	// �趨���ж��㵽��ǰ������λ��
	public void setOffset(int x, int y) {

	}

	// ֻ�趨��ת���㵽��ǰ������λ��
	public void setRotateOffset(int x, int y) {

	}

	// ƽ����ק������������λ���趨�¶��������
	public void resetPoints(int x, int y) {

	}

	// ��ת��ק����������ֻ�趨��ת���������
	public void resetPointsRotate(int x, int y) {

	}

	// ��ЩͼԪ����ת���Ϊ���֣�һ������������Ķ��㣬һ������������Ķ��㣬���������
	public void setRotateOwner(boolean rotateOwner) {
		this.rotateOwner = rotateOwner;
	}

	public boolean getRotateOwner() {
		return this.rotateOwner;
	}

	// �趨����1������
	public void setX1Y1(int x, int y) {
		this.x1 = x;
		this.y1 = y;
	}

	// �趨����2������
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

	// ��ȡͼԪ�߽���Σ������жϻ����Ƿ�Ҫ����ͬʱ��ӻ����Ĺ�����
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
	 * ���ϵ����ݶ��ǹ���ͼԪ�����ģ���������ݾ��ǹ���ͼԪ�������������趨��
	 */
	// owner�������룬son�����������������������ʾ�Ƿ���������������ͼԪ
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

	// �趨�ͻ�ȡ����ĵ�����������ͼԪ
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

	// ����ͼԪ������������ͼԪ������������б���ɾȥ
	public void resetConnectedOwner() {

	}

	public void resetConnectedSon() {

	}

	// �������ֱ��ǽ���ͼԪ������������ͼԪ������������б���ɾȥ
	public void resetConnectedPointOwner(CanvasElement ce) {

	}

	public void resetConnectedPointSon(CanvasElement ce) {

	}

	// ��ȡͼԪ����������ͼԪ�б�
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

	// ��������ͼԪ��ͼԪ������ӵ�λ��
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
