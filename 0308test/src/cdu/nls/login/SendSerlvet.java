package cdu.nls.login;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cdu.nls.sql.DBConn;

/**
 * Servlet implementation class SendSerlvet
 */
@WebServlet("/SendSerlvet")
public class SendSerlvet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SendSerlvet() {
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
		Connection conn=DBConn.getConnection();	
		
		try{
			System.out.println("Ω¯»ÎSendSerlvet");
			String username=request.getParameter("username");
			String name=request.getParameter("name");
			String head=request.getParameter("head");
			String title=request.getParameter("title");
			String message=request.getParameter("message");
			String photo=request.getParameter("photo");	
			System.out.println(photo);
			String sql="insert into forum(username,name,head,title,message,photo) values(?,?,?,?,?,?)";
			PreparedStatement ps=conn.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, name);
			ps.setString(3, head);
			ps.setString(4,title);
			ps.setString(5,message);
			ps.setString(6, photo);
			
			ps.execute();
			out.print("insert successful");
		}catch(Exception e){
			e.printStackTrace();
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
