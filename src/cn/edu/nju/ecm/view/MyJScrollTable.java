package cn.edu.nju.ecm.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.text.MessageFormat;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import cn.edu.nju.ecm.canvas.model.ECModel;

public class MyJScrollTable extends JPanel{
	private static final long serialVersionUID = 1L;
	
	private JTable tableView;
	
	private JLabel title;
	private ECModel model;
	
	
	public MyJScrollTable(ECModel model){
		this.model = model;
		
		setLayout(new BorderLayout());

		this.add(createTable(), BorderLayout.CENTER);

		JPanel top = new JPanel();
		top.setLayout(new BorderLayout());
		JButton printButton = new JButton("打印");
		printButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				printTable();
			}
		});
		title = new JLabel(model.getTitle()) ;
		title.setFont(new java.awt.Font("宋体", 0, 24));
		top.add(title,BorderLayout.CENTER);
		top.add(printButton, BorderLayout.EAST);
		
		this.add(top, BorderLayout.NORTH);
	}
	
	public JScrollPane createTable() {

		// final
		final String[] names = { "图元类型", "ID(唯一标识符)", "名称", "内容" };

		// Create the dummy data (a few rows of names)
		final Object[][] data = {
				{ "Mike", "Albers", "hahaahaa",
						new Double(44.0) },
				{ "Mark", "Andrews", "TablePrint.curse",
						new Double(3) },
				{ "Brian", "Beck", "TablePrint.bluesbros",
						new Double(2.7182818285) },
				{ "Lara", "Bunni", "TablePrint.airplane",
						new Double(15) },
				{ "Roger", "Brinkley", "TablePrint.man",
						new Double(13) },
				{ "Brent", "Christian", "TablePrint.bladerunner",
						new Double(23) },
				{ "Mark", "Davidson", "TablePrint.brazil",
						new Double(27) },
				{ "Jeff", "Dinkins", "TablePrint.ladyvanishes",
						new Double(8) },
				{ "Ewan", "Dinkins", "TablePrint.bugs", new Double(2) },
				{ "Amy", "Fowler", "TablePrint.reservoir",
						new Double(3) },
				{ "Hania", "Gajewska", "TablePrint.jules",
						new Double(5) },
				{ "David", "Geary", "TablePrint.pulpfiction",
						new Double(3) },

				{ "Eric", "Hawkes", "TablePrint.bladerunner",
						new Double(.693) },
				{ "Brent", "Christian", "TablePrint.bladerunner",
						new Double(23) },
				{ "Mark", "Davidson", "TablePrint.brazil",
						new Double(27) },
				{ "Jeff", "Dinkins", "TablePrint.ladyvanishes",
						new Double(8) },
				{ "Ewan", "Dinkins", "TablePrint.bugs", new Double(2) },
				{ "Amy", "Fowler", "TablePrint.reservoir",
						new Double(3) },
				{ "Hania", "Gajewska", "TablePrint.jules",
						new Double(5) },
				{ "David", "Geary", "TablePrint.pulpfiction",
						new Double(3) },

				{ "Eric", "Hawkes", "TablePrint.bladerunner",
						new Double(.693) },
				{ "Brent", "Christian", "TablePrint.bladerunner",
						new Double(23) },
				{ "Mark", "Davidson", "TablePrint.brazil",
						new Double(27) },
				{ "Jeff", "Dinkins", "TablePrint.ladyvanishes",
						new Double(8) },
				{ "Ewan", "Dinkins", "TablePrint.bugs", new Double(2) },
				{ "Amy", "Fowler", "TablePrint.reservoir",
						new Double(3) },
				{ "Hania", "Gajewska", "TablePrint.jules",
						new Double(5) },
				{ "David", "Geary", "TablePrint.pulpfiction",
						new Double(3) },

				{ "Eric", "Hawkes", "TablePrint.bladerunner",
						new Double(.693) },
				{ "Shannon", "Hickey", "TablePrint.shawshank",
						new Double(2) } };

		// Create a model of the data.
		TableModel dataModel = new AbstractTableModel() {
			private static final long serialVersionUID = 1L;

			public int getColumnCount() {
				return names.length;
			}

			public int getRowCount() {
				return data.length;
			}

			public Object getValueAt(int row, int col) {
				return data[row][col];
			}

			public String getColumnName(int column) {
				return names[column];
			}

			public Class<? extends Object> getColumnClass(int c) {
				return getValueAt(0, c).getClass();
			}

			public boolean isCellEditable(int row, int col) {
				return col != 5;
			}

			public void setValueAt(Object aValue, int row, int column) {
				data[row][column] = aValue;
			}
		};

		tableView = new JTable(dataModel);

		JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.addItem("链体");
		comboBox.addItem("链头");
		comboBox.addItem("连结点");

		TableColumn colorColumn = tableView.getColumn("图元类型");
		colorColumn.setCellEditor(new DefaultCellEditor(comboBox));

		tableView.setRowHeight(25);

		JScrollPane scrollpane = new JScrollPane(tableView);
		return scrollpane;
	}

	private void printTable() {
		MessageFormat headerFmt;
		MessageFormat footerFmt;

		JTable.PrintMode printMode = JTable.PrintMode.FIT_WIDTH;

		String text;
		text = "this is a test";
		if (text != null && text.length() > 0) {
			headerFmt = new MessageFormat(text);
		} else {
			headerFmt = null;
		}

		text = "this is a test 1";
		if (text != null && text.length() > 0) {
			footerFmt = new MessageFormat(text);
		} else {
			footerFmt = null;
		}

		try {
			boolean status = tableView.print(printMode, headerFmt, footerFmt);

			if (status) {
				JOptionPane.showMessageDialog(tableView.getParent(),
						"TablePrint.printingComplete",
						"TablePrint.printingResult",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(tableView.getParent(),
						"TablePrint.printingCancelled",
						"TablePrint.printingResult",
						JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (PrinterException pe) {
			String errorMessage = MessageFormat.format(
					"TablePrint.printingFailed",
					new Object[] { pe.getMessage() });
			JOptionPane.showMessageDialog(tableView.getParent(), errorMessage,
					"TablePrint.printingResult",
					JOptionPane.ERROR_MESSAGE);
		} catch (SecurityException se) {
			String errorMessage = MessageFormat.format(
					"TablePrint.printingFailed",
					new Object[] { se.getMessage() });
			JOptionPane.showMessageDialog(tableView.getParent(), errorMessage,
					"TablePrint.printingResult",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void changeTitle(){
		title.setText(model.getTitle());
	}
}
