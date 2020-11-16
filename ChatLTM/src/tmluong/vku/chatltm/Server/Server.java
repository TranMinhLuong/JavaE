package tmluong.vku.chatltm.Server;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import jdk.internal.util.xml.impl.Input;
import tmluong.vku.chatltm.Model.Message;
import tmluong.vku.chatltm.Model.User;

import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.Color;
import java.awt.Event;

public class Server extends JFrame{

	private JPanel contentPane;
	JLabel lblAlertSV;
	JButton btnAcceptSV;
	ServerSocket serverSocket;
	Vector<String> users = new Vector<String>();
	Vector<ManagementUsers> clients = new Vector<ManagementUsers>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Server frame = new Server();
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
	public Server(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 362, 215);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblAlertSV = new JLabel("Let's start the Server");
		lblAlertSV.setFont(new Font("Times New Roman", Font.BOLD, 25));
		lblAlertSV.setBounds(58, 37, 239, 34);
		contentPane.add(lblAlertSV);
		
		btnAcceptSV = new JButton("Accept");
		btnAcceptSV.setBackground(Color.LIGHT_GRAY);
		btnAcceptSV.setFont(new Font("Times New Roman", Font.BOLD, 15));
		btnAcceptSV.setBounds(121, 95, 89, 34);
		contentPane.add(btnAcceptSV);
		
		addControls();
	}
	
	private void addControls() {
		// TODO Auto-generated method stub
		btnAcceptSV.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					serverSocket = new ServerSocket(4420);
					System.out.println("The Server is started!");
					dispose();
					while (true) {
						Socket newsocket = serverSocket.accept();
						System.out.println("Clients incoming!");
						ManagementUsers managementUsers = new ManagementUsers(newsocket);
					}
				} catch (Exception ex) {
					// TODO: handle exception
					ex.printStackTrace();
				}
			}
		});
		
	}
	
	public void sendtoall(Message message) throws IOException {
        for (ManagementUsers managementUsers : clients) {
        	if(!managementUsers.user.getUser().equals(message.getName())) {
        		managementUsers.sendMessage(message);
        	}
        }
    }
	
	class ManagementUsers extends Thread{
		User user;
		String id = String.valueOf(Calendar.getInstance().getTimeInMillis());
		
		public ManagementUsers(Socket incoming) throws Exception{
			InputStream inputStream = incoming.getInputStream();
			ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
			Message message = (Message) objectInputStream.readObject();
			this.user = new User(message.getMessage(), incoming, id);
			clients.add(this);
			users.add(user.getUser());
			sendtoall(new Message("Nofication Server", this.user.getUser()+" joined room!"));
			sendtoall(new Message("Nofication Server", users.toString()));
			start();
		}
		
		public void sendMessage(Message messenge) throws IOException {
            OutputStream ops = user.getSocket().getOutputStream();
            ObjectOutputStream ots = new ObjectOutputStream(ops);
            ots.writeObject(messenge);
            ots.flush();
        }
		
		@Override
		public void run() {
			Message message = null;
			try {
				while(true) {
					if(!user.getSocket().isClosed()) {
						InputStream inputStream = user.getSocket().getInputStream();
						ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
						message = (Message) objectInputStream.readObject();
						if(message.getMessage() != null && message.getMessage().equals("end")) {
							clients.remove(this);
							users.remove(message.getName());
							sendtoall(new Message("Nofication Server", message.getName()+" exited!"));
							sendtoall(new Message("Nofication Server", users.toString()));
							break;
						}
						sendtoall(message);
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				clients.remove(this);
				users.remove(user.getUser());
				e.printStackTrace();
			}
		}
	}

}
