package cn.edu.nju.ecm.canvas.model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.edu.nju.ecm.canvas.model.CanvasElement.ElementType;
import cn.edu.nju.ecm.utility.Undotooler;
import cn.edu.nju.ecm.utility.undoCommand;
import cn.edu.nju.ecm.utility.undoCommand.ActionType;

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

	public void insertNewWElement(CanvasElement element,boolean isUndo) {
		if(!isUndo){					//如果不是由于撤销操作引起的则增加undo记录
			this.maxIDNumber++;
			element.setID(this.maxIDNumber);
			Undotooler.pushUndoCommand(new undoCommand(element,ActionType.New));
		}
		this.getElements().add(0, element);
	}

	public boolean deleteElement(int ID, boolean isUndo) {
		CanvasElement element = this.getElementByID(ID);
		if (element != null) {
			this.getElements().remove(element);
			if (!isUndo) 			// 如果不是由于撤销操作引起的则增加undo记录
				Undotooler.pushUndoCommand(new undoCommand(element,
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
		}  else if (element.getElementType() == ElementType.Body) {
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
}
