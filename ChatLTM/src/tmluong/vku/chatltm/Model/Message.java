package tmluong.vku.chatltm.Model;

import java.io.Serializable;

public class Message implements Serializable {
	private String name;
	private String message;
	private byte[] files;
	
	public Message(String name, String message) {
		super();
		this.name = name;
		this.message = message;
	}

	public Message(String name, String message, byte[] files) {
		super();
		this.name = name;
		this.message = message;
		this.files = files;
	}

	public Message(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	public byte[] getFiles() {
		return files;
	}

	public void setFiles(byte[] files) {
		this.files = files;
	}

	@Override
	public String toString() {
		return this.name+"\t"+this.message;
	}
	
}
