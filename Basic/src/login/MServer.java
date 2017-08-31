package login;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

//1.��������
//2.JDBC ����̹� �ε�
//3.Connection ��ü ����
//4.SQL�� �ۼ�
//5.���� ��ü ����
//6.����==>ResultSet
//7.�ݺ�
//8.�ڿ��ݳ�

/*DB----------------------------------

table Member
cloums ID PW




table Callender
cloums ID,TITLE,MEMO,STARTDATE,ENDDATE

--------------------------------------*/

public class MServer extends Thread {
	// ��������
	final int VIEWALL =0;
	final int INPUT =1;
	final int SEARCH =2;
	final int UPDATE =3;
	final int DELETE =4 ;
	final int JOIN =5;
	final int LOGIN =6;
	Socket client;
	boolean loginCheck = false;
	boolean joinCheck = false;
	String inputCheck;
	String updateCheck;
	String deleteCheck;
	CalCover cCov;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	CallenderDAO cDao;
	// �������� end

	// ������ ȣ��
	public MServer(Socket client) {
		System.out.println("������ ȣ����");
		this.client = client; // �Ű������� ������ ������ �޾� �����ڸ� ȣ��
		System.out.println(client);
		System.out.println(client.getInetAddress().getHostAddress());
	}

	@Override
	public void run() {

		try {
			// Ķ����DAO �������� �� �ν��Ͻ�
			cDao = new CallenderDAO();

			System.out.println(ois);
			System.out.println("��ǲ��Ʈ������");
			// ��ǲ��Ʈ��,�ƿ�ǲ��Ʈ�� �������� �� �ν��Ͻ�
			ois = new ObjectInputStream(client.getInputStream());
			oos = new ObjectOutputStream(client.getOutputStream());
		} catch (Exception ec) {
			System.out.println("��ǲ�ƿ�ǲ��Ʈ����������");

		}

		try {
			while ((cCov = (CalCover) ois.readObject()) != null) {
				// ��ü �Է� ��Ʈ������ ���� ��ü�� �о��, ���������Ʈ�� null�� �ƴϸ� ����ؼ� �ݺ�
				if (cCov.getCommand() == VIEWALL) { // 1����ȸ
					ArrayList<CallenderVO> list = cDao.selectTerm(cCov.getEtc());
					// �о�� ĮĿ������ Etc�� ������ Dao�� ����Ʈ�Ҹ޼��带 ������ ���� list�� ����
					for(CallenderVO v:list){
						System.out.println(v.getTitle());
					}
					oos.writeObject(list); // list�� ����Ʈ������Ʈ�� �������� ����(�ø�����������)
					oos.reset(); // oos�� ����

				} else if (cCov.getCommand() == INPUT) { // �Է�
					System.out.println("�Է½���");
					inputCheck = cDao.inputSchedule(cCov.getV());
					// ĮĿ������ vo�� �������� DAO�� ��ǲ�����ٸ޼����� �Ű������� ����Ͽ� ������ ���ϰ���
					// inputCheck�� ����
					oos.writeObject(inputCheck); // inputCheck�� ���� ����Ʈ������Ʈ�� ��������
													// ����(�ø�����������)
					oos.reset(); // oos�� ����
					System.out.println("�Է�����");

				} else if (cCov.getCommand() == SEARCH) { // Ű����˻�
					ArrayList<CallenderVO> list = cDao.SearchTitle(cCov.getV());
					// ĮĿ������ vo�� �������� DAO�� ��ġŸ��Ʋ�޼����� �Ű������� ����Ͽ� ������ ���ϰ��� list��
					// ����
					oos.writeObject(list); // list�� ����Ʈ������Ʈ�� �������� ����(�ø�����������)
					oos.reset(); // oos�� ����

				} else if (cCov.getCommand() == UPDATE) { // ����
					System.out.println("��������");
					updateCheck = cDao.updateSchedule(cCov.getV());
					// ĮĿ������ vo�� �������� DAO�� ������Ʈ�����ٸ޼����� �Ű������� ����Ͽ� ������ ���ϰ���
					// updateCheck�� ����
					oos.writeObject(updateCheck); // updateCheck�� ���� ����Ʈ������Ʈ��
													// �������� ����(�ø�����������)
					oos.reset(); // oos�� ����
					System.out.println("��������");

				} else if (cCov.getCommand() == DELETE) { // ����
					System.out.println("��������");
					deleteCheck = cDao.deleteSchedule(cCov.getV());
					// ĮĿ������ vo�� �������� DAO�� ����Ʈ�����ٸ޼����� �Ű������� ����Ͽ� ������ ���ϰ���
					// deleteCheck�� ����
					oos.writeObject(deleteCheck); // deleteCheck�� ���� ����Ʈ������Ʈ��
													// �������� ����(�ø�����������)
					oos.reset(); // oos�� ����
					System.out.println("��������");
				} else if (cCov.getCommand() == JOIN) { // ȸ������
					joinCheck = cDao.joinMember(cCov.getEtc());
					// ĮĿ������ ETC�� ��������(��,��,ID�� ���ø����� ������ �۾��� joinmember��������)
					// DAO�� ���θ���޼����� �Ű������� ����Ͽ� ������ ���ϰ��� joinCheck�� ����
					oos.writeObject(joinCheck); // joinCheck�� ���� ����Ʈ������Ʈ��
												// �������Խ���(�ø�����������)
					oos.reset(); // oos�� ����

				} else if (cCov.getCommand() == LOGIN) {
					loginCheck = cDao.loginMember(cCov.getEtc());
					// ĮĿ������ ETC�� ��������(��,��,ID�� ���ø����� ������ �۾��� loginmember��������)
					// DAO�� �α��θ���޼����� �Ű������� ����Ͽ� ������ ���ϰ��� loginCheck�� ����

					oos.writeObject(loginCheck); // loginCheck�� ���� ����Ʈ������Ʈ��
													// �������Խ���(�ø�����������)
					oos.reset(); // oos�� ����
				}
			}
		} catch (SocketException e) {
			System.out.println("������ ��������");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
