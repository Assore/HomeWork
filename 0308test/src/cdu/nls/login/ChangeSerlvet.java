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
 * Servlet implementation class ChangeSerlvet
 */
@WebServlet("/ChangeSerlvet")
public class ChangeSerlvet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChangeSerlvet() {
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
		
		String code=request.getParameter("code");
		
		if(code.equals("set")){
			System.out.println("change head");
			try{
				
				String username=request.getParameter("username");
				String headPicture=request.getParameter("head");
				String sql="update head set head.head=?where head.username=? ";
				String sql2="update forum set forum.head=? where forum.username=?";
				PreparedStatement st=conn.prepareStatement(sql);
				PreparedStatement st2=conn.prepareStatement(sql2);
				st.setString(1, headPicture);
				st.setString(2, username);
				
				st2.setString(1,headPicture);
				st2.setString(2, username);
				st.execute();
				st2.execute();
				System.out.println(headPicture);
				
				
				
				
				out.print("上传成功");
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		
		if(code.equals("selectRead")){
			try{
				String id=request.getParameter("id");
				
				
				
				int idI=Integer.parseInt(id);
				String sql2="select readcode from transaction where id=?";
				PreparedStatement st2=conn.prepareStatement(sql2);
				
				
				st2.setInt(1, idI);
				
				int readCode=0;
				ResultSet selectResult=st2.executeQuery();
				while(selectResult.next()){
					readCode=selectResult.getInt("readcode");
				}
				if(readCode==0){
					out.write("待查看");
				}else if(readCode==3){
					out.write("可申诉");
				}else if(readCode==4){
					out.write("已拒绝");
				}
					
				
				
				out.flush();
				out.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		if(code.equals("updateRead")){
			try{
				String id=request.getParameter("id");
				String readcode=request.getParameter("readcode");
				
				int readcodeI=Integer.parseInt(readcode);
				int idI=Integer.parseInt(id);
				String sql2="Update transaction set readcode=? where id=?";
				PreparedStatement st2=conn.prepareStatement(sql2);
				
				st2.setInt(1, readcodeI);
				st2.setInt(2, idI);
				
				
				st2.executeUpdate();
				
				out.write(" 更新成功");
				out.flush();
				out.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		if(code.equals("insert")){
			try{
				String sendname=request.getParameter("sendname");
				String receivename=request.getParameter("receivename");		
				String message=request.getParameter("message");
				
				String sql2="insert into transaction(sendname,receivename,message) values (?,?,?)";
				String sqlB="select *from black where username=?";
				PreparedStatement stB=conn.prepareStatement(sqlB);
				stB.setString(1, receivename);
				ResultSet rsB=stB.executeQuery();
				if(rsB ==null){
					PreparedStatement st2=conn.prepareStatement(sql2);
					
					st2.setString(1, sendname);
					st2.setString(2, receivename);		
					st2.setString(3, message);
					
					
					st2.executeUpdate();
					
					out.write(" 插入成功");
					System.out.print(st2.toString());
				}
				else out.write("black");
				
				
				out.flush();
				out.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		if(code.equals("send")){
			try{
				String username=request.getParameter("sendname");
				String sql="select *from transaction where sendname=? order by id  desc";
				PreparedStatement st=conn.prepareStatement(sql);
				st.setString(1, username);
				ResultSet rs=st.executeQuery();
				JSONArray jsonArray=new JSONArray();
				
				while(rs.next()){
					int id=rs.getInt("id");
					String receivename=rs.getString("receivename");
					String message=rs.getString("message");
					int readcode=rs.getInt("readcode");
					JSONObject jsonObject=new JSONObject();
					jsonObject.put("id", id);
					jsonObject.put("receivename", receivename);
					jsonObject.put("message", message);		
					jsonObject.put("readcode", readcode);	
					jsonArray.add(jsonObject);
				}
				System.out.print(jsonArray.toString());
				out.write(jsonArray.toString());
				out.flush();
				out.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		if(code.equals("receive")){
			try{
				String username=request.getParameter("receivename");
				String sql="select *from transaction where receivename=? order by id  desc";
				PreparedStatement st=conn.prepareStatement(sql);
				st.setString(1, username);
				ResultSet rs=st.executeQuery();
				JSONArray jsonArray=new JSONArray();
				
				while(rs.next()){
					int id=rs.getInt("id");
					String sendname=rs.getString("sendname");
					String message=rs.getString("message");
					int readcode=rs.getInt("readcode");
					JSONObject jsonObject=new JSONObject();
					jsonObject.put("id", id);
					jsonObject.put("sendname", sendname);
					jsonObject.put("message", message);
					jsonObject.put("readcode", readcode);
					jsonArray.add(jsonObject);
				}
				out.write(jsonArray.toString());
				out.flush();
				out.close();
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
