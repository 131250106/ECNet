package cn.edu.nju.ecm.view.entity.panel;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;

import cn.edu.nju.ecm.entity.Element;
import cn.edu.nju.ecm.entity.detail.EBody;
import cn.edu.nju.ecm.utility.EvidenceFileTypeManagement;
import cn.edu.nju.ecm.utility.EvidenceFileTypeManagement.EvidenceFileType;
import cn.edu.nju.ecm.view.ViewHelper;
import cn.edu.nju.ecm.view.entity.ElementPanel;

public class EBodyPanel extends ElementPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField ebodyName;
	private JTextArea ebodyContent;
	private JPanel panel;

	private List<String> filePaths = new ArrayList<String>();

	/**
	 * Create the panel.
	 */
	public EBodyPanel() {

		JLabel lblNewLabel = new JLabel("链体名称");
		lblNewLabel.setFont(new Font("宋体", Font.PLAIN, 15));

		ebodyName = new JTextField();
		ebodyName.setFont(new Font("宋体", Font.PLAIN, 15));
		ebodyName.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("链体内容");
		lblNewLabel_1.setFont(new Font("宋体", Font.PLAIN, 15));

		JLabel lblNewLabel_2 = new JLabel("关联文件");
		lblNewLabel_2.setFont(new Font("宋体", Font.PLAIN, 15));

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		panel = new JPanel();
		scrollPane_1.setViewportView(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JButton addFileButton = new JButton("添加文件");
		addFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File selectedFile = ViewHelper.showOpenFileDialog(panel, null, filePaths);
				if (selectedFile != null) {
					String opennedPath = selectedFile.getAbsolutePath();
					filePaths.add(opennedPath);
					FileItemPanel fileItem = new FileItemPanel(selectedFile.getAbsolutePath(), panel, filePaths);
					panel.add(fileItem);
					panel.updateUI();
				}
			}
		});
		JScrollPane scrollPane = new JScrollPane();

		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
				groupLayout.createParallelGroup(Alignment.TRAILING).addGroup(groupLayout.createSequentialGroup()
						.addContainerGap().addGroup(groupLayout
								.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup()
										.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
												.addComponent(lblNewLabel_1).addComponent(lblNewLabel))
										.addPreferredGap(ComponentPlacement.RELATED)
										.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
												.addComponent(ebodyName, GroupLayout.DEFAULT_SIZE, 351,
														Short.MAX_VALUE)
												.addComponent(scrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE,
														351, Short.MAX_VALUE)))
								.addGroup(groupLayout.createSequentialGroup().addComponent(lblNewLabel_2)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
												.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 351,
														Short.MAX_VALUE)
												.addComponent(addFileButton, GroupLayout.PREFERRED_SIZE, 103,
														GroupLayout.PREFERRED_SIZE))))
						.addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
				.createSequentialGroup().addContainerGap()
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblNewLabel).addComponent(
						ebodyName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(lblNewLabel_1)
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblNewLabel_2)
						.addComponent(addFileButton))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE).addContainerGap()));

		ebodyContent = new JTextArea();
		ebodyContent.setLineWrap(true);
		ebodyContent.setWrapStyleWord(true);
		scrollPane.setViewportView(ebodyContent);
		setLayout(groupLayout);

	}

	public Element generateElement() {
		String name = ebodyName.getText();
		String content = ebodyContent.getText();
		EBody ebody = new EBody(name, content);
		Map<String, EvidenceFileType> evidenceFiles = new HashMap<String, EvidenceFileType>();
		for (String filePath : this.filePaths) {
			String[] fileNameParts = filePath.split("\\.");
			evidenceFiles.put(filePath,
					EvidenceFileTypeManagement.decodeFromExtension(fileNameParts[fileNameParts.length - 1]));
		}

		ebody.setEvidenceFilePathsAndType(evidenceFiles);

		return ebody;
	}
}
