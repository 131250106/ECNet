package cn.edu.nju.ecm.utility;

import java.util.Stack;

public class Undotooler {			//��������������
	private static Stack<undoCommand> undolist = new Stack<undoCommand>();
	
	public static void pushUndoCommand(undoCommand undo){
		undolist.push(undo);
	}
	public static undoCommand popUndoCommand(){
		if(!undolist.isEmpty())
			return undolist.pop();
		return null;
	}
}
