package login;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

//1.변수선언
//2.JDBC 드라이버 로딩
//3.Connection 객체 생성
//4.SQL문 작성
//5.문장 객체 생성
//6.실행==>ResultSet
//7.반복
//8.자원반납

/*DB----------------------------------

table Member
cloums ID PW




table Callender
cloums ID,TITLE,MEMO,STARTDATE,ENDDATE

--------------------------------------*/

public class MServer extends Thread {
	// 변수선언
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
	// 변수선언 end

	// 생성자 호출
	public MServer(Socket client) {
		System.out.println("생성자 호출중");
		this.client = client; // 매개변수로 유저의 소켓을 받아 생성자를 호출
		System.out.println(client);
		System.out.println(client.getInetAddress().getHostAddress());
	}

	@Override
	public void run() {

		try {
			// 캘린더DAO 변수선언 및 인스턴스
			cDao = new CallenderDAO();

			System.out.println(ois);
			System.out.println("인풋스트림생성");
			// 인풋스트림,아웃풋스트림 변수선언 및 인스턴스
			ois = new ObjectInputStream(client.getInputStream());
			oos = new ObjectOutputStream(client.getOutputStream());
		} catch (Exception ec) {
			System.out.println("인풋아웃풋스트림생성실패");

		}

		try {
			while ((cCov = (CalCover) ois.readObject()) != null) {
				// 객체 입력 스트림으로 부터 객체를 읽어옴, 리드오브젝트가 null이 아니면 계속해서 반복
				if (cCov.getCommand() == VIEWALL) { // 1달조회
					ArrayList<CallenderVO> list = cDao.selectTerm(cCov.getEtc());
					// 읽어온 칼커버에서 Etc를 겟한후 Dao의 셀렉트텀메서드를 실행한 값을 list에 삽입
					for(CallenderVO v:list){
						System.out.println(v.getTitle());
					}
					oos.writeObject(list); // list를 라이트오브젝트로 유저에게 쏴줌(시리얼라이저블로)
					oos.reset(); // oos를 리셋

				} else if (cCov.getCommand() == INPUT) { // 입력
					System.out.println("입력시작");
					inputCheck = cDao.inputSchedule(cCov.getV());
					// 칼커버에서 vo를 가져온후 DAO의 인풋스케줄메서드의 매개변수로 사용하여 실행후 리턴값을
					// inputCheck에 삽입
					oos.writeObject(inputCheck); // inputCheck의 값을 라이트오브젝트로 유저에게
													// 쏴줌(시리얼라이저블로)
					oos.reset(); // oos를 리셋
					System.out.println("입력종료");

				} else if (cCov.getCommand() == SEARCH) { // 키워드검색
					ArrayList<CallenderVO> list = cDao.SearchTitle(cCov.getV());
					// 칼커버에서 vo를 가져온후 DAO의 서치타이틀메서드의 매개변수로 사용하여 실행후 리턴값을 list에
					// 삽입
					oos.writeObject(list); // list를 라이트오브젝트로 유저에게 쏴줌(시리얼라이저블로)
					oos.reset(); // oos를 리셋

				} else if (cCov.getCommand() == UPDATE) { // 수정
					System.out.println("수정시작");
					updateCheck = cDao.updateSchedule(cCov.getV());
					// 칼커버에서 vo를 가져온후 DAO의 업데이트스케줄메서드의 매개변수로 사용하여 실행후 리턴값을
					// updateCheck에 삽입
					oos.writeObject(updateCheck); // updateCheck의 값을 라이트오브젝트로
													// 유저에게 쏴줌(시리얼라이저블로)
					oos.reset(); // oos를 리셋
					System.out.println("수정종료");

				} else if (cCov.getCommand() == DELETE) { // 삭제
					System.out.println("삭제시작");
					deleteCheck = cDao.deleteSchedule(cCov.getV());
					// 칼커버에서 vo를 가져온후 DAO의 딜리트스케줄메서드의 매개변수로 사용하여 실행후 리턴값을
					// deleteCheck에 삽입
					oos.writeObject(deleteCheck); // deleteCheck의 값을 라이트오브젝트로
													// 유저에게 쏴줌(시리얼라이저블로)
					oos.reset(); // oos를 리셋
					System.out.println("삭제종료");
				} else if (cCov.getCommand() == JOIN) { // 회원가입
					joinCheck = cDao.joinMember(cCov.getEtc());
					// 칼커버에서 ETC를 가져온후(년,월,ID를 스플릿으로 나누는 작업을 joinmember에서실행)
					// DAO의 조인멤버메서드의 매개변수로 사용하여 실행후 리턴값을 joinCheck에 삽입
					oos.writeObject(joinCheck); // joinCheck의 값을 라이트오브젝트로
												// 유저에게쏴줌(시리얼라이저블로)
					oos.reset(); // oos를 리셋

				} else if (cCov.getCommand() == LOGIN) {
					loginCheck = cDao.loginMember(cCov.getEtc());
					// 칼커버에서 ETC를 가져온후(년,월,ID를 스플릿으로 나누는 작업을 loginmember에서실행)
					// DAO의 로그인멤버메서드의 매개변수로 사용하여 실행후 리턴값을 loginCheck에 삽입

					oos.writeObject(loginCheck); // loginCheck의 값을 라이트오브젝트로
													// 유저에게쏴줌(시리얼라이저블로)
					oos.reset(); // oos를 리셋
				}
			}
		} catch (SocketException e) {
			System.out.println("소켓이 닫혀있음");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
