package login;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

public class CallenderDAO {
	// 변수선언
	String driver = "oracle.jdbc.driver.OracleDriver";
	String url = "jdbc:oracle:thin:@orcl.cnfoop449y4h.us-east-2.rds.amazonaws.com:1521:orcl";
	String user = "notscott";
	String password = "nottiger";
	Connection conn = null;
	ResultSet rs = null;
	PreparedStatement pstmt = null;
	StringBuffer sb = new StringBuffer();

	public CallenderDAO() {
		// db 연결
		try {
			Class.forName(driver); // 드라이버로딩
			conn = DriverManager.getConnection(url, user, password); // 커넥션객체생성
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버연결실패");
		} catch (SQLException e) {
			System.out.println("db연결실패");
		}

	}// Constructor end

	public ArrayList<CallenderVO> selectTerm(String etc) { // 기간조회
		System.out.println(etc);
		String[] splt = etc.split(" "); // 매개변수를 스플릿으로 나눔
		String ids; 
		String cates;
		ids = splt[2]; // ids변수에 splt[2]의 값을 삽입
		cates = splt[3];
		StringBuffer sbStart  = new StringBuffer();
		sbStart.append(splt[0]);
		sbStart.append("-");
		if(Integer.parseInt(splt[1])<10)
			sbStart.append("0");
		sbStart.append(splt[1]);
		sbStart.append("-01-00-00");
		
		StringBuffer sbEnd  = new StringBuffer();
		sbEnd.append(splt[0]);
		sbEnd.append("-");
		splt[1] = (Integer.parseInt(splt[1])+1)+"";
		if(Integer.parseInt(splt[1])+1<10)
			sbEnd.append("0");
		sbEnd.append(splt[1]);
		sbEnd.append("-01-00-00");
		
		System.out.println(sbStart.toString());
		System.out.println(sbEnd.toString());
		
												
		sb.setLength(0); // 스트링버퍼초기화
		
		ArrayList<CallenderVO> list = new ArrayList<CallenderVO>();
		//ArrayList 생성
		
		// 기본값 전체보기
		if(cates.equals("전체보기")){
			// 쿼리문작성
			sb.append("SELECT id , title , category , memo , to_char(startday,'YYYY-MM-DD-HH24-MI') , ");
			sb.append("to_char(endday,'YYYY-MM-DD-HH24-MI') , allday , inputTime , repeatTerm , repeatNum FROM CALLENDER ");
			sb.append("where startday >= TO_Date( ? , 'YYYY-MM-DD-HH24-MI') AND ");
			sb.append("startday < TO_Date( ? , 'YYYY-MM-DD-HH24-MI') ");
			sb.append("AND id = ? order by startday");
			
			try {
				// 문장객체생성
				pstmt = conn.prepareStatement(sb.toString());
				// 문장객체에서 ?에 해당하는 값들을 셋
				pstmt.setString(1, sbStart.toString());
				pstmt.setString(2, sbEnd.toString());
				pstmt.setString(3, ids);
				

				// 실행
				rs = pstmt.executeQuery();

				// 다음 로우가 없을때까지 계속 반복하여 해당 값들을 가져와 vo에 대입후 list에 순서대로 삽입
				while (rs.next()) {
					String id = rs.getString("ID");
					String title = rs.getString("TITLE");
					String category = rs.getString("CATEGORY");
					String memo = rs.getString("MEMO");
					String startDay = rs.getString(5);
					String endDay = rs.getString(6);
					boolean allDay = rs.getBoolean("ALLDAY");
					boolean inputTime = rs.getBoolean("INPUTTIME");
					int repeatTerm = rs.getInt("REPEATTERM");
					int repeatNum = rs.getInt("REPEATNUM");
					CallenderVO vo = new CallenderVO(id, title,category , memo, startDay, endDay, allDay, inputTime, repeatTerm,
							repeatNum);
					list.add(vo);
				}

			} catch (SQLException e) {
				System.out.println("sql문에 문제가 있습니다.");
				System.out.println(e.getMessage());
				
			}	
		}		
		else {//category 선택시
		sb.append("SELECT id, title, category, memo, to_char(startDay,'YYYY-MM-DD-HH24-MI'), ");
		sb.append("to_char(endDay,'YYYY-MM-DD-HH24-MI'), allday, inputTime, repeatTerm, repeatNum FROM CALLENDER ");
		sb.append("where startday > TO_Date( ? , 'YYYY-MM-DD-HH24-MI') AND ");
		sb.append("startday < TO_Date( ? , 'YYYY-MM-DD-HH24-MI') ");
		sb.append("AND id = ? And category = ? order by startday");
		// 어레이리스트 생성
		
		try {
			// 문장객체생성
			pstmt = conn.prepareStatement(sb.toString());
			// 문장객체에서 ?에 해당하는 값들을 셋
			pstmt.setString(1, sbStart.toString());
			pstmt.setString(2, sbEnd.toString());
			pstmt.setString(3, ids);
			pstmt.setString(4, cates);

			// 실행
			rs = pstmt.executeQuery();

			// 다음 로우가 없을때까지 계속 반복하여 해당 값들을 가져와 vo에 대입후 list에 순서대로 삽입
			while (rs.next()) {
				String id = rs.getString("ID");
				String title = rs.getString("TITLE");
				String category = rs.getString("CATEGORY");
				String memo = rs.getString("MEMO");
				String startDay = rs.getString(5);
				String endDay = rs.getString(6);
				boolean allDay = rs.getBoolean("ALLDAY");
				boolean inputTime = rs.getBoolean("INPUTTIME");
				int repeatTerm = rs.getInt("REPEATTERM");
				int repeatNum = rs.getInt("REPEATNUM");
				CallenderVO vo = new CallenderVO(id, title,category , memo, startDay, endDay, allDay, inputTime, repeatTerm,
						repeatNum);
				list.add(vo);
			}
			for(CallenderVO v:list){
				System.out.println(v.getTitle());
			}

		} catch (SQLException e) {
			System.out.println("sql문에 문제가 있습니다.");
		}
		}
		return list;// 리턴값으로 list를 줌
	}

	public String inputSchedule(CallenderVO vo) { // 스케줄삽입
		String inputCheck = null; // 변수선언
		int repTerm = vo.getRepeatTerm();
		int repNum = vo.getRepeatNum();
		int lastNum = repNum;
		System.out.println(vo.toString());
				
			
		//repTime이 0이어도 한번은 실행되어야하니 do While문을 쓰자
		String[] spltStart = vo.getStartDay().split("-");
		String[] spltEnd = vo.getEndDay().split("-");
		StringBuffer sb = new StringBuffer();
		int i=0;
		
		do{
			// 스트링버퍼 초기화
			sb.setLength(0);
			// 쿼리문 작성
			sb.append("INSERT INTO CALLENDER ");
			sb.append("VALUES (?,?,?,?, TO_Date( ? , 'YYYY-MM-DD-HH24-MI'), ");
			sb.append("TO_Date( ? , 'YYYY-MM-DD-HH24-MI'), ? , ? , ? , ? ) ");
			String strStart, strEnd;
			
			try {
				// 문장객체생성, ?자리에 셋
				
				strStart = spltStart[0]+"-"+spltStart[1]+"-"+spltStart[2]+"-"+spltStart[3]+"-"+
						spltStart[4];
				strEnd = spltEnd[0]+"-"+spltEnd[1]+"-"+spltEnd[2]+"-"+spltEnd[3]+"-"+
						spltEnd[4];
				
				pstmt = conn.prepareStatement(sb.toString());
				pstmt.setString(1, vo.getId());
				pstmt.setString(2, vo.getTitle());
				pstmt.setString(3, vo.getCategory());				
				pstmt.setString(4, vo.getMemo());
				pstmt.setString(5, vo.getStartDay());
				pstmt.setString(6, vo.getEndDay());
				
				
				pstmt.setString(5, strStart);
				pstmt.setString(6, strEnd);
				pstmt.setBoolean(7, vo.isAllDay());
				pstmt.setBoolean(8, vo.isInputTime());
				pstmt.setInt(9, vo.getRepeatTerm());
				pstmt.setInt(10, lastNum);
				//남은 횟수를 한개씩 줄여준다.
				
				lastNum--;
				// 실행
				pstmt.executeUpdate();
				// 정상적으로 실행이 완료되었으므로 inputCheck에 입력되었다는 값을 삽입
				inputCheck = "입력되었습니다.";
				
			} catch (SQLException e) {
				System.out.println("인풋스케줄sql문에 이상이 있습니다.");
				e.getStackTrace();
				// 쿼리문실행이 정상적으로 이루어지지않았기때문에 inputCheck에 입력이 실패했다는 값을 삽입
				inputCheck = "입력에 실패하였습니다";
			}
			spltStart[2]= (Integer.parseInt(spltStart[2])+repTerm)+"";
			spltStart = dateOverflow(spltStart);
			spltEnd[2]= (Integer.parseInt(spltEnd[2])+repTerm)+"";
			spltEnd=dateOverflow(spltEnd);
			
			i++;
		}while(i<repNum);
		
		return inputCheck; // inputCheck를 리턴		

	}
	//6월37일 이런거 안받아준다 따로 처리해주자
	public String[] dateOverflow(String[] splt){
		//받아오는 문자열 집합의 형태는 0=년 1=월 2=일 3=시 4=분
		int day = Integer.parseInt(splt[2]);
		int month = Integer.parseInt(splt[1]);
		int Year = Integer.parseInt(splt[0]);
		Calendar cal = Calendar.getInstance();
		
		//splt[2]의 값이 cal.set(Calendar.YEAR,splt[0]); cal.set(Calendar.MONTH,splt[0])의 
		//마지막날보다 크면 월을 + 해주고 일에서 마지막날만큼 빼야됨
		//이걸 날짜가 해당 달의 마지막날보다 작아질때까지 해야됭
		
		//마찬가지로 13월도 안받으니까 개월수도 처리해야되네 - 이건 그나마 쉽다 12보다 크면 년+1하고 월-12 해주자
		//이것도 월 이 12이하가될때까지 해야됨...
		
		//이거 할필요없이 그냥 캘린더객체에 넣다뻇다하자 캘린더 갓갓
		cal.set(Year, month-1,day);
		splt[0] = cal.get(Calendar.YEAR)+"";
		splt[1] = (cal.get(Calendar.MONTH)+1)+"";
		splt[2] = cal.get(Calendar.DAY_OF_MONTH)+"";
		
		return splt;
	}

	
	
	public ArrayList<CallenderVO> SearchTitle(CallenderVO cvo) { // 찾기
		// 스트링버퍼 초기화
		sb.setLength(0);
		System.out.println(cvo.getId()+ " " + cvo.getTitle());
		// 쿼리문작성
		sb.append("SELECT id , title , category , memo , to_char(startday,'YYYY-MM-DD-HH24-MI') , ");
		sb.append("to_char(endday,'YYYY-MM-DD-HH24-MI') , allday , inputTime , repeatTerm , repeatNum FROM CALLENDER ");
		sb.append("WHERE ID = ? and TITLE LIKE ? ");
		sb.append("ORDER BY TITLE desc , STARTDAY desc ");
		// 리스트선언 및 인스턴스
		ArrayList<CallenderVO> list = new ArrayList<CallenderVO>();

		try {
			// 문장객체생성
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, cvo.getId());
			String regTitle = "%"+cvo.getTitle()+"%";
			pstmt.setString(2, regTitle);
			// 실행
			rs = pstmt.executeQuery();
			// 만약 해당 테이블의 다음 로우값이 있으면 계속 반복실행, 값들을 겟한후 vo에 삽입하고 vo를 리스트에 삽입
			while (rs.next()) {
				String id = rs.getString("ID");
				String title = rs.getString("TITLE");
				String category = rs.getString("CATEGORY");
				String memo = rs.getString("MEMO");
				String startDay = rs.getString(5);
				String endDay = rs.getString(6);
				boolean allDay = rs.getBoolean("ALLDAY");
				boolean inputTime = rs.getBoolean("INPUTTIME");
				int repeatTerm = rs.getInt("REPEATTERM");
				int repeatNum = rs.getInt("REPEATNUM");
				CallenderVO vo = new CallenderVO(id, title, category,memo, startDay, endDay, allDay, inputTime, repeatTerm,
						repeatNum);
				list.add(vo);
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
			System.out.println("서치타이틀 쿼리문이 제대로 실행되지 않았습니다.");
		}

		return list;
	}

	public String updateSchedule(CallenderVO vo) { // 수정하기
		// 계속반복되는 내용임
		String updateCheck;
		sb.setLength(0);
		sb.append("UPDATE CALLENDER ");
		sb.append("SET TITLE = ? , CATEGORY = ?, MEMO = ? , StartDay = TO_Date( ? , 'YYYY-MM-DD-HH24-MI'), endDay = TO_Date( ? , 'YYYY-MM-DD-HH24-MI') ");
		sb.append("WHERE ID = ? AND TITLE = ? AND startday = TO_Date( ? , 'YYYY-MM-DD-HH24-MI') ");

		try {
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getCategory());
			pstmt.setString(3, vo.getMemo());
			pstmt.setString(4, vo.getStartDay());
			pstmt.setString(5, vo.getEndDay());
			pstmt.setString(6, vo.getId());
			pstmt.setString(7, vo.getTitle());
			pstmt.setString(8, vo.getStartDay());
			pstmt.executeUpdate();
			updateCheck = "수정되었습니다.";
		} catch (SQLException e) {
			updateCheck = "수정에 실패하였습니다";
		}

		return updateCheck;
	}

	public String deleteSchedule(CallenderVO vo) {
		//계속 반복되는 내용
		String deleteCheck;
		sb.setLength(0);
		sb.append("DELETE FROM CALLENDER ");
		sb.append("WHERE ID = ? AND TITLE = ?  AND StartDay = TO_Date( ? , 'YYYY-MM-DD-HH24-MI')");

		try {
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, vo.getId());
			pstmt.setString(2, vo.getTitle());
			pstmt.setString(3, vo.getStartDay());
			pstmt.executeUpdate();
			deleteCheck = "삭제되었습니다.";
		} catch (SQLException e) {
			deleteCheck = "삭제를 실패하였습니다";
		}

		return deleteCheck;
	}

	public boolean joinMember(String etc) {
		// 스트링버퍼 초기화
		sb.setLength(0);
		//쿼리문작성
		sb.append("INSERT INTO MEMBER ");
		sb.append("VALUES (?,?) ");
		//변수선언
		boolean f = false;
		//매개변수를 스플릿으로 짤라서 ID와 PW값을 나눈후 각각 다른 변수에 대입
		String[] splt = etc.split(" ");
		String userId = splt[0];
		String userPw = splt[1];

		try {
			//문장객체 생성
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, userId);
			pstmt.setString(2, userPw);
			pstmt.executeUpdate();
			//쿼리문이 정상적으로 실행됬다면 다음 문장이 실행되므로 f에 true값을 대입
			f = true;
		} catch (SQLException e) {
			System.out.println("생성실패");
			//쿼리문이 정상작동하지않아 익셉션이 발생하였으므로 f에 false를 대입
			f = false;
		}
		return f; //f값을 리턴

	}

	public boolean loginMember(String etc) {
		//스트링버퍼 초기화
		sb.setLength(0);
		//쿼리문작성
		sb.append("SELECT * FROM member ");
		sb.append("where ID = ? and PW = ? ");
		//매개변수를 스플릿으로 나눔
		String[] splt = etc.split(" ");
		String userId = splt[0];
		String userPw = splt[1];
		//변수선언
		boolean f = false;
		try {
			//문장객체생성
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, userId);
			pstmt.setString(2, userPw);
			//실행
			rs = pstmt.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			if (rs.next()) {
				f = true;
				//아이디가 있으니 다음 로우가 있으니 트루값을 줌
			} else {
				f = false;
				//아이디가 없으니 다음 로우값이 없어 rs.next가 불가능해 false값을 줌
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("loginMember쿼리문에 이상이 있습니다.");
		}
		return f;//f값을 리턴

	}

}
