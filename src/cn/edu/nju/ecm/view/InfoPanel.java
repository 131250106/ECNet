package cn.edu.nju.ecm.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.GroupLayout;
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

public class InfoPanel extends JPanel {									//�Ҳ���Ϣ��panel

	private static final long serialVersionUID = 1L;

	public InfoPanel(CanvasElement element) {
		this.element = element;
		initComponents();
	}

	public InfoPanel() {
		initComponents();
	}

	private JLabel modelLabel;
	private JLabel type;
	private JLabel title;
	private JTextField titleField;
	private JLabel content;
	private JTextArea contentArea;
	private JButton modify;
	private JButton reset;
	private JButton delete;
	private CanvasElement element;

	private JLabel modeltitle;
	private JLabel modelcontent;

	private JTextField modeltitleField;
	private JTextArea modelcontentArea;

	private JLabel modellabel1;
	private JLabel modellabel2;
	private JLabel modellabel3;
	private JLabel modellabel4;
	private JTextField modeltitleField1;
	private JTextField modeltitleField2;
	private JTextField modeltitleField3;
	private JTextField modeltitleField4;
	
	private CanvasPanel canvasPanel;

	private void initComponents() {

		modelLabel = new JLabel();
		modelLabel.setFont(new java.awt.Font("����", 1, 24));
		modelLabel.setText("֤������Ϣ");

		modeltitle = new JLabel();
		modeltitle.setFont(new java.awt.Font("����", 0, 18));
		modeltitle.setText("����:");

		modelcontent = new JLabel();
		modelcontent.setFont(new java.awt.Font("����", 0, 18));
		modelcontent.setText("���:");

		modeltitleField = new JTextField();
		modeltitleField.addFocusListener(new MyFocusListener());
		JScrollPane jScrollPane2 = new JScrollPane();

		modelcontentArea = new JTextArea();
		modelcontentArea.setColumns(120);
		modelcontentArea.setRows(5);
		modelcontentArea.setLineWrap(true);
		modelcontentArea.addFocusListener(new MyFocusListener());
		jScrollPane2.setViewportView(modelcontentArea);
		
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
		

		type.setFont(new java.awt.Font("����", 1, 24)); 

		title.setFont(new java.awt.Font("����", 0, 18));
		title.setText("���ƣ�");

		content.setFont(new java.awt.Font("����", 0, 18));
		content.setText("����:");

		contentArea.setColumns(120);
		contentArea.setRows(5);
		contentArea.setLineWrap(true);
		jScrollPane1.setViewportView(contentArea);
		
		modellabel1 = new JLabel("label1");
		modellabel1.setFont(new java.awt.Font("����", 0, 18));
		modeltitleField1 = new JTextField();
		
		modellabel2 = new JLabel("label2");
		modellabel2.setFont(new java.awt.Font("����", 0, 18));
		modeltitleField2 = new JTextField();
		
		modellabel3 = new JLabel("label3");
		modellabel3.setFont(new java.awt.Font("����", 0, 18));
		modeltitleField3 = new JTextField();
		
		modellabel4 = new JLabel("label4");
		modellabel4.setFont(new java.awt.Font("����", 0, 18));
		modeltitleField4 = new JTextField();

		modify.setText("ȷ���޸�");
		modify.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				modifyCanvasElement();
			}
		});

		reset.setText("������Ϣ");
		reset.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if (element != null) {
					setInfo(element);
				} else
					reSetInfo();
			}
		});

		delete.setText("ɾ��ͼԪ");
		delete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(canvasPanel!=null){
					canvasPanel.deleteElementById(getID(),false);
					reSetInfo();
				}
			}
		});

		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(jSeparator1, GroupLayout.Alignment.TRAILING)
				.addGroup(
						layout.createSequentialGroup()
								.addGap(0, 0, Short.MAX_VALUE)
								.addComponent(modelLabel)
								.addContainerGap(GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE))
				.addGroup(
						layout.createSequentialGroup()
								.addGap(15, 15, 15)
								.addGroup(
										layout.createParallelGroup(
												GroupLayout.Alignment.TRAILING)
												.addGroup(
														GroupLayout.Alignment.LEADING,
														layout.createSequentialGroup()
																.addGroup(
																		layout.createParallelGroup(
																				GroupLayout.Alignment.TRAILING)
																				.addGroup(
																						GroupLayout.Alignment.LEADING,
																						layout.createSequentialGroup()
																								.addGroup(
																										layout.createParallelGroup(
																												GroupLayout.Alignment.LEADING)
																												.addComponent(
																														modeltitle)
																												.addComponent(
																														modelcontent))
																								.addPreferredGap(
																										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																								.addGroup(
																										layout.createParallelGroup(
																												GroupLayout.Alignment.LEADING)
																												.addComponent(
																														jScrollPane2)
																												.addComponent(
																														modeltitleField)))
																				.addGroup(
																						GroupLayout.Alignment.LEADING,
																						layout.createSequentialGroup()
																								.addGroup(
																										layout.createParallelGroup(
																												GroupLayout.Alignment.LEADING)
																												.addComponent(
																														content)
																												.addComponent(
																														title)
																												.addComponent(
																														modellabel1)
																												.addComponent(
																														modellabel2)
																												.addComponent(
																														modellabel3)
																												.addComponent(
																														modellabel4))
																								.addPreferredGap(
																										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																								.addGroup(
																										layout.createParallelGroup(
																												GroupLayout.Alignment.LEADING)
																												.addComponent(
																														titleField)
																														.addComponent(
																														modeltitleField1)
																														.addComponent(
																														modeltitleField2)
																														.addComponent(
																														modeltitleField3)
																														.addComponent(
																														modeltitleField4)
																												.addComponent(
																														jScrollPane1))))
																.addGap(15, 15,
																		15))
												.addGroup(
														GroupLayout.Alignment.LEADING,
														layout.createSequentialGroup()
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED,
																		GroupLayout.DEFAULT_SIZE,
																		Short.MAX_VALUE)
																.addGroup(
																		layout.createParallelGroup(
																				GroupLayout.Alignment.LEADING)
																				.addComponent(
																						delete)
																				.addGroup(
																						layout.createSequentialGroup()
																								.addComponent(
																										modify)
																								.addPreferredGap(
																										javax.swing.LayoutStyle.ComponentPlacement.RELATED,
																										GroupLayout.DEFAULT_SIZE,
																										Short.MAX_VALUE)
																								.addComponent(
																										reset)))
																.addContainerGap(
																		GroupLayout.DEFAULT_SIZE,
																		Short.MAX_VALUE))))
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap(106, Short.MAX_VALUE)
								.addComponent(type)
								.addGap(0, 104, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap(18, Short.MAX_VALUE)
								.addComponent(modelLabel)
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED,
										12, Short.MAX_VALUE)
								.addGroup(
										layout.createParallelGroup(
												GroupLayout.Alignment.BASELINE)
												.addComponent(
														modeltitleField,
														GroupLayout.PREFERRED_SIZE,
														24,
														GroupLayout.PREFERRED_SIZE)
												.addComponent(modeltitle))
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED,
										12, Short.MAX_VALUE)
								.addGroup(
										layout.createParallelGroup(
												GroupLayout.Alignment.LEADING)
												.addComponent(
														jScrollPane2,
														GroupLayout.PREFERRED_SIZE,
														112,
														GroupLayout.PREFERRED_SIZE)
												.addComponent(
														modelcontent,
														GroupLayout.PREFERRED_SIZE,
														18,
														GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED,
										19, Short.MAX_VALUE)
								.addComponent(jSeparator1,
										GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED,
										14, Short.MAX_VALUE)
								.addComponent(type)
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED,
										12, Short.MAX_VALUE)
								.addGroup(
										layout.createParallelGroup(
												GroupLayout.Alignment.BASELINE)
												.addComponent(
														titleField,
														GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE,
														GroupLayout.PREFERRED_SIZE)
												.addComponent(title))
								.addGap(1, 1, 1)
								.addGroup(
										layout.createParallelGroup(
												GroupLayout.Alignment.BASELINE)
												.addComponent(
														modeltitleField1,
														GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE,
														GroupLayout.PREFERRED_SIZE)
												.addComponent(modellabel1))
								.addGap(1, 1, 1)
								.addGroup(
										layout.createParallelGroup(
												GroupLayout.Alignment.BASELINE)
												.addComponent(
														modeltitleField2,
														GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE,
														GroupLayout.PREFERRED_SIZE)
												.addComponent(modellabel2))
								.addGap(1, 1, 1)
								.addGroup(
										layout.createParallelGroup(
												GroupLayout.Alignment.BASELINE)
												.addComponent(
														modeltitleField3,
														GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE,
														GroupLayout.PREFERRED_SIZE)
												.addComponent(modellabel3))
								.addGap(1, 1, 1)
								.addGroup(
										layout.createParallelGroup(
												GroupLayout.Alignment.BASELINE)
												.addComponent(
														modeltitleField4,
														GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE,
														GroupLayout.PREFERRED_SIZE)
												.addComponent(modellabel4))
								.addGap(1, 1, 1)
								.addGroup(
										layout.createParallelGroup(
												GroupLayout.Alignment.LEADING)
												.addComponent(
														jScrollPane1,
														GroupLayout.PREFERRED_SIZE,
														112,
														GroupLayout.PREFERRED_SIZE)
												.addComponent(content))
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED,
										12, Short.MAX_VALUE)
								.addGroup(
										layout.createParallelGroup(
												GroupLayout.Alignment.LEADING)
												.addComponent(modify)
												.addComponent(reset))
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED,
										12, Short.MAX_VALUE)
								.addComponent(delete)
								.addContainerGap(GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)));

		setInfo(element);
		
	}

	public int getID() {
		if (element != null)
			return element.getID();
		return -1;
	}

	public void reSetInfo() {
		titleField.setText("");
		title.setText("����:");
		contentArea.setText("");
		content.setText("����:");
		type.setText("δѡ��");
		element = null;
		modellabel1.setVisible(false);
		modeltitleField1.setVisible(false);
		modellabel2.setVisible(false);
		modeltitleField2.setVisible(false);
		modellabel3.setVisible(false);
		modeltitleField3.setVisible(false);
		modellabel4.setVisible(false);
		modeltitleField4.setVisible(false);
	}

	public void setInfo(CanvasElement element) {
		reSetInfo();			//������һ����Ϣ
		
		this.element = element;

		String typestr = "δѡ��";
		String titlestr = "";
		String titleName="";
		String contentstr = "";
		String contentname="";
		if (element != null) {
			if (element.getElementType() == ElementType.Body) {
				typestr = "����";
				EBody eb = ((EBodyModel) element).geteBody();
				titlestr = eb.getName();
				contentstr = eb.getContent();
				titleName="֤������:";
				contentname="֤����ϸ:";
				modellabel1.setText("֤������:");
				modellabel1.setVisible(true);
				modeltitleField1.setText(eb.getEvidenceType());
				modeltitleField1.setVisible(true);
				
				modellabel2.setVisible(true);
				modellabel2.setText("�ύ��:");
				modeltitleField2.setVisible(true);
				modeltitleField2.setText(eb.getCommiter());
				
				modellabel3.setVisible(true);
				modellabel3.setText("��֤����:");
				modeltitleField3.setVisible(true);
				modeltitleField3.setText(eb.getEvidenceReason());
				
				modellabel4.setVisible(true);
				modellabel4.setText("��֤����:");
				modeltitleField4.setVisible(true);
				modeltitleField4.setText(eb.getEvidenceConclusion());
			} else if (element.getElementType() == ElementType.Header) {
				typestr = "��ͷ";
				EHeader eh = ((EHeaderModel) element).geteHeader();
				titlestr = eh.getName();
				contentstr = eh.getContent();
				titleName="��ͷ����:";
				contentname="��ͷ����:";
				modellabel1.setText("�ؼ��ı�:");
				modellabel1.setVisible(true);
				modeltitleField1.setVisible(true);
				modeltitleField1.setText(eh.getKeySentence());
			} else if (element.getElementType() == ElementType.Connector) {
				typestr = "�����";
				HConnector hc = ((ConnectorModel) element).gethConnector();
				titlestr = hc.getName();
				contentstr = hc.getContent();
				titleName="��ʵ����:";
				contentname="��ʵ��Ϣ:";
			} else if (element.getElementType() == ElementType.Relation) {
				typestr = "��ͷ";
				HRelation hr = ((HRelationModel) element).gethRelation();
				titlestr = hr.getName();
				contentstr = hr.getContent();
				titleName="��ͷ����:";
				contentname="��ͷ���:";
			}
			type.setText(typestr);
			titleField.setText(titlestr);
			contentArea.setText(contentstr);
			title.setText(titleName);
			content.setText(contentname);
		}
		
	}

	public void setCanvasPanel(CanvasPanel canvasPanel) {
		this.canvasPanel = canvasPanel;
		modeltitleField.setText(canvasPanel.model.getTitle());
		modelcontentArea.setText(canvasPanel.model.getDescription());
	}

	public void reSetModel() {
		this.canvasPanel = null;
		modeltitleField.setText("");
		modelcontentArea.setText("");
		reSetInfo();
	}

	private void modifyCanvasElement() {
		if (element == null)
			reSetInfo();
		else {
			if (element.getElementType() == ElementType.Body) {
				EBody eb = ((EBodyModel) element).geteBody();
				eb.setName(titleField.getText());
				eb.setContent(contentArea.getText());
				eb.setEvidenceType(modeltitleField1.getText());
				eb.setCommiter(modeltitleField2.getText());
				eb.setEvidenceReason(modeltitleField3.getText());
				eb.setEvidenceConclusion(modeltitleField4.getText());
			} else if (element.getElementType() == ElementType.Header) {
				EHeader eh = ((EHeaderModel) element).geteHeader();
				eh.setName(titleField.getText());
				eh.setContent(contentArea.getText());
				eh.setKeySentence(modeltitleField1.getText());
			} else if (element.getElementType() == ElementType.Connector) {
				HConnector hc = ((ConnectorModel) element).gethConnector();
				hc.setName(titleField.getText());
				hc.setContent(contentArea.getText());
			} else if (element.getElementType() == ElementType.Relation) {
				HRelation hr = ((HRelationModel) element).gethRelation();
				hr.setName(titleField.getText());
				hr.setContent(contentArea.getText());
			}
			canvasPanel.refresh();
		}
	}

	class MyFocusListener implements FocusListener {
		@Override
		public void focusGained(FocusEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void focusLost(FocusEvent e) {
			if (canvasPanel != null) {
				if (canvasPanel.model.getTitle().equals(modeltitleField.getText())
						&& canvasPanel.model.getDescription().equals(
								modelcontentArea.getText())) {
				} else{
					canvasPanel.model.setTitle(modeltitleField.getText());
					canvasPanel.model.setDescription(modelcontentArea.getText());
					canvasPanel.getMytable().changeTitle();
					canvasPanel.setChanged(true);
				}
			}
		}

	}
}
