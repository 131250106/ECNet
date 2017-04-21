package cn.edu.nju.ecm.view.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import javax.swing.table.TableCellRenderer;

public class TableCellTextAreaRenderer extends JTextArea implements
		TableCellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TableCellTextAreaRenderer() {
		setLineWrap(true);
		setWrapStyleWord(true);
		setBorder(new LineBorder(Color.LIGHT_GRAY, 1, false));
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		// 计算当下行的最佳高度
		int maxPreferredHeight = 0;
		for (int i = 0; i < table.getColumnCount(); i++) {
			setText("" + table.getValueAt(row, i));
			setSize(table.getColumnModel().getColumn(column).getWidth(), 0);
			if(i!=2){
				maxPreferredHeight = Math.max(maxPreferredHeight,
						45);
			}
		}

		if (table.getRowHeight(row) < maxPreferredHeight) // 少了这行则处理器瞎忙
			table.setRowHeight(row, maxPreferredHeight);

		setText(value == null ? "" : value.toString());
		return this;
	}
}
