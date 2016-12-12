package cn.edu.nju.ecm.utility;

import java.util.ArrayList;

import cn.edu.nju.ecm.canvas.model.CanvasElement;

public class UndoCommand {
	
	private ArrayList<CanvasElement> elementlist; //��������ͼԪ���󼯺ϣ�ֻ��format��ʱ����õ���
	
	private CanvasElement element;		//��������ͼԪ����
	public enum ActionType{
		Move, New, Delete, Format, MoveAll
	}
	private ActionType type;			//����ģʽ
	
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
