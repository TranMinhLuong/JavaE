package tmluong.vku.tygia.Model;

import java.io.Serializable;
import java.util.Vector;

public class VectorModels implements Serializable{
	private Vector<Models> models;
	private String event;
	private int length;
	

	public VectorModels(Vector<Models> models, String event, int length) {
		super();
		this.models = models;
		this.event = event;
		this.length = length;
	}
	
	
	
	public VectorModels(int length) {
		super();
		this.length = length;
	}



	public VectorModels(String event) {
		super();
		this.event = event;
	}



	public VectorModels(Vector<Models> models) {
		super();
		this.models = models;
	}



	public Vector<Models> getModels() {
		return models;
	}
	public void setModels(Vector<Models> models) {
		this.models = models;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}

	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.event;
	}
	
}
