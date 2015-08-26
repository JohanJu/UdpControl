
/*
cmd typ: 1 = pinIn, 2 = pinOut, 3 = dRead, 4 = aRead 5 = dWrite;
*/
import java.net.*;

class C {
	public static void main(String args[]) throws Exception {
		System.out.println("Start C");
		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress IPAddress = InetAddress.getByName("192.168.0.5");
		byte[] sendData = new byte[8];
		// while (true) {
		byte sum = 0;
		sendData[0] = (byte) 0xaa;
		
		sendData[2] = 1;
		sum+=sendData[2];
		sendData[3] = 2;
		sum+=sendData[3];
		sendData[4] = (byte) 0xf1;
		sum+=sendData[4];
		sendData[5] = (byte) 0xf2;
		sum+=sendData[5];
		System.out.println(sum);
		sendData[1] = sum;
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8000);
		clientSocket.send(sendPacket);
		System.out.println("Send");
//		Thread.sleep(60000);
		// }
		clientSocket.close();
		System.out.println("End C");
	}
}