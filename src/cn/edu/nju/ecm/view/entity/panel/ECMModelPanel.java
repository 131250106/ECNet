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
	private JTextArea ecmDescText;

	/**
	 * Create the panel.
	 */
	public ECMModelPanel() {
		super();

		JLabel lblNewLabel = new JLabel("\u8BC1\u636E\u94FE\u6807\u9898\uFF1A");
		lblNewLabel.setFont(new Font("����", Font.PLAIN, 15));

		ecmNameText = new JTextField();
		ecmNameText.setFont(new Font("����", Font.PLAIN, 15));
		ecmNameText.setColumns(10);
		TextChecker nameChecker = new TextChecker(ecmNameText, this, "֤�������Ʊ�������5����", 5, 0);
		nameChecker.setCheckType(CheckType.Length);
		addChecker(nameChecker);

		JLabel lblNewLabel_1 = new JLabel("\u8BC1\u636E\u94FE\u7B80\u4ECB\uFF1A");
		lblNewLabel_1.setFont(new Font("����", Font.PLAIN, 15));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup().addContainerGap()
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup().addComponent(lblNewLabel)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(ecmNameText,
												GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE))
								.addGroup(groupLayout.createSequentialGroup().addComponent(lblNewLabel_1)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(scrollPane,
												GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)))
						.addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
				.createSequentialGroup().addContainerGap()
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblNewLabel).addComponent(
						ecmNameText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGap(18).addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(lblNewLabel_1)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE))
				.addContainerGap()));

		ecmDescText = new JTextArea();
		ecmDescText.setLineWrap(true);
		ecmDescText.setFont(new Font("Monospaced", Font.PLAIN, 15));
		scrollPane.setViewportView(ecmDescText);
		TextChecker descChecker = new TextChecker(ecmDescText, this, "֤������������20����", 20, 0);
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