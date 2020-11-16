package tmluong.vku.chatltm.Model;

import java.io.Serializable;
import java.net.Socket;

public class User implements Serializable {

	private String user;
	private Socket socket;
	private String id;

	public User() {
		
	}

	public User(String user, Socket socket, String id) {
		super();
		this.user = user;
		this.socket = socket;
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		user = user;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
	
}
