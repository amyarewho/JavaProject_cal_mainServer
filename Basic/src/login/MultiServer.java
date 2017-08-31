package login;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
// 사용자의 접속을 대기하고 새로운 객체를 생성. 멀티쓰레드 처리

public class MultiServer {
	public static void main(String[] args) {
		
		try {
			ServerSocket ss = new ServerSocket(5000); //서버소켓을 5000번으로 인스턴스후 서버소켓ss을 선언하고 삽입
			while(true){ //while문의 조건이 true로 무한히 반복
			System.out.println("사용자 접속 대기중");
			Socket Client = ss.accept(); //5000번 포트로 접속자가 잇을떄까지 대기하고 있을경우 Client라는 소켓에 접속자를 입력
			MServer ms = new MServer(Client); //메인서버에 Client소켓을 매개변수로 하여 ms로 생성
			ms.start(); //ms를 실행
			}
		} catch (IOException e) { //예외처리
			e.printStackTrace();
		}
	}

}
