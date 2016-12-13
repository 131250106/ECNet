package cn.edu.nju.ecm.view.table;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.table.JTableHeader;

public class MyHeaderUI extends BasicTableHeaderUI {
	private JTableHeader header;
	private JTable table;
	private String title;
	private String[] names;

	public MyHeaderUI(JTable table, String title, String[] names) {
		this.table = table;
		this.title = title;
		this.names = names;
	}

	public void paint(Graphics g, JComponent c) {
		header = (JTableHeader) c;
		table.getTableHeader().setPreferredSize(
				new Dimension(table.getWidth(), 50));// ���ñ�ͷ��С������������㹻��
		JLabel label = getLabel(title);
		label.setFont(new Font("Dialog", Font.PLAIN, 20));
		rendererPane.paintComponent(g, label, header, 0, 0, table.getWidth(),
				25, true);
		for (int i = 0; i < names.length; i++) {
			label = getLabel(names[i]);
			rendererPane.paintComponent(g, label, header, getX(i), 25,
					getWidth(i), 25, true);
		}
	}

	// �õ�ָ���е���ʼ����
	private int getX(int column) {
		int x = 0;
		for (int i = 0; i < column; i++)
			x += header.getColumnModel().getColumn(i).getWidth();
		return x;
	}

	// �õ�ָ���еĿ��
	private int getWidth(int column) {
		return header.getColumnModel().getColumn(column).getWidth();
	}

	// �õ�����ָ���ı��ı�ǩ
	private JLabel getLabel(String text) {
		JLabel label = new JLabel(text, JLabel.CENTER);
		label.setFont(new Font("Dialog", Font.PLAIN, 16));
		// label.setBorder(new LineBorder(Color.GRAY,1,false));
		label.setBorder(BorderFactory.createEtchedBorder());
		return label;
	}

}
