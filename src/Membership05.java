import java.sql.*;
import java.util.*;

public class Membership05
{
	Scanner sc = new Scanner(System.in);
	Connection con;
	PreparedStatement pstmt5;
	PreparedStatement pstmt6;
	PreparedStatement pstmt7;
	PreparedStatement pstmt8;
	PreparedStatement pstmt801;
	PreparedStatement pstmt9;
	
	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}catch(ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
		
	}
	
	public void connectMembership04DB()
	{
		try {
			con = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:xe",
					"scott",
					"tiger");
			
			String sql5 = "select * from membership where id= ?";
			pstmt5 = con.prepareStatement(sql5);
			
			String sql6 = "insert into membership values(MEMBERSHIP_SEQ.nextval, ?, ?, ?)";
			pstmt6 = con.prepareStatement(sql6);
			
			String sql7 = "delete from membership where id= ?";
			pstmt7 = con.prepareStatement(sql7);
			
			String sql8 = "select * from membership order by memnum";
			pstmt8 = con.prepareStatement(sql8);
			
			String sql801 = "update membership set black = ? where id = ?";
			pstmt801 = con.prepareStatement(sql801);
			
			String sql9 = "select rentid, (to_date(returndate,'YY/MM/DD')-sysdate)"
						+ "  from rental"
						+ " where rnum = ?";
			pstmt9 = con.prepareStatement(sql9);
			
		} 
		catch(Exception e)	{
			e.printStackTrace();
		}
	}
	
	public void showmemberMenu()
	{
		System.out.println("===========================");
		System.out.println("    1. 회원      등록");
		System.out.println("    2. 회원      조회");
		System.out.println("    3. 회원      삭제");
		System.out.println("    4. 종          료");
		System.out.println("    5. 회원 전체 조회");
		System.out.println("    6. 회원 정보 수정");
		System.out.println("    7. 연체 회원 조회");
		System.out.println("===========================");
		System.out.print("     선택 : ");
	}
	
	public void doMemberRun()	
	{
		showmemberMenu();
		connectMembership04DB();
		
		int choice;
		while(true) 
		{
			choice = sc.nextInt();
			sc.nextLine();
			
			switch(choice) {
			case 1:
				addmember();
				break;
			case 2:
				selmember();
				break;
			case 3:
				delmember();
				break;
			case 4:
				System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~");
				System.out.println("   ~~~~회원 창을~~~~");
				System.out.println("  ~~~~종료합니다.~~~~");
				System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~");
				return;
			case 5:
				listmember();
				break;
			case 6:
				change();
				break;
			case 7:
				blackList();
				break;
			default:
				System.out.println(">>>>잘못입력하셨습니다. 다시입력하세요.<<<<");
				break;
			}
			showmemberMenu();
		}
		
	}
	
	public void addmember()
	{
		System.out.print("회원아이디 : ");
		String id = sc.nextLine();
		
		try	{
			pstmt5.setString(1, id);
			ResultSet rs = pstmt5.executeQuery();
			if(rs.next() != true) {
				
				System.out.print("회원이름 : ");
				String name = sc.nextLine();
				System.out.print("대여가능여부 : ");
				String black = sc.nextLine();
				pstmt6.setString(1, id);
				pstmt6.setString(2, name);
				pstmt6.setString(3, black);
				int updateCount = pstmt6.executeUpdate();
				System.out.println("회원에 추가되었습니다.");
			} else {
				System.out.println(">>>>동일한 회원 아이디가 존재합니다.<<<<");
				System.out.println("   >>>>다른 아이디로 등록하세요.<<<<");
			}
			rs.close();
		}catch(Exception e)	{
			e.printStackTrace();
			System.out.println("addmember() 에러");
		}
	}
	
	public void selmember()
	{
		System.out.print("조회할 회원 id : ");
		
		String id = sc.nextLine();
		
		try	{
			pstmt5.setString(1, id);
			ResultSet rs = pstmt5.executeQuery();
			if(rs.next()) {
				System.out.println("--------------------------------------------");
				System.out.println("     회원아이디 : " + rs.getString(2));
				System.out.println("     회원이름   : " + rs.getString(3));
				if(rs.getString(4) == null){
					System.out.println("         대여  가능");
					System.out.println("--------------------------------------------");
				} else {
					System.out.println("         대여불가능");
					System.out.println("--------------------------------------------");
				}
			} else {
				System.out.println(">>>>해당 회원이 없습니다.<<<<");
				System.out.println("  >>>>새로 가입하세요.<<<<");
			}
			rs.close();
		} catch(Exception e)	{
			System.out.println("selmember() 에러");
		}
	}
	
	public void delmember() 
	{
		System.out.print("삭제할 회원 아이디 : ");
		String id = sc.nextLine();
		
		try {
			pstmt7.setString(1, id);
			int updateCount = pstmt7.executeUpdate();
			System.out.println("-------------------------------");
			System.out.println("     [ id : "+id+"님이 ]");
			System.out.println(" 회원목록에서 삭제되었습니다.");
			System.out.println("      복구는 불가능하오니");
			System.out.println("       다시 이용하실 경우 ");
			System.out.println("  회원 등록 후 이용 바랍니다.");
			System.out.println("-------------------------------");
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("delmember() 에러");
		}
	}
	
	public void listmember()
	{
		System.out.println("보유한 회원 전체 리스트");
		
		try {
			int count = 0;
			ResultSet rs = pstmt8.executeQuery();
			while(rs.next()) {
				System.out.println("--------------------------------------------");
				System.out.println("     회원번호 : " + rs.getString(1));
				System.out.println("     회원아이디 : " + rs.getString(2));
				System.out.println("     회원이름 : " + rs.getString(3));
				if(rs.getString(4) == null) {
					System.out.println("          대여  가능");
				} else {
					System.out.println(">>>>       대여불가능       <<<<");
				}
				System.out.println("--------------------------------------------");
				count++;
			} 			
			if(count == 0) {
				System.out.println(">>>>보유한 회원이 없습니다.<<<<");
			}
			rs.close();
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("listmember() 에러");
		}	
	}
	
	
	public void change()
	{
		System.out.println("대여 상태 수정할 회원의 id : ");
		String id = sc.nextLine();
		
		System.out.println("대여 불가 상태일 경우 '불가능'입력");
		System.out.println("아닐 경우 엔터");
		String black = sc.nextLine();
		try {
			pstmt801.setString(1, black);
			pstmt801.setString(2, id);
			ResultSet rs = pstmt801.executeQuery();
			
			pstmt5.setString(1, id);
			ResultSet showblack = pstmt5.executeQuery();
			if(showblack.next()) {
				if(showblack.getString(4) == null) {
					System.out.println(">>>대여 가능<< 으로 변경 완료되었습니다.");
				}else {
					System.out.println(">>>대여 불가능<<< 으로 변경 완료되었습니다.");
				}
			}
			showblack.close();
			rs.close();
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("change() 에러");
		}
		
	}
	
	public void blackList()
	{
		System.out.println(">>>>연체 중인 회원<<<<");
		System.out.println("=======================");
		System.out.println();
		try {
			for(int i=1;i<1000;i++) {
				pstmt9.setInt(1, i);
				ResultSet rs = pstmt9.executeQuery();
				double blackdate;
				String blackid;
				if(rs.next()) {
					blackid = rs.getString(1);
					blackdate = rs.getDouble(2);
					if(blackdate<0) {
						System.out.println("  ▷ "+blackid);
					}
				}
			}
			System.out.println();
			System.out.println("=======================");
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("blackList() 에러");
		}
	}
	
	public void membershipclose()
	{
		try {
			pstmt5.close();
			pstmt6.close();
			pstmt7.close();
			pstmt8.close();
			pstmt801.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}


}
