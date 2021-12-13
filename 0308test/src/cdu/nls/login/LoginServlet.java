package cdu.nls.login;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cdu.nls.sql.DBConn;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    String pname,pgrade,pmajor,pacad;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
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
		System.out.print("ke");
		try{
			String user=request.getParameter("username");
			String password=request.getParameter("password");
			Connection conn=DBConn.getConnection();
			String sql1="select password from student where username=?";
			PreparedStatement ps1=conn.prepareStatement(sql1);
			ps1.setString(1, user);
			ResultSet rs1=ps1.executeQuery();
			while(rs1.next()){
				if(rs1.getString("password").equals(password)){
					String sql="select * from student where username=?";
					PreparedStatement ps=conn.prepareStatement(sql);
					ps.setString(1, user);
					ResultSet rs=ps.executeQuery();
					while(rs.next()){
						pname=rs.getString("name");
						pgrade=rs.getString("grade");
						pmajor=rs.getString("major");
						pacad=rs.getString("academy");
					}
					JSONObject jsonObject=new JSONObject();
					jsonObject.put("name", pname);
					jsonObject.put("grade", pgrade);
					jsonObject.put("major", pmajor);
					jsonObject.put("acad", pacad);
					jsonObject.put("user", user);
					//System.out.print(jsonObject);
					URLEncoder.encode(jsonObject.toString(),"utf-8");
					out.write(jsonObject.toString());
					out.flush();
					out.close();
					rs.close();
					ps.close();
					
				}else out.print("error");
			}
			rs1.close();
			ps1.close();
			conn.close();	
			

		}catch(Exception e){
			e.printStackTrace();
			out.print("µÇÂ½Ê§°Ü");
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
