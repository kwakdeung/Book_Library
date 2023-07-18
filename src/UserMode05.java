import java.sql.*;
import java.util.*;

public class UserMode05
{
	
	Scanner sc = new Scanner(System.in);
	Connection con;
	
	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}catch(ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
		
	}
	
	public static void main(String[] args)
	{
		
		UserMode05 um = new UserMode05();
		um.doUserRun();
	}
	
	public void showUserMenu()
	{
		System.out.println("=======회원메뉴=========");
		System.out.println("    1. 도 서  검 색");
		System.out.println("    2. 대        여");
		System.out.println("    3. 반        납");
		System.out.println("    4. 종        료");
		System.out.println("========================");
		System.out.print("선택 : ");
	}
	
	public void doUserRun()	
	{
		int choice;
		
		while(true) 
		{
			randomBook();
			showUserMenu();
			choice = sc.nextInt();
			sc.nextLine();
			
			switch(choice) {
			case 1:
				userSelBookDB();
				break;
			case 2:
				userBookRent();
				break;
			case 3:
				userBookReturn();
				break;
			case 4:
				System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				System.out.println("         ~~~프로그램을~~~");
				System.out.println("        ~~~~종료합니다.~~~~");
				System.out.println("     ~~~~반납 예정일에 맞춰~~~~");
				System.out.println("  ~~~~반드시 반납 부탁드립니다.~~~~");
				System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				return;
			default:
				System.out.println("잘못입력하셨습니다. 다시입력하세요.");
				break;
			}
		}
		
	}
	
	public void userSelBookDB()
	{
		BookEditor05 userBookEditor = new BookEditor05();
		userBookEditor.connectBookEditor04DB();
		
		userBookEditor.selBook();
		
	}
	
	public void userBookRent()
	{
		Rental05 userRent = new Rental05();
		userRent.connectRental05DB();
		
		userRent.brent();
	}

	public void userBookReturn()
	{
		Rental05 userReturn = new Rental05();
		userReturn.connectRental05DB();
		
		userReturn.breturn();
	}
	
	public void randomBook()
	{
		BookEditor05 userRandomBook = new BookEditor05();
		userRandomBook.connectBookEditor04DB();
		
		userRandomBook.randomBook();
	}
}
