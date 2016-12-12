package cn.edu.nju.ecm.utility;

import java.util.Stack;

public class Undotooler {			//��������������
	private Stack<UndoCommand> undolist = new Stack<UndoCommand>();
	
	public void pushUndoCommand(UndoCommand undo){
		undolist.push(undo);
	}
	public UndoCommand popUndoCommand(){
		if(!undolist.isEmpty())
			return undolist.pop();
		return null;
	}
}
