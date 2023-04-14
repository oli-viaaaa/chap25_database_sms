package com.javalab.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SmsMain {

	// [멤버변수]
		// 1. oracle 드라이버 이름 문자열 상수
		public static final String DRIVER_NAME = "oracle.jdbc.driver.OracleDriver";

		// 2. oracle 데이터베이스 접속 경로(url) 문자열 상수
		public static final String DB_URL = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";

		// 3. 데이터베이스 접속 객체
		public static Connection con = null;

		// 4. query 실행 객체
		public static PreparedStatement pstmt = null;

		// 5. select 결과 저장 객체
		public static ResultSet rs = null;

		// 6. oracle 계정(id/pwd)
		public static String oracleId = "sms";

		// 7. oracle password
		public static String oraclePwd = "1234";
		
		public static void main(String[] args) {
			
			//1. 디비 접속 메소드 호출
			connectDB();
			
			// 2. 학과 조회
			getDeptList();
			
			// 3. 학생 조회
			getStudentList();
			
			// 4. 교수 조회
			getProfessorList();
			
			// 5. 강좌 조회
			getCourseList();
			
			// 6. 수강 조회
			getEnrollmentList();
			
			// 자원반납 메소드
			closeResource();
			
			
		} // main end

		//1. 디비 접속 메소드 호출
		private static void connectDB() {
			try {
				Class.forName(DRIVER_NAME);
				System.out.println("드라이버 로드 성공");
				
				con = DriverManager.getConnection(DB_URL, oracleId, oraclePwd);
				System.out.println("커넥션 객체 생성 성공");
				
			} catch (ClassNotFoundException e) {
				System.out.println("드라이버 ERR! : " + e.getMessage());
				System.out.println("나는 1번");
			} catch (SQLException e) {
				System.out.println("SQL ERR! : " + e.getMessage());
				System.out.println("나는 1번");
			}
			System.out.println();
		} //end 1. 디비 접속 메소드 호출
		
		// 2. 학과 조회
		private static void getDeptList() {
			try {
				String sql = "select dept_id, dept_name from tbl_dept";
			
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			System.out.println("  2. 학과 목록            ");
			System.out.println("===============");
			System.out.println("학과번호       학과 명");
			
			while (rs.next()) {
				System.out.println(rs.getInt("dept_id")+"\t"
								  + rs.getString("dept_name"));
			}
				
			} catch (SQLException e) {
				System.out.println("SQL ERR! : " + e.getMessage());
				System.out.println("나는 2번");
			} finally {
				// 자원반납 메소드
				closeResource();
			}
			System.out.println();
		}// end 2. 게시물 목록 조회
		
		// 3. 학생조회
		private static void getStudentList() {
			try {
				String sql = "select c.stu_id, c.stu_name, c.resident_id, c.gender, c.address, c.grade, d.dept_id, d.dept_name ";
				sql += " from tbl_student c, tbl_dept d ";
				sql += " where c.dept_id = d.dept_id ";
				sql += " order by d.dept_id asc, grade desc";
				
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			System.out.println("                                                    3. 학생조회                                                  ");
			System.out.println("===========================================================================");
			System.out.println("학번            이름           주민번호                  성별            주소         학년          학과번호         학과");

			while (rs.next()) {
				System.out.println(rs.getInt("stu_id")+"\t"
								  + rs.getString("stu_name")+"\t"
								  + rs.getString("resident_id")+"\t"
								  + rs.getString("gender")+"\t"
								  + rs.getString("address")+"\t"
								  + rs.getInt("grade")+"\t"
								  + rs.getInt("dept_id")+"\t"
								  + rs.getString("dept_name"));
			}
			
			} catch (SQLException e) {
				System.out.println("SQL ERR! : " + e.getMessage());
				System.out.println("나는 3번");
			} finally {
				// 자원반납 메소드
				closeResource();
			}
			System.out.println();
		} // end 3.
		
		// 4. 교수 조회
		private static void getProfessorList() {
			try {
				String sql = "select p.professor_id, p.PROFESSOR_NAME, p.resident_id, d.dept_id, d.dept_name, p.hiredate ";
				sql += " from tbl_professor p, tbl_dept d ";
				sql += " where p.dept_id = d.dept_id ";
				sql += " order by d.dept_id asc, d.dept_name desc, p.PROFESSOR_NAME asc ";
				
				pstmt = con.prepareStatement(sql);
				rs = pstmt.executeQuery();
				
				System.out.println("                                                    4. 교수조회                                                  ");
				System.out.println("===========================================================================");
				System.out.println("교번           교수명           주민번호               학과번호      학과명                 입사일자");
				
				while (rs.next()) {
					System.out.println(rs.getInt("professor_id")+"\t"
									 + rs.getString("PROFESSOR_NAME")+"\t"
									 + rs.getString("resident_id")+"\t"
									 + rs.getInt("dept_id")+"\t"
									 + rs.getString("dept_name")+"\t"
									 + rs.getDate("hiredate"));
				}

			} catch (SQLException e) {
				System.out.println("SQL ERR! : " + e.getMessage());
				System.out.println("나는 4번");
			} finally {
				// 자원반납 메소드
				closeResource();
			}
			System.out.println();
		} // end 4. 교수조회 
		
		// 5. 강좌 조회
		private static void getCourseList() {
			try {
				String sql = "select c.course_id, c.course_name, c.credit, p.professor_id, p.professor_name, c.c_date,r.room_id, r.room_name ";
				sql += " from tbl_course c ";
				sql += " inner join tbl_professor p ";
				sql += " on c.professor_id = p.professor_id ";
				sql += " inner join tbl_classroom r ";
				sql += " on c.room_id = r.room_id ";
				sql += " order by c.professor_id asc, c.credit desc";
				
				pstmt = con.prepareStatement(sql);
				rs = pstmt.executeQuery();
				
				System.out.println("                           6. 강좌 조회");
				System.out.println("=========================================================================");
				System.out.println("강좌번호    강좌이름   취득학점           교번         교수명       강의일자           강의실 번호       강의실명 ");
				
				while (rs.next()) {
					System.out.println(rs.getInt("course_id")+"\t"
									 + rs.getString("course_name")+"\t"
									 + rs.getInt("credit")+"\t"
									 + rs.getInt("professor_id")+"\t"
									 + rs.getString("professor_name")+"\t"
									 + rs.getDate("c_date")+"\t"
									 + rs.getInt("room_id")+"\t"
									 + rs.getString("room_name"));
				}
			} catch (SQLException e) {
				System.out.println("SQL ERR! : " + e.getMessage());
				System.out.println("나는 5번");
			} finally {
				// 자원반납 메소드
				closeResource();
			}
			System.out.println();
		} // end 5. 강좌 조회
		
		// 6. 수강 조회
		private static void getEnrollmentList() {
			try {
				String sql = "select e.enrollment_id, s.stu_id, s.stu_name,  ";
				sql += " c.course_id, c.course_name, e.professor_id, p.professor_name,  ";
				sql += " e.score, e.enrollment_date,r.room_name ";
				sql += " from tbl_enrollment e ";
				sql += " inner join tbl_student s ";
				sql += " on e.stu_id = s.stu_id ";
				sql += " inner join tbl_course c ";
				sql += " on c.course_id = e.course_id ";
				sql += " inner join tbl_classroom r ";
				sql += " on c.room_id = r.room_id ";
				sql += " inner join tbl_professor p ";
				sql += " on p.professor_id = e.professor_id";

				pstmt = con.prepareStatement(sql);
				rs = pstmt.executeQuery();
				
				System.out.println("                                6. 수강 조회");
				System.out.println("=======================================================================");
				System.out.println("수강번호    학번    학생명    강좌번호    강좌명    교번    교수명    점수    신청일자    강의실");
				
				while (rs.next()) {
					System.out.println(rs.getInt("enrollment_id")+"\t"
									+ rs.getInt("stu_id")+"\t"
									+ rs.getString("stu_name")+"\t"
									+ rs.getInt("course_id")+"\t"
									+ rs.getString("course_name")+"\t"
									+ rs.getInt("professor_id")+"\t"
									+ rs.getString("professor_name")+"\t"
									+ rs.getString("score")+"\t"
									+ rs.getDate("enrollment_date")+"\t"
									+ rs.getString("room_name"));
				}

			} catch (SQLException e) {
				System.out.println("SQL ERR! : " + e.getMessage());
				System.out.println("나는 6번");
			} finally {
				// 자원반납 메소드
				closeResource();
			}
			System.out.println();
		} // end 6. 수강조회
		
		// 자원반납
		private static void closeResource() {
			try {
				if (rs != null) {
					rs.close();
				} if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException e) {
				System.out.println("SQL ERR! : " + e.getMessage());
			}
		} // end 자원반납
		
}
