
import java.sql.*;
import java.util.*;

public class BookEditor05
{
	Random rand = new Random();
	Scanner sc = new Scanner(System.in);
	Connection con;
	PreparedStatement pstmt1;
	PreparedStatement pstmt21;
	PreparedStatement pstmt3;
	PreparedStatement pstmt4;
	PreparedStatement randompstmt1;
	
	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}catch(ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
			System.out.println("OracleDriver 에러");
		}
		
	}
	public void connectBookEditor04DB()
	{
		try {
			con = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:xe",
					"scott",
					"tiger");
			
			
			String sql1 = "insert into BookDB values(BOOKDB_SEQ.nextval, ?, ?)";
			pstmt1 = con.prepareStatement(sql1);
			
			String sql21 = "select * from BookDB where btitle like ?";
			pstmt21 = con.prepareStatement(sql21);
			
			String sql3 = "delete from BookDB where btitle= ?";
			pstmt3 = con.prepareStatement(sql3);
			
			String sql4 = "select * from BookDB order by bnum";
			pstmt4 = con.prepareStatement(sql4);
			
			String randomsql1 = "select * from BookDB where bnum = ?";
			randompstmt1 = con.prepareStatement(randomsql1);
		} 
		catch(Exception e)	{
			e.printStackTrace();
			System.out.println("connectBookEditor04DB() 에러");
		}
	}
	
	public void showBookMenu()
	{
		System.out.println("===========================");
		System.out.println("     1. 책 등     록");
		System.out.println("     2. 책 조     회");
		System.out.println("     3. 책 전체 조회");
		System.out.println("     4. 종        료");
		System.out.println("     5. 책 삭     제");
		System.out.println("===========================");
		 System.out.print("      선택 : ");
	}
	
	public void doBookRun()	
	{
		connectBookEditor04DB();
		int choice;
		
		while(true) 
		{
			showBookMenu();
			choice = sc.nextInt();
			sc.nextLine();
			
			switch(choice) {
			case 1:
				addBook();
				break;
			case 2:
				selBook();
				break;
			case 3:
				listBook();
				break;
			case 4:
				System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~");
				System.out.println("   ~~~~도서 창을~~~~");
				System.out.println("  ~~~~종료합니다.~~~~");
				System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~");
				return;
			case 5:
				delBook();
				break;
			default:
				System.out.println(">>>>잘못입력하셨습니다. 다시입력하세요.<<<<");
				break;
			}
		}
		
	}
	
	public void addBook()
	{
		
		System.out.print("제목 : ");
		String btitle = sc.nextLine();
		
		
		try	{
			pstmt21.setString(1, btitle);
			ResultSet rs = pstmt21.executeQuery();
			if(rs.next() != true)
			{
				System.out.print("권수 : ");
				int bcount = sc.nextInt();
				pstmt1.setString(1, btitle);
				pstmt1.setInt(2, bcount);
				int updateCount = pstmt1.executeUpdate();
				System.out.println("도서 목록에 추가되었습니다.");
			}else {
				System.out.println(">>>>동일한 책이 존재합니다. 다시 확인하세요.<<<<");
			}
			rs.close();
		}catch(Exception e)	{
			e.printStackTrace();
			System.out.println("addBook() 에러");
		}
	}
	public void selBook()
	{
		System.out.print("조회할 도서 제목 : ");
		
		String btitle = "%"+sc.nextLine()+"%";
		
		try	{
			pstmt21.setString(1, btitle);
			ResultSet rs = pstmt21.executeQuery();
			int count = 0;
			while(rs.next()) {
				System.out.println("--------------------------------------------");
				System.out.println("    도서 제목 : " + rs.getString(2));
				System.out.println("        "+rs.getString(3)+"권 보유");
				System.out.println("--------------------------------------------");
				count++;
			}
			if(count == 0) {
				System.out.println(">>>>해당 도서가 없습니다.<<<<");
			}
			rs.close();
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("selBook() 에러");
		}
	}
	
	public void delBook()
	{
		System.out.print("삭제할 도서 제목 : ");
		String btitle = sc.nextLine();
		
	
		try {
			pstmt3.setString(1, btitle);
			int updateCount = pstmt3.executeUpdate();
			System.out.println("도서 목록에서 삭제되었습니다.");
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("delBook() 에러");
		}
	}
	
	public void listBook()
	{
		System.out.println("보유한 도서 전체 리스트");
		
		try {
			ResultSet rs = pstmt4.executeQuery();
			int count=0;
			while(rs.next()) {
				
				System.out.println("--------------------------------------------");
				System.out.println("     도서 번호 : " + rs.getString(1));
				System.out.println("     도서 제목 : " + rs.getString(2));
				System.out.println("         "+rs.getString(3)+" 권  보유");
				System.out.println("--------------------------------------------");
				count++;
			} 
			if(count == 0) {
				System.out.println(">>>>보유한 책이 없습니다.<<<<");
			}
			rs.close();
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("listBook() 에러");
		}		
	}
	
	public void randomBook()
	{
		int randomBook = rand.nextInt(20)+1;
		
		try
		{
			randompstmt1.setInt(1, randomBook);
			ResultSet rs = randompstmt1.executeQuery();
			if(rs.next()) {
				System.out.println("================추천 도서=================");
				System.out.print("    ****");
				System.out.print(rs.getString(2));
				System.out.println("****");
				System.out.println("==========================================");
			}
			
		} catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("random() 에러");
		}
	}
	
	public void bookdbclose()
	{
		try {
			pstmt1.close();
			pstmt21.close();
			pstmt3.close();
			pstmt4.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
