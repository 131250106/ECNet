package cn.edu.nju.ecm.view.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import cn.edu.nju.ecm.canvas.model.CanvasElement;
import cn.edu.nju.ecm.canvas.model.CanvasElement.ElementType;
import cn.edu.nju.ecm.canvas.model.entity.ConnectorModel;
import cn.edu.nju.ecm.canvas.model.entity.EBodyModel;
import cn.edu.nju.ecm.canvas.model.entity.EHeaderModel;
import cn.edu.nju.ecm.canvas.model.entity.HRelationModel;
import cn.edu.nju.ecm.entity.detail.EBody;
import cn.edu.nju.ecm.entity.detail.EHeader;
import cn.edu.nju.ecm.entity.detail.HConnector;
import cn.edu.nju.ecm.view.CanvasPanel;
import cn.edu.nju.ecm.view.ECMMainFrame;

public class MyButtonEditor extends AbstractCellEditor implements
		TableCellEditor {
	private static final long serialVersionUID = 1L;

	private JPanel panel;

	private JButton delete;

	private JButton modify;

	private int row = -1;

	private int ID;

	private CanvasPanel canvasPanel;

	public MyButtonEditor(CanvasPanel canvasPanel) {
		this.canvasPanel = canvasPanel;

		initButton();

		initPanel();
	}

	private void initButton() {
		delete = new JButton();
    	delete.setFont(new Font("微软雅黑", Font.PLAIN, 13));
    	modify = new JButton();
    	modify.setFont(new Font("微软雅黑", Font.PLAIN, 13));

		delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (ID != -1) {
					if (((JButton) e.getSource()).getText().equals("删除")) {
						canvasPanel.model.deleteElement(ID, true);
						canvasPanel.getMytable().ResetTableView();
						ECMMainFrame.resetElementInfo();
					} else if (((JButton) e.getSource()).getText().equals("修改")) {
						CanvasElement ce = canvasPanel.model.getElementByID(ID);
						if (ce != null) {
							if (ce.getElementType() == ElementType.Body) {
								Object[][] data = canvasPanel.getMytable()
										.getDataOfEvidence();
								EBody body = ((EBodyModel) ce).geteBody();
								int temprow = canvasPanel.getMytable()
										.getEvidenceMap().visibleCell(row, 0);
								body.setName((String) data[temprow][1]);
								body.setContent((String) data[temprow][2]);
								body.setEvidenceType((String) data[temprow][3]);
								body.setCommiter((String) data[temprow][4]);
								body.setEvidenceReason((String) data[temprow][5]);
								body.setEvidenceConclusion((String) data[temprow][6]);
							}else if (ce.getElementType() == ElementType.Connector) {
								Object[][] data = canvasPanel.getMytable()
										.getDataOfFact();
								int temprow = canvasPanel.getMytable()
										.getFactMap().visibleCell(row, 0);
								HConnector connector = ((ConnectorModel) ce).gethConnector();
								connector.setName((String) data[temprow][1]);
								connector.setContent((String) data[temprow][2]);
							}
						}
					}else if (((JButton) e.getSource()).getText().equals("删除连接")) {
						Object[][] data = canvasPanel.getMytable()
								.getDataOfFact();
						CanvasElement connector = canvasPanel.model.getElementByID((int)data[row][0]);
						if(connector!=null){
							CanvasElement relation = canvasPanel.model.findRelation(connector.getID(),ID);
							if(relation!=null)
								canvasPanel.model.deleteElement(relation.getID(), true);
						}
						canvasPanel.getMytable().ResetTableView();
						ECMMainFrame.resetElementInfo();
					}
				}
				canvasPanel.refresh();
			}
		});

		modify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (ID != -1) {
					CanvasElement ce = canvasPanel.model.getElementByID(ID);
					if (ce != null) {
						if (((JButton) e.getSource()).getText().equals("新增链头")) {
							if (ce.getElementType() == ElementType.Body) {
								Object[][] data = canvasPanel.getMytable()
										.getDataOfEvidence();
								EHeader eHeaderEntity = new EHeader("",
										(String) data[row][7],
										(String) data[row][8]);
								EHeaderModel eHeaderModel = new EHeaderModel(
										50, 50, 2);
								eHeaderModel.seteHeader(eHeaderEntity);
								eHeaderModel.setConnectedOwner(ce);
								canvasPanel.model
										.insertTableElement(eHeaderModel);
							} else if (ce.getElementType() == ElementType.Connector) {
								Object[][] data = canvasPanel.getMytable()
										.getDataOfFact();
								int bodyId = Integer.parseInt(((String)data[row][4]).trim());
								CanvasElement body = canvasPanel.model.getElementByID(bodyId);
								if(body!=null){
									ArrayList<CanvasElement> headers = canvasPanel.model.getFormat().getAllHeader(body);
									for(CanvasElement header:headers){
										if(((EHeaderModel)header).geteHeader().getContent().equals((String)data[row][3])){
											if(!canvasPanel.model.isExistRelation(ce,header)){
												HRelationModel relation = new HRelationModel(50, 50, 2);
												relation.setConnectedSon(canvasPanel.model.getElementByID((int)data[row][0]));
												relation.setConnectedOwner(header);
												canvasPanel.model
												.insertTableElement(relation);
											}
											break;
										}
									}
								}
							}
							canvasPanel.getMytable().ResetTableView();
							canvasPanel.model.autoFormat();
						} else if (((JButton) e.getSource()).getText().equals(
								"修改链头")) {
							Object[][] data = canvasPanel.getMytable()
									.getDataOfEvidence();
							EHeader eh = ((EHeaderModel) ce).geteHeader();
							eh.setContent((String) data[row][7]);
							eh.setKeySentence((String) data[row][8]);
						}
					}
				} else if (ID == -1) { // 新增操作
					if (((JButton) e.getSource()).getText().equals("新增联结")) {
							Object[][] data = canvasPanel.getMytable()
									.getDataOfFact();
							HConnector hConnectorEntity = new HConnector(
									(String) data[row][1],
									(String) data[row][2]);
							ConnectorModel connectorModel = new ConnectorModel(50,
									50, 2);
							connectorModel.sethConnector(hConnectorEntity);
							canvasPanel.model.insertTableElement(connectorModel);
					} else if (((JButton) e.getSource()).getText().equals(
							"新增链体")) {
						Object[][] data = canvasPanel.getMytable()
								.getDataOfEvidence();
						EBody eBodyEntity = new EBody((String) data[row][1],
								(String) data[row][2], (String) data[row][3],
								(String) data[row][4], (String) data[row][5],
								(String) data[row][6]);
						EBodyModel ebodyModel = new EBodyModel(50, 50, 2);
						ebodyModel.seteBody(eBodyEntity);
						canvasPanel.model.insertTableElement(ebodyModel);
					}
					canvasPanel.getMytable().ResetTableView();
					canvasPanel.model.autoFormat();
				}
				canvasPanel.refresh();
			}
		});

	}

	private void initPanel() {
		panel = new JPanel();

		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

		panel.add(this.delete);
		panel.add(this.modify);

		// panel.setBackground(new Color(1, 128, 215));
		panel.setBackground(Color.white);
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		String data[] = ((String) value).split(",");

		delete.setText(value == null ? "" : data[0]);
		modify.setText(value == null ? "" : data[1]);
		this.ID = (value == null ? -1 : Integer.parseInt(data[2]));

		this.row = row;

		return panel;
	}

	@Override
	public Object getCellEditorValue() {
		return delete.getText() + "," + modify.getText() + "," + ID;
	}

}