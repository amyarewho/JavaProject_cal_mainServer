package login;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
// ������� ������ ����ϰ� ���ο� ��ü�� ����. ��Ƽ������ ó��

public class MultiServer {
	public static void main(String[] args) {
		
		try {
			ServerSocket ss = new ServerSocket(5000); //���������� 5000������ �ν��Ͻ��� ��������ss�� �����ϰ� ����
			while(true){ //while���� ������ true�� ������ �ݺ�
			System.out.println("����� ���� �����");
			Socket Client = ss.accept(); //5000�� ��Ʈ�� �����ڰ� ���������� ����ϰ� ������� Client��� ���Ͽ� �����ڸ� �Է�
			MServer ms = new MServer(Client); //���μ����� Client������ �Ű������� �Ͽ� ms�� ����
			ms.start(); //ms�� ����
			}
		} catch (IOException e) { //����ó��
			e.printStackTrace();
		}
	}

}
