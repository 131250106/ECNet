package cn.edu.nju.ecm.view.table;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.print.PrinterException;
import java.text.MessageFormat;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import cn.edu.nju.ecm.canvas.model.CanvasElement;
import cn.edu.nju.ecm.canvas.model.CanvasElement.ElementType;
import cn.edu.nju.ecm.canvas.model.entity.ConnectorModel;
import cn.edu.nju.ecm.canvas.model.entity.EBodyModel;
import cn.edu.nju.ecm.canvas.model.entity.EHeaderModel;
import cn.edu.nju.ecm.canvas.model.entity.HRelationModel;
import cn.edu.nju.ecm.view.CanvasPanel;
import cn.edu.nju.ecm.view.ECMMainFrame;

public class MyJScrollTable extends JPanel {
	private static final long serialVersionUID = 1L;

	private JTable tableView;

	private JLabel title;
	private CanvasPanel canvasPanel;

	private Object[][] data;
	private TableModel dataModel;
	private String[] names = { "图元类型", "ID(唯一标识符)", "名称", "OwnerId", "操作" };

	public MyJScrollTable(CanvasPanel canvasPanel) {
		this.canvasPanel = canvasPanel;

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
		title = new JLabel(canvasPanel.model.getTitle());
		title.setFont(new java.awt.Font("宋体", 0, 24));
		top.add(title, BorderLayout.CENTER);
		top.add(printButton, BorderLayout.EAST);

		this.add(top, BorderLayout.NORTH);
	}

	public JScrollPane createTable() {
		tableView = new JTable();
		loadData();
		JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.addItem("链体");
		comboBox.addItem("链头");
		comboBox.addItem("连结点");
		comboBox.addItem("箭头");

		TableColumn typeColumn = tableView.getColumn("图元类型");
		typeColumn.setCellEditor(new DefaultCellEditor(comboBox));

		DefaultTableCellRenderer tcrLeft = new DefaultTableCellRenderer();
		tcrLeft.setHorizontalAlignment(JLabel.LEFT);
		tableView.getColumn("ID(唯一标识符)").setCellRenderer(tcrLeft);

		tableView.setRowHeight(30);

		tableView.getColumnModel().getColumn(4)
				.setCellEditor(new MyButtonEditor(canvasPanel));

		tableView.getColumnModel().getColumn(4)
				.setCellRenderer(new MyButtonRenderer());

		tableView.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				int row = tableView.getSelectedRow();
				if (row == -1 || row == canvasPanel.model.getElements().size()) {
					ECMMainFrame.resetElementInfo();
				} else
					ECMMainFrame.showElementInfo(canvasPanel.model
							.getElementByID((int) data[row][1]));
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		JScrollPane scrollpane = new JScrollPane(tableView);
		return scrollpane;
	}

	private void printTable() {
		MessageFormat headerFmt;
		MessageFormat footerFmt;

		JTable.PrintMode printMode = JTable.PrintMode.FIT_WIDTH;

		String text;
		text = canvasPanel.model.getTitle();
		if (text != null && text.length() > 0) {
			headerFmt = new MessageFormat(text);
		} else {
			headerFmt = null;
		}

		text = canvasPanel.model.getDescription();
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
					"TablePrint.printingResult", JOptionPane.ERROR_MESSAGE);
		} catch (SecurityException se) {
			String errorMessage = MessageFormat.format(
					"TablePrint.printingFailed",
					new Object[] { se.getMessage() });
			JOptionPane.showMessageDialog(tableView.getParent(), errorMessage,
					"TablePrint.printingResult", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void changeTitle() {
		title.setText(canvasPanel.model.getTitle());
	}

	private void loadData() {
		data = getModelData();

		dataModel = new AbstractTableModel() {
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
				if (col == 1)
					return false;
				if (col == 4 || col == 2)
					return true;
				if (row == data.length - 1)
					return true;
				if (col == 3 && !((String) data[row][3]).equals(""))
					return true;
				return false;
			}

			public void setValueAt(Object aValue, int row, int column) {
				data[row][column] = aValue;
			}
		};

		tableView.setModel(dataModel);
	}

	public void reloadData() {
		data = getModelData();
		tableView.repaint();
	}

	private Object[][] getModelData() {
		List<CanvasElement> elements = canvasPanel.model.getElements();
		Object[][] data = new Object[elements.size() + 1][5];
		for (int i = 0; i < elements.size(); i++) {
			if (elements.get(i).getElementType() == ElementType.Body) {
				data[i][0] = "链体";
				data[i][1] = elements.get(i).getID();
				data[i][2] = ((EBodyModel) elements.get(i)).geteBody()
						.getName();
				data[i][3] = "";
			} else if (elements.get(i).getElementType() == ElementType.Header) {
				data[i][0] = "   链头";
				data[i][1] = elements.get(i).getID();
				data[i][2] = ((EHeaderModel) elements.get(i)).geteHeader()
						.getName();
				if (elements.get(i).isConnectedOwner())
					data[i][3] = elements.get(i).getConnectedOwner().getID()
							+ "";
				else
					data[i][3] = " ";
			} else if (elements.get(i).getElementType() == ElementType.Connector) {
				data[i][0] = "       联结点";
				data[i][1] = elements.get(i).getID();
				data[i][2] = ((ConnectorModel) elements.get(i)).gethConnector()
						.getName();
				String temp = "";
				// TODO 暂时没想好怎么做，图表有没有必要显示箭头
				// ArrayList<CanvasElement> relationlist =
				// canvasPanel.model.getFormat()
				// .getAllRelation(elements.get(i));
				// for (CanvasElement ce : relationlist) {
				// temp += (ce.getID() + " ; ");
				// }
				// if (temp.length() >= 3)
				// temp = temp.substring(0, temp.length() - 3);
				// else
				// temp=" ";
				data[i][3] = temp;
			} else if (elements.get(i).getElementType() == ElementType.Relation) {
				data[i][0] = "           箭头";
				data[i][1] = elements.get(i).getID();
				HRelationModel temp = ((HRelationModel) elements.get(i));
				data[i][2] = temp.gethRelation().getName();

				if (temp.isConnectedOwner() && temp.isConnectedSon()) {
					data[i][3] = temp.getConnectedOwner().getID() + "&"
							+ temp.getConnectedSon().getID();
				} else if (temp.isConnectedOwner()) {
					data[i][3] = temp.getConnectedOwner().getID() + "&";
				} else if (temp.isConnectedSon()) {
					data[i][3] = "&" + temp.getConnectedSon().getID();
				} else {
					data[i][3] = "&";
				}
			}
			data[i][4] = "删除,确认修改," + elements.get(i).getID();
		}
		data[elements.size()][0] = "";
		data[elements.size()][1] = "";
		data[elements.size()][2] = "";
		data[elements.size()][3] = "";
		data[elements.size()][4] = "重置,确认新增," + -1;
		return data;
	}

	public Object[][] getData() {
		return data;
	}

	public void ResetTableView() {
		loadData();
		tableView.getColumnModel().getColumn(4)
				.setCellEditor(new MyButtonEditor(canvasPanel));
		tableView.getColumnModel().getColumn(4)
				.setCellRenderer(new MyButtonRenderer());
	}
}
