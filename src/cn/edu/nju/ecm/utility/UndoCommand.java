package cn.edu.nju.ecm.utility;

import java.util.ArrayList;

import cn.edu.nju.ecm.canvas.model.CanvasElement;

public class UndoCommand {
	
	private ArrayList<CanvasElement> elementlist; //被操作的图元对象集合（只有format的时候会用到）
	
	private CanvasElement element;		//被操作的图元对象
	public enum ActionType{
		Move, New, Delete, Format, MoveAll
	}
	private ActionType type;			//命令模式
	
	public UndoCommand(ArrayList<CanvasElement> elementlist ,ActionType type){
		this.type = type;
		this.elementlist = elementlist;
	}
	
	
	public UndoCommand(CanvasElement element,ActionType type){
		this.type = type;
		this.element = element;
	}

	public CanvasElement getElement() {
		return element;
	}

	public void setElement(CanvasElement element) {
		this.element = element;
	}

	public ActionType getType() {
		return type;
	}

	public void setType(ActionType type) {
		this.type = type;
	}

	public ArrayList<CanvasElement> getElementlist() {
		return elementlist;
	}
	
}
