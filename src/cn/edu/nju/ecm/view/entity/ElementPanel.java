package cn.edu.nju.ecm.view.entity;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import cn.edu.nju.ecm.entity.Element;
import cn.edu.nju.ecm.view.checker.ContentChecker;

public class ElementPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<ContentChecker> checkers;

	/**
	 * Create the panel.
	 */
	public ElementPanel() {
		this.checkers = new ArrayList<ContentChecker>();
	}

	public Element generateElement() {
		return null;
	}

	public boolean checkContent() {
		for (ContentChecker checker : this.checkers) {
			if (!checker.check()) {
				return false;
			}
		}
		return true;
	}

	public void addChecker(ContentChecker checker) {
		this.checkers.add(checker);
	}
}
