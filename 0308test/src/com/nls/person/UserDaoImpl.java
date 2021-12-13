package com.nls.person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import cdu.nls.sql.DBConn;

public class UserDaoImpl  {
	private static PreparedStatement ps;
	private static ResultSet rs;
	private static Connection con;
	
	
	public static List<UserInfo> getUser(){
		List<UserInfo> users=new ArrayList<UserInfo>();
		try{
			con=DBConn.getConnection();
			String sql="select * from student";
			ps=con.prepareStatement(sql);
			rs=ps.executeQuery();
			while(rs.next()){
				UserInfo userinfo=new UserInfo(rs.getString("name"),rs.getString("grade"),rs.getString("academy"),rs.getString("major"));
				users.add(userinfo);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(con!=null){
				try{
					con.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		return users;
	}
	
}
