
/*
cmd typ: 0 = pinIn, 1 = pinOut, 2 = dRead, 3 = aRead 4 = dWrite;
*/
import java.net.*;

class C {
	public static void main(String args[]) throws Exception {
		System.out.println("Start C");
		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress IPAddress = InetAddress.getByName("192.168.0.5");
		byte[] sendData = new byte[16];
		// while (true) {
		sendData[0] = 1;
		sendData[1] = 2;
		sendData[3] = 3;
		sendData[4] = 4;
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8000);
		clientSocket.send(sendPacket);
		System.out.println("Send");
//		Thread.sleep(60000);
		// }
		clientSocket.close();
		System.out.println("End C");
	}
}