package cn.edu.nju.ecm.utility;

import cn.edu.nju.ecm.canvas.model.CanvasElement;

public class UndoCommand {
	private CanvasElement element;		//被操作的图元对象
	public enum ActionType{
		Move, New, Delete
	}
	private ActionType type;			//命令模式
	
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
	
	
}
