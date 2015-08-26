
/*
cmd typ: 1 = pinIn, 2 = pinOut, 3 = dRead, 4 = aRead 5 = dWrite
*/
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.*;

import javax.swing.*;


class C implements ActionListener{
	 JFrame frame = new JFrame("Arduino controller");
	 JButton add = new JButton("Add");
	 JButton send = new JButton("Send");
	 JPanel left = new JPanel();
	 JPanel rigth = new JPanel();
	 JTextField typ = new JTextField("0");
	 JTextField pin = new JTextField("0");
	 JTextField val = new JTextField("0");
	 JTextArea show = new JTextArea();
	 byte[] sendData = new byte[128];
	 byte b = 2, sum = 0; 
	public static void main(String args[]) throws Exception {
		new C(); 
	}

	public C() {
		add.addActionListener(this);
		send.addActionListener(this);
		left.setLayout(new BoxLayout(left, BoxLayout.PAGE_AXIS));
		left.add(typ);
		left.add(pin);
		left.add(val);
		left.add(add);
		rigth.setLayout(new BoxLayout(rigth, BoxLayout.PAGE_AXIS));
		rigth.add(show);
		rigth.add(send);
		frame.setLayout(new GridLayout(1, 2));
		frame.add(left);
		frame.add(rigth);
		frame.setSize(400, 200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		Server S = new Server();
		S.addText(show);
		S.start();
	}
	public  void send() throws IOException {
		System.out.println("Start C");
		DatagramSocket clientSocket;
		clientSocket = new DatagramSocket();
		InetAddress IPAddress = InetAddress.getByName("192.168.0.5");
		sendData[0] = (byte) 0xaa;		
		sendData[1] = sum;
		DatagramPacket sendPacket = new DatagramPacket(sendData, b, IPAddress, 8001);
		clientSocket.send(sendPacket);
		System.out.println("Send");
		clientSocket.close();
		b = 2;
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == add) {
			byte tmp = 0;
			tmp = (byte) Integer.parseInt(typ.getText());
			sendData[b++] = tmp;
			sum += tmp;
			show.append(tmp+" ");
			tmp =  (byte) Integer.parseInt(pin.getText());
			sendData[b++] = tmp;
			sum += tmp;
			show.append(tmp+" ");
			tmp = (byte) Integer.parseInt(val.getText());
			sendData[b++] = tmp;
			sum += tmp;
			show.append(tmp+"\n");
		}else if(e.getSource() == send) {
			if (b == 2){
				System.out.println("First Add");
			}else{
//			for (int i = 0; i < b; i++) {
//				System.out.println(sendData[i]);
//			}
				try {
					send();
					show.setText("");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		
	}

}