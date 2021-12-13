package cdu.nls.login;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cdu.nls.sql.DBConn;

/**
 * Servlet implementation class HeadSerlvet
 */
@WebServlet("/HeadSerlvet")
public class HeadSerlvet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HeadSerlvet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8"); //1
		response.setContentType("text/html;charset=utf-8");  //2
		response.setCharacterEncoding("utf-8");  //3
		
		PrintWriter out=response.getWriter();
		System.out.print("shangchuanchenggong");
		try{
			
			String username=request.getParameter("username");
			String code=request.getParameter("code");
			Connection conn=DBConn.getConnection();
			if(code.equals("get")){
				String sql1="select head from head where username=?";
				PreparedStatement st1=conn.prepareStatement(sql1);
				st1.setString(1, username);								
				ResultSet rs=st1.executeQuery();
				String head="Ã·»° ß∞‹";
				while(rs.next()){
					head=rs.getString("head");
					//System.out.print(head);
					out.write(head);
				}
				//out.print(head);
			}
			
			
			 
		}catch(Exception e){
			e.printStackTrace();
			System.out.print(e);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
