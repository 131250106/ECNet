package cn.edu.nju.ecm.canvas.model;

import java.awt.Rectangle;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;





import cn.edu.nju.ecm.canvas.model.CanvasElement.ElementType;
import cn.edu.nju.ecm.canvas.model.format.MyFormat;
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
	
	private MyFormat format;
	
	private Undotooler Undotooler;

	public ECModel(Undotooler Undotooler) {
		this.elements = new ArrayList<CanvasElement>();
		format = new MyFormat(elements);
		this.Undotooler = Undotooler;
	}

	public ECModel(String title, String description, File file,Undotooler Undotooler) {
		this(Undotooler);
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
	
	public void insertTableElement(CanvasElement element) {
		this.maxIDNumber++;
		element.setID(this.maxIDNumber);
		this.getElements().add(0, element);
	}

	public boolean deleteElement(int ID, boolean isUndo) {
		CanvasElement element = this.getElementByID(ID);
		if (element != null) {
			element.resetConnectedOwner();
			element.resetConnectedSon();
			if(element.getElementType()==ElementType.Body){
				for(CanvasElement ce:element.getConnectedOutputs()){
					ce.setConnectedOwner(false);
				}
				element.getConnectedOutputs().clear();
			}else if(element.getElementType()==ElementType.Connector){
				for(CanvasElement ce:element.getConnectedInputs()){
					ce.setConnectedSon(false);
				}
				element.getConnectedInputs().clear();
			}
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
							element.resetConnectedPointOwner(ce);
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
							element.resetConnectedPointOwner(ce);
							element.setConnectedOwner(ce);
						}
						ce.setChoosed(false);

					} else if (ce.getElementType() == ElementType.Connector) {
						if (ce.pointWithInMe(element.getX2(), element.getY2())) {
							element.resetConnectedPointSon(ce);
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
	
	public void reSetAllElements(){
		for (CanvasElement ce : this.elements) {
			updateConnectable(ce);
			reSetConnected(ce);
		}
	}

	public void autoFormat() {
		format.autoFormat();
		for (CanvasElement ce : this.elements) {
			for (CanvasElement connectedRelation : ce
					.getConnectedOutputs()) {
				connectedRelation.resetConnectedPointOwner(ce);
			}
			for (CanvasElement connectedRelation : ce.getConnectedInputs()) {
				connectedRelation.resetConnectedPointSon(ce);
			}
		}
	}

	public MyFormat getFormat() {
		return format;
	}

	public List<CanvasElement> getSortedElementsByTable() {			//根据表格所需要的格式排序的elements
		return format.getSortedElementsByTable();
	}

	public ArrayList<CanvasElement> chooseElementList(int pressX, int pressY, int currentX, int currentY) {			//选择矩形中所有元素
		ArrayList<CanvasElement> result = new ArrayList<CanvasElement>();
		int x1 = pressX;
		int y1 = pressY;
		int x2 = currentX;
		int y2 = currentY;
		if(pressX>currentX){
			x1 = currentX;
			x2 = pressX;
		}
		if(pressY>currentY){
			y1=currentY;
			y2=pressY;
		}
		Rectangle temp = new Rectangle(x1,y1,x2-x1,y2-y1);
		for(CanvasElement ce:this.elements){
			Rectangle rectangle = new Rectangle();
			if(ce.getElementType()==ElementType.Header){
				rectangle.setBounds(ce.getX2()-ce.getWidth(), ce.getY2()-ce.getWidth(), ce.getWidth()*2, ce.getWidth()*2); 
			}else if(ce.getElementType()==ElementType.Connector||ce.getElementType()==ElementType.Body){
				rectangle.setBounds(ce.getX1(), ce.getY1(), ce.getWidth(), ce.getHeight());
			}else if(ce.getElementType()==ElementType.Relation){
				int tempx1 = ce.getX1();
				int tempy1 = ce.getY1();
				if(tempx1>ce.getX2()){
					tempx1 = ce.getX2();
				}
				if(tempy1>ce.getY2()){
					tempy1=ce.getY2();
				}
				rectangle.setBounds(tempx1, tempy1, Math.abs(ce.getX1()-ce.getX2())+1, Math.abs(ce.getY1()-ce.getY2())+1);
			}
			if(temp.intersects(rectangle)){
				result.add(ce);
			}
		}
		return result;
	}

	public List<CanvasElement> getAllBodys() {
		ArrayList<CanvasElement> bodys = new ArrayList<CanvasElement>();
		for(CanvasElement ce:this.elements){
			if(ce.getElementType()==ElementType.Body)
				bodys.add(ce);
		}
		return bodys;
	}

	public List<CanvasElement> getAllConnectors() {
		ArrayList<CanvasElement> bodys = new ArrayList<CanvasElement>();
		for(CanvasElement ce:this.elements){
			if(ce.getElementType()==ElementType.Connector)
				bodys.add(ce);
		}
		return bodys;
	}
}
