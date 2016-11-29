package cn.edu.nju.ecm.entity;

import java.util.Date;

import cn.edu.nju.ecm.file.ECMFileManage;

public class Element {

	public enum Status {
		Normal, Deleted
	}

	protected int ID;
	protected String name = "";
	protected String content = "";
	protected Date createTime;
	protected Date lastEditTime;

	protected Status status;

	public Element() {
		this.createTime = new Date();
		this.lastEditTime = new Date();
		this.status = Status.Normal;
	}

	public Element(String name, String content) {
		this();
		this.name = name;
		this.content = content;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastEditTime() {
		return lastEditTime;
	}

	public void setLastEditTime(Date lastEditTime) {
		this.lastEditTime = lastEditTime;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String toXMLString() {
		String result = "\t\t<name>" + this.getName() + "</name>" + ECMFileManage.NEW_LINE;
		result += "\t\t<content>" + this.getContent() + "</content>" + ECMFileManage.NEW_LINE;
		return result;
	}
}
