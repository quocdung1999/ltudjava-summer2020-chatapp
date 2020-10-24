package Analyze;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Vector;

import com.sun.org.apache.bcel.internal.generic.NEW;

import GUI.ClientGUI;
import sun.applet.resources.MsgAppletViewer;

public class ClientHandler extends Thread {
	private String username;
	private final ObjectInputStream ois;
	private final ObjectOutputStream oos;
	private Socket s;
	public ClientHandler(Socket s,String username, ObjectInputStream ois, ObjectOutputStream oos) 
			throws UnknownHostException, IOException
	{
		this.s = s;
		this.oos = oos;
		this.ois = ois;	
		this.username = username;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		while (this.s.isConnected())
		{	
			try {
				Message received = (Message) ois.readObject();
				switch (received.getType()) {
					case "addGroup":
					{
						boolean outFlag = true;
						Vector<String> users = received.getUsers();
						for (int i=0;i<users.size();i++)
						{
							boolean flag = false;
							for (ClientHandler c:Server.active)
							{
								if (c.username.equals(users.get(i)))
								{
									flag = true;
									break;
								}
							}
							if (flag == false)
							{
								oos.writeObject(new Message("addGroupFailed", null, null, null, null));
								outFlag = false;
								break;
							}
						}
						if (outFlag == true)
						{
							
							
							
							for (String user:users)
							{
								for (ClientHandler c:Server.active)
								{
									if (c.username.equals(user))
									{	
										StringBuilder groupName = new StringBuilder();
										for (String u:users)
										{
											if (!c.username.equals(u))
											{
												groupName.append(u+", ");
											}
										}
										groupName.delete(groupName.length()-2, groupName.length());
										if (!c.username.equals(received.getFrom()))
											c.oos.writeObject
											(new Message("addGroup", groupName.toString(), "no", null, null));
										else
											c.oos.writeObject
											(new Message("addGroup", groupName.toString(), "yes", null, null));
									}
								}
							}
						}
						break;
					}
					case "message" :
					{
						for (ClientHandler c : Server.active)
						{
							if (received.getUsers().size()<2)
							{
								if (c.getUsername().equals(received.getUsers().get(0)))
								{
									Message m = new Message(received.getType(), received.getMessage(),received.getFrom(),
											received.getUsers(), null);
									c.oos.writeObject(m);
									break;
								}
							}
							else
							{
								
								if (received.getUsers().contains(c.getUsername()))
								{
									Vector<String> users = new Vector<String>();
									users.add(username);
									Message m = new Message(received.getType(), received.getMessage(),received.getFrom(),
											users, null);
	
									c.oos.writeObject(m);
								}
							}
						}
						break;
					}
					case "update" :
					{
						switch (received.getMessage())
						{
							case "setName":
							{
								this.setUsername(received.getFrom());
								break;
							}
							case "add":
							{
								Server.active.add(this);
								Message m = new Message(received.getType(),received.getMessage(), null, null, null);
								Message n = new Message("initial","getList",null,null,null);
								Vector<String> users = new Vector<String>();								
								for (ClientHandler c:Server.active)
								{
									//System.out.println("Active: "+c.username);
									users.add(c.username);
								}
								for (ClientHandler c:Server.active)
								{
									Vector<String> user = (Vector<String>) users.clone();
									user.remove(c.username);
									m.setUsers(user);
									if (!c.username.equals(this.username))
										c.oos.writeObject(m);
									else
									{
										n.setUsers(user);
										c.oos.writeObject(n);
									}
								}
								break;
							}
							case "remove":
							{
								Server.active.remove(this);
								Message m = new Message(received.getType(), received.getMessage(), 
										received.getFrom(),null, null);
								Vector<String> users = new Vector<String>();

								for (ClientHandler c:Server.active)
								{
									c.oos.writeObject(m);
								}
								stop();
								break;
							}
							
						}
						break;
					}
					case "file":
					{
						Vector<String> users = received.getUsers();
						for (String user : users)
						{
							for (ClientHandler c:Server.active)
							{
								if (c.username.equals(user)&&!c.username.equals(username))
								{
									c.oos.writeObject(received);
									break;
								}
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClientHandler other = (ClientHandler) obj;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
}
