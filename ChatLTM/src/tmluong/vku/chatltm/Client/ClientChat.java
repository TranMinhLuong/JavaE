package tmluong.vku.chatltm.Client;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FileDialog;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;

import tmluong.vku.chatltm.Client.ClientChat.MessageThread;
import tmluong.vku.chatltm.Model.Message;
import tmluong.vku.chatltm.Model.User;

import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.List;
import java.awt.TextArea;
import java.awt.Button;
import java.awt.ComponentOrientation;
import java.awt.Dialog;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;

public class ClientChat extends JFrame {

	private JPanel contentPane;
	public static JTextArea txtaListMessage,listUser;
	private JTextField txtMessage;
	private JLabel lblUsername;
	private JLabel lblLogout,lblFiles, lblSend;
	private Socket socket;
	MessageThread messageThread;
	private String name = ClientGUI.user.getUser();
	public static boolean check = true;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientChat frame = new ClientChat();
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
	public ClientChat() {
		this.socket = ClientGUI.user.getSocket();
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 561, 507);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		
		txtaListMessage = new JTextArea();
		txtaListMessage.setEditable(false);
		txtaListMessage.setFont(new Font("Times New Roman", Font.BOLD, 15));
		txtaListMessage.setBounds(126, 46, 409, 342);
		contentPane.add(txtaListMessage);
		
		txtMessage = new JTextField();
		txtMessage.setBounds(126, 411, 305, 30);
		contentPane.add(txtMessage);
		txtMessage.setColumns(10);
		
		lblUsername = new JLabel(ClientGUI.user.getUser());
		lblUsername.setFont(new Font("Times New Roman", Font.BOLD, 25));
		lblUsername.setBounds(129, 11, 138, 24);
		contentPane.add(lblUsername);
		
		listUser = new JTextArea();
		listUser.setEditable(false);
		listUser.setFont(new Font("Times New Roman", Font.BOLD, 15));
		listUser.setBounds(0, 46, 119, 342);
		contentPane.add(listUser);
		
		lblLogout = new JLabel("");
		lblLogout.setIcon(new ImageIcon("C:\\Users\\Admin\\eclipse-workspace\\ChatLTM\\ImgAndIcon\\iconback.png"));
		lblLogout.setFont(new Font("Times New Roman", Font.BOLD, 15));
		lblLogout.setBounds(277, 11, 38, 24);
		contentPane.add(lblLogout);
		
		lblFiles = new JLabel("");
		lblFiles.setIcon(new ImageIcon("C:\\Users\\Admin\\eclipse-workspace\\ChatLTM\\ImgAndIcon\\iconfile.png"));
		lblFiles.setBounds(441, 411, 46, 30);
		contentPane.add(lblFiles);
		
		lblSend = new JLabel("");
		lblSend.setIcon(new ImageIcon("C:\\Users\\Admin\\eclipse-workspace\\ChatLTM\\ImgAndIcon\\iconsend.png"));
		lblSend.setBounds(497, 411, 38, 30);
		contentPane.add(lblSend);
		
		addControls();
		messageThread = new MessageThread(); 
		messageThread.start();
		setTimeOut();
	}

	private void addControls() {
		// TODO Auto-generated method stub
		lblSend.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String context = txtMessage.getText().trim();
				if(context.length() > 0) {
					try {
						Message message = new Message("", context);
						ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
						objectOutputStream.writeObject(new Message(ClientGUI.user.getUser(), context));
						objectOutputStream.flush();
						txtMessage.setText("");
						txtaListMessage.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
						txtaListMessage.append("You: "+context+"\n");
					} catch (Exception e2) {
						// TODO: handle exception
						e2.printStackTrace();
					}
				}
			}
		});
		lblLogout.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
					objectOutputStream.writeObject(new Message(name, "end"));
					objectOutputStream.flush();
					messageThread.stop();
					socket.close();
					System.exit(0);
				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
			}
		});
		lblFiles.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					JFileChooser chooser = new JFileChooser(System.getProperty("user.home")+"/Desktop");
					int result = chooser.showOpenDialog(null);
					if (result == chooser.APPROVE_OPTION) { 
						File file = chooser.getSelectedFile();
						if (file.exists()) {
							if (file.length() / 1024 > 50) {
								JOptionPane.showMessageDialog(null, "File has exceeded the specified size limit!");
								return;
							}
							FileInputStream imgg = new FileInputStream(file);
    						byte i[] = new byte[(int) file.length()];
    						imgg.read(i, 0, (int) file.length());
    						ObjectOutputStream objectOutputStream;
    						objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
    						objectOutputStream.writeObject(new Message(name,file.getName(),i));
    						objectOutputStream.flush();
    						//txtaListMessage.append("You has sent files: "+ file.getName() +"\n");
						}
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});
	}
	
	class MessageThread extends Thread{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message message;
			try {
				while(true) {
					ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
					message = (Message) objectInputStream.readObject();
					if (message.getName().equals("Nofication Server") && message.getMessage().indexOf("[") != -1) {
						String s = message.getMessage().replace("[", "").replace("]","");
						String[] arr = s.split(",");
						listUser.setText("");
						for (String user : arr) {
							listUser.append(" "+user.trim()+"\n");
						}
					} else if (message.getFiles() != null) {
						int n = JOptionPane.showConfirmDialog(null, message.getName()+" has sent a file. Do you want to dowload it?","Dowload file",JOptionPane.YES_NO_OPTION);
						if (n == JOptionPane.YES_OPTION) {
							UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
							JFileChooser chooser = new JFileChooser(System.getProperty("user.home")+"/Desktop");
							chooser.setSelectedFile(new File(message.getMessage()));
							chooser.setAcceptAllFileFilterUsed(false);
							int result = chooser.showSaveDialog(null);
							if (result == chooser.APPROVE_OPTION) { 
								String newFile = chooser.getSelectedFile().getAbsolutePath();
								newFile = (newFile.endsWith(message.getMessage().substring(message.getMessage().indexOf(".")))) ? newFile : newFile+".txt";
								File file = new File(newFile);
								//txtaListMessage.append(message.getName()+"has sent file: "+file.getName()+"\n");
								if (!file.exists()) {
									OutputStream fs = new FileOutputStream(file);
									fs.write(message.getFiles());
									fs.close();
									JOptionPane.showMessageDialog(null, "Dowload success");
								}
								else {
									JOptionPane.showMessageDialog(null, "File already exists! Please rename !");
								}
							}
						}
					} else {
						txtaListMessage.append(message.getName()+": "+message.getMessage()+"\n");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void setTimeOut() {
		// TODO Auto-generated method stub
		JOptionPane jOptionPane = new JOptionPane("You banned because you didn't inactivity!", JOptionPane.WARNING_MESSAGE);
		final JDialog jDialog = jOptionPane.createDialog(null, "Warning!");
		jDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(!check) {
					if(jDialog.isShowing()) {
						jDialog.dispose();
					}
					jDialog.show();
					try {
						ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
						objectOutputStream.writeObject(new Message(name, "end"));
						objectOutputStream.flush();
						messageThread.stop();
						socket.close();
						System.exit(0);
					} catch (Exception e2) {
						// TODO: handle exception
						e2.printStackTrace();
					}
				}else {
					check = false;
				}
			}
		};
		Timer timer = new Timer("Timer");
		timer.schedule(timerTask, 0, 300000);
	}
	
}
