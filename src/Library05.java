import java.sql.*;
import java.util.*;

public class Library05 
{
	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);
		Library05 lib = new Library05();
		lib.welcome();
		
		System.out.print("아이디 : ");
		String id = sc.nextLine();
		
		if(id.equals("manager")) {
			System.out.print("비밀번호 : ");
			String pw = sc.nextLine();
			
			if(pw.equals("manager")) {
				lib.doManagerRun();
			}else {
				System.out.print("비밀번호가 틀렸습니다.");
				System.out.println("(o言o╬)");
			}
		}else if(id.equals("book")){
			UserMode05 usermode = new UserMode05();
			usermode.doUserRun();
		}else {
			System.out.println("    >>>>잘못된 접근입니다.<<<<");
			System.out.println(">>>>유의사항을 다시 읽어주세요.<<<<");
		}
	}
	
	Scanner sc = new Scanner(System.in);
	Connection con;
	
	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}catch(ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
		
	}
	public void connectManagerDB()
	{
		try {
			con = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:xe",
					"scott",
					"tiger");
		} 
		catch(Exception e)	{
			e.printStackTrace();
		}
	}
	
	public void welcome()
	{
		System.out.println("================================");
		System.out.println("★★★★★★★★★★★환영합니다★★★★★★★★★★★");
		System.out.println("        회원이실 경우");
		System.out.println("   회원 공용 아이디 : book 를");
		System.out.println("관리자이실 경우 관리자 아이디 를");
		System.out.println("        입력해주세요.");
		System.out.println("================================");
	}
	
	public void showManagerMenu()
	{
		System.out.println("========관리자 메뉴========");
		System.out.println("     1. 도 서  관 리");
		System.out.println("     2. 회 원  관 리");
		System.out.println("     3. 대 여  관 리");
		System.out.println("     4. 종        료");
		System.out.println("===========================");
		System.out.print("       선택 : ");
	}
	
	
	
	public void doManagerRun()	
	{
		connectManagerDB();
		int choice;
		
		while(true) 
		{
			showManagerMenu();
			choice = sc.nextInt();
			sc.nextLine();
			
			switch(choice) {
			case 1:
				BookEditor05 be = new BookEditor05();
				be.doBookRun();
				break;
			case 2:
				Membership05 ms = new Membership05();
				ms.doMemberRun();
				break;
			case 3:
				Rental05 rt = new Rental05();
				rt.doRentalRun();
				break;
			case 4:
				System.out.println();
				System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~");
				System.out.println("   ~~~프로그램을~~~");
				System.out.println("  ~~~~종료합니다.~~~~");
				System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~");
				System.out.println();
				doallclose();
				return;
			default:
				System.out.println(">>>>잘못입력하셨습니다.<<<<");
				System.out.println("  >>>>다시입력하세요.<<<<");
				break;
			}
		}
		
	}
	
	public void doallclose()
	{
		try {
			con.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
