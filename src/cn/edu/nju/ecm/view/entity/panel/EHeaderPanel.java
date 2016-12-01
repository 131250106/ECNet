package cn.edu.nju.ecm.view.entity.panel;

import java.awt.Font;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import cn.edu.nju.ecm.entity.Element;
import cn.edu.nju.ecm.entity.detail.EHeader;
import cn.edu.nju.ecm.view.entity.ElementPanel;

public class EHeaderPanel extends ElementPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField eheadName;
	private JTextArea eheadContent;

	/**
	 * Create the panel.
	 */
	public EHeaderPanel() {
		super();

		JLabel lblNewLabel = new JLabel("链头标题");
		lblNewLabel.setFont(new Font("宋体", Font.PLAIN, 15));

		eheadName = new JTextField();
		eheadName.setFont(new Font("宋体", Font.PLAIN, 15));
		eheadName.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("链头简介");
		lblNewLabel_1.setFont(new Font("宋体", Font.PLAIN, 15));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addContainerGap()
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup().addComponent(lblNewLabel)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(eheadName,
												GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE))
								.addGroup(groupLayout.createSequentialGroup().addComponent(lblNewLabel_1)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(scrollPane,
												GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)))
						.addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
				.createSequentialGroup().addContainerGap()
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblNewLabel).addComponent(
						eheadName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGap(18).addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(lblNewLabel_1)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE))
				.addContainerGap()));

		eheadContent = new JTextArea();
		eheadContent.setLineWrap(true);
		eheadContent.setFont(new Font("Monospaced", Font.PLAIN, 15));
		scrollPane.setViewportView(eheadContent);

		setLayout(groupLayout);

	}


	public Element generateElement() {
		String name = eheadName.getText();
		String content = eheadContent.getText();
		EHeader eheader = new EHeader(name, content);
		return eheader;
	}
}
