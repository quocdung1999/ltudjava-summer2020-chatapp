package Analyze;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;


public class Server {
	public static Vector<ClientHandler> active;
	public static Vector<Vector<String>> users;
	private static final int serverPort = 5000;
	public static void init() {
    	
    	try {
    		users = new Vector<Vector<String>>();
    		active = new Vector<ClientHandler>();
    		ServerSocket ss = new ServerSocket(serverPort);
    		do {
				System.out.println("Waiting for a client");
				
				Socket s = ss.accept();
				
				ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
				ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
				System.out.println("Welcome client");
				
				
				ClientHandler c = new ClientHandler(s, "", ois, oos);
				c.start();
				
    		} while (true);
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    	}
    }
    
   
    public static boolean addUser(String username,String password)
    {
    	for (Vector<String> temp:users)
    	{
    		if (temp.get(0).equals(username))
    		{
    			return false;
    		}
    	}
    	Vector<String> user = new Vector<String>();
    	user.add(username);user.add(password);
    	users.add(user);
    	return true;
    }
    
   
}
