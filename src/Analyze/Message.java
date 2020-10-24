package Analyze;

import java.io.Serializable;
import java.util.Vector;

@SuppressWarnings("serial")
public class Message implements Serializable {
	private String type;
	private String message;
	private String from;
	private Vector<String> users;
	private byte[] data;
	public Vector<String> getUsers() {
		return users;
	}
	public void setUsers(Vector<String> user) {
		this.users = user;
	}
	public String getType() {
		return type;
	}
	public String getMessage() {
		return message;
	}
	public byte[] getData() {
		return data;
	}
	public Message(String type, String message, String from, Vector<String> users, byte[] data) {
		super();
		this.type = type;
		this.message = message;
		this.from = from;
		this.users = users;
		this.data = data;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	
}
