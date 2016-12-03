package cn.edu.nju.ecm.canvas.model.format;

public class Node {
	
	private int x;
	private int y;
	private String Id;
	
	public Node(int x,int y,String id){
		this.x = x;
		this.y = y;
		this.Id = id;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}
	
	
}
