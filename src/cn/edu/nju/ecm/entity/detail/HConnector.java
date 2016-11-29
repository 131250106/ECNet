package cn.edu.nju.ecm.entity.detail;

import java.util.ArrayList;
import java.util.List;

import cn.edu.nju.ecm.entity.Element;

public class HConnector extends Element{

	private List<EHeader> input;
	private List<EHeader> output;
	
	public HConnector(){
		super();
		this.setInput(new ArrayList<EHeader>());
		this.setOutput(new ArrayList<EHeader>());
	}
	
	public HConnector(String name, String content){
		super(name, content);
		this.setInput(new ArrayList<EHeader>());
		this.setOutput(new ArrayList<EHeader>());
	}

	public List<EHeader> getInput() {
		return input;
	}

	public void setInput(List<EHeader> input) {
		this.input = input;
	}

	public List<EHeader> getOutput() {
		return output;
	}

	public void setOutput(List<EHeader> output) {
		this.output = output;
	} 
}
