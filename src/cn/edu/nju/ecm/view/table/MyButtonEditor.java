package cn.edu.nju.ecm.view.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

		delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(row + " : 删除");
			}
		});

		modify = new JButton();

		modify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (ID != -1) {					//修改操作
					CanvasElement ce = canvasPanel.model.getElementByID(ID);
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
							//TODO 暂时没想好怎么做，图表有没有必要显示箭头
							
							String[] ids = ((String) canvasPanel.getMytable()
									.getData()[row][3]).split(";");
							for(int i=0;i<ids.length;i++){
								
							}
						}catch(Exception ex){
							ex.printStackTrace();
						}
						
					} else if (ce.getElementType() == ElementType.Header) {
						((EHeaderModel) ce).geteHeader()
								.setName(
										(String) canvasPanel.getMytable()
												.getData()[row][2]);
						try {
							String temp = ((String) canvasPanel.getMytable()
									.getData()[row][3]).trim();
							if (temp.equals("")) {
								ce.setConnectedOwner(false);
								ce.resetConnectedOwner();
							} else {
								int ownerId = Integer.parseInt(temp);
								CanvasElement body = canvasPanel.model
										.getElementByID(ownerId);
								if(body==null || body.getElementType()!=ElementType.Body){
									ce.setConnectedOwner(false);
									ce.resetConnectedOwner();
								}else{
									ce.setConnectedOwner(true);
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
								ce.setConnectedOwner(false);
								ce.resetConnectedOwner();
								ce.setConnectedSon(false);
								ce.resetConnectedSon();
							}else if(ids.length == 1){
								if (!ids[0].equals("")) {
									int ownerId = Integer.parseInt(ids[0]);
									CanvasElement header = canvasPanel.model
											.getElementByID(ownerId);
									if(header==null || header.getElementType()!=ElementType.Header){
										ce.setConnectedOwner(false);
										ce.resetConnectedOwner();
									}else{
										ce.setConnectedOwner(true);
										ce.setConnectedOwner(header);
									}
									
								} else {
									ce.setConnectedOwner(false);
									ce.resetConnectedOwner();
								}
								ce.setConnectedSon(false);
								ce.resetConnectedSon();
							}else if (ids.length == 2) {
								if (!ids[0].equals("")) {
									int ownerId = Integer.parseInt(ids[0]);
									CanvasElement header = canvasPanel.model
											.getElementByID(ownerId);
									if(header==null || header.getElementType()!=ElementType.Header){
										ce.setConnectedOwner(false);
										ce.resetConnectedOwner();
									}else{
										ce.setConnectedOwner(true);
										ce.setConnectedOwner(header);
									}
									
								} else {
									ce.setConnectedOwner(false);
									ce.resetConnectedOwner();
								}
								if (!ids[1].equals("")) {
									int sonId = Integer.parseInt(ids[1]);
									CanvasElement connector = canvasPanel.model.getElementByID(sonId);
									if(connector==null || connector.getElementType()!=ElementType.Connector){
										ce.setConnectedSon(false);
										ce.resetConnectedSon();
									}else{
										ce.setConnectedSon(true);
										ce.setConnectedSon(connector);
									}
								} else {
									ce.setConnectedSon(false);
									ce.resetConnectedSon();
								}
							}
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
					ECMMainFrame.showElementInfo(canvasPanel.model
							.getElementByID(ID));
				} else if (ID == -1) {				//新增操作

				}
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

}