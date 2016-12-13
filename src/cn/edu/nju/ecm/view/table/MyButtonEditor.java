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
import cn.edu.nju.ecm.entity.detail.HRelation;
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
    	delete.setFont(new Font("Dialog", Font.PLAIN, 10));
    	modify = new JButton();
    	modify.setFont(new Font("Dialog", Font.PLAIN, 10));
    	
		delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(ID);
				if (ID != -1) {
					canvasPanel.model.deleteElement(ID, true);
					canvasPanel.getMytable().ResetTableView();
					ECMMainFrame.resetElementInfo();
				}
				canvasPanel.refresh();
			}
		});


		modify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (ID != -1) {					//修改操作
					CanvasElement ce = canvasPanel.model.getElementByID(ID);
					modifyElement(ce);
				} else if (ID == -1) {				//新增操作
					Object[][] data = canvasPanel.getMytable().getData();
					String type = (String)data[row][0];
					if(type.equals("链体")){
						EBody eBodyEntity = new EBody((String)data[row][2], "","","","","");
						EBodyModel eBodyModel = new EBodyModel(50, 50, 2);
						eBodyModel.seteBody(eBodyEntity);
						canvasPanel.model.insertTableElement(eBodyModel);
					}else if(type.equals("链头")){
						EHeader eHeaderEntity = new EHeader((String)data[row][2], "","");
						EHeaderModel eHeaderModel = new EHeaderModel(50, 50, 2);
						eHeaderModel.seteHeader(eHeaderEntity);
						canvasPanel.model.insertTableElement(eHeaderModel);
						modifyElement(canvasPanel.model.getElementByID(eHeaderModel.getID()));
					}else if(type.equals("连结点")){
						HConnector hConnectorEntity = new HConnector((String)data[row][2], "");
						ConnectorModel hConnectorModel = new ConnectorModel(50, 50, 2);
						hConnectorModel.sethConnector(hConnectorEntity);
						canvasPanel.model.insertTableElement(hConnectorModel);
					}else if(type.equals("箭头")){
						HRelation hRelationEntity = new HRelation((String)data[row][2], "");
						HRelationModel hRelationModel = new HRelationModel(50, 50, 2);
						hRelationModel.sethRelation(hRelationEntity);
						canvasPanel.model.insertTableElement(hRelationModel);
						modifyElement(canvasPanel.model.getElementByID(hRelationModel.getID()));
					}
					canvasPanel.getMytable().ResetTableView();
				}
				canvasPanel.model.autoFormat();
				canvasPanel.refresh();
			}
		});

	}

	private void initPanel() {
		panel = new JPanel();

		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));

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
	
	private void modifyElement(CanvasElement ce){
		if (ce.getElementType() == ElementType.Body) {
			((EBodyModel) ce).geteBody()
					.setName(
							(String) canvasPanel.getMytable()
									.getData()[row][2]);
		} else if (ce.getElementType() == ElementType.Connector) {
			((ConnectorModel) ce).gethConnector()
					.setName(
							(String) canvasPanel.getMytable()
									.getData()[row][2]);
			try{
				ArrayList<CanvasElement> headerlist = canvasPanel.model.getFormat().getAllHeader(ce);
				
				String[] ids = ((String) canvasPanel.getMytable()
						.getData()[row][3]).split(";");
				for(int i=0;i<ids.length;i++){
					String temp = ids[i].trim();
					int headerId = Integer.parseInt(temp);
					boolean isExist = false;
					for(CanvasElement header:headerlist){
						if(header.getID()==headerId){
							header.setFlag(-100);
							isExist = true;
							break;
						}
					}
					if(!isExist){				//说明是新增的,因此要新增一条relation
						HRelation hRelationEntity = new HRelation("", "");
						HRelationModel hRelationModel = new HRelationModel(50, 50, 2);
						hRelationModel.sethRelation(hRelationEntity);
						CanvasElement header = canvasPanel.model.getElementByID(headerId);
						if(header!=null&& header.isConnectedOwner()){		//如果是游离态的链头，原则上不让链接
							hRelationModel.setConnectedOwner(header);
							hRelationModel.setConnectedSon(ce);
							canvasPanel.model.insertTableElement(hRelationModel);
						}
					}
				}
				for(CanvasElement header:headerlist){		//删除不存在的
					if(header.getFlag()==0){
						for(CanvasElement relation:canvasPanel.model.getElements()){
							if(relation.getElementType()==ElementType.Relation && relation.isConnectedOwner() && relation.getConnectedOwner().getID()==header.getID() && relation.isConnectedSon()&&relation.getConnectedSon().getID()==ce.getID()){
								canvasPanel.model.deleteElement(relation.getID(), true);
								break;
							}
						}
						
					}
				}
			}catch(Exception ex){
				System.out.println("illegal input!");
			}
			canvasPanel.model.getFormat().resetElement();			//修改过后一定要重置flag
		} else if (ce.getElementType() == ElementType.Header) {
			((EHeaderModel) ce).geteHeader()
					.setName(
							(String) canvasPanel.getMytable()
									.getData()[row][2]);
			try {
				String temp = ((String) canvasPanel.getMytable()
						.getData()[row][3]).trim();
				if (temp.equals("")) {
					ce.resetConnectedOwner();
				} else {
					int ownerId = Integer.parseInt(temp);
					CanvasElement body = canvasPanel.model
							.getElementByID(ownerId);
					if(body==null || body.getElementType()!=ElementType.Body){
						ce.resetConnectedOwner();
					}else{
						ce.setConnectedOwner(body);
					}
					
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else if (ce.getElementType() == ElementType.Relation) {
			((HRelationModel) ce).gethRelation()
					.setName(
							(String) canvasPanel.getMytable()
									.getData()[row][2]);
			try {
				String[] ids = ((String) canvasPanel.getMytable()
						.getData()[row][3]).split("&");
				if(ids.length == 0){
					ce.resetConnectedOwner();
					ce.resetConnectedSon();
				}else if(ids.length == 1){
					if (!ids[0].equals("")) {
						int ownerId = Integer.parseInt(ids[0]);
						CanvasElement header = canvasPanel.model
								.getElementByID(ownerId);
						if(header==null || header.getElementType()!=ElementType.Header){
							ce.resetConnectedOwner();
						}else{
							ce.setConnectedOwner(header);
						}
						
					} else {
						ce.resetConnectedOwner();
					}
					ce.resetConnectedSon();
				}else if (ids.length == 2) {
					if (!ids[0].equals("")) {
						int ownerId = Integer.parseInt(ids[0]);
						CanvasElement header = canvasPanel.model
								.getElementByID(ownerId);
						if(header==null || header.getElementType()!=ElementType.Header){
							ce.resetConnectedOwner();
						}else{
							ce.setConnectedOwner(header);
						}
						
					} else {
						ce.resetConnectedOwner();
					}
					if (!ids[1].equals("")) {
						int sonId = Integer.parseInt(ids[1]);
						CanvasElement connector = canvasPanel.model.getElementByID(sonId);
						if(connector==null || connector.getElementType()!=ElementType.Connector){
							ce.resetConnectedSon();
						}else{
							ce.setConnectedSon(connector);
						}
					} else {
						ce.resetConnectedSon();
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		ECMMainFrame.showElementInfo(canvasPanel.model
				.getElementByID(ID));
	}

}