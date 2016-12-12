package cn.edu.nju.ecm.entity.detail;

import java.util.ArrayList;
import java.util.List;

import cn.edu.nju.ecm.entity.Element;
import cn.edu.nju.ecm.file.ECMFileManage;

public class EHeader extends Element {

	private EBody father;
	private List<Element> outputEntities;
	private List<Element> inputEntities;
	
	private String keySentence;				//证据中的关键文本

	public EHeader() {
		super();
		this.outputEntities = new ArrayList<Element>();
		this.inputEntities = new ArrayList<Element>();
	}

	public EHeader(String name, String content, String keySentence) {
		super(name, content);
		this.keySentence = keySentence;
		this.outputEntities = new ArrayList<Element>();
		this.inputEntities = new ArrayList<Element>();
	}

	public EBody getFather() {
		return father;
	}

	public void setFather(EBody father) {
		this.father = father;
	}

	public List<Element> getOutputEntities() {
		return outputEntities;
	}

	public void setOutputEntities(List<Element> outputEntities) {
		this.outputEntities = outputEntities;
	}

	public List<Element> getInputEntities() {
		return inputEntities;
	}

	public void setInputEntities(List<Element> inputEntities) {
		this.inputEntities = inputEntities;
	}

	public String getKeySentence() {
		return keySentence;
	}

	public void setKeySentence(String keySentence) {
		this.keySentence = keySentence;
	}

	public String toXMLString() {
		String result = super.toXMLString();
		result += "\t\t<keySentence>" + this.getKeySentence() + "</keySentence>" + ECMFileManage.NEW_LINE;
		return result;
	}
}
