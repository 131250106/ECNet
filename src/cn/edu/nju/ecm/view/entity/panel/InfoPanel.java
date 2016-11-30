package cn.edu.nju.ecm.view.entity.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;

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
import cn.edu.nju.ecm.view.ECMMainFrame;

public class InfoPanel extends JPanel {

	/**
	 * Creates new form NewJPanel
	 */
	public InfoPanel(CanvasElement element) {
		this.element = element;
		initComponents();
	}

	public InfoPanel() {
		initComponents();
	}

	private JLabel infoLabel;
	private JLabel type;
	private JLabel title;
	private JTextField titleField;
	private JLabel content;
	private JTextArea contentArea;
	private JButton modify;
	private JButton reset;
	private JButton delete;
	private CanvasElement element;

	private void initComponents() {
		
		infoLabel = new JLabel();
		JSeparator jSeparator1 = new JSeparator();
		type = new JLabel();
		title = new JLabel();
		titleField = new JTextField();
		content = new JLabel();
		JScrollPane jScrollPane1 = new JScrollPane();
		contentArea = new JTextArea();
		modify = new JButton();
		reset = new JButton();
		delete = new JButton();

		infoLabel.setFont(new java.awt.Font("宋体", 1, 36)); // NOI18N
		infoLabel.setText("信息栏");

		type.setFont(new java.awt.Font("宋体", 1, 18)); // NOI18N

		title.setText("标题：");

		content.setText("内容:");

		contentArea.setColumns(120);
		contentArea.setRows(5);
		jScrollPane1.setViewportView(contentArea);

		modify.setText("确认修改");

		reset.setText("重置信息");
		reset.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(element!=null){
					setInfo(element);
				}else
					reSetInfo();
			}
		});
		
		delete.setText("删除图元");
		delete.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				ECMMainFrame.deleteElement();
				reSetInfo();
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(jSeparator1,
						javax.swing.GroupLayout.Alignment.TRAILING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
												.addGroup(
														layout.createSequentialGroup()
																.addGroup(
																		layout.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.LEADING)
																				.addComponent(
																						title)
																				.addComponent(
																						content))
																.addGap(18, 18,
																		18)
																.addGroup(
																		layout.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.LEADING)
																				.addComponent(
																						titleField)
																				.addComponent(
																						jScrollPane1)))
												.addGroup(
														layout.createSequentialGroup()
																.addGap(0,
																		0,
																		Short.MAX_VALUE)
																.addComponent(
																		infoLabel)
																.addGap(0,
																		0,
																		Short.MAX_VALUE))
												.addGroup(
														layout.createSequentialGroup()
																.addGap(0,
																		0,
																		Short.MAX_VALUE)
																.addComponent(
																		type)
																.addGap(0,
																		0,
																		Short.MAX_VALUE)))
								.addContainerGap())
				.addGroup(
						layout.createSequentialGroup()
								.addGap(24, 24, 24)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
												.addGroup(
														layout.createSequentialGroup()
																.addComponent(
																		delete)
																.addContainerGap(
																		javax.swing.GroupLayout.DEFAULT_SIZE,
																		Short.MAX_VALUE))
												.addGroup(
														layout.createSequentialGroup()
																.addComponent(
																		modify)
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED,
																		58,
																		Short.MAX_VALUE)
																.addComponent(
																		reset)
																.addGap(35, 35,
																		35)))));
		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addGap(8, 8, 8)
								.addComponent(infoLabel)
								.addGap(18, 18, 18)
								.addComponent(jSeparator1,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGap(18, 18, 18)
								.addComponent(type)
								.addGap(35, 35, 35)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(title)
												.addComponent(
														titleField,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED,
										42, Short.MAX_VALUE)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
												.addGroup(
														layout.createSequentialGroup()
																.addComponent(
																		content)
																.addGap(0,
																		0,
																		Short.MAX_VALUE))
												.addComponent(
														jScrollPane1,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														251, Short.MAX_VALUE))
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED,
										31, Short.MAX_VALUE)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(modify)
												.addComponent(reset))
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED,
										33, Short.MAX_VALUE)
								.addComponent(delete)
								.addContainerGap(40, Short.MAX_VALUE)));
		
		setInfo(element);
	}

	public int getID() {
		if(element!=null)
			return element.getID();
		return -1;
	}

	public void reSetInfo() {
		titleField.setText("");
		contentArea.setText("");
		type.setText("未选中");
		element = null;
	}

	public void setInfo(CanvasElement element) {
		this.element = element;
		
		String typestr = "未选中";
		String titlestr = "";
		String contentstr = "";
		if (element != null) {
			if (element.getElementType() == ElementType.Body) {
				typestr = "链体";
				EBody eb = ((EBodyModel) element).geteBody();
				titlestr = eb.getName();
				contentstr = eb.getContent();
			} else if (element.getElementType() == ElementType.Header) {
				typestr = "链头";
				EHeader eh = ((EHeaderModel) element).geteHeader();
				titlestr = eh.getName();
				contentstr = eh.getContent();
			} else if (element.getElementType() == ElementType.Connector) {
				typestr = "箭头";
				HConnector hc = ((ConnectorModel) element).gethConnector();
				titlestr = hc.getName();
				contentstr = hc.getContent();
			} else if (element.getElementType() == ElementType.Relation) {
				typestr = "联结";
				HRelation hr = ((HRelationModel) element).gethRelation();
				titlestr = hr.getName();
				contentstr = hr.getContent();
			}
		}
		type.setText(typestr);
		titleField.setText(titlestr);
		contentArea.setText(contentstr);
	}

	private void modifyCanvasElement(CanvasElement element, String titlestr,
			String contentstr) {
		// todo
	}

	private void resetCanvasElement(CanvasElement element) {
		setInfo(element);
	}

}
