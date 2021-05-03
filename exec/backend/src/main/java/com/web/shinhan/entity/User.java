package com.web.shinhan.entity;

import java.time.LocalDateTime;

import javax.persistence.*;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity(name = "user")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;

	private int employeeNum;
	private String email;
	private String userName;
	private String password;
	private String department;
	private String position;
	private String contact;
	private String days;
	private String sidoCode;
	private String gugunCode;
	private int balance;
	private int cardLimit;
	private int active;
	private LocalDateTime accessTime;
	private LocalDateTime limitTime;

	@Builder
	public User(int userId, int employeeNum, String email, String userName, String password, String department,
			String position, String contact, String days, String sidoCode, String gugunCode, int balance, int cardLimit,
			int active, LocalDateTime accessTime, LocalDateTime limitTime) {
		super();
		this.userId = userId;
		this.employeeNum = employeeNum;
		this.email = email;
		this.userName = userName;
		this.password = password;
		this.department = department;
		this.position = position;
		this.contact = contact;
		this.days = days;
		this.sidoCode = sidoCode;
		this.gugunCode = gugunCode;
		this.balance = balance;
		this.cardLimit = cardLimit;
		this.active = active;
		this.accessTime = accessTime;
		this.limitTime = limitTime;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", employeeNum=" + employeeNum + ", email=" + email + ", userName=" + userName
				+ ", password=" + password + ", department=" + department + ", position=" + position + ", contact="
				+ contact + ", days=" + days + ", balance=" + balance + ", cardLimit=" + cardLimit + ", active="
				+ active + ", accessTime=" + accessTime + "]";
	}

}