package cn.edu.nju.ecm.utility;

import java.util.Stack;

public class Undotooler {			//撤销操作工具类
	private static Stack<UndoCommand> undolist = new Stack<UndoCommand>();
	
	public static void pushUndoCommand(UndoCommand undo){
		undolist.push(undo);
	}
	public static UndoCommand popUndoCommand(){
		if(!undolist.isEmpty())
			return undolist.pop();
		return null;
	}
}
