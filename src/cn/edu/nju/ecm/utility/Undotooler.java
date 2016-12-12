package cn.edu.nju.ecm.utility;

import java.util.Stack;

public class Undotooler {			//撤销操作工具类
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
