package cn.edu.nju.ecm.utility;

import cn.edu.nju.ecm.canvas.model.CanvasElement;

public class undoCommand {
	private CanvasElement element;		//��������ͼԪ����
	public enum ActionType{
		Move, New, Delete
	}
	private ActionType type;			//����ģʽ
	
	public undoCommand(CanvasElement element,ActionType type){
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
