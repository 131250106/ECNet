package cn.edu.nju.ecm.view.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class MyButtonRenderer  implements TableCellRenderer {
    private JPanel panel;


	private JButton delete;
	
	private JButton modify;
	
	private int row=-1;
	
	private int ID=-1;
    
    public MyButtonRenderer() {
        initButton();

        initPanel();
    }

    private void initButton() {
    	delete = new JButton();
    	modify = new JButton();
    }

    private void initPanel() {
        panel = new JPanel();

        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5,0));
		
		panel.add(this.delete);
		panel.add(this.modify);
		
		panel.setBackground(Color.white);
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
    	String data[] = ((String) value).split(",");
		delete.setText(value == null ? "" : data[0]);
		modify.setText(value == null ? "" : data[1]);
		this.ID = (value == null ? -1 : Integer.parseInt(data[2]));
		this.row = row;
        return panel;
    }

}

