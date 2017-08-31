package login;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

public class CallenderDAO {
	// ��������
	String driver = "oracle.jdbc.driver.OracleDriver";
	String url = "jdbc:oracle:thin:@orcl.cnfoop449y4h.us-east-2.rds.amazonaws.com:1521:orcl";
	String user = "notscott";
	String password = "nottiger";
	Connection conn = null;
	ResultSet rs = null;
	PreparedStatement pstmt = null;
	StringBuffer sb = new StringBuffer();

	public CallenderDAO() {
		// db ����
		try {
			Class.forName(driver); // ����̹��ε�
			conn = DriverManager.getConnection(url, user, password); // Ŀ�ؼǰ�ü����
		} catch (ClassNotFoundException e) {
			System.out.println("����̹��������");
		} catch (SQLException e) {
			System.out.println("db�������");
		}

	}// Constructor end

	public ArrayList<CallenderVO> selectTerm(String etc) { // �Ⱓ��ȸ
		System.out.println(etc);
		String[] splt = etc.split(" "); // �Ű������� ���ø����� ����
		String ids; 
		String cates;
		ids = splt[2]; // ids������ splt[2]�� ���� ����
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
		
												
		sb.setLength(0); // ��Ʈ�������ʱ�ȭ
		
		ArrayList<CallenderVO> list = new ArrayList<CallenderVO>();
		//ArrayList ����
		
		// �⺻�� ��ü����
		if(cates.equals("��ü����")){
			// �������ۼ�
			sb.append("SELECT id , title , category , memo , to_char(startday,'YYYY-MM-DD-HH24-MI') , ");
			sb.append("to_char(endday,'YYYY-MM-DD-HH24-MI') , allday , inputTime , repeatTerm , repeatNum FROM CALLENDER ");
			sb.append("where startday >= TO_Date( ? , 'YYYY-MM-DD-HH24-MI') AND ");
			sb.append("startday < TO_Date( ? , 'YYYY-MM-DD-HH24-MI') ");
			sb.append("AND id = ? order by startday");
			
			try {
				// ���尴ü����
				pstmt = conn.prepareStatement(sb.toString());
				// ���尴ü���� ?�� �ش��ϴ� ������ ��
				pstmt.setString(1, sbStart.toString());
				pstmt.setString(2, sbEnd.toString());
				pstmt.setString(3, ids);
				

				// ����
				rs = pstmt.executeQuery();

				// ���� �ο찡 ���������� ��� �ݺ��Ͽ� �ش� ������ ������ vo�� ������ list�� ������� ����
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
				System.out.println("sql���� ������ �ֽ��ϴ�.");
				System.out.println(e.getMessage());
				
			}	
		}		
		else {//category ���ý�
		sb.append("SELECT id, title, category, memo, to_char(startDay,'YYYY-MM-DD-HH24-MI'), ");
		sb.append("to_char(endDay,'YYYY-MM-DD-HH24-MI'), allday, inputTime, repeatTerm, repeatNum FROM CALLENDER ");
		sb.append("where startday > TO_Date( ? , 'YYYY-MM-DD-HH24-MI') AND ");
		sb.append("startday < TO_Date( ? , 'YYYY-MM-DD-HH24-MI') ");
		sb.append("AND id = ? And category = ? order by startday");
		// ��̸���Ʈ ����
		
		try {
			// ���尴ü����
			pstmt = conn.prepareStatement(sb.toString());
			// ���尴ü���� ?�� �ش��ϴ� ������ ��
			pstmt.setString(1, sbStart.toString());
			pstmt.setString(2, sbEnd.toString());
			pstmt.setString(3, ids);
			pstmt.setString(4, cates);

			// ����
			rs = pstmt.executeQuery();

			// ���� �ο찡 ���������� ��� �ݺ��Ͽ� �ش� ������ ������ vo�� ������ list�� ������� ����
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
			System.out.println("sql���� ������ �ֽ��ϴ�.");
		}
		}
		return list;// ���ϰ����� list�� ��
	}

	public String inputSchedule(CallenderVO vo) { // �����ٻ���
		String inputCheck = null; // ��������
		int repTerm = vo.getRepeatTerm();
		int repNum = vo.getRepeatNum();
		int lastNum = repNum;
		System.out.println(vo.toString());
				
			
		//repTime�� 0�̾ �ѹ��� ����Ǿ���ϴ� do While���� ����
		String[] spltStart = vo.getStartDay().split("-");
		String[] spltEnd = vo.getEndDay().split("-");
		StringBuffer sb = new StringBuffer();
		int i=0;
		
		do{
			// ��Ʈ������ �ʱ�ȭ
			sb.setLength(0);
			// ������ �ۼ�
			sb.append("INSERT INTO CALLENDER ");
			sb.append("VALUES (?,?,?,?, TO_Date( ? , 'YYYY-MM-DD-HH24-MI'), ");
			sb.append("TO_Date( ? , 'YYYY-MM-DD-HH24-MI'), ? , ? , ? , ? ) ");
			String strStart, strEnd;
			
			try {
				// ���尴ü����, ?�ڸ��� ��
				
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
				//���� Ƚ���� �Ѱ��� �ٿ��ش�.
				
				lastNum--;
				// ����
				pstmt.executeUpdate();
				// ���������� ������ �Ϸ�Ǿ����Ƿ� inputCheck�� �ԷµǾ��ٴ� ���� ����
				inputCheck = "�ԷµǾ����ϴ�.";
				
			} catch (SQLException e) {
				System.out.println("��ǲ������sql���� �̻��� �ֽ��ϴ�.");
				e.getStackTrace();
				// ������������ ���������� �̷�������ʾұ⶧���� inputCheck�� �Է��� �����ߴٴ� ���� ����
				inputCheck = "�Է¿� �����Ͽ����ϴ�";
			}
			spltStart[2]= (Integer.parseInt(spltStart[2])+repTerm)+"";
			spltStart = dateOverflow(spltStart);
			spltEnd[2]= (Integer.parseInt(spltEnd[2])+repTerm)+"";
			spltEnd=dateOverflow(spltEnd);
			
			i++;
		}while(i<repNum);
		
		return inputCheck; // inputCheck�� ����		

	}
	//6��37�� �̷��� �ȹ޾��ش� ���� ó��������
	public String[] dateOverflow(String[] splt){
		//�޾ƿ��� ���ڿ� ������ ���´� 0=�� 1=�� 2=�� 3=�� 4=��
		int day = Integer.parseInt(splt[2]);
		int month = Integer.parseInt(splt[1]);
		int Year = Integer.parseInt(splt[0]);
		Calendar cal = Calendar.getInstance();
		
		//splt[2]�� ���� cal.set(Calendar.YEAR,splt[0]); cal.set(Calendar.MONTH,splt[0])�� 
		//������������ ũ�� ���� + ���ְ� �Ͽ��� ����������ŭ ���ߵ�
		//�̰� ��¥�� �ش� ���� ������������ �۾��������� �ؾ߉�
		
		//���������� 13���� �ȹ����ϱ� �������� ó���ؾߵǳ� - �̰� �׳��� ���� 12���� ũ�� ��+1�ϰ� ��-12 ������
		//�̰͵� �� �� 12���ϰ��ɶ����� �ؾߵ�...
		
		//�̰� ���ʿ���� �׳� Ķ������ü�� �ִٖP������ Ķ���� ����
		cal.set(Year, month-1,day);
		splt[0] = cal.get(Calendar.YEAR)+"";
		splt[1] = (cal.get(Calendar.MONTH)+1)+"";
		splt[2] = cal.get(Calendar.DAY_OF_MONTH)+"";
		
		return splt;
	}

	
	
	public ArrayList<CallenderVO> SearchTitle(CallenderVO cvo) { // ã��
		// ��Ʈ������ �ʱ�ȭ
		sb.setLength(0);
		System.out.println(cvo.getId()+ " " + cvo.getTitle());
		// �������ۼ�
		sb.append("SELECT id , title , category , memo , to_char(startday,'YYYY-MM-DD-HH24-MI') , ");
		sb.append("to_char(endday,'YYYY-MM-DD-HH24-MI') , allday , inputTime , repeatTerm , repeatNum FROM CALLENDER ");
		sb.append("WHERE ID = ? and TITLE LIKE ? ");
		sb.append("ORDER BY TITLE desc , STARTDAY desc ");
		// ����Ʈ���� �� �ν��Ͻ�
		ArrayList<CallenderVO> list = new ArrayList<CallenderVO>();

		try {
			// ���尴ü����
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, cvo.getId());
			String regTitle = "%"+cvo.getTitle()+"%";
			pstmt.setString(2, regTitle);
			// ����
			rs = pstmt.executeQuery();
			// ���� �ش� ���̺��� ���� �ο찪�� ������ ��� �ݺ�����, ������ ������ vo�� �����ϰ� vo�� ����Ʈ�� ����
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
			System.out.println("��ġŸ��Ʋ �������� ����� ������� �ʾҽ��ϴ�.");
		}

		return list;
	}

	public String updateSchedule(CallenderVO vo) { // �����ϱ�
		// ��ӹݺ��Ǵ� ������
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
			updateCheck = "�����Ǿ����ϴ�.";
		} catch (SQLException e) {
			updateCheck = "������ �����Ͽ����ϴ�";
		}

		return updateCheck;
	}

	public String deleteSchedule(CallenderVO vo) {
		//��� �ݺ��Ǵ� ����
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
			deleteCheck = "�����Ǿ����ϴ�.";
		} catch (SQLException e) {
			deleteCheck = "������ �����Ͽ����ϴ�";
		}

		return deleteCheck;
	}

	public boolean joinMember(String etc) {
		// ��Ʈ������ �ʱ�ȭ
		sb.setLength(0);
		//�������ۼ�
		sb.append("INSERT INTO MEMBER ");
		sb.append("VALUES (?,?) ");
		//��������
		boolean f = false;
		//�Ű������� ���ø����� ©�� ID�� PW���� ������ ���� �ٸ� ������ ����
		String[] splt = etc.split(" ");
		String userId = splt[0];
		String userPw = splt[1];

		try {
			//���尴ü ����
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, userId);
			pstmt.setString(2, userPw);
			pstmt.executeUpdate();
			//�������� ���������� ������ٸ� ���� ������ ����ǹǷ� f�� true���� ����
			f = true;
		} catch (SQLException e) {
			System.out.println("��������");
			//�������� �����۵������ʾ� �ͼ����� �߻��Ͽ����Ƿ� f�� false�� ����
			f = false;
		}
		return f; //f���� ����

	}

	public boolean loginMember(String etc) {
		//��Ʈ������ �ʱ�ȭ
		sb.setLength(0);
		//�������ۼ�
		sb.append("SELECT * FROM member ");
		sb.append("where ID = ? and PW = ? ");
		//�Ű������� ���ø����� ����
		String[] splt = etc.split(" ");
		String userId = splt[0];
		String userPw = splt[1];
		//��������
		boolean f = false;
		try {
			//���尴ü����
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, userId);
			pstmt.setString(2, userPw);
			//����
			rs = pstmt.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			if (rs.next()) {
				f = true;
				//���̵� ������ ���� �ο찡 ������ Ʈ�簪�� ��
			} else {
				f = false;
				//���̵� ������ ���� �ο찪�� ���� rs.next�� �Ұ����� false���� ��
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("loginMember�������� �̻��� �ֽ��ϴ�.");
		}
		return f;//f���� ����

	}

}
