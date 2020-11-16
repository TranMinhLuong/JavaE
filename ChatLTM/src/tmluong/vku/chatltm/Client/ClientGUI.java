package tmluong.vku.chatltm.Client;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import sun.util.resources.Bundles;
import tmluong.vku.chatltm.Model.Message;
import tmluong.vku.chatltm.Model.User;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Color;

public class ClientGUI extends JFrame {

	private JPanel contentPane;
	private JTextField txtUsername;
	private JButton btnOk;
	private Socket socket;
	private String name;
	public static User user ;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientGUI frame = new ClientGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ClientGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 385, 204);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblAlert = new JLabel("M\u1EDDi nh\u1EADp t\u00EAn c\u1EE7a b\u1EA1n:");
		lblAlert.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblAlert.setBounds(10, 29, 199, 26);
		contentPane.add(lblAlert);
		
		txtUsername = new JTextField();
		txtUsername.setFont(new Font("Times New Roman", Font.BOLD, 15));
		txtUsername.setBounds(31, 84, 217, 26);
		contentPane.add(txtUsername);
		txtUsername.setColumns(10);
		
		btnOk = new JButton("Accept");
		btnOk.setFont(new Font("Times New Roman", Font.BOLD, 15));
		btnOk.setBackground(Color.LIGHT_GRAY);
		btnOk.setBounds(268, 84, 88, 26);
		contentPane.add(btnOk);
		
		addControls();
	}
	
	public void Connection (String name) throws UnknownHostException, IOException{
		Message message = new Message(name, name);
		OutputStream outputStream = socket.getOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
		objectOutputStream.writeObject(message);
		objectOutputStream.flush();
	}
	
	private void addControls() {
		// TODO Auto-generated method stub
		btnOk.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					while(true) {
						name = txtUsername.getText();
						if (name == null) {
							System.exit(0);
						}else if(!name.equals("")) {
							socket = new Socket("localhost", 4420);
							Connection(name);
							user = new User(name, socket, "");
							ClientChat clientChat = new ClientChat();
							clientChat.setVisible(true);
							dispose();
							break;
						}else {
							JOptionPane.showMessageDialog(null,"Tên của bạn còn trống");
						}
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});
	}
	
	
}
