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
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class SqlSerlvet
 */
@WebServlet("/SqlSerlvet")
public class SqlSerlvet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SqlSerlvet() {
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
		String code=request.getParameter("code");
		Connection conn=DBConn.getConnection();
		if(code.equals("all")){
			try{
				
				String sql="select *from forum order by id  desc";
				PreparedStatement st=conn.prepareStatement(sql);
				ResultSet rs=st.executeQuery();
				JSONArray jsonArray=new JSONArray();
				
				while(rs.next()){
					int id=rs.getInt("id");
					String name=rs.getString("name");
					String head=rs.getString("head");
					String title=rs.getString("title");
					String message=rs.getString("message");
					String photo=rs.getString("photo");
					
					JSONObject jsonObject=new JSONObject();
					jsonObject.put("id", id);
					jsonObject.put("name", name);
					jsonObject.put("head", head);
					jsonObject.put("title", title);
					jsonObject.put("message", message);
					jsonObject.put("photo", photo);
					
					jsonArray.add(jsonObject);
				}
				System.out.print(jsonArray);
				out.write(jsonArray.toString());
				out.flush();
				out.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		
		if(code.equals("black")){
			try{
				
				String sql="select *from black order by id  desc";
				PreparedStatement st=conn.prepareStatement(sql);
				ResultSet rs=st.executeQuery();
				JSONArray jsonArray=new JSONArray();
				
				while(rs.next()){
					int id=rs.getInt("id");
					String username=rs.getString("username");
					
					
					JSONObject jsonObject=new JSONObject();
					jsonObject.put("id", id);
					jsonObject.put("username", username);
					
					
					jsonArray.add(jsonObject);
				}
				System.out.print(jsonArray);
				out.write(jsonArray.toString());
				out.flush();
				out.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		if(code.equals("blackinsert")){
			try{
				
				String sql="insert into black(username) values(?)";
				PreparedStatement st=conn.prepareStatement(sql);
				String username=request.getParameter("username");
				st.setString(1, username);
				st.execute();
				
							
				out.flush();
				out.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		
		
		if(code.equals("blackdelete")){
			try{
				
				String sql="delete from black where username=?";
				PreparedStatement st=conn.prepareStatement(sql);
				String username=request.getParameter("username");
				st.setString(1, username);
				st.execute();
				
				System.out.print(st.toString());			
				out.flush();
				out.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		if(code.equals("insert")){
			String name=request.getParameter("name");
			String head=request.getParameter("head");
			String title=request.getParameter("title");
			String message=request.getParameter("message");
			String photo=request.getParameter("photo");
			
			try{
				String sql="insert into forum values(?,?,?,?,?)";
				PreparedStatement ps=conn.prepareStatement(sql);
				ps.setString(1, name);
				ps.setString(2, head);
				ps.setString(3,title);
				ps.setString(4,message);
				ps.setString(5, photo);
				
				ps.execute();
				out.print("successful");
			}catch(Exception e){
				e.printStackTrace();
			}
			
			
			
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
