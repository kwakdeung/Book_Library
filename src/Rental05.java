import java.sql.*;
import java.util.*;

public class Rental05
{
	Scanner sc = new Scanner(System.in);
	Connection con;
	
	PreparedStatement pstmt2;
	PreparedStatement pstmt9;
	PreparedStatement pstmt10;
	PreparedStatement pstmt11;
	PreparedStatement pstmt12;
	PreparedStatement pstmt13;
	PreparedStatement pstmt14;
	PreparedStatement pstmt15;
	PreparedStatement pstmt16;
	PreparedStatement pstmt17;
	PreparedStatement pstmt18;
	
	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}catch(ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
			System.out.println("OracleDriver 에러");
		}
		
	}
	
	public void connectRental05DB()
	{
		try {
			
			con = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:xe",
					"scott",
					"tiger");
			
			String sql2 = "select * from BookDB where btitle= ?";
			pstmt2 = con.prepareStatement(sql2);
			
			String sql9 = "select * from rental";
			pstmt9 = con.prepareStatement(sql9);
			
			String sql10 = "update bookdb set bcount = bcount - ? where btitle = ?";
			pstmt10 = con.prepareStatement(sql10);
			
			String slq11 = "select black from membership where id = ?";
			pstmt11 = con.prepareStatement(slq11);
			
			String sql12 = "insert into rental values(RENTAL_SEQ.nextval, ?, ?, sysdate, sysdate+7, ?)";
			pstmt12 = con.prepareStatement(sql12);
			
			String sql13 = "select * from rental where rentid = ?";
			pstmt13 = con.prepareStatement(sql13);
			
			String sql14 = "update bookdb set bcount = bcount + ? where btitle = ?";
			pstmt14 = con.prepareStatement(sql14);
			
			String sql15 = "delete from rental where rnum= ?";
			pstmt15 = con.prepareStatement(sql15);
			
			String sql16 = "select bookname from rental where rentid = ? and rnum = ?";
			pstmt16 = con.prepareStatement(sql16);
			
			String sql17 = "update rental set rcount = rcount - ? where rnum = ?";
			pstmt17 = con.prepareStatement(sql17);
			
			String sql18 = "select rcount from rental where rnum = ?";
			pstmt18 = con.prepareStatement(sql18);
		} 
		catch(Exception e)	{
			e.printStackTrace();
			System.out.println("connectRental04DB() 에러");
		}
	}
	
	public void showMenu()
	{
		System.out.println("===========================");
		System.out.println("    1. 대 여  현 황");
		System.out.println("    2. 대        여");
		System.out.println("    3. 반        납");
		System.out.println("    4. 종        료");
		System.out.println("===========================");
		System.out.print("    선택 : ");
	}
	
	public void doRentalRun()
	{
		connectRental05DB();
		int choice;
		
		while(true) 
		{
			showMenu();
			choice = sc.nextInt();
			sc.nextLine();
			
			switch(choice) {
			case 1:
				listrental();
				break;
			case 2:
				brent();
				break;
			case 3:
				breturn();
				break;
			case 4:
				System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~");
				System.out.println("   ~~~~대여 창을~~~~");
				System.out.println("  ~~~~종료합니다.~~~~");
				System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~");
				return;
			default:
				System.out.println(">>>>잘못입력하셨습니다. 다시입력하세요.<<<<");
				break;
			}
		}
	}
	
	public void listrental()
	{
		System.out.println("대여 현황");
		try {
			int count = 0;
			ResultSet rs = pstmt9.executeQuery();
			while(rs.next()) 
			{				
				System.out.println("--------------------------------------------");
				System.out.println("     대여 번호 : "+rs.getString(1));
				System.out.println("     대여한 책 제목 : "+rs.getString(2));
				System.out.println("     대여자 아이디 : "+rs.getString(3));
				System.out.println("     대여일자 : "+rs.getString(4));
				System.out.println("     반납일자 : "+rs.getString(5));
				System.out.println("     대여 권 수 : "+rs.getString(6));
				System.out.println("--------------------------------------------");
				count++;
			}
			if(count == 0) {
				System.out.println(">>>>대여 현황이 없습니다.<<<<");
			}
			rs.close();
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("listrental() 에러");
		}
		
	}
	
	public void brent()
	{
		System.out.println("대여할 책 이름 : ");
		String bookname = sc.nextLine();
		try {
			pstmt2.setString(1, bookname);
			ResultSet rs = pstmt2.executeQuery();
			if(rs.next()) 
			{
				if(rs.getInt(3) != 0)
				{
					System.out.println("-------------------------");
					System.out.println("         대여가능");
					System.out.println("-------------------------");
					
					System.out.println("책을 대여할 회원 id : ");
					String rentid = sc.nextLine();
					pstmt11.setString(1, rentid);
					ResultSet rid = pstmt11.executeQuery();
					
					if(rid.next())
					{
						if(rid.getString(1) == null)
						{
							System.out.println(bookname+"를 몇 권 대여합니까?");
							int bookcount = sc.nextInt();
							sc.nextLine();
							
							
							pstmt10.setInt(1, bookcount);
							pstmt10.setString(2, bookname);
							ResultSet rbook = pstmt10.executeQuery();
							
							pstmt12.setString(1, bookname);
							pstmt12.setString(2, rentid);
							pstmt12.setInt(3, bookcount);
							ResultSet rental = pstmt12.executeQuery();
							System.out.println("대여 완료");
							
							pstmt13.setString(1, rentid);
							ResultSet rentdate = pstmt13.executeQuery();
							while(rentdate.next()) {
								System.out.println("--------------------------------------------");
								System.out.println("     대여한 책 제목 : "+rentdate.getString(2));
								System.out.println("     반납 날짜 : "+rentdate.getString(5));
								System.out.println("     대여 권수 : "+rentdate.getInt(6));
								System.out.println("--------------------------------------------");
							}
							System.out.println("         반납 날짜를 지켜주시기 바랍니다.");
							System.out.println("*연체될 경우 추후 도서 대여에 문제가 생길 수 있습니다.*");
							
							rentdate.close();
							rbook.close();
							rental.close();
						}else {
							System.out.println("    >>>>대여불가/연체 중<<<<");
							System.out.println(">>>>관리자 문의 후 이용하세요.<<<<");
						}
					} else {
						System.out.println("       >>>>존재하지 않는 id입니다.<<<<");
						System.out.println(">>>>관리자를 통해 회원 등록 후 이용하세요.<<<<");
					}
					rid.close();
					
				} else {
					System.out.println(">>>>"+bookname+"이 0권으로 대여가 불가능 합니다.<<<<");
					System.out.println("         >>>>관리자 문의 후 이용하세요.<<<<");
				}
			} else {
				System.out.println(">>>>존재하지 않는 책입니다.<<<<");
				System.out.println(">>>>관리자 문의 후 이용하세요.<<<<");
			}
			rs.close();
			
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("brent 에러");
		}
	}
	
	public void breturn()
	{
		System.out.println("회원 id : ");
		String rid = sc.nextLine();
		
		try {
			pstmt13.setString(1, rid);
			ResultSet rs = pstmt13.executeQuery();
			int count = 0;
			while(rs.next()) {
				System.out.println("--------------------------------------------");
				System.out.println("     대여 번호 : "+rs.getInt(1));
				System.out.println("     대여한 책 제목 : "+ rs.getString(2));
				System.out.println("     대여 날짜 : "+rs.getString(4));
				System.out.println("     반납 날짜 : "+rs.getString(5));
				System.out.println("     대여 권수 : "+rs.getInt(6));
				System.out.println("--------------------------------------------");
				count++;
			}
			if(count != 0) {
				System.out.println("반납할 도서의 대여번호 : ");
				int rnum = sc.nextInt();
				sc.nextLine();
				
				pstmt16.setString(1, rid); 
				pstmt16.setInt(2, rnum);		//대여번호로 '책제목' 출력
				ResultSet btitle = pstmt16.executeQuery();
				if(btitle.next()) 
				{
					System.out.println("반납할 도서 권 수 : ");
					int rcount = sc.nextInt();
					sc.nextLine();
					
					pstmt18.setInt(1, rnum);
					ResultSet comparenum = pstmt18.executeQuery();
					
					if(comparenum.next()) {
						
						String bname = btitle.getString(1);
						pstmt14.setInt(1, rcount);
						pstmt14.setString(2, bname);
						ResultSet breturn = pstmt14.executeQuery();
						int nCnt = comparenum.getInt(1);
						if(nCnt == rcount) {
							System.out.println("전 권이 반납되었습니다.");
							
							pstmt15.setInt(1, rnum);
							int updatecount1 = pstmt15.executeUpdate();
						}else if(nCnt > rcount) {
							System.out.println(rcount+"권 만 반납되었습니다.");
							
							pstmt17.setInt(1, rcount);
							pstmt17.setInt(2, rnum);
							int updatecount2 = pstmt17.executeUpdate();
						}
						breturn.close();
					}
				comparenum.close();
				}else {
					System.out.println(">>>>올바르지 않는 대여번호 입니다.<<<<");
					System.out.println("   >>>>다시 반납 신청을 하세요.<<<<");
				}
				btitle.close();
			}else {
				System.out.println(" >>>>대여하지 않은 id 입니다.<<<<");
				System.out.println(">>>>관리자 문의 후 이용하세요.<<<<");
			}
			rs.close();
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("breturn 에러");
		}
	}
	
	public void bSomeReturn()
	{
		//일부 반납했을 때
		System.out.println("회원 id : ");
		String rsomeid = sc.nextLine();
		
		try {
			pstmt13.setString(1, rsomeid);
			ResultSet rs = pstmt13.executeQuery();
			int count = 0;
			while(rs.next()) {
				System.out.println("--------------------------------------------");
				System.out.println("     대여 번호 : "+rs.getInt(1));
				System.out.println("     대여한 책 제목 : "+ rs.getString(2));
				System.out.println("     대여 날짜 : "+rs.getString(4));
				System.out.println("     반납 날짜 : "+rs.getString(5));
				System.out.println("     대여 권수 : "+rs.getInt(6));
				System.out.println("--------------------------------------------");
				count++;
			}
			if(count != 0) {
				System.out.println("반납할 도서의 대여번호 : ");
				int rsomenum = sc.nextInt();
				sc.nextLine();
				
				pstmt16.setInt(1, rsomenum);
				ResultSet somebtitle = pstmt16.executeQuery();
				if(somebtitle.next()) {
					System.out.println("반납할 도서 권 수 : ");
					int rsomecount = sc.nextInt();
					sc.nextLine();
					
					String somebname = somebtitle.getString(1);
					pstmt14.setInt(1, rsomecount);
					pstmt14.setString(2, somebname);
					ResultSet somecount = pstmt14.executeQuery(); 		//bookdb에 대여 권수 증가
					
					
					pstmt17.setInt(1, rsomecount);
					pstmt17.setString(2, somebname);
					int updatecount = pstmt17.executeUpdate();			//rental에 대여 권수 감소
					System.out.println("일부 반납이 완료되었습니다.");
					
					somecount.close();
					
				}
				somebtitle.close();
				
			}
			rs.close();
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("bSomeReturn() 에러");
		}
	}
	
	public void rentalclose()
	{
		try {
			pstmt2.close();
			pstmt9.close();
			pstmt10.close();
			pstmt11.close();
			pstmt12.close();
			pstmt13.close();
			pstmt14.close();
			pstmt15.close();
			pstmt16.close();
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("rentalclose() 에러");
		}
	}
	
	
}
