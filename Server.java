import java.io.IOException;
import java.net.*;
import javax.swing.JTextArea;

class Server implements Runnable {
	private Thread t;
	JTextArea show;
	public void run() {
		DatagramSocket serverSocket;
		try {
			serverSocket = new DatagramSocket(8002);
			byte[] receiveData = new byte[256];
			while (true) {
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);
				String re = new String(receivePacket.getData());
				System.out.println("Re: " + re);
				show.setText("Re: " +re);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public void addText(JTextArea show){
		this.show = show;
	}
	

	public void start() {
		if (t == null) {
			t = new Thread(this);
			t.start();
		}
	}

}
