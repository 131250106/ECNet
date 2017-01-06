package cn.edu.nju.ecm.view.table;

import cn.edu.nju.ecm.canvas.model.CanvasElement;
import cn.edu.nju.ecm.canvas.model.ECModel;

public class EvidenceMap implements CMap {
	private Object[][] data;
	private ECModel model;
	
	public EvidenceMap(Object[][] data,ECModel model){
		this.data = data;
		this.model = model;
	}
	public int span(int row, int column) {
		if(row==data.length-1)
			return 1;
		if(isSpan(column)){
			int id = (int)data[row][0];
			CanvasElement body = model.getElementByID(id);
			return body.getDegree()+1;
		}
		return 1;
	}

	public int visibleCell(int row, int column) {
		if(row==data.length-1)
			return row;
		if(row>0&&span(row, column)==span(row-1, column)&&isSpan(column)){
			int id1 = (int)data[row-1][0];
			int id2 = (int)data[row][0];
			if(id1==id2)
				return visibleCell(row-1, column);
		}
		return row;
	}
	
	private boolean isSpan(int column){
		if (column == 0 || column == 1|| column == 2|| column == 3|| column == 4|| column == 5|| column == 6)
			return true;
		return false;
	}
}
