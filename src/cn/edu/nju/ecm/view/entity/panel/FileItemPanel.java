package cn.edu.nju.ecm.view.entity.panel;

import javax.swing.JPanel;


import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.event.ActionEvent;

public class FileItemPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JTextField fileNameText;
	public JButton deleteFileButton;

	/**
	 * Create the panel.
	 */
	public FileItemPanel(String filePath, JPanel parent, List<String> filePaths) {
		FileItemPanel tmpFile = this;
		
		fileNameText = new JTextField(filePath);
		fileNameText.setEditable(false);
		fileNameText.setColumns(10);
		
		deleteFileButton = new JButton("\u5220\u9664");
		deleteFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent.remove(tmpFile);
				filePaths.remove(fileNameText.getText());
				parent.updateUI();
			}
		});
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addGap(2)
					.addComponent(deleteFileButton, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(fileNameText, GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(fileNameText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(deleteFileButton))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		setLayout(groupLayout);

	}

}
