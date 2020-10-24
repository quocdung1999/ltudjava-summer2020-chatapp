package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;

import Analyze.Server;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;
import java.awt.event.ActionEvent;

public class RegisterGUI extends JFrame implements FocusListener
{

	private JPanel contentPane;
	private JTextField userField;
	private JPasswordField passField;
	private JPasswordField rePassField;

	public RegisterGUI() {
		
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 686, 394);
		setLocationRelativeTo(null);
		setTitle("Đăng ký người dùng mới");
		contentPane = new JPanel();
		contentPane.setBackground(new Color(233,245,248));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel headerLabel = new JLabel();
		headerLabel.setText("Đăng ký tài khoản");
		headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		headerLabel.setFont(new Font("Tahoma", Font.BOLD, 26));
		headerLabel.setBounds(219, 33, 254, 31);
		contentPane.add(headerLabel);
		
		JLabel userLabel = new JLabel("Username");
		userLabel.setHorizontalAlignment(SwingConstants.CENTER);
		userLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		userLabel.setBounds(0, 92, 164, 31);
		contentPane.add(userLabel);
		
		userField = new JTextField();
		userField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		userField.setBackground(new Color(228,228,228));
		userField.setBounds(165, 89, 474, 40);
		userField.addFocusListener(this);
		contentPane.add(userField);
		userField.setColumns(10);
		
		passField =  new JPasswordField();
		passField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		passField.setBackground(new Color(228,228,228));
		passField.setColumns(10);
		passField.setBounds(165, 159, 474, 40);
		passField.addFocusListener(this);
		contentPane.add(passField);
		
		rePassField = new JPasswordField();
		rePassField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		rePassField.setBackground(new Color(228,228,228));
		rePassField.setColumns(10);
		rePassField.setBounds(165, 229, 474, 40);
		rePassField.addFocusListener(this);
		contentPane.add(rePassField);
		
		JButton execButton = new JButton();
		execButton.setForeground(Color.WHITE);
		execButton.setBackground(Color.BLUE);
		execButton.setText("Đăng ký");
		execButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = userField.getText();
				String password = new String(passField.getPassword());
				String rePass = new String(rePassField.getPassword());
				CharSequence[] special = {",",".",";",":","\'","\"","/","!","@","#","$","%","^","&","*",
						"(",")","<",">","[","]","{","}"," "};	
				boolean f = true;
				for (int i = 0;i<special.length;i++)
				{
					if (username.contains(special[i]))
					{
						f = false;
						break;
					}
				}
				if (f == false)
				{
					JOptionPane.showMessageDialog(null, "Tên đăng nhập không được có kí tự đặc biệt!");
				}
				else if (!password.equals(rePass))
				{
					JOptionPane.showMessageDialog(null, "Mật khẩu nhập lại không trùng khớp!");
				}
				else if (username.isEmpty()||password.isEmpty()||rePass.isEmpty())
				{
					JOptionPane.showMessageDialog(null, "Không được bỏ trống trường nào!");
				}
				else 
				{
					boolean flag = true;
					for (Vector<String> user:Server.users)
					{
						if (user.get(0).equals(username))
						{
							flag = false;
							break;
						}
					}
					if (!flag)
					{
						JOptionPane.showMessageDialog(null, "Tên đăng nhập bị trùng với người khác!");
					}
					else
					{
						
						Server.addUser(username,password);
						dispose();		
						JOptionPane.showMessageDialog(null, "Đăng ký tài khoản thành công");
					
					}
				}
			}
		});
		execButton.setFont(new Font("Tahoma", Font.BOLD, 15));
		execButton.setBounds(295, 301, 108, 31);
		contentPane.add(execButton);
		
		JLabel passLabel = new JLabel();
		passLabel.setText("Mật khẩu");
		passLabel.setHorizontalAlignment(SwingConstants.CENTER);
		passLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		passLabel.setBounds(0, 162, 164, 31);
		contentPane.add(passLabel);
		
		JLabel rePassLabel = new JLabel();
		rePassLabel.setText("Nhập lại mật khẩu");
		rePassLabel.setHorizontalAlignment(SwingConstants.CENTER);
		rePassLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		rePassLabel.setBounds(0, 232, 164, 31);
		contentPane.add(rePassLabel);
		
		
	}

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		for (Component c:getContentPane().getComponents())
		{
			if (c instanceof JTextField )
			{
				((JTextField) c).setCaretColor(Color.BLACK);
			}
		}
	}


	@Override
	public void focusLost(FocusEvent e) {
		// TODO Auto-generated method stub
		
	}

}
