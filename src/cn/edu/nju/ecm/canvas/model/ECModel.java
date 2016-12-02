package cn.edu.nju.ecm.canvas.model;

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
	 * 自动化排版功能
	 * */

	public void autoFormat() {
		// 定义相对坐标：
		int dx = 200;
		int DY = -1; // 不在一个连通图内的
		System.out.println("Format start!!");
		ArrayList<CanvasElement> queueElements = new ArrayList<CanvasElement>();
		CanvasElement max = findMaxDegreeElement(this.elements);
		queueElements.add(max);

		max.setRelativeXY(0, 0);
		max.setStartAngle(0);
		while (max != null) {
			CanvasElement currentElement = null;
			// 先遍历该图元所在的连通图：这里采用的是广度优先遍历
			while (!queueElements.isEmpty()) {
				currentElement = queueElements.get(0);
				queueElements.remove(0);
				
				currentElement.setFlag(2);
				double angle = 0;
				ArrayList<CanvasElement> nextElements = getSortedNextElements(currentElement);
				if (nextElements.size() > 0)
					angle = Math.PI / nextElements.size();

				for (int i = 0; i < nextElements.size(); i++) {
					queueElements.add(nextElements.get(i));
					System.out.println(currentElement.getStartAngle()
							- i
							* angle
							+ " : "
							+ Math.cos(currentElement.getStartAngle() - i
									* angle)
							+ " , "
							+ Math.sin(currentElement.getStartAngle() - i
									* angle));
					nextElements.get(i).setRelativeXY(
							(int) (currentElement.getRelativeX() + dx
									* Math.cos(currentElement.getStartAngle()
											- i * angle)),
							(int) (currentElement.getRelativeY() + dx
									* Math.sin(currentElement.getStartAngle()
											- i * angle)));
					nextElements.get(i).setStartAngle(
							currentElement.getStartAngle() - i * angle);
				}

			}

			max = findMaxDegreeElement(this.elements);
			if (max == null)
				break;
			DY = getMaxRelativeY() + dx;
			max.setRelativeXY(0, DY);
			max.setStartAngle(0);
			queueElements.add(max);
		}

		// setHeaderCoordinate();

		reSetDrawCoordinate();

		printCoordinate();

		resetElement();
	}

	// 设置链头坐标位置
	private void setHeaderCoordinate() {
		// TODO 暂时不考虑联结点和链体之间由多个链头链接
		for (CanvasElement ce : this.elements) {
			if (ce.getElementType() == ElementType.Header) {
				CanvasElement body = ce.getConnectedOwner();
				ArrayList<CanvasElement> tempRelations = getAllRelation(ce);
				for (int i = 0; i < tempRelations.size(); i++) {
					if (tempRelations.get(i).connectedSon) {
						CanvasElement tempConnector = tempRelations.get(i)
								.getConnectedSon();
						ce.setRelativeXY((body.getRelativeX() + tempConnector
								.getRelativeX()) / 2,
								(body.getRelativeY() + tempConnector
										.getRelativeY()) / 2);
					}
				}
			}
		}
	}

	// 设置链头坐标位置
	private void setHeaderCoordinate2() {
		// TODO 暂时不考虑联结点和链体之间由多个链头链接
		for (CanvasElement ce : this.elements) {
			if (ce.getElementType() == ElementType.Header) {
				CanvasElement body = ce.getConnectedOwner();
				ArrayList<CanvasElement> tempRelations = getAllRelation(ce);
				for (int i = 0; i < tempRelations.size(); i++) {
					if (tempRelations.get(i).connectedSon) {
						CanvasElement tempConnector = tempRelations.get(i)
								.getConnectedSon();
						ce.setX2Y2((body.getX1() + body.getWidth() / 2
								+ tempConnector.getX1() + tempConnector
								.getWidth() / 2) / 2,
								(body.getY1() + body.getHeight() / 2
										+ tempConnector.getY1() + tempConnector
										.getHeight() / 2) / 2);
						// ce.setRelativeXY((body.getRelativeX() + tempConnector
						// .getRelativeX()) / 2,
						// (body.getRelativeY() + tempConnector
						// .getRelativeY()) / 2);
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
		setHeaderCoordinate2();
		for (CanvasElement ce : this.elements) {
			updateConnectable(ce);
			reSetConnected(ce);
		}

	}

	private void printCoordinate() {
		for (CanvasElement ce : elements) {
			if (ce.getElementType() == ElementType.Body
					|| ce.getElementType() == ElementType.Connector) {
				System.out.println(ce.getID() + ": ("
						+ (ce.getX1() + ce.getWidth() / 2) + " , "
						+ (ce.getY1() + ce.getHeight() / 2) + ")    "
						+ ce.getElementType().toString());
			} else if (ce.getElementType() == ElementType.Header) {
				System.out
						.println(ce.getID() + ": (" + ce.getX2() + " , "
								+ ce.getY2() + ")    "
								+ ce.getElementType().toString());
			}
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
							&& temp.get(i).getConnectedSon().getFlag() != 2)
						tempSet.add(temp.get(i).getConnectedSon());
				}
			}
		} else if (currentElement.getElementType() == ElementType.Connector) {
			ArrayList<CanvasElement> listofRelation = getAllRelation(currentElement);
			for (CanvasElement ce : listofRelation) {
				if (ce.connectedOwner
						&& ce.getConnectedOwner().connectedOwner
						&& ce.getConnectedOwner().getConnectedOwner().getFlag() != 2) {
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
				return c1.getDegree() - c2.getDegree();
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
}
