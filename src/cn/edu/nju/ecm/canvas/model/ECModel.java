package cn.edu.nju.ecm.canvas.model;

import java.awt.Canvas;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import javax.lang.model.element.ElementKind;

import cn.edu.nju.ecm.canvas.model.CanvasElement.ElementType;
import cn.edu.nju.ecm.utility.Undotooler;
import cn.edu.nju.ecm.utility.UndoCommand;
import cn.edu.nju.ecm.utility.UndoCommand.ActionType;
import cn.edu.nju.ecm.view.CanvasPanel;
import ecm.main.Edge;
import ecm.main.Node;
import ecm.main.Spring;

public class ECModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<CanvasElement> elements;

	private int ID;
	private String title = "";
	private String description = "";

	private int maxIDNumber = 0;

	private File file;

	public ECModel() {
		this.elements = new ArrayList<CanvasElement>();
	}

	public ECModel(String title, String description, File file) {
		this();
		this.title = title;
		this.description = description;
		this.file = file;
	}

	public void insertNewWElement(CanvasElement element, boolean isUndo) {
		if (!isUndo) { // 如果不是由于撤销操作引起的则增加undo记录
			this.maxIDNumber++;
			element.setID(this.maxIDNumber);
			Undotooler
					.pushUndoCommand(new UndoCommand(element, ActionType.New));
		}
		this.getElements().add(0, element);
	}

	public boolean deleteElement(int ID, boolean isUndo) {
		CanvasElement element = this.getElementByID(ID);
		if (element != null) {
			this.getElements().remove(element);
			if (!isUndo) // 如果不是由于撤销操作引起的则增加undo记录
				Undotooler.pushUndoCommand(new UndoCommand(element,
						ActionType.Delete));
			return true;
		}
		return false;
	}

	public CanvasElement getElementByID(int ID) {
		for (CanvasElement element : this.getElements()) {
			if (element.getID() == ID) {
				return element;
			}
		}
		return null;
	}

	public int getMaxIDNumber() {
		return maxIDNumber;
	}

	public void setMaxIDNumber(int maxIDNumber) {
		this.maxIDNumber = maxIDNumber;
	}

	public List<CanvasElement> getElements() {
		return elements;
	}

	public void setElements(List<CanvasElement> elements) {
		this.elements = elements;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public CanvasElement getChoosedElement(int x, int y) {
		// TODO Auto-generated method stub
		for (CanvasElement ce : this.elements) {
			if (ce.withInMe(x, y)) {
				return ce;
			}
		}
		return null;
	}

	public void updateConnectable(CanvasElement element) {
		boolean hasChoosedOwner = false;
		boolean hasChoosedSon = false;
		if (element.getElementType() == ElementType.Header) {
			for (CanvasElement ce : this.elements) {
				if (ce.getElementType() == ElementType.Body) {
					if (!hasChoosedOwner
							&& ce.pointWithInMe(element.getX1(),
									element.getY1())
							&& !ce.pointWithInMe(element.getX2(),
									element.getY2())) {
						ce.setChoosed(true);
						hasChoosedOwner = true;
					} else {
						ce.setChoosed(false);
					}
				}
			}
		}
		if (element.getElementType() == ElementType.Relation) {
			for (CanvasElement ce : this.elements) {
				if (ce.getElementType() == ElementType.Header
						|| ce.getElementType() == ElementType.Connector) {
					if (!hasChoosedOwner
							&& ce.pointWithInMe(element.getX1(),
									element.getY1())
							&& !ce.pointWithInMe(element.getX2(),
									element.getY2())) {
						ce.setChoosed(true);
						hasChoosedOwner = true;
						continue;
					}
					if (!hasChoosedSon
							&& ce.pointWithInMe(element.getX2(),
									element.getY2())
							&& !ce.pointWithInMe(element.getX1(),
									element.getY1())) {
						ce.setChoosed(true);
						hasChoosedSon = true;
					} else {
						ce.setChoosed(false);
					}
				}
			}
		}
	}

	public void reSetConnected(CanvasElement element) {
		if (element.getElementType() == ElementType.Header) {
			if (element.getElementType() == ElementType.Header) {
				for (CanvasElement ce : this.elements) {
					if (ce.getElementType() == ElementType.Body) {
						if (ce.pointWithInMe(element.getX1(), element.getY1())) {
							ce.setChoosed(false);
							element.setConnectedOwner(true);
							element.resetConnectedPointOwner(ce);
							ce.getConnectedOutputs().add(element);
							element.setConnectedOwner(ce);
							break;
						}
					}
				}
			}
			for (CanvasElement connectedRelation : element
					.getConnectedOutputs()) {
				connectedRelation.resetConnectedPointOwner(element);
			}

		} else if (element.getElementType() == ElementType.Connector) {
			for (CanvasElement connectedRelation : element.getConnectedInputs()) {
				connectedRelation.resetConnectedPointSon(element);
			}
		} else if (element.getElementType() == ElementType.Body) {
			for (CanvasElement connectedHeader : element.getConnectedOutputs()) {
				connectedHeader.resetConnectedPointOwner(element);
			}
		} else if (element.getElementType() == ElementType.Relation) {
			for (CanvasElement ce : this.elements) {
				if (ce.isChoosed()) {
					if (ce.getElementType() == ElementType.Header) {
						if (ce.pointWithInMe(element.getX1(), element.getY1())) {
							element.setConnectedOwner(true);
							element.resetConnectedPointOwner(ce);
							ce.getConnectedOutputs().add(element);
							element.setConnectedOwner(ce);
						}
						ce.setChoosed(false);

					} else if (ce.getElementType() == ElementType.Connector) {
						if (ce.pointWithInMe(element.getX2(), element.getY2())) {
							element.setConnectedSon(true);
							element.resetConnectedPointSon(ce);
							ce.getConnectedInputs().add(element);
							element.setConnectedSon(ce);
						}
						ce.setChoosed(false);
					}
				}
			}
		} else {

		}
	}

	public int getMaxWidth() {
		int max = 0;
		for (CanvasElement ce : this.elements) {
			if (ce.getX1() > max)
				max = ce.getX1();
		}
		return max;
	}

	public int getMaxHeight() {
		int max = 0;
		for (CanvasElement ce : this.elements) {
			if (ce.getY1() > max)
				max = ce.getY1();
		}
		return max;
	}

	/**
	 * 自动化排版功能 使用力向导算法
	 * */
	public void autoFormat2() {
		Spring sp = new Spring();
		List<Node> lNodes = new ArrayList<Node>();
		List<Edge> lEdges = new ArrayList<Edge>();
		for (CanvasElement ce : this.elements) {
			if (ce.getElementType() == ElementType.Connector
					|| ce.getElementType() == ElementType.Body) {
				lNodes.add(new Node(ce.getX1(), ce.getY1(), "" + ce.getID()));
			} else if (ce.getElementType() == ElementType.Header) {
				if (ce.connectedOwner) {
					ArrayList<CanvasElement> relations = getAllRelation(ce);
					for (int i = 0; i < relations.size(); i++) {
						CanvasElement tempRelation = relations.get(i);
						if (tempRelation.connectedSon)
							lEdges.add(new Edge(""
									+ ce.getConnectedOwner().getID(),
									tempRelation.getConnectedSon().getID() + ""));
					}
				}
			}
		}
		List<Node> reSetNodes = sp.springLayout(lNodes, lEdges);
		// 4.反复2,3步 迭代300次
		for (int i = 0; i < 1000; i++) {
			reSetNodes = sp.springLayout(reSetNodes, lEdges);
		}

		for (Node node : reSetNodes) {
			for (CanvasElement ce : this.elements) {
				if (Integer.parseInt(node.getId()) == ce.getID()) {
					ce.setRelativeXY(node.getX(), node.getY());
					;
				}
			}
		}

		reSetDrawCoordinate();

		resetElement();

	}

	/**
	 * 自动化排版功能 优先定义连体和链接点的位置，然后再将链头的位置插入
	 * */
	public void autoFormat() {
		boolean isNeedFormat2 = false;
		
		// 定义相对坐标：
		int dx = 180;
		int DY = -1; // 不在一个连通图内的
		System.out.println("Format start!!");
		ArrayList<CanvasElement> queueElements = new ArrayList<CanvasElement>();
		CanvasElement max = findMaxDegreeElement(this.elements);
		queueElements.add(max);

		max.setRelativeXY(0, 0);
		max.setStartAngle(0);
		max.setStart(true);
		max.setTotalAngle(Math.PI);
		System.out.println(max.getID());
		while (max != null) {
			CanvasElement currentElement = null;
			// 先遍历该图元所在的连通图：这里采用的是广度优先遍历
			while (!queueElements.isEmpty()) {
				currentElement = queueElements.get(0);
				queueElements.remove(0);

				currentElement.setFlag(-1);
				double angle = Math.PI;
				ArrayList<CanvasElement> nextElements = getSortedNextElements(currentElement);
				if (nextElements.size() > 1 && (currentElement.isStart()||currentElement.getTotalAngle()!=Math.PI))
					angle = currentElement.getTotalAngle() / (nextElements.size() - 1);
				else if (nextElements.size() > 0)
					angle = currentElement.getTotalAngle() / (nextElements.size());

				for (int i = 0; i < nextElements.size(); i++) {
					queueElements.add(nextElements.get(i));

					double DeltaAngle = currentElement.getStartAngle();
					
					int tempY = currentElement.getRelativeY();
					
					if (currentElement.isClockwise()) {
						DeltaAngle-=i*angle;
						tempY -= dx* Math.sin(DeltaAngle);
						if(Math.abs(DeltaAngle)<=Math.PI/2)
							nextElements.get(i).setClockwise(false);
						else
							nextElements.get(i).setClockwise(true);
						
					} else {
						DeltaAngle+=i*angle;
						tempY -= dx* Math.sin(DeltaAngle);
						if(Math.abs(DeltaAngle)<=Math.PI/2)
							nextElements.get(i).setClockwise(true);
						else
							nextElements.get(i).setClockwise(false);
					}
					int tempX = (int) (currentElement.getRelativeX() + dx
							* Math.cos(DeltaAngle));
					
					nextElements.get(i).setStartAngle(DeltaAngle);

					if (nextElements.get(i).getFlag() == 0)
						nextElements.get(i).setRelativeXY(tempX, tempY);
					else {
						isNeedFormat2=  true;
						tempX += nextElements.get(i).getRelativeX()
								* nextElements.get(i).getFlag();
						tempY += nextElements.get(i).getRelativeY()
								* nextElements.get(i).getFlag();
						nextElements.get(i).setRelativeXY(
								tempX / (nextElements.get(i).getFlag() + 1),
								tempY / (nextElements.get(i).getFlag() + 1));
					}
					
					if(i==0)
						nextElements.get(i).setTotalAngle(Math.PI);
					else if(i==nextElements.size()-1&&currentElement.isStart())
						nextElements.get(i).setTotalAngle(Math.PI);
					else
						nextElements.get(i).setTotalAngle(Math.abs(angle));
				}

			}

			max = findMaxDegreeElement(this.elements);
			if (max == null)
				break;
			DY = getMaxRelativeY() + dx;
			max.setRelativeXY(0, DY);
			max.setStartAngle(0);
			max.setStart(true);
			max.setTotalAngle(Math.PI);
			queueElements.add(max);
		}

		reSetDrawCoordinate();

		resetElement();
		
		if(isNeedFormat2)
			autoFormat2();
	}

	// 设置链头坐标位置
	private void setHeaderCoordinate() {
		for (CanvasElement ce : this.elements) {
			if (ce.getElementType() == ElementType.Header) {
				CanvasElement body = ce.getConnectedOwner();
				ArrayList<CanvasElement> tempRelations = getAllRelation(ce);
				for (int i = 0; i < tempRelations.size(); i++) {
					if (tempRelations.get(i).connectedSon) {
						CanvasElement tempConnector = tempRelations.get(i)
								.getConnectedSon();
						int tempX = (body.getX1() + body.getWidth() / 2
								+ tempConnector.getX1() + tempConnector
								.getWidth() / 2) / 2;
						int tempY = (body.getY1() + body.getHeight() / 2
								+ tempConnector.getY1() + tempConnector
								.getHeight() / 2) / 2;
						if (ce.getFlag() == 0) {
							ce.setX2Y2(tempX, tempY);
						} else {
							tempX += ce.getX2() * ce.getFlag();
							tempY += ce.getY2() * ce.getFlag();
							ce.setX2Y2(tempX / (ce.getFlag() + 1),
									tempY / (ce.getFlag() + 1));
						}
						ce.setFlag(ce.getFlag() + 1);
					}
				}
			}
		}

		// 检查链头是否与其他的图元（只有可能是链体）重合
		for (CanvasElement ce : this.elements) {
			if (ce.getElementType() == ElementType.Header) {
				if (ce.connectedOwner
						&& isOverlying(ce, ce.getConnectedOwner())) {
					// 暂定策略是移动header
					moveHeader(ce, ce.getConnectedOwner());
				}
			}
		}

		// 检查链头是否与其他的链头重合
		for (int i = 0; i < this.elements.size(); i++) {
			if (elements.get(i).getElementType() == ElementType.Header) {
				for (int j = i + 1; j < elements.size(); j++) {
					if (elements.get(j).getElementType() == ElementType.Header
							&& elements.get(i).getX2() == elements.get(j)
									.getX2()
							&& elements.get(i).getY2() == elements.get(j)
									.getY2()) {
						moveHeader(elements.get(i));
						break;
					}
				}
			}
		}

	}

	private void reSetDrawCoordinate() {
		int minx = 100;
		int miny = 100;
		for (CanvasElement ce : this.elements) {
			if (minx > ce.getRelativeX())
				minx = ce.getRelativeX();
			if (miny > ce.getRelativeY())
				miny = ce.getRelativeY();
		}
		int dx = Math.abs(minx) + 100;
		int dy = Math.abs(miny) + 100;
		for (CanvasElement ce : this.elements) {
			if (ce.getElementType() == ElementType.Body
					|| ce.getElementType() == ElementType.Connector) {
				ce.setX1Y1(ce.getRelativeX() + dx - ce.width / 2,
						ce.getRelativeY() + dy - ce.getHeight() / 2);
			}
		}
		setHeaderCoordinate();
		for (CanvasElement ce : this.elements) {
			updateConnectable(ce);
			reSetConnected(ce);
		}

	}

	private int getMaxRelativeY() {
		int max = 0;
		for (CanvasElement ce : this.elements) {
			if (ce.getRelativeY() > max)
				max = ce.getRelativeY();
		}
		return max;
	}

	private void resetElement() {
		for (CanvasElement ce : this.elements) {
			ce.setRelativeX(-1);
			ce.setRelativeY(-1);
			ce.setStartAngle(0);
			if (ce.getFlag() != 0)
				ce.setFlag(0);
		}

	}

	private CanvasElement findMaxDegreeElement(List<CanvasElement> elements) {
		CanvasElement maxDegree = null;
		for (CanvasElement ce : elements) {
			if (ce.getDegree() != -1
					&& ce.getFlag() == 0
					&& (maxDegree == null || maxDegree.getDegree() < ce
							.getDegree())) {
				maxDegree = ce;
			}
		}
		return maxDegree;
	}

	private ArrayList<CanvasElement> getSortedNextElements(
			CanvasElement currentElement) {
		ArrayList<CanvasElement> nextElements = new ArrayList<CanvasElement>();
		Set<CanvasElement> tempSet = new HashSet<CanvasElement>();

		if (currentElement.getElementType() == ElementType.Body) {
			ArrayList<CanvasElement> listofHeader = getAllHeader(currentElement);
			for (CanvasElement ce : listofHeader) {
				ArrayList<CanvasElement> temp = getAllRelation(ce);
				for (int i = 0; i < temp.size(); i++) {
					if (temp.get(i).connectedSon
							&& temp.get(i).getConnectedSon().getFlag() != -1)
						tempSet.add(temp.get(i).getConnectedSon());
				}
			}
		} else if (currentElement.getElementType() == ElementType.Connector) {
			ArrayList<CanvasElement> listofRelation = getAllRelation(currentElement);
			for (CanvasElement ce : listofRelation) {
				if (ce.connectedOwner
						&& ce.getConnectedOwner().connectedOwner
						&& ce.getConnectedOwner().getConnectedOwner().getFlag() != -1) {
					tempSet.add(ce.getConnectedOwner().getConnectedOwner());
				}
			}
		}
		for (CanvasElement ce : tempSet) {
			nextElements.add(ce);
		}
		Comparator<CanvasElement> comparator = new Comparator<CanvasElement>() {
			@Override
			public int compare(CanvasElement c1, CanvasElement c2) {
				return c2.getDegree() - c1.getDegree();
			}
		};
		Collections.sort(nextElements, comparator);
		return nextElements;
	}

	private ArrayList<CanvasElement> getAllHeader(CanvasElement element) {
		if (element.getElementType() == ElementType.Body) {
			ArrayList<CanvasElement> result = new ArrayList<CanvasElement>();
			for (CanvasElement ce : elements) {
				if (ce.getElementType() == ElementType.Header
						&& ce.connectedOwner
						&& ce.getConnectedOwner().getID() == element.getID()) {
					result.add(ce);
				}
			}
			return result;
		}
		return new ArrayList<CanvasElement>();
	}

	private ArrayList<CanvasElement> getAllRelation(CanvasElement element) {
		if (element.getElementType() == ElementType.Connector) {
			ArrayList<CanvasElement> result = new ArrayList<CanvasElement>();
			for (CanvasElement ce : elements) {
				if (ce.getElementType() == ElementType.Relation
						&& ce.connectedSon
						&& ce.getConnectedSon().getID() == element.getID()) {
					result.add(ce);
				}
			}
			return result;
		} else if (element.getElementType() == ElementType.Header) {
			ArrayList<CanvasElement> result = new ArrayList<CanvasElement>();
			for (CanvasElement ce : elements) {
				if (ce.getElementType() == ElementType.Relation
						&& ce.connectedOwner
						&& ce.getConnectedOwner().getID() == element.getID()) {
					result.add(ce);
				}
			}
			return result;
		}

		return new ArrayList<CanvasElement>();
	}

	private boolean isOverlying(CanvasElement header, CanvasElement body) { // 判断header是否和body有重合
		if (body.pointWithInMe(header.getX2(), header.getY2())
				|| body.pointWithInMe(header.getX2() + header.getWidth() * 2,
						header.getY2())
				|| body.pointWithInMe(header.getX2(),
						header.getY2() + header.getHeight() * 2)
				|| body.pointWithInMe(header.getX2() + header.getWidth(),
						header.getY2() + header.getHeight() * 2))
			return true;
		return false;
	}

	private void moveHeader(CanvasElement header, CanvasElement body) { // header相对于body进行一个身位的平移
		int bodyX = body.getX1() + body.getWidth() / 2;
		int bodyY = body.getY1() + body.getHeight() / 2;
		int headX = header.getX2();
		int headY = header.getY2();
		int dx = headX - bodyX;
		int dy = headY - bodyY;
		// 忽略由于double转换为int的误差
		if (Math.abs(dx) < 2)
			dx = 0;
		if (Math.abs(dy) < 2)
			dy = 0;
		if (dx == 0 && dy == 0) {
			if (Math.abs(body.getStartAngle()) <= Math.PI / 4
					|| Math.abs(body.getStartAngle()) >= Math.PI * 3 / 4)
				dy -= 3 * header.getWidth();
			else
				dx -= (2 * header.getWidth() + body.getWidth() / 2);
		} else {
			while ((dx * dx + dy * dy) < (Math.pow(
					(3 * header.getWidth() + body.getWidth() / 2), 2) + Math
					.pow((3 * header.getWidth() + body.getHeight() / 2), 2))) {
				if (dx > 0) {
					dx += header.getWidth() * 2;
				} else if (dx < 0) {
					dx -= header.getWidth() * 2;
				}
				if (dy > 0) {
					dy += header.getWidth() * 2;
				} else if (dy < 0) {
					dy -= header.getWidth() * 2;
				}
			}
		}
		header.setX2Y2(dx + bodyX, dy + bodyY);
	}

	private void moveHeader(CanvasElement header) { // 解决链头与链头重合问题
		if (header.connectedOwner) {
			ArrayList<CanvasElement> overLyingHeaders = new ArrayList<CanvasElement>();
			for (CanvasElement ce : this.elements) {
				if (ce.getElementType() == ElementType.Header
						&& ce.getX2() == header.getX2()
						&& ce.getY2() == header.getY2()) {
					overLyingHeaders.add(ce);
				}
			}
			int headX = header.getX2();
			int headY = header.getY2();
			CanvasElement body = header.getConnectedOwner();
			int bodyX = body.getX1() + body.getWidth() / 2;
			int bodyY = body.getY1() + body.getHeight() / 2;
			int DX = headX - bodyX;
			int DY = headY - bodyY;

			double distance = Math.pow((DX * DX + DY * DY), 0.5);
			int dx = (int) (3 * header.getWidth() / distance * DY);
			int dy = (int) (3 * header.getWidth() / distance * DX);

			System.out.println("haarara: " + DX + " , " + DY);
			System.out.println("heerere: " + dx + " , " + dy);
			int half = overLyingHeaders.size() / 2;

			for (int i = 0; i < half; i++) {
				CanvasElement temp = overLyingHeaders.get(i);
				temp.setX2Y2(temp.getX2() + dx * (i + 1), temp.getY2() - dy
						* (i + 1));
			}

			for (int i = half; i < half * 2; i++) {
				CanvasElement temp = overLyingHeaders.get(i);
				temp.setX2Y2(temp.getX2() - dx * (i + 1 - half), temp.getY2()
						+ dy * (i + 1 - half));
			}
		}
	}
}
