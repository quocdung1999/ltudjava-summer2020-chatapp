package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import Analyze.Message;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.awt.event.ActionEvent;

public class ChooseGUI extends JFrame {

	private JPanel contentPane;


	/**
	 * Create the frame.
	 */
	public ChooseGUI(String username, DefaultListModel<String> d, ObjectOutputStream oos) {
		setTitle("Lựa chọn người đang online");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setBounds(100, 100, 443, 489);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(152,251,152));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		DefaultListModel<String> model = new DefaultListModel<String>();
		for (int i = 0 ;i<d.size();i++)
		{
			if (!d.get(i).contains(","))
			{
				model.addElement(d.get(i));
			}
		}
		JList<String> list = new JList<String>(model);
		list.setBackground(new Color(255,250,205));
		list.setFont(new Font("Tahoma", Font.BOLD, 15));
		list.setCellRenderer(new CheckboxListCellRenderer());
		list.setBounds(10,32,417,369);
		list.setPreferredSize(list.getPreferredSize());
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		JScrollPane scrollPane = new JScrollPane(list,   
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBounds(10,32,417,369);
		contentPane.add(scrollPane);
		
		JButton groupButton = new JButton("Tạo nhóm chat");
		groupButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> s = (ArrayList<String>) list.getSelectedValuesList();
				Vector<String> selected = new Vector<String>();
				for (String user:s)
				{
					selected.add(user);
				}
				selected.add(0,username);
				if (selected.size()<3)
				{
					JOptionPane.showMessageDialog(null, "Phải chọn tối thiểu 2 người!");
				}
				else
				{
 					try {
 						
						oos.writeObject(new Message("addGroup",null , username, selected, null));
						dispose();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
				}
 			}
		});
		groupButton.setFont(new Font("Tahoma", Font.BOLD, 13));
		groupButton.setBackground(Color.ORANGE);
		groupButton.setBounds(144, 412, 137, 33);
		contentPane.add(groupButton);
		
		setVisible(true);
	}
}

class CheckboxListCellRenderer extends JCheckBox implements ListCellRenderer<String> {

	@Override
    public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, 
            boolean isSelected, boolean cellHasFocus) {

        setComponentOrientation(list.getComponentOrientation());
        setFont(list.getFont());
        setBackground(list.getBackground());
        setForeground(list.getForeground());
        setSelected(isSelected);
        setEnabled(list.isEnabled());

        setText(value == null ? "" : value.toString());  

        return this;
    }
}
