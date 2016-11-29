package cn.edu.nju.ecm.view;

import java.awt.Component;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ViewHelper {

	public static void fileTitleDisplay(String fileName, JLabel titleLabel) {
		if (fileName.length() > 14) {
			titleLabel.setText(fileName.substring(0, 11) + "...");
		} else {
			titleLabel.setText(fileName);
		}
	}

	public static void showSaveFileDialog(Component parent, CanvasPanel canvasPanel, JTabbedPane tabbedCanvasPanel)
			throws IOException {
		JFileChooser chooser = new JFileChooser("ѡ�񱣴�Ŀ¼") {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void approveSelection() {
				File saveFile = this.getSelectedFile();

				String savePath = saveFile.getAbsolutePath();
				if (!savePath.endsWith(".ecm")) {
					savePath += ".ecm";
				}

				int tabCount = tabbedCanvasPanel.getTabCount();
				for (int canvasIndex = 0; canvasIndex < tabCount; canvasIndex++) {
					File openFileTmp = ((CanvasPanel) tabbedCanvasPanel.getComponentAt(canvasIndex)).model.getFile();
					if (openFileTmp != null && openFileTmp.getAbsolutePath().equals(savePath)) {
						JOptionPane.showMessageDialog(parent, "����ѡ�ļ��Ѿ��򿪣�������ѡ��");
						return;
					}
				}
				if (saveFile.exists()) {
					int overWrite = JOptionPane.showConfirmDialog(parent, "��ѡ����ļ��Ѿ����ڣ���Ҫ����ԭ�ļ���", "�Ƿ񸲸�",
							JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					if (overWrite != JOptionPane.YES_OPTION) {
						return;
					}
				}
				super.approveSelection();
			}

		};
		chooser.setFileFilter(new FileNameExtensionFilter("Evidence Chain: .ecm", "ecm"));
		int returnValue = chooser.showSaveDialog(parent);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File saveFile = chooser.getSelectedFile();
			String savePath = saveFile.getAbsolutePath();
			if (!savePath.endsWith(".ecm")) {
				savePath += ".ecm";
			}
			canvasPanel.saveModel(savePath);
		}
	}

	public static File showOpenFileDialog(Component parent, FileNameExtensionFilter fileFilter, List<String> existing) {
		JFileChooser chooser = new JFileChooser("ѡ��ѡ����Ҫ�򿪵��ļ�") {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void approveSelection() {
				File saveFile = this.getSelectedFile();
				String savePath = saveFile.getAbsolutePath();

				if (existing != null && existing.contains(savePath)) {
					JOptionPane.showMessageDialog(parent, "����ѡ�ļ��Ѿ��򿪣�������ѡ��");
					return;
				}

				super.approveSelection();
			}

		};

		chooser.setFileFilter(fileFilter);
		int returnValue = chooser.showOpenDialog(parent);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			return new File(chooser.getSelectedFile().getAbsolutePath());
		} else {
			return null;
		}
	}

	public static Rectangle getBounds(int width, int height) {
		return new Rectangle(ECMMainFrame.ScreenCenterX - width / 2, ECMMainFrame.ScreenCenterY - height / 2, width, height);
	}
}
