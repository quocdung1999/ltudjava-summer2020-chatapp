package GUI;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;

import Analyze.*;


import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.EOFException;
import java.io.IOException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;


import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import javax.swing.SwingConstants;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.event.ListSelectionListener;

import java.io.File;
import java.io.FileOutputStream;

import javax.swing.event.ListSelectionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ClientGUI extends JFrame {
	private final int serverPort = 5000;
	private JPanel contentPane;
	private Socket s;
	private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private JTextArea textArea;
    private JPanel onlinePanel;
    private JPanel outPanel;
    private JPanel chatPanel;
    private JButton sendButton;
    private JButton fileButton;
    private JList<String> list;
	private String previous = null;
	private ReadThread r;
	private String user;
	private Vector<ChatPanel> chats;
	
	public void init() throws UnknownHostException, IOException
	{
		this.s = new Socket("localhost",serverPort);
		this.oos = new ObjectOutputStream(s.getOutputStream()); 
		this.ois = new ObjectInputStream(s.getInputStream()); 
		chats = new Vector<ChatPanel>();
	}
	public ClientGUI(String username) throws UnknownHostException, IOException   {
		init();
		
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setSize(1300,730);
		setLocationRelativeTo(null);
		
		user = new String(username);
		
		r = new ReadThread();
		Thread t = new Thread(r);
		t.start();
		t.setPriority(Thread.MAX_PRIORITY);
		list = new JList<String>();
		list.setBackground(new Color(255,250,205));
		
		oos.writeObject(new Message("update", "setName", username, null, null));		
			
		oos.writeObject(new Message("update", "add", null, null, null));
		
		setTitle("Xin chào "+ username );

		addWindowListener(new WindowAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void windowClosed(WindowEvent e) {
				try {
					t.stop();				
					oos.writeObject(new Message("update", "remove", username, null, null));
					ois.close();
					oos.close();
					s.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		
		contentPane = new JPanel();
		contentPane.setLayout(null);
		contentPane.setBackground(new Color(152,251,152));
		contentPane.setBorder(new EmptyBorder(0, 0, 0 , 0));
		setContentPane(contentPane);
		
		JPanel headerPanel = new JPanel();
		headerPanel.setBounds(0, 0, 1294, 45);
		headerPanel.setBackground(new Color(152,251,152));
		contentPane.add(headerPanel);
		headerPanel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Danh sách Online");
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 16));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(10, 11, 235, 23);
		headerPanel.add(lblNewLabel);
		
		JButton groupButton = new JButton("Tạo nhóm chat");
		groupButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (((DefaultListModel<String>) list.getModel()).size()>0)
				{
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							try {
								
								ChooseGUI c = new ChooseGUI(username,(DefaultListModel<String>) list.getModel(),oos);
							} catch (Exception f) {
								f.printStackTrace();
							}
						}
					});
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Không có người đang online!");
				}
			}
		});
		groupButton.setBackground(Color.ORANGE);
		groupButton.setFont(new Font("Tahoma", Font.BOLD, 13));
		groupButton.setBounds(1147, 5, 137, 33);
		headerPanel.add(groupButton);
		
		onlinePanel = new JPanel();
		onlinePanel.setBounds(0, 45, 248, 656);
		contentPane.add(onlinePanel);
		onlinePanel.setLayout(new BorderLayout(0, 0));
		
		
		
		outPanel = new JPanel();
		outPanel.setBackground(new Color(152,251,152));
		outPanel.setBounds(248, 45, 1046, 656);
		contentPane.add(outPanel);
		outPanel.setLayout(null);
		
		chatPanel = new JPanel();
		chatPanel.setBounds(32, 0, 993, 550);
		chatPanel.setBackground(new Color(152,251,152));
		chatPanel.setLayout(null);
		//outPanel.add(chatPanel);
		
		textArea = new JTextArea();
		textArea.setBackground(new Color(255,250,205));
		textArea.setBounds(32, 572, 865, 73);
		textArea.setFont(new Font("Times New Roman",Font.BOLD,17));
		
		textArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode()== KeyEvent.VK_ENTER)
				{
					String msg = textArea.getText();
		            textArea.setText("");
		            if (msg.length()>100)
		            {
		            	JOptionPane.showMessageDialog(null, "Số lượng kí tự không được vượt quá 100");
		            }
		            else if (!msg.isEmpty())  
		                try { 
		                    // write on the output stream 
		                	Vector<String> s = new Vector<String>();
		                	
		                	JLabel j = createMyMessage(msg);
		                	
		                	chatPanel.setVisible(false);
		                	chatPanel.add(j);
		                	chatPanel.setVisible(true);
		                	if (!previous.contains(","))
			                {
		                		s.add(previous);
			                    oos.writeObject(new Message("message", msg, username, 
			                    		s, null));
		                	}
		                	else
		                	{
		                		StringTokenizer st = new StringTokenizer(previous, ", ");
		                		while (st.hasMoreTokens())
		                		{
		                			s.add(st.nextToken());
		                		}
		                		oos.writeObject(new Message("message", msg, previous+", "+username, 
			                    		s, null));
		                	}
		                    
		                } catch (IOException e1) { 
		                    e1.printStackTrace(); 
		   
		 
		                }
				}
			}
		});
		
		sendButton = new JButton("Gửi");
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 
								
            // read the message to deliver. 
			
            String msg = textArea.getText();
            textArea.setText("");
            if (msg.length()>100)
            {
            	JOptionPane.showMessageDialog(null, "Số lượng kí tự không được vượt quá 100");
            }
            else if (!msg.isEmpty())  
                try { 
                    // write on the output stream 
                	Vector<String> s = new Vector<String>();
                	
                	JLabel j = createMyMessage(msg);
                	
                	chatPanel.setVisible(false);
                	chatPanel.add(j);
                	chatPanel.setVisible(true);
                	if (!previous.contains(","))
	                {
                		s.add(previous);
	                    oos.writeObject(new Message("message", msg, username, 
	                    		s, null));
                	}
                	else
                	{
                		StringTokenizer st = new StringTokenizer(previous, ", ");
                		while (st.hasMoreTokens())
                		{
                			s.add(st.nextToken());
                		}
                		oos.writeObject(new Message("message", msg, previous+", "+username, 
	                    		s, null));
                	}
                } catch (IOException e1) { 
                    e1.printStackTrace(); 
   
 
                }
			}
		});
		sendButton.setBackground(Color.ORANGE);
		sendButton.setFont(new Font("Tahoma", Font.BOLD, 17));
		sendButton.setBounds(907, 605, 118, 40);
		
		fileButton = new JButton("Gửi File");
		fileButton.setBackground(Color.ORANGE);
		fileButton.setFont(new Font("Tahoma", Font.BOLD, 17));
		fileButton.setBounds(907,570,118,30);
		
		fileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = 	new JFileChooser();
				fileChooser.setMultiSelectionEnabled(false);
				fileChooser.setDialogTitle("Chọn file để gửi");
				if (fileChooser.showOpenDialog(fileButton) == JFileChooser.APPROVE_OPTION)
				{
					File file = fileChooser.getSelectedFile();
					if (file.isFile())
					{
						try {
							byte[] data = Files.readAllBytes(file.toPath());
							Vector<String> users = new Vector<String>();
							if (!previous.contains(","))
							{
								users.add(previous);
								oos.writeObject(new Message("file", file.getName(), username, users, data));
							}
							else
							{
								StringTokenizer st = new StringTokenizer(previous, ", ");
		                		while (st.hasMoreTokens())
		                		{
		                			users.add(st.nextToken());
		                		}
		                		users.add(username);
		                		oos.writeObject(new Message("file", file.getName(), username, users, data));
							}
								JLabel j = createMyMessage(file.getName());
								j.setFont(new Font("Times New Roman",Font.ITALIC,17));
								j.addMouseListener(new MouseAdapter() {
									@Override
									public void mouseClicked(MouseEvent e) {
									JFileChooser dChooser = new JFileChooser();
						            dChooser.setDialogTitle("Chọn đường dẫn");
						            dChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
						            dChooser.setAcceptAllFileFilterUsed(false);
						            if (dChooser.showOpenDialog(chatPanel) == JFileChooser.APPROVE_OPTION)
						            {
						            	try {
							            		File f = new File(dChooser.getSelectedFile().toString()+"\\"+file.getName());
								            	f.createNewFile();
								            	FileOutputStream fos = new FileOutputStream(f);
								            	fos.write(data);
								            	fos.close();
								            	JOptionPane.showMessageDialog(chatPanel, "Tải file thành công!");
											} catch (IOException e1) {
												JOptionPane.showMessageDialog(chatPanel, "Tải file không thành công!");
											}  
						            }
									}
								});
								chatPanel.setVisible(false);
								chatPanel.add(j);
								chatPanel.setVisible(true);
							
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			}
		});
		
		setVisible(true);
		
	}	
	public JLabel createMyMessage(String msg)
	{
		int yBound = 0;
    	int index = 0;
    	for (int i=0;i<chats.size();i++)
    	{
    		if (chats.get(i).getUsername().equals(previous))
    		{
    			yBound = chats.get(i).getyBound();
    			index = i;
    			break;
    		}
    	}
		JLabel piece = new JLabel(msg);
    	piece.setOpaque(true);
		piece.setFont(new Font("Times New Roman",Font.BOLD,17));
		piece.setSize(piece.getPreferredSize());
		
		piece.setBackground(new Color(32,178,170));
		piece.setForeground(Color.WHITE);
		piece.setBounds(chatPanel.getWidth()-35-piece.getWidth(),yBound,piece.getWidth()+15,piece.getHeight()+5);
		piece.setHorizontalAlignment(JLabel.CENTER);
		chats.get(index).setyBound(chats.get(index).getyBound()+piece.getHeight()+10);
		if (chats.get(index).getyBound()>=chatPanel.getPreferredSize().getHeight())
		{
			chatPanel.setPreferredSize(new Dimension(993,chats.get(index).getyBound()+40));
		}
		return piece;
	}
	
	public JLabel createYourMessage(String msg,String from)
	{
		
		int yBound = 0;
    	int index = 0;
    	for (int i=0;i<chats.size();i++)
    	{
    		if (chats.get(i).getUsername().equals(from))
    		{
    			yBound = chats.get(i).getyBound();
    			index = i;
    			break;
    		}
    	}
		
		JLabel piece = new JLabel(msg);
		piece.setOpaque(true);
		piece.setFont(new Font("Times New Roman",Font.BOLD,17));
		piece.setSize(piece.getPreferredSize());
		piece.setBounds(13,yBound,piece.getWidth()+15,piece.getHeight()+5);
		piece.setHorizontalAlignment(JLabel.CENTER);
		piece.setBackground(new Color(220,220,220));
		piece.setForeground(Color.BLACK);
		chats.get(index).setyBound(chats.get(index).getyBound()+piece.getHeight()+10);
		if (chats.get(index).getyBound()>=chatPanel.getPreferredSize().getHeight())
		{
			chats.get(index).getChat().setPreferredSize(new Dimension(993,chats.get(index).getyBound()+40));
		}
		return piece;
	}
	public void replacePanel(String username,JPanel j)
	{
		for (ChatPanel c:chats)
		{
			if (c.getUsername().equals(username))
			{
				c.setChat(j);
				break;
			}
		}
	}
	public JPanel getPanel(String username)
	{
		for (ChatPanel c:chats)
		{
			if (c.getUsername().equals(username))
			{
				return c.getChat();
			}
		}
		return null;
	}
	
	public JPanel create()
	{
		JPanel chatPanel = new JPanel();
		chatPanel.setBounds(32, 0, 993, 550);
		chatPanel.setPreferredSize(new Dimension(993,550));
		chatPanel.setBackground(new Color(255,250,205));
		chatPanel.setLayout(null);
		return chatPanel;
	}
	class ReadThread implements Runnable
	{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			while (true)
			{		
				try {
					Message msg =  (Message) ois.readObject();
					switch (msg.getType())
					{
						case "addGroupFailed":
						{
							JOptionPane.showMessageDialog(chatPanel, "Có người trong nhóm đã thoát!", 
									"Tạo nhóm thất bại",JOptionPane.ERROR_MESSAGE);
							break;
						}
						case "addGroup":
						{
							DefaultListModel<String> d = (DefaultListModel<String>) list.getModel();
							
							String groupName = msg.getMessage();
							boolean isValid = true;
							for (int i = 0;i<d.size();i++)
							{			
								if (d.get(i).contains(", "))
								{
									boolean flag = false;
									StringTokenizer s = new StringTokenizer(groupName, ", ");
									while (s.hasMoreTokens())
									{
										String k = s.nextToken();
										if (!d.get(i).contains(k))
										{
											flag = true;
											break;
										}
									}
									if (flag == false)
									{
										isValid = false;
										break;
									}
								}
							}
							if (isValid)
							{
								list.setVisible(false);
								d.addElement(groupName);
								chats.add(new ChatPanel(groupName, create(), 10));
								if (msg.getFrom().equals("yes"))
								{
									JOptionPane.showMessageDialog(chatPanel, "Tạo nhóm thành công!");
								}
								list.setVisible(true);
							}
							else
							{
								if (msg.getFrom().equals("yes"))
								{
									JOptionPane.showMessageDialog(chatPanel, "Nhóm này đã được tạo", 
											"Tạo nhóm thất bại", JOptionPane.ERROR_MESSAGE);
								}
							}
							break;
						}
						case "message":
						{
							if (!msg.getFrom().contains(", "))
							{
								JLabel piece = createYourMessage(msg.getMessage(), msg.getFrom());
								if (msg.getFrom().equals(previous))
								{
	
									chatPanel.setVisible(false);
									chatPanel.add(piece);
									chatPanel.setVisible(true);
								}
								else
								{
									//System.out.println("Username: "+user +" from: "+msg.getFrom());
									JPanel panel = getPanel(msg.getFrom());
									panel.add(piece);
									replacePanel(msg.getFrom(), panel);
								}
							}
							else
							{
								StringTokenizer st = new StringTokenizer(msg.getFrom(), ", ");
								ArrayList<String> tokens = new ArrayList<String>(); 
								while (st.hasMoreTokens())
								{
									tokens.add(st.nextToken());
								}
								tokens.remove(user);
								String correct = new String();
								for (ChatPanel c:chats)
								{
									ArrayList<String> subs = new ArrayList<String>();
									String name = c.getUsername();
									StringTokenizer st1 = new StringTokenizer(name, ", ");
									while (st1.hasMoreTokens())
									{
										subs.add(st1.nextToken());
									}
									boolean flag = true;
									for (String k:tokens)
									{
										if (!subs.contains(k))
										{
											flag = false;
											break;
										}
									}
									if (flag == true)
									{
										correct = c.getUsername();
										break;
									}
								}
								JLabel piece = createYourMessage(msg.getUsers().get(0)+":  "+
								msg.getMessage(), correct);
								if (correct.equals(previous))
								{
	
									chatPanel.setVisible(false);
									chatPanel.add(piece);
									chatPanel.setVisible(true);
								}
								else
								{
									JPanel panel = getPanel(correct);
									panel.add(piece);
									replacePanel(correct, panel);
								}
							}
							break;
						}
						case "update":
						{
							Vector<String> users = msg.getUsers();
							DefaultListModel<String> d = (DefaultListModel<String>) list.getModel();
							list.setVisible(false);
							switch (msg.getMessage())
							{
								case "add":
								{
									for (String user:users)
									{
										if (!d.contains(user))
										{
											d.addElement(user);
											chats.add(new ChatPanel(user, create(), 10));
										}
									}
									break;
								}
								case "remove":
								{
									for (int i = 0;;)
									{
										if (i==d.size())
											break;
										if (d.get(i).contains(msg.getFrom()))
										{
											d.remove(i);
										}
										else
											i++;
									}
									for (int i = 0;;)
									{
										if (i==chats.size())
											break;
										if (chats.get(i).getUsername().contains(msg.getFrom()))
										{
											chats.remove(i);
										}
										else
											i++;
									}
									break;
								}
							}
							list.setModel(d);
							list.setVisible(true);
							break;
						}
						case "initial":
						{
							Vector<String> users = msg.getUsers();
							DefaultListModel<String> model = new DefaultListModel<String>();
							for (String user:users)
							{
								model.addElement(user);
								chats.add(new ChatPanel(user, create(), 10));
							}
							list.setVisible(false);
							list.setModel(model);
							list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
							
							
							list.setCellRenderer(new OnlineRenderer());
							
							list.setBorder(new LineBorder(Color.BLACK, 2));
							onlinePanel.add(list, BorderLayout.CENTER);
							list.addListSelectionListener(new ListSelectionListener() {
								public void valueChanged(ListSelectionEvent e) {
									if (!e.getValueIsAdjusting())
									{
										chatPanel.setBackground(new Color(255,250,205));
										if (previous == null)
										{
											previous = list.getSelectedValue();
											if ((getPanel(previous) != null))
											{
												outPanel.setVisible(false);
												chatPanel = getPanel(previous);
												JScrollPane chatScrollPane = new JScrollPane(chatPanel,   
														ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
														ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
												chatScrollPane.setBounds(32,0,993,559);
												outPanel.removeAll();
												outPanel.add(chatScrollPane);
												outPanel.add(fileButton);
												outPanel.add(textArea);
												outPanel.add(sendButton);
												outPanel.setVisible(true);
											}
										}
										else
										{
											replacePanel(previous, chatPanel);
											
											String pre = list.getSelectedValue();
											
											if (pre != null)
											{
												previous = pre;
												outPanel.setVisible(false);
												outPanel.removeAll();
												chatPanel = getPanel(previous);
												JScrollPane chatScrollPane = new JScrollPane(chatPanel,   
														ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.
														HORIZONTAL_SCROLLBAR_NEVER);
												chatScrollPane.setBounds(32,0,993,559);
												outPanel.add(chatScrollPane);
												outPanel.add(textArea);
												outPanel.add(fileButton);
												outPanel.add(sendButton);
												outPanel.setVisible(true);
											}
											else
											{
												DefaultListModel<String> d = (DefaultListModel<String>) 
														list.getModel();
												if (!d.contains(previous))
												{
													outPanel.setVisible(false);
													outPanel.removeAll();
													outPanel.setVisible(true);
												}
												else
												{
													list.setSelectedValue(previous, true);
												}
												previous = pre;
											}
										}
									}
								}
							});
							list.setVisible(true);
							break;
						}
						case "file":
						{
							if (msg.getUsers().size()<2)
							{
								String from = msg.getFrom();
								JLabel piece = createYourMessage(msg.getMessage(), from);
								piece.setFont(new Font("Times New Roman",Font.ITALIC,17));
								piece.addMouseListener(new MouseAdapter() {
									@Override
									public void mouseClicked(MouseEvent e) {
										JFileChooser dChooser = new JFileChooser();
							            dChooser.setDialogTitle("Chọn đường dẫn");
							            dChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
							            dChooser.setAcceptAllFileFilterUsed(false);
							            if (dChooser.showOpenDialog(chatPanel) == JFileChooser.APPROVE_OPTION)
							            {
							            	try {
							            		File f = new File(dChooser.getSelectedFile().toString()+"\\"+msg.getMessage());
								            	f.createNewFile();
								            	FileOutputStream fos = new FileOutputStream(f);
								            	fos.write(msg.getData());
								            	fos.close();
								            	JOptionPane.showMessageDialog(chatPanel, "Tải file thành công!");
											} catch (IOException e1) {
												JOptionPane.showMessageDialog(chatPanel, "Tải file không thành công!");
											}	
							            }
									}
									});
								if (msg.getFrom().equals(previous))
								{
	
									chatPanel.setVisible(false);
									chatPanel.add(piece);
									chatPanel.setVisible(true);
								}
								else
								{
									JPanel panel = getPanel(msg.getFrom());
									panel.add(piece);
									replacePanel(msg.getFrom(), panel);
								}		
							}
							else
							{
								
								Vector<String> tokens = msg.getUsers();
								tokens.remove(user);
								String correct = new String();
								for (ChatPanel c:chats)
								{
									ArrayList<String> subs = new ArrayList<String>();
									String name = c.getUsername();
									StringTokenizer st1 = new StringTokenizer(name, ", ");
									while (st1.hasMoreTokens())
									{
										subs.add(st1.nextToken());
									}
									boolean flag = true;
									for (String k:tokens)
									{
										if (!subs.contains(k))
										{
											flag = false;
											break;
										}
									}
									if (flag == true)
									{
										correct = c.getUsername();
										break;
									}
								}
								
								
								JLabel piece = createYourMessage(msg.getFrom()+": "+msg.getMessage(), correct);
								piece.setFont(new Font("Times New Roman",Font.ITALIC,17));
								piece.addMouseListener(new MouseAdapter() {
									@Override
									public void mouseClicked(MouseEvent e) {
										JFileChooser dChooser = new JFileChooser();
							            dChooser.setDialogTitle("Chọn đường dẫn");
							            dChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
							            dChooser.setAcceptAllFileFilterUsed(false);
							            if (dChooser.showOpenDialog(chatPanel) == JFileChooser.APPROVE_OPTION)
							            {
							            	try {
							            		File f = new File(dChooser.getSelectedFile().toString()+"\\"+msg.getMessage());
								            	f.createNewFile();
								            	FileOutputStream fos = new FileOutputStream(f);
								            	fos.write(msg.getData());
								            	fos.close();
								            	JOptionPane.showMessageDialog(chatPanel, "Tải file thành công!");
											} catch (IOException e1) {
												JOptionPane.showMessageDialog(chatPanel, "Tải file không thành công!");
											}	
							            }
									}
									});
								if (correct.equals(previous))
								{
	
									chatPanel.setVisible(false);
									chatPanel.add(piece);
									chatPanel.setVisible(true);
								}
								else
								{
									JPanel panel = getPanel(correct);
									panel.add(piece);
									replacePanel(correct, panel);
								}		
								
								
							}
							break;
						}
					}
					
			        
				} catch (EOFException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
}

class ChatPanel {
	private JPanel chat;
	private String username;
	private int yBound;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getyBound() {
		return yBound;
	}
	public void setyBound(int yBound) {
		this.yBound = yBound;
	}
	public ChatPanel(String username,JPanel chat, int yBound) {
		super();
		this.username = username;
		this.yBound = yBound;
		this.chat = chat;
	}
	
	public JPanel getChat() {
		return chat;
	}
	public void setChat(JPanel chat) {
		this.chat = chat;
	}

}

class OnlineRenderer extends JPanel implements ListCellRenderer<String> {

	private JLabel lbName = new JLabel();
	
	public OnlineRenderer() {
        setLayout(new BorderLayout(5, 5));
 
        JPanel panelText = new JPanel(new CardLayout());
        panelText.add(lbName);
        add(panelText, BorderLayout.CENTER);
    }
	@Override
	public Component getListCellRendererComponent(JList<? extends String> list, String value, int index,
			boolean isSelected, boolean cellHasFocus) {
		lbName.setText(value);
		lbName.setFont(new Font("Times New Roman", Font.BOLD, 25));
		lbName.setOpaque(true);
		lbName.setHorizontalAlignment(SwingConstants.CENTER);
		
		if (isSelected) {
	        lbName.setBackground(new Color(0,255,0));
	        setBackground(list.getSelectionBackground());
	    } else { 
	        lbName.setBackground(list.getBackground());
	        setBackground(list.getBackground());
	    }
		return this;
	}
	
}
