package cn.edu.nju.ecm.view.entity.panel;

import java.awt.Font;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;

import cn.edu.nju.ecm.entity.Element;
import cn.edu.nju.ecm.view.checker.TextChecker;
import cn.edu.nju.ecm.view.checker.TextChecker.CheckType;
import cn.edu.nju.ecm.view.entity.ElementPanel;

public class ECMModelPanel extends ElementPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField ecmNameText;
	private JTextField caseReason;
	private JTextField caseNumber;
	private JTextArea ecmDescText;

	/**
	 * Create the panel.
	 */
	public ECMModelPanel() {
		super();

		JLabel lblNewLabel = new JLabel("证据链标题");
		lblNewLabel.setFont(new Font("宋体", Font.PLAIN, 15));

		ecmNameText = new JTextField();
		ecmNameText.setFont(new Font("宋体", Font.PLAIN, 15));
		ecmNameText.setColumns(10);
		TextChecker nameChecker = new TextChecker(ecmNameText, this, "证据链名称必须至少5个字", 5, 0);
		nameChecker.setCheckType(CheckType.Length);
		addChecker(nameChecker);
		
		JLabel lblCaseReason = new JLabel("案      由");
		lblCaseReason.setFont(new Font("宋体", Font.PLAIN, 15));
		caseReason = new JTextField();
		caseReason.setFont(new Font("宋体", Font.PLAIN, 15));
		caseReason.setColumns(10);
		
		JLabel lblCaseNumber = new JLabel("案      号");
		lblCaseNumber.setFont(new Font("宋体", Font.PLAIN, 15));
		caseNumber = new JTextField();
		caseNumber.setFont(new Font("宋体", Font.PLAIN, 15));
		caseNumber.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("证据链简介");
		lblNewLabel_1.setFont(new Font("宋体", Font.PLAIN, 15));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addContainerGap()
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup().addComponent(lblNewLabel)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(ecmNameText,
												GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE))
								.addGroup(groupLayout.createSequentialGroup().addComponent(lblCaseReason)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(caseReason,
												GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE))
								.addGroup(groupLayout.createSequentialGroup().addComponent(lblCaseNumber)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(caseNumber,
												GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE))
								.addGroup(groupLayout.createSequentialGroup().addComponent(lblNewLabel_1)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(scrollPane,
												GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)))
						.addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
				.createSequentialGroup().addContainerGap()
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblNewLabel).addComponent(
						ecmNameText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblCaseReason).addComponent(
						caseReason, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblCaseNumber).addComponent(
						caseNumber, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGap(18).addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(lblNewLabel_1)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE))
				.addContainerGap()));

		ecmDescText = new JTextArea();
		ecmDescText.setLineWrap(true);
		ecmDescText.setFont(new Font("Monospaced", Font.PLAIN, 15));
		scrollPane.setViewportView(ecmDescText);
		TextChecker descChecker = new TextChecker(ecmDescText, this, "证据链描述至少20个字", 20, 0);
		descChecker.setCheckType(CheckType.Length);
		addChecker(descChecker);

		setLayout(groupLayout);

	}

	public Element generateElement() {
		Element element = new Element();
		element.setName(ecmNameText.getText());
		element.setContent(ecmDescText.getText());
		return element;
	}
}
