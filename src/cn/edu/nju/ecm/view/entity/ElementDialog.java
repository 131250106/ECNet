package cn.edu.nju.ecm.view.entity;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import cn.edu.nju.ecm.entity.Element;

public class ElementDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum ElementType {
		Model, EBody, EHeader, Connector, HRelation
	}

	public enum OpenModal {
		Create, Edit, View
	}

	private JPanel contentPane;
	private JButton okButton;
	private JButton cancelButton;

	private Element resultElement;
	private ElementType type;
	private OpenModal modal;

	private ElementPanel elementPanel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ElementDialog frame = new ElementDialog(null, ElementType.EBody);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ElementDialog(JFrame owner, ElementType type) {
		super(owner, true);
		setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);

		this.type = type;

		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(ElementPanelFactory.getRecByType(type));
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel buttonPanel = new JPanel();
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));

		okButton = new JButton();
		buttonPanel.add(okButton);

		cancelButton = new JButton("È¡Ïû");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				resultElement = null;
				setVisible(false);
				dispose();
			}

		});
		buttonPanel.add(cancelButton);

		JPanel informationPanel = new JPanel();
		contentPane.add(informationPanel, BorderLayout.CENTER);

		this.elementPanel = ElementPanelFactory.getPanelByType(type);
		contentPane.add(elementPanel, BorderLayout.CENTER);
	}

	public Element showCreateDialog() {
		this.modal = OpenModal.Create;
		this.setTitle(ElementPanelFactory.getTitleByModelType(modal, type));
		okButton.setText(ElementPanelFactory.getButtonTextByOpenModel(modal));
		okButton.addActionListener(new SaveActionListener());
		this.setVisible(true);

		return this.resultElement;
	}

	public Element showEditDialog(Element formerElement) {
		return null;
	}

	public Element showViewDialog(Element formerElement) {
		return null;
	}

	class SaveActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			if (!elementPanel.checkContent()) {
				return;
			}
			resultElement = elementPanel.generateElement();
			setVisible(false);
			dispose();
		}

	}

	class EditActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub

		}

	}

}
