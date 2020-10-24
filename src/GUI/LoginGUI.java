package GUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

import Analyze.ClientHandler;
import Analyze.Server;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;
import java.awt.event.ActionEvent;

public class LoginGUI extends JFrame {

	private JPanel contentPane;
	private JTextField userField;
	private JPasswordField passField;

	public LoginGUI() {
	
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 550);	
		setLocationRelativeTo(null);
		setTitle("Đăng nhập chat");
		contentPane = new JPanel();
		contentPane.setBackground(new Color(233,245,248));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel headerLabel = new JLabel();
		headerLabel.setText("Ä�Äƒng nháº­p");
		headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		headerLabel.setFont(new Font("Tahoma", Font.BOLD, 30));
		headerLabel.setBounds(325, 47, 250, 44);
		contentPane.add(headerLabel);
		
		JLabel userLabel = new JLabel("Username");
		userLabel.setHorizontalAlignment(SwingConstants.CENTER);
		userLabel.setFont(new Font("Tahoma", Font.BOLD, 19));
		userLabel.setBounds(75, 157, 207, 36);
		contentPane.add(userLabel);
		
		JLabel passLabel = new JLabel();
		passLabel.setText("Máº­t kháº©u");
		passLabel.setHorizontalAlignment(SwingConstants.CENTER);
		passLabel.setFont(new Font("Tahoma", Font.BOLD, 19));
		passLabel.setBounds(75, 260, 207, 36);
		contentPane.add(passLabel);
		
		userField = new JTextField();
		userField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		userField.setColumns(10);
		userField.setBackground(new Color(228, 228, 228));
		userField.setBounds(292, 150, 500, 44);
		contentPane.add(userField);
		
		passField = new JPasswordField();
		passField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		passField.setColumns(10);
		passField.setBackground(new Color(228, 228, 228));
		passField.setBounds(292, 253, 500, 44);
		contentPane.add(passField);
		
		JButton execButton = new JButton();
		execButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = userField.getText();
				String password = new String(passField.getPassword());
				passField.setText("");
				if (Server.users.size()>0)
				{
					boolean outFlag = false;
					for (Vector<String> user:Server.users)
					{
						if (user.get(0).equals(username)&&user.get(1).equals(password))
						{
							outFlag = true;
							boolean flag = true;
							for (ClientHandler c:Server.active)
							{
								if (c.getUsername().equals(username))
								{
									flag = false;
									break;
								}
							}
							if (!flag)
							{
								JOptionPane.showMessageDialog(null, "Báº¡n Ä‘Ã£ Ä‘Äƒng nháº­p trÆ°á»›c Ä‘Ã³!");
								break;
							}
							else
							{
								userField.setText("");
								EventQueue.invokeLater(new Runnable() {
									public void run() {
										try {
											ClientGUI d = new ClientGUI(username);
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								});
					
							}
						}						
					}
					if (!outFlag)
						JOptionPane.showMessageDialog(null, "TÃ i khoáº£n hoáº·c máº­t kháº©u khÃ´ng Ä‘Ãºng!");
				}
				else
				{
					JOptionPane.showMessageDialog(null, "ChÆ°a cÃ³ tÃ i khoáº£n nÃ o trÃªn há»‡ thá»‘ng!");
				}
			}
		});
		execButton.setText("Ä�Äƒng nháº­p");
		execButton.setForeground(Color.WHITE);
		execButton.setFont(new Font("Tahoma", Font.BOLD, 19));
		execButton.setBackground(Color.BLUE);
		execButton.setBounds(375, 372, 150, 36);
		contentPane.add(execButton);
		
		JButton registerButton = new JButton();
		registerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							
							RegisterGUI frame = new RegisterGUI();
						} catch (Exception f) {
							f.printStackTrace();
						}
					}
				});
			}
		});
		registerButton.setText("Ä�Äƒng kÃ½");
		registerButton.setForeground(Color.WHITE);
		registerButton.setFont(new Font("Tahoma", Font.BOLD, 16));
		registerButton.setBackground(Color.BLUE);
		registerButton.setBounds(754, 11, 119, 29);
		contentPane.add(registerButton);
		
		setVisible(true);
	}
}
