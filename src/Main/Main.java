package Main;

import java.awt.EventQueue;
import java.io.IOException;

import Analyze.*;
import GUI.ClientGUI;
import GUI.LoginGUI;

public class Main {
	public static void main(String[] args ) throws IOException
	{
		LoginGUI l = new LoginGUI();
		Server.init();
	}
}
