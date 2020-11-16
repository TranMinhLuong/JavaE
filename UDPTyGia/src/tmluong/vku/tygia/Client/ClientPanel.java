package tmluong.vku.tygia.Client;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import tluong.vku.tygia.Server.Server;
import tmluong.vku.tygia.Model.Models;
import tmluong.vku.tygia.Model.VectorModels;

public class ClientPanel extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private InetAddress IPAddress;
	private int PORT = 4420;
	private DefaultTableModel model;
	private DatagramSocket clientSocket;
	private Vector<Models> listchangerates;



	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientPanel frame = new ClientPanel();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}  

	/**
	 * Create the frame.
	 * @throws Exception 
	 */
	public ClientPanel() throws Exception {
		listchangerates = new Vector<Models>();
		clientSocket = new DatagramSocket();
		IPAddress = InetAddress.getByName("localhost");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(30, 79, 879, 262);
		contentPane.add(scrollPane);
		model = new DefaultTableModel(
				new Object[][] {
			},
			new String[] {
				"Country", "Money in"
			});
		
		table = new JTable();
		scrollPane.setViewportView(table);
		table.setModel(model);
		table.setEnabled(false);
		

		requestData();
		Timeout(1000);
	}

	private void requestData() throws Exception {
		// TODO Auto-generated method stub
		VectorModels vectorModels = new VectorModels(Server.Event.REQUEST_DATA);
		byte[] reqData = Server.ConvertObjectToBytes(vectorModels);
		
		DatagramPacket requestPacket = new DatagramPacket(reqData,reqData.length,IPAddress,PORT);
		clientSocket.send(requestPacket);
		
		byte[] receiveLength= new byte[1024];
		DatagramPacket receivePacket = new DatagramPacket(receiveLength, receiveLength.length);
		clientSocket.receive(receivePacket);
		
		VectorModels length = (VectorModels) Server.readByteToObject(receiveLength);
		
		byte[] receiveData = new byte[(int)length.getLength()];
		DatagramPacket receivePacketData = new DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(receivePacketData);
		
		VectorModels models = Server.readByteToObject(receiveData);
		System.out.println("Data Memory :"+models.getModels());

		for(int i=0;i<models.getModels().size();i++) {
			model.addRow(new Object[]{ models.getModels().get(i).getCountry(), models.getModels().get(i).getCash()});
		}
	}

	private void Timeout(int time) {
		TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					model.setRowCount(0);
					requestData();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		};
		Timer timer = new Timer("Timer");
		timer.schedule(task, 0, 1000);
	}
	
}
