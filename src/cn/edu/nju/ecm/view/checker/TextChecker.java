package cn.edu.nju.ecm.view.checker;

import java.awt.Component;

import javax.swing.JOptionPane;
import javax.swing.text.JTextComponent;

public class TextChecker implements ContentChecker {

	private JTextComponent toBeChecked;
	private Component parent;

	private String message;
	private int minLength = 0;
	private int maxLength = 0;

	public enum CheckType {
		Length, Contain
	}

	private CheckType checkType;

	public TextChecker(JTextComponent checked, Component parent, String message, int minLength, int maxLength) {
		this.toBeChecked = checked;
		this.parent = parent;
		this.message = message;
		this.minLength = minLength;
		this.maxLength = maxLength;
	}

	public void setCheckType(CheckType type) {
		this.checkType = type;
	}

	@Override
	public boolean check() {
		// TODO Auto-generated method stub
		boolean result = true;
		if (this.checkType == CheckType.Length) {
			result = this.checkLength();
			if (!result) {
				JOptionPane.showMessageDialog(parent, this.message);
				return result;
			}
		} else {

		}
		return result;
	}

	public boolean checkLength() {
		boolean ok = true;
		int length = this.toBeChecked.getText().length();
		if (minLength > 0) {
			ok = ok && (length >= minLength ? true : false);
		}
		if (maxLength > 0) {
			ok = ok && (length <= maxLength ? true : false);
		}
		return ok;
	}

}
