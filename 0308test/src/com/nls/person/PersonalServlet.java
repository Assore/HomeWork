package com.nls.person;

import java.io.IOException;
import java.sql.*;
import java.sql.DriverManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class PersonalServlet
 */
@WebServlet("/PersonalServlet")
public class PersonalServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
   

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html;charset=UTF-8");
		String user_front=(String) request.getAttribute("uname");
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.print("驱动加载成功\n");
			String url="jdbc:mysql://localhost:3306/hsd";
			Connection conn=DriverManager.getConnection(url,"root","123456");
			System.out.print("链接建立成功\n");
			String sql1="SELECT * FROM student where username=?";
			PreparedStatement st=conn.prepareStatement(sql1);
			st.setString(1, user_front);
			ResultSet rs=st.executeQuery();
			if(rs!=null) System.out.println("查找成功");
			UserInfo userinfo=new UserInfo();
			while(rs.next()){
				userinfo.setName(rs.getString("name"));
				userinfo.setGrade(rs.getString("grade"));
				userinfo.setAcad(rs.getString("academy"));
				userinfo.setMajor(rs.getString("major"));
				
			}
			
			JSONObject jsonObject=new JSONObject();
			JSONArray jsonArray=new JSONArray();
			jsonObject.put("name", userinfo.getName());
			jsonObject.put("grade", userinfo.getGrade());
			jsonObject.put("acad", userinfo.getAcad());
			jsonObject.put("major", userinfo.getMajor());
			jsonObject.put("class", userinfo.getClass());
			jsonArray.add(jsonObject);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		//request.getRequestDispatcher("person.jsp").forward(request, response);


	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
