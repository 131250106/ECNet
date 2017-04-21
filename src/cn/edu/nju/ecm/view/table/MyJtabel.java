package cn.edu.nju.ecm.view.table;

import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JTable;


public class MyJtabel extends JTable {
	private static final long serialVersionUID = 1L;
	
	public CMap map;

	public MyJtabel(CMap cmp) {
		map = cmp;
		setUI(new MyUI());
	}

	public Rectangle getCellRect(int row, int column, boolean includeSpacing) {
		if (map == null)
			return super.getCellRect(row, column, includeSpacing);
		int sk = map.visibleCell(row, column);
		Rectangle r1 = super.getCellRect(sk, column, includeSpacing);
		if (map.span(sk, column) != 1)
			for (int i = 1; i < map.span(sk, column); i++) {
				r1.height += this.getRowHeight(sk + i);
			}
//		r1.height = Math.max(r1.height,
//				((TableCellTextAreaRenderer)getDefaultRenderer(Object.class)).getPreferredSize().height);
		return r1;
	}

	public int rowAtPoint(Point p) {
		int x = super.columnAtPoint(p);
		if (x < 0)
			return x;
		int y = super.rowAtPoint(p);
		return map.visibleCell(y, x);
	}

	public CMap getMap() {
		return map;
	}

	public void setMap(CMap map) {
		this.map = map;
	}
}