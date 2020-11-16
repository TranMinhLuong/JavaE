package tluong.vku.tygia.Server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Random;
import java.util.Vector;

import tmluong.vku.tygia.Model.Models;
import tmluong.vku.tygia.Model.VectorModels;


public class Server {
	private static Vector<Models> ListCash = new Vector<Models>();
	private static String[] listct = {"Việt Nam", "Lào"};
	public static void main(String[] args) throws Exception {
		DatagramSocket socket = new DatagramSocket(4420);
		System.out.println("Server started!");
		byte[] receiveData = new byte[1024];
		while (true) {
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			socket.receive(receivePacket);
			ListCash.clear();
			InetAddress IPAddress = receivePacket.getAddress();
			int port = receivePacket.getPort();
			VectorModels vectorModels = readByteToObject(receiveData); 
			
			for (int i = 0; i < listct.length; i++) {
				Models models = new Models(null, null, null);
				models.setId(Integer.toString(i));
				models.setCountry(listct[i]);
				models.setCash(RandomCash());
				ListCash.add(models);
			}
			
			System.out.println(ListCash);
            
			byte[] sendData = RequestDataReceive(vectorModels.getEvent());
			byte[] requestData = ConvertObjectToBytes(new VectorModels(receiveData.length));

           	DatagramPacket sendPacket =   new DatagramPacket(requestData, requestData.length, IPAddress, port);   	
           	//Gui dl lai cho client
           	socket.send(sendPacket);
           	
           	DatagramPacket requestPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
           	socket.send(requestPacket);
		}
	}
	

	private static String RandomCash() {
		// TODO Auto-generated method stub
		int max = 0;
		int min = 100000;
		int range = max - min + 1;
		
		String text=null;
		try {
			
			int random = (int) (Math.random() * (range) + min);
			text = formatted("###,###", random)+" VNĐ";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return text;
	}

	
	public static String formatted(String pattern, int value) {
		DecimalFormat myFormatter = new DecimalFormat(pattern);
		String output = myFormatter.format(value);
		return output;
	}
	
	
	public static byte[] ConvertObjectToBytes(VectorModels vectorModels) throws Exception {
		ByteArrayOutputStream BOS = new ByteArrayOutputStream();
		ObjectOutput ops = new ObjectOutputStream(BOS);
		ops.writeObject(vectorModels);
		ops.close();
		byte[] sendData = BOS.toByteArray();
		return sendData;
	}
	public static VectorModels readByteToObject(byte[] receiveData) throws Exception {
		ObjectInputStream OIS = new ObjectInputStream(new ByteArrayInputStream(receiveData));
		VectorModels dataClass = (VectorModels) OIS.readObject();
		OIS.close();
		return dataClass;
	}

	public class Event {
		public static final String REQUEST_DATA= "REQUEST_DATA";
		public static final String RELOAD = "RELOAD";
		public static final String GET_LENGTH = "GET_LENGTH";
	}

	
	public static byte[] RequestDataReceive(String event) throws Exception {
		byte[] requestData = null;
		System.out.println("Event :"+event);
		switch (event) {
			case Event.RELOAD:
			case Event.REQUEST_DATA:
				VectorModels dataMemory = new VectorModels(ListCash);
				requestData = ConvertObjectToBytes(dataMemory);
				break;
		}
		return requestData;
	}

}
