package cdu.nls.login;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cdu.nls.sql.DBConn;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		
		String user = request.getParameter("username"); 
		String pass= request.getParameter("password");
		String name = request.getParameter("name"); 
		String grade= request.getParameter("grade");
		String numb = request.getParameter("number"); 
		String acad= request.getParameter("academy");
		String major = request.getParameter("major"); 
		String clas= request.getParameter("class");
		String tele= request.getParameter("telephone");
		
		//boolean type= false;
		PrintWriter out = response.getWriter(); 
		  
		System.out.print("获取jsp界面的：");
		System.out.print("  "+user);
		System.out.print("  ");
		System.out.print("length:"+user.length());
		System.out.print("  "+pass);
		System.out.print("  ");
		System.out.print("length:"+pass.length());
		  
		
		try{
			Class.forName("com.mysql.jdbc.Driver");
			String url="jdbc:mysql://localhost:3306/hsd?useUnicode=true&characterEncoding=UTF-8";
			Connection conn=DriverManager.getConnection(url,"root","123456");
			
			/*String sql1="select username from student where username=?";
			PreparedStatement st1=conn.prepareStatement(sql1);
			st1.setString(1, user);
			ResultSet res=st1.executeQuery(sql1);
			if(res!=null) {out.print("账号已存在");return;}*/
			
			String sql2="insert into student values (?,?,?,?,?,?,?,?,?)";
			String sql3="insert into head(username) values(?)";
			PreparedStatement st2=conn.prepareStatement(sql2);
			PreparedStatement st3=conn.prepareStatement(sql3);
			st2.setString(1, user);
			st2.setString(2, pass);
			st2.setString(3, name);
			st2.setString(4, grade);
			st2.setString(5, numb);
			st2.setString(6, acad);
			st2.setString(7, major);
			st2.setString(8, clas);
			st2.setString(9, tele);
			
			st3.setString(1, user);
			st2.execute();
			st3.execute();
			out.print("账号注册成功");
			
			
			
			
				}catch(Exception e){e.printStackTrace();out.print("注册失败，账号名已被使用");}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
