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
	
	private String evidenceType;		//证据类型
	private String commiter;	//提交人
	private String 	evidenceReason; 	//质证理由
	private String 	evidenceConclusion;	//质证结论

	public EBody() {
		super();
		this.setChildren(new ArrayList<EHeader>());
		this.setEvidenceFilePathsAndType(new HashMap<String, EvidenceFileType>());
	}

	public EBody(String name, String content, String evidenceType, String commiter, String evidenceReason, String evidenceConclusion) {
		super(name, content);
		
		this.evidenceType = evidenceType;
		this.commiter = commiter;
		this.evidenceReason = evidenceReason;
		this.evidenceConclusion = evidenceConclusion;
		
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
	
	public String getEvidenceType() {
		return evidenceType;
	}

	public void setEvidenceType(String evidenceType) {
		this.evidenceType = evidenceType;
	}

	public String getCommiter() {
		return commiter;
	}

	public void setCommiter(String commiter) {
		this.commiter = commiter;
	}

	public String getEvidenceReason() {
		return evidenceReason;
	}

	public void setEvidenceReason(String evidenceReason) {
		this.evidenceReason = evidenceReason;
	}

	public String getEvidenceConclusion() {
		return evidenceConclusion;
	}

	public void setEvidenceConclusion(String evidenceConclusion) {
		this.evidenceConclusion = evidenceConclusion;
	}

	public String toXMLString() {
		String result = super.toXMLString();
		result += "\t\t<evidenceType>" + this.getEvidenceType() + "</evidenceType>" + ECMFileManage.NEW_LINE;
		result += "\t\t<commiter>" + this.getCommiter() + "</commiter>" + ECMFileManage.NEW_LINE;
		result += "\t\t<evidenceReason>" + this.getEvidenceReason() + "</evidenceReason>" + ECMFileManage.NEW_LINE;
		result += "\t\t<evidenceConclusion>" + this.getEvidenceConclusion() + "</evidenceConclusion>" + ECMFileManage.NEW_LINE;
		result += "\t\t<files>" + ECMFileManage.NEW_LINE;
		for (String filePath : this.getEvidenceFilePathsAndType().keySet()) {
			result += "\t\t\t<filePath type=\"" + this.getEvidenceFilePathsAndType().get(filePath) + "\">" + filePath
					+ "</filePath>" + ECMFileManage.NEW_LINE;
		}
		result += "\t\t</files>" + ECMFileManage.NEW_LINE;
		return result;
	}
}
