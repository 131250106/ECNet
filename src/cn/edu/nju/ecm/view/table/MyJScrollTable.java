package cn.edu.nju.ecm.view.table;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import cn.edu.nju.ecm.canvas.model.CanvasElement;
import cn.edu.nju.ecm.canvas.model.entity.ConnectorModel;
import cn.edu.nju.ecm.canvas.model.entity.EBodyModel;
import cn.edu.nju.ecm.canvas.model.entity.EHeaderModel;
import cn.edu.nju.ecm.view.CanvasPanel;

public class MyJScrollTable extends JPanel {
	private static final long serialVersionUID = 1L;

	private MyJtabel tableViewOfEvidence;
	private MyJtabel tableViewOfFact;

	private JLabel title;
	private CanvasPanel canvasPanel;

	private Object[][] dataOfEvidence;
	private Object[][] dataOfFact;

	private MyButtonEditor myButtonEditor;
	private MyButtonRenderer myButtonRenderer;

	private String[] namesOfEvidence = { "证据序号", "证据名称", "证据明细", "证据种类", "提交人",
			"质证理由", "质证结论", "链头信息", "证据中的关键文本（短句", "操作" };
	private String[] namesOfFact = { "事实序号", "事实名称", "事实明细", "链头信息", "证据序号",
			"证据中的关键文本（短句）", "操作" };

	private JScrollPane Evidencescrollpane;
	private JScrollPane Factscrollpane;

	public MyJScrollTable(CanvasPanel canvasPanel) {
		this.canvasPanel = canvasPanel;

		setLayout(new BorderLayout());

		JPanel top = new JPanel();
		top.setLayout(new BorderLayout());

		JButton ExportButton = new JButton("导出");
		ExportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					exportExcle();
				} catch (IOException e) {
					JOptionPane.showMessageDialog(getParent(),
							e.getLocalizedMessage(), "文件无法保存",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		JButton printButton = new JButton("打印");
		printButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				printTable();
			}
		});
		title = new JLabel(canvasPanel.model.getTitle());
		title.setFont(new java.awt.Font("宋体", 0, 24));
		top.add(title, BorderLayout.CENTER);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(5));
		buttonPanel.add(ExportButton);
		buttonPanel.add(printButton);
		top.add(buttonPanel, BorderLayout.EAST);

		this.add(top, BorderLayout.NORTH);

		JPanel tableData = new JPanel(); // 用于放置两个表格
		this.add(tableData, BorderLayout.CENTER);

		tableData.setLayout(new BorderLayout());
		tableData.add(createEvidenceTable(), BorderLayout.NORTH);
		tableData.add(createFactTable(), BorderLayout.CENTER);
		ResetTableView();

	}

	public JScrollPane createEvidenceTable() {
		tableViewOfEvidence = new MyJtabel(new EvidenceMap(dataOfEvidence,
				canvasPanel.model));
		tableViewOfEvidence.setDefaultRenderer(Object.class, new TableCellTextAreaRenderer()); 
		myButtonEditor = new MyButtonEditor(canvasPanel);
		myButtonRenderer = new MyButtonRenderer();

		tableViewOfEvidence.setRowHeight(30);
		tableViewOfEvidence.getTableHeader().setUI(
				new MyHeaderUI(tableViewOfEvidence, "证据清单", namesOfEvidence));
		Evidencescrollpane = new JScrollPane(tableViewOfEvidence);
		return Evidencescrollpane;
	}

	public JScrollPane createFactTable() {
		tableViewOfFact = new MyJtabel(new FactMap(dataOfFact,
				canvasPanel.model));
		tableViewOfFact.setDefaultRenderer(Object.class, new TableCellTextAreaRenderer()); 
		tableViewOfFact.setRowHeight(30);
		tableViewOfFact.getTableHeader().setUI(
				new MyHeaderUI(tableViewOfFact, "事实清单", namesOfFact));
		Factscrollpane = new JScrollPane(tableViewOfFact);
		return Factscrollpane;
	}

	public void changeTitle() {
		title.setText(canvasPanel.model.getTitle());
	}

	private void loadData() {
		getModelData();

		TableModel EvidencedataModel = new AbstractTableModel() {
			private static final long serialVersionUID = 1L;

			public int getColumnCount() {
				return namesOfEvidence.length;
			}

			public int getRowCount() {
				return dataOfEvidence.length;
			}

			public Object getValueAt(int row, int col) {
				return dataOfEvidence[row][col];
			}

			public String getColumnName(int column) {
				return namesOfEvidence[column];
			}

			public Class<? extends Object> getColumnClass(int c) {
				return getValueAt(0, c).getClass();
			}

			public boolean isCellEditable(int row, int col) {
				if (col == 0)
					return false;
				return true;
			}

			public void setValueAt(Object aValue, int row, int column) {
				dataOfEvidence[row][column] = aValue;
			}
		};

		TableModel FactdataModel = new AbstractTableModel() {
			private static final long serialVersionUID = 1L;

			public int getColumnCount() {
				return namesOfFact.length;
			}

			public int getRowCount() {
				return dataOfFact.length;
			}

			public Object getValueAt(int row, int col) {
				return dataOfFact[row][col];
			}

			public String getColumnName(int column) {
				return namesOfFact[column];
			}

			public Class<? extends Object> getColumnClass(int c) {
				return getValueAt(0, c).getClass();
			}

			public boolean isCellEditable(int row, int col) {
				if (col == 0)
					return false;
				return true;
			}

			public void setValueAt(Object aValue, int row, int column) {
				dataOfFact[row][column] = aValue;
			}
		};

		tableViewOfEvidence.setModel(EvidencedataModel);
		tableViewOfFact.setModel(FactdataModel);
		FitEvidenceTable(tableViewOfEvidence);
		FitFactTable(tableViewOfFact);
	}

	private void getModelData() {
		List<CanvasElement> bodys = canvasPanel.model.getAllBodys();
		Object[][] data = new Object[canvasPanel.model.getElements().size()
				+ bodys.size() + 1][10];
		int numberOfEvidence = 0;
		for (CanvasElement ce : bodys) {
			ArrayList<CanvasElement> headers = canvasPanel.model.getFormat()
					.getAllHeader(ce);
			for (CanvasElement header : headers) {
				data[numberOfEvidence][0] = ce.getID();
				data[numberOfEvidence][1] = ((EBodyModel) ce).geteBody()
						.getName();
				data[numberOfEvidence][2] = ((EBodyModel) ce).geteBody()
						.getContent();
				data[numberOfEvidence][3] = ((EBodyModel) ce).geteBody()
						.getEvidenceType();
				data[numberOfEvidence][4] = ((EBodyModel) ce).geteBody()
						.getCommiter();
				data[numberOfEvidence][5] = ((EBodyModel) ce).geteBody()
						.getEvidenceReason();
				data[numberOfEvidence][6] = ((EBodyModel) ce).geteBody()
						.getEvidenceConclusion();
				data[numberOfEvidence][7] = ((EHeaderModel) header)
						.geteHeader().getContent();
				data[numberOfEvidence][8] = ((EHeaderModel) header)
						.geteHeader().getKeySentence();
				data[numberOfEvidence][9] = "删除,修改链头," + header.getID();
				numberOfEvidence++;
			}
			data[numberOfEvidence][0] = ce.getID();
			data[numberOfEvidence][1] = ((EBodyModel) ce).geteBody().getName();
			data[numberOfEvidence][2] = ((EBodyModel) ce).geteBody()
					.getContent();
			data[numberOfEvidence][4] = ((EBodyModel) ce).geteBody()
					.getEvidenceType();
			data[numberOfEvidence][3] = ((EBodyModel) ce).geteBody()
					.getCommiter();
			data[numberOfEvidence][5] = ((EBodyModel) ce).geteBody()
					.getEvidenceReason();
			data[numberOfEvidence][6] = ((EBodyModel) ce).geteBody()
					.getEvidenceConclusion();
			data[numberOfEvidence][7] = "";
			data[numberOfEvidence][8] = "";
			data[numberOfEvidence][9] = "修改,新增链头," + ce.getID();
			numberOfEvidence++;
		}
		data[numberOfEvidence][0] = "";
		data[numberOfEvidence][1] = "";
		data[numberOfEvidence][2] = "";
		data[numberOfEvidence][3] = "";
		data[numberOfEvidence][4] = "";
		data[numberOfEvidence][5] = "";
		data[numberOfEvidence][6] = "";
		data[numberOfEvidence][7] = "";
		data[numberOfEvidence][8] = "";
		data[numberOfEvidence][9] = "重置,新增链体,-1";
		numberOfEvidence++;
		dataOfEvidence = new Object[numberOfEvidence][10];
		System.arraycopy(data, 0, dataOfEvidence, 0, dataOfEvidence.length);
		tableViewOfEvidence.setMap(new EvidenceMap(dataOfEvidence,
				canvasPanel.model));

		List<CanvasElement> connectors = canvasPanel.model.getAllConnectors();
		data = new Object[canvasPanel.model.getElements().size()
				+ connectors.size() + 1][7];
		int numberOfFact = 0;
		for (CanvasElement ce : connectors) {
			ArrayList<CanvasElement> headers = canvasPanel.model.getFormat()
					.getAllHeader(ce);
			for (CanvasElement header : headers) {
				data[numberOfFact][0] = ce.getID();
				data[numberOfFact][1] = ((ConnectorModel) ce).gethConnector()
						.getName();
				data[numberOfFact][2] = ((ConnectorModel) ce).gethConnector()
						.getContent();
				data[numberOfFact][3] = ((EHeaderModel) header).geteHeader()
						.getContent();
				data[numberOfFact][4] = "";
				if (header.isConnectedOwner())
					data[numberOfFact][4] = ""
							+ header.getConnectedOwner().getID();
				data[numberOfFact][5] = ((EHeaderModel) header).geteHeader()
						.getKeySentence();
				data[numberOfFact][6] = "删除连接,修改," + header.getID();
				numberOfFact++;
			}
			data[numberOfFact][0] = ce.getID();
			data[numberOfFact][1] = ((ConnectorModel) ce).gethConnector()
					.getName();
			data[numberOfFact][2] = ((ConnectorModel) ce).gethConnector()
					.getContent();
			data[numberOfFact][3] = "";
			data[numberOfFact][4] = "";
			data[numberOfFact][5] = "";
			data[numberOfFact][6] = "修改,新增链头," + ce.getID();
			numberOfFact++;
		}
		data[numberOfFact][0] = "";
		data[numberOfFact][1] = "";
		data[numberOfFact][2] = "";
		data[numberOfFact][3] = "";
		data[numberOfFact][4] = "";
		data[numberOfFact][5] = "";
		data[numberOfFact][6] = "重置,新增联结,-1";
		numberOfFact++;
		dataOfFact = new Object[numberOfFact][7];
		System.arraycopy(data, 0, dataOfFact, 0, dataOfFact.length);
		tableViewOfFact.setMap(new FactMap(dataOfFact, canvasPanel.model));
	}

	public void ResetTableView() { // 需要重新reset布局
		loadData();
		tableViewOfEvidence.getColumnModel().getColumn(9)
				.setCellEditor(myButtonEditor);

		tableViewOfEvidence.getColumnModel().getColumn(9)
				.setCellRenderer(myButtonRenderer);

		tableViewOfFact.getColumnModel().getColumn(6)
				.setCellEditor(myButtonEditor);

		tableViewOfFact.getColumnModel().getColumn(6)
				.setCellRenderer(myButtonRenderer);

		DefaultTableCellRenderer tcrcenter = new DefaultTableCellRenderer();
		tcrcenter.setHorizontalAlignment(JLabel.CENTER);
		tableViewOfEvidence.getColumn("证据序号").setCellRenderer(tcrcenter);
		tableViewOfFact.getColumn("事实序号").setCellRenderer(tcrcenter);

		Evidencescrollpane.setPreferredSize(new Dimension(500,
				(dataOfEvidence.length + 2) * 30 + 5));
		Factscrollpane.setPreferredSize(new Dimension(500,
				(dataOfFact.length + 2) * 30 + 5));
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
			boolean status = tableViewOfEvidence.print(printMode, headerFmt,
					footerFmt);
			status &= tableViewOfFact.print(printMode, headerFmt, footerFmt);
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
			JOptionPane.showMessageDialog(tableViewOfEvidence.getParent(),
					errorMessage, "TablePrint.printingResult",
					JOptionPane.ERROR_MESSAGE);
		} catch (SecurityException se) {
			String errorMessage = MessageFormat.format(
					"TablePrint.printingFailed",
					new Object[] { se.getMessage() });
			JOptionPane.showMessageDialog(tableViewOfEvidence.getParent(),
					errorMessage, "TablePrint.printingResult",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public Object[][] getDataOfEvidence() {
		return dataOfEvidence;
	}

	public Object[][] getDataOfFact() {
		return dataOfFact;
	}

	public CMap getEvidenceMap() {
		return tableViewOfEvidence.getMap();
	}

	public CMap getFactMap() {
		return tableViewOfFact.getMap();
	}

	public void exportExcle() throws IOException {
		ExportExcel exportExcel = new ExportExcel(tableViewOfEvidence,
				tableViewOfFact);
		exportExcel.export();
	}

	private void FitEvidenceTable(JTable myTable) { // 列寬自適應
		JTableHeader header = myTable.getTableHeader();
		Enumeration columns = myTable.getColumnModel().getColumns();
		while (columns.hasMoreElements()) {
			TableColumn column = (TableColumn) columns.nextElement();
			int col = header.getColumnModel().getColumnIndex(
					column.getIdentifier());
			switch (col) {
			case 2:
				int width = (int) myTable
				.getTableHeader()
				.getDefaultRenderer()
				.getTableCellRendererComponent(myTable,
						column.getIdentifier(), false, false, -1, col)
				.getPreferredSize().getWidth();
				width = Math.max(width,550);
				header.setResizingColumn(column);
				column.setWidth(width + myTable.getIntercellSpacing().width);
				break;
			default:
				break;
			}
		}
	}
	private void FitFactTable(JTable myTable) { // 列寬自適應
		JTableHeader header = myTable.getTableHeader();
		Enumeration columns = myTable.getColumnModel().getColumns();
		while (columns.hasMoreElements()) {
			TableColumn column = (TableColumn) columns.nextElement();
			int col = header.getColumnModel().getColumnIndex(
					column.getIdentifier());
			int width = 0;
			switch (col) {
			case 2:
				 width = (int) myTable
				.getTableHeader()
				.getDefaultRenderer()
				.getTableCellRendererComponent(myTable,
						column.getIdentifier(), false, false, -1, col)
				.getPreferredSize().getWidth();
				width = Math.max(width,700);
				header.setResizingColumn(column);
				column.setWidth(width + myTable.getIntercellSpacing().width);
				break;
			default:
				break;
			}
		}
	}
}
