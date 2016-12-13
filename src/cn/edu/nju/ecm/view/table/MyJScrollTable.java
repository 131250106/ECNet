package cn.edu.nju.ecm.view.table;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.print.PrinterException;
import java.text.MessageFormat;
import java.util.ArrayList;
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

	private JTable tableViewOfEvidence;
	private JTable tableViewOfFact;

	private JLabel title;
	private CanvasPanel canvasPanel;

	private Object[][] data;
	private TableModel dataModel;
	private MyButtonEditor myButtonEditor;
	private MyButtonRenderer myButtonRenderer;

	private String[] namesOfEvidence = { "证据序号", "证据名称", "证据明细", "证据种类", "提交人", "质证理由", "质证结论", "链头信息", "证据中的关键文本（短句","操作"};
	private String[] namesOfFact = { "事实序号", "事实名称", "事实明细", "链头信息", "证据序号", "证据中的关键文本（短句）"};

	public MyJScrollTable(CanvasPanel canvasPanel) {
		this.canvasPanel = canvasPanel;

		setLayout(new BorderLayout());

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
		
		this.add(createEvidenceTable(), BorderLayout.CENTER);
		this.add(createFactTable(), BorderLayout.SOUTH);
		ResetTableView();

	}

	public JScrollPane createEvidenceTable() {
		tableViewOfEvidence = new JTable();

		myButtonEditor = new MyButtonEditor(canvasPanel);
		myButtonRenderer = new MyButtonRenderer();

		tableViewOfEvidence.setRowHeight(30);

		tableViewOfEvidence.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				int row = tableViewOfEvidence.getSelectedRow();
				if (row == -1 || row == data.length - 1) {
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
		tableViewOfEvidence.getTableHeader().setUI(new MyHeaderUI(tableViewOfEvidence,"证据清单",namesOfEvidence));
		JScrollPane scrollpane = new JScrollPane(tableViewOfEvidence);
		return scrollpane;
	}
	
	public JScrollPane createFactTable() {
		tableViewOfFact = new JTable();

		tableViewOfFact.setRowHeight(30);

		tableViewOfFact.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				int row = tableViewOfFact.getSelectedRow();
				if (row == -1 || row == data.length - 1) {
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
		tableViewOfFact.getTableHeader().setUI(new MyHeaderUI(tableViewOfFact,"事实清单",namesOfFact));
		JScrollPane scrollpane = new JScrollPane(tableViewOfFact);
		return scrollpane;
	}

	public void changeTitle() {
		title.setText(canvasPanel.model.getTitle());
	}
	
	private void loadData() {
		data = getModelData();

		dataModel = new AbstractTableModel() {
			private static final long serialVersionUID = 1L;

			public int getColumnCount() {
				return namesOfEvidence.length;
			}

			public int getRowCount() {
				return data.length;
			}

			public Object getValueAt(int row, int col) {
				return data[row][col];
			}

			public String getColumnName(int column) {
				return namesOfEvidence[column];
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

		tableViewOfEvidence.setModel(dataModel);
		tableViewOfFact.setModel(dataModel);
	}

	private Object[][] getModelData() {
		List<CanvasElement> elements = canvasPanel.model
				.getSortedElementsByTable();
		Object[][] data = new Object[elements.size() + 1][10];
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
				data[i][0] = "      联结点";
				data[i][1] = elements.get(i).getID();
				data[i][2] = ((ConnectorModel) elements.get(i)).gethConnector()
						.getName();
				String temp = "";
				// 暂定不显示箭头，联结点直接显示所链接的链头ID
				ArrayList<CanvasElement> headlist = canvasPanel.model
						.getFormat().getAllHeader(elements.get(i));
				for (CanvasElement ce : headlist) {
					temp += (ce.getID() + " ; ");
				}
				if (temp.length() >= 3)
					temp = temp.substring(0, temp.length() - 3);
				else
					temp = " ";
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
			data[i][4]="删除,确认修改," + elements.get(i).getID();
			data[i][5]="";
			data[i][6]="";
			data[i][7]="";
			data[i][8]="";
			data[i][9] = "删除,确认修改," + elements.get(i).getID();
		}
		data[elements.size()][0] = "";
		data[elements.size()][1] = "";
		data[elements.size()][2] = "";
		data[elements.size()][3] = "";
		data[elements.size()][4] = "重置,确认新增," + -1;
		data[elements.size()][5]="";
		data[elements.size()][6]="";
		data[elements.size()][7]="";
		data[elements.size()][8]="";
		data[elements.size()][9] = "删除,确认新增," + -1;
		return data;
	}

	public Object[][] getData() {
		return data;
	}

	public void reloadData() {							//只需要重新读一次数据就行
		data = getModelData();
		tableViewOfEvidence.repaint();
		tableViewOfFact.repaint();
	}

	public void ResetTableView() {						//需要重新reset布局
		loadData();
		tableViewOfEvidence.getColumnModel().getColumn(4).setCellEditor(myButtonEditor);

		tableViewOfEvidence.getColumnModel().getColumn(4)
				.setCellRenderer(myButtonRenderer);

//		TableColumn typeColumn = tableView.getColumn("图元类型");
//		typeColumn.setCellEditor(new DefaultCellEditor(comboBox));
//
//		DefaultTableCellRenderer tcrLeft = new DefaultTableCellRenderer();
//		tcrLeft.setHorizontalAlignment(JLabel.LEFT);
//		tableView.getColumn("ID(唯一标识符)").setCellRenderer(tcrLeft);
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
			boolean status = tableViewOfEvidence.print(printMode, headerFmt, footerFmt);

			if (status) {
				JOptionPane.showMessageDialog(tableViewOfEvidence.getParent(),
						"TablePrint.printingComplete",
						"TablePrint.printingResult",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(tableViewOfEvidence.getParent(),
						"TablePrint.printingCancelled",
						"TablePrint.printingResult",
						JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (PrinterException pe) {
			String errorMessage = MessageFormat.format(
					"TablePrint.printingFailed",
					new Object[] { pe.getMessage() });
			JOptionPane.showMessageDialog(tableViewOfEvidence.getParent(), errorMessage,
					"TablePrint.printingResult", JOptionPane.ERROR_MESSAGE);
		} catch (SecurityException se) {
			String errorMessage = MessageFormat.format(
					"TablePrint.printingFailed",
					new Object[] { se.getMessage() });
			JOptionPane.showMessageDialog(tableViewOfEvidence.getParent(), errorMessage,
					"TablePrint.printingResult", JOptionPane.ERROR_MESSAGE);
		}
	}

}
