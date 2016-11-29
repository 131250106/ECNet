package cn.edu.nju.ecm.entity.detail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.nju.ecm.entity.Element;
import cn.edu.nju.ecm.file.ECMFileManage;
import cn.edu.nju.ecm.utility.EvidenceFileTypeManagement.EvidenceFileType;

public class EBody extends Element {

	private List<EHeader> children;
	private Map<String, EvidenceFileType> evidenceFilePathsAndType;

	public EBody() {
		super();
		this.setChildren(new ArrayList<EHeader>());
		this.setEvidenceFilePathsAndType(new HashMap<String, EvidenceFileType>());
	}

	public EBody(String name, String content) {
		super(name, content);
		this.setChildren(new ArrayList<EHeader>());
		this.setEvidenceFilePathsAndType(new HashMap<String, EvidenceFileType>());
	}

	public List<EHeader> getChildren() {
		return children;
	}

	public void setChildren(List<EHeader> children) {
		this.children = children;
	}

	public Map<String, EvidenceFileType> getEvidenceFilePathsAndType() {
		return evidenceFilePathsAndType;
	}

	public void setEvidenceFilePathsAndType(Map<String, EvidenceFileType> evidenceFilePathsAndType) {
		this.evidenceFilePathsAndType = evidenceFilePathsAndType;
	}

	public String toXMLString() {
		String result = super.toXMLString();
		result += "\t\t<files>" + ECMFileManage.NEW_LINE;
		for (String filePath : this.getEvidenceFilePathsAndType().keySet()) {
			result += "\t\t\t<filePath type=\"" + this.getEvidenceFilePathsAndType().get(filePath) + "\">" + filePath
					+ "</filePath>" + ECMFileManage.NEW_LINE;
		}
		result += "\t\t</files>" + ECMFileManage.NEW_LINE;
		return result;
	}
}
