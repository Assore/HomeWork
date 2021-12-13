package cdu.nls.login;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cdu.nls.sql.DBConn;


/**
 * Servlet implementation class MessageDeleteSerlvet
 */
@WebServlet("/MessageDeleteSerlvet")
public class MessageDeleteSerlvet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MessageDeleteSerlvet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stubrequest.setCharacterEncoding("utf-8"); //1
		response.setContentType("text/html;charset=utf-8");  //2
		response.setCharacterEncoding("utf-8");  //3
		
		PrintWriter out=response.getWriter();
		
		Connection conn=DBConn.getConnection();
		
		String username=request.getParameter("username");
		String id=request.getParameter("id");
		int idI=Integer.parseInt(id);
		String Respon="连接失败";
		
		try{
			
			String sql1="delete  from forum where username=? and id=?";
			PreparedStatement ps=conn.prepareStatement(sql1);
			ps.setString(1, username);
			ps.setInt(2, idI);
			
			ps.execute();
			Respon="删除成功"+username;
			
					
				
			
			
			
			
		}catch(Exception e){
			e.printStackTrace();
			Respon="您不是发布这";
		}
		
		out.write(Respon);
		out.flush();
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
