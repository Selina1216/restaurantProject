package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import vo.BoardVO;
import vo.MemberVO;

public class BoardDAO {
	
	public static final int EACH = 5;
	
	// ��ü �Խù� ��� ��ȸ �޼ҵ�
	public ArrayList<BoardVO> getBoardList(){
		ArrayList<BoardVO> list = new ArrayList<BoardVO>();
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@70.12.111.108:1521:xe","board","board");
			String sql = "select boardseq ��ȣ, boardtitle ����,  boardwriter �ۼ���,"
				+ " to_char(boardtime, 'YYYY/MM/DD hh24:mi:ss') �ۼ��ð�, boardviewcount ��ȸ��"
				+ " from board order by ��ȣ desc";
			PreparedStatement pt = con.prepareStatement(sql);
			ResultSet rs = pt.executeQuery();
				
			while(rs.next()){
				BoardVO vo = new BoardVO();
				vo.setBoardseq(rs.getInt("��ȣ"));
				vo.setBoardtitle(rs.getString("����"));
				vo.setBoardwriter(rs.getString("�ۼ���"));
				vo.setBoardtime(rs.getString("�ۼ��ð�"));
				vo.setBoardviewcount(rs.getInt("��ȸ��"));
				list.add(vo);
			}
			 
			con.close();
		}catch(Exception e) {
				e.printStackTrace();
		}
		return list;
	}
	
	// �Խù� ����¡ �޼ҵ�
	// �����ְ� ���� ������ ��ȣ / ���������� �����ַ��� �Խù� ����
	public ArrayList<BoardVO> getBoardList(int pagenum){
		ArrayList<BoardVO> list = new ArrayList<BoardVO>();
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@70.12.111.108:1521:xe","board","board");
			String sql = "select X.r, X.boardseq ��ȣ, X.boardtitle ����, X.boardwriter �ۼ���,"
					 + " to_char(X.boardtime, 'YYYY/MM/DD hh24:mi:ss') �ۼ��ð�, X.boardviewcount ��ȸ��"
					 + " from (select rownum r, A.boardseq, A.boardtitle, A.boardwriter, A.boardtime, A.boardviewcount"
					 + " from (select * from board order by boardtime desc) A"
					 + " where rownum <= ?) X"
					 + " where X.r >=?";
			PreparedStatement pt = con.prepareStatement(sql);
			int start = (pagenum-1)*EACH +1;
			int end = pagenum*EACH;		
			pt.setInt(2, start);
			pt.setInt(1, end);			
			ResultSet rs = pt.executeQuery();	
			while(rs.next()){
				BoardVO vo = new BoardVO();
				vo.setBoardseq(rs.getInt("��ȣ"));
				vo.setBoardtitle(rs.getString("����"));
				vo.setBoardwriter(rs.getString("�ۼ���"));
				vo.setBoardtime(rs.getString("�ۼ��ð�"));
				vo.setBoardviewcount(rs.getInt("��ȸ��"));
				list.add(vo);
			}
			 
			con.close();
		}catch(Exception e) {
				e.printStackTrace();
		}
		return list;
	}
	
	// �Խù� ��� �˻� �޼ҵ�
		public ArrayList<BoardVO> getBoardList(String tag, String word){
			ArrayList<BoardVO> list = new ArrayList<BoardVO>();
			
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				Connection con = DriverManager.getConnection("jdbc:oracle:thin:@70.12.111.108:1521:xe","board","board");
				PreparedStatement pt = null;
				
				// ���� �˻�
				if(tag.equals("boardtitle")) { 
					String sql = "select boardseq ��ȣ, boardtitle ����,  boardwriter �ۼ���,"
							+ " to_char(boardtime, 'YYYY/MM/DD hh24:mi:ss') �ۼ��ð�, boardviewcount ��ȸ��"
							+ " from board where boardtitle like ? order by ��ȣ desc";
					pt = con.prepareStatement(sql);
					pt.setString(1, "%"+word+"%");
					pt.executeUpdate();
					
				// ���� �˻�	
				} else if(tag.equals("boardcontents")) {
					String sql = "select boardseq ��ȣ, boardtitle ����,  boardwriter �ۼ���,"
							+ " to_char(boardtime, 'YYYY/MM/DD hh24:mi:ss') �ۼ��ð�, boardviewcount ��ȸ��"
							+ " from board where boardcontents like ? order by ��ȣ desc";
					pt = con.prepareStatement(sql);
					pt.setString(1, "%"+word+"%");
					pt.executeUpdate();

				// �ۼ��� �˻�
				} else {
					String sql = "select boardseq ��ȣ, boardtitle ����,  boardwriter �ۼ���,"
							+ " to_char(boardtime, 'YYYY/MM/DD hh24:mi:ss') �ۼ��ð�, boardviewcount ��ȸ��"
							+ " from board where boardwriter like ? order by ��ȣ desc";
					pt = con.prepareStatement(sql);
					pt.setString(1, "%"+word+"%");
					pt.executeUpdate();
					}
				
				ResultSet rs = pt.executeQuery();
				while(rs.next()){
					BoardVO vo = new BoardVO();
					vo.setBoardseq(rs.getInt("��ȣ"));
					vo.setBoardtitle(rs.getString("����"));
					vo.setBoardwriter(rs.getString("�ۼ���"));
					vo.setBoardtime(rs.getString("�ۼ��ð�"));
					vo.setBoardviewcount(rs.getInt("��ȸ��"));
					list.add(vo);
				}
				
				con.close();
			}catch(Exception e) {
					e.printStackTrace();
			}
			return list;
		}
	
	// �Խù� ��ȸ �޼ҵ�
	public BoardVO getBoardDetail(int seq){
		BoardVO vo = new BoardVO();
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@70.12.111.108:1521:xe","board","board");
			String sql = "select boardseq ��ȣ, boardtitle ����,  boardcontents ����, boardwriter �ۼ���"
					+ ", to_char(boardtime, 'YYYY/MM/DD hh24:mi:ss') �ۼ��ð�, boardviewcount ��ȸ��"
					+ " from board where boardseq = ? order by ��ȣ desc";
			
			PreparedStatement pt = con.prepareStatement(sql);
				
			pt.setInt(1, seq);
			ResultSet rs = pt.executeQuery();
			rs.next();
			vo.setBoardseq(rs.getInt("��ȣ"));
			vo.setBoardtitle(rs.getString("����"));
			vo.setBoardcontents(rs.getString("����"));
			vo.setBoardwriter(rs.getString("�ۼ���"));
			vo.setBoardtime(rs.getString("�ۼ��ð�"));
			vo.setBoardviewcount(rs.getInt("��ȸ��"));
			con.close();
		}catch(Exception e) {
				e.printStackTrace();
		}			
		return vo;
	}
	
	// �Խù� ���� �޼ҵ�
	public void insertBoard(BoardVO vo) {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@70.12.111.108:1521:xe","board","board");
			String sql = 
					"insert into board values "
					+ " ( (select max(boardseq)+1 from board) , ? , ? , ? , sysdate, 0)";
			PreparedStatement pt = con.prepareStatement(sql);
	
			pt.setString(1, vo.getBoardtitle());
			pt.setString(2, vo.getBoardcontents());
			pt.setString(3, vo.getBoardwriter());
	
			int insertRow = pt.executeUpdate();

			con.close();
		}catch(Exception e) {
				e.printStackTrace();
		}
	}
	
	// �Խù� ���� �޼ҵ�
	public void updateBoard(int seq, String newtitle, String newcontents) {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@70.12.111.108:1521:xe","board","board");
			String sql = 
					"update board set boardtitle=? "
					+ ", boardcontents = ? where boardseq = ?";
			PreparedStatement pt = con.prepareStatement(sql);
	
			pt.setString(1, newtitle);
			pt.setString(2, newcontents);
			pt.setInt(3, seq);
			pt.executeUpdate();
			con.close();
		}catch(Exception e) {
				e.printStackTrace();
		}
		
	}
	
	// �Խù� ���� �޼ҵ�
	public void deleteBoard(int seq) {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@70.12.111.108:1521:xe","board","board");
			String sql = 
					"delete board where boardseq = ?";
			PreparedStatement pt = con.prepareStatement(sql);
			pt.setInt(1, seq);
			pt.executeUpdate();
			con.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
			
	}
	
	// ��ȸ�� ī��Ʈ �޼���
	public void viewCount(int seq) {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@70.12.111.108:1521:xe","board","board");
			String sql = 
					"update board set boardviewcount = (select max(boardviewcount)+1 from board where boardseq= ?) where boardseq=?";
			PreparedStatement pt = con.prepareStatement(sql);
			pt.setInt(1, seq);
			pt.setInt(2, seq);
			pt.executeUpdate();
			con.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// �� �Խù� �� ��ȸ
	
	public int getTotalContents() {
		int count = 0;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@70.12.111.108:1521:xe","board","board");
			String sql = "select count(*) from board";
			PreparedStatement pt = con.prepareStatement(sql);
			ResultSet rs = pt.executeQuery();
			rs.next();
			count = rs.getInt("count(*)");
			con.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	
}