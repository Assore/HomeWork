package com.nls.person;

public class UserInfo {
	String name;
	String grade;
	String acad;
	String major;
	UserInfo(String name,String grade,String acad,String major){
		this.name=name;
		this.grade=grade;
		this.acad=acad;
		this.major=major;
	}
	public UserInfo() {
		// TODO Auto-generated constructor stub
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getAcad() {
		return acad;
	}
	public void setAcad(String acad) {
		this.acad = acad;
	}
	public String getMajor() {
		return major;
	}
	public void setMajor(String major) {
		this.major = major;
	}

}
