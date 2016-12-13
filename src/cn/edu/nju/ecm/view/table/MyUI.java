package cn.edu.nju.ecm.view.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicTableUI;
import javax.swing.table.TableCellRenderer;

public class MyUI extends BasicTableUI {
	public void paint(Graphics g, JComponent c) {
		Rectangle r = g.getClipBounds();
		int firstCol = table.columnAtPoint(new Point(r.x, 0));
		int lastCol = table.columnAtPoint(new Point(r.x + r.width, 0));
		if (lastCol < 0)
			lastCol = table.getColumnCount() - 1;
		for (int i = firstCol; i <= lastCol; i++)
			paintCol(i, g);
	}

	private void paintCol(int col, Graphics g) {
		Rectangle r = g.getClipBounds();
		for (int i = 0; i < table.getRowCount(); i++) {
			Rectangle r1 = table.getCellRect(i, col, true);
			if (r1.intersects(r)) // at least a part is visible
			{
				int sk = ((MyJtabel) table).map.visibleCell(i, col);
				paintCell(sk, col, g, r1);
				// increment the column counter
				i += ((MyJtabel) table).map.span(sk, col) - 1;
			}
		}
	}

	private void paintCell(int row, int column, Graphics g, Rectangle area) {
		int verticalMargin = table.getRowMargin();
		int horizontalMargin = table.getColumnModel().getColumnMargin();
		Color c = g.getColor();
		g.setColor(table.getGridColor());
		g.drawRect(area.x, area.y, area.width - 1, area.height - 1);
		g.setColor(c);
		area.setBounds(area.x + horizontalMargin / 2, area.y + verticalMargin
				/ 2, area.width - horizontalMargin, area.height
				- verticalMargin);
		if (table.isEditing() && table.getEditingRow() == row
				&& table.getEditingColumn() == column) {
			Component component = table.getEditorComponent();
			component.setBounds(area);
			component.validate();
		} else {
			TableCellRenderer renderer = table.getCellRenderer(row, column);
			Component component = table.prepareRenderer(renderer, row, column);
			if (component.getParent() == null)
				rendererPane.add(component);
			rendererPane.paintComponent(g, component, table, area.x, area.y,
					area.width, area.height, true);
		}
	}
}