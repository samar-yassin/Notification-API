package com.module.notification.notificationData;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class NotificationDataSQL implements NotificationDataInterface {

    public void addNotification(Notification notification) throws IOException{
		try{  
			Class.forName("com.mysql.cj.jdbc.Driver");  
			Connection con=DriverManager.getConnection( "jdbc:mysql://db4free.net:3306/notificationapi","marwaomar","123456789");
			Statement stmt = con.createStatement();
			notification.createMessage();
			SendNotification sender;
			if (notification.getChannel().equalsIgnoreCase("email")) {
				sender = new SendEmail();
			} else {
				sender = new SendSMS();
			}
			boolean status = sender.send(notification);
			String sql = "";
			if (status == true) {
				sql = "INSERT INTO notifications (subject, channel, receiver, message, status) " +"VALUES ('"+ notification.getSubject() +"', '"+ notification.getChannel() +"', '"+ notification.getReceiver() +"', '"+ notification.getMessage() +"', 'sent')";
			} else {
				sql = "INSERT INTO notifications (subject, channel, receiver, message, status) " +"VALUES ('"+ notification.getSubject() +"', '"+ notification.getChannel() +"', '"+ notification.getReceiver() +"', '"+ notification.getMessage() +"', 'not sent')";
			}
			stmt.executeUpdate(sql);
			con.close();  
		} catch(Exception e){ 
			System.out.println(e);
		}   
    }
    public ArrayList<Notification> GetMailNotifications() throws IOException{
    	ArrayList<Notification> mailNotifications = new ArrayList<Notification>();  
		try{
			  Class.forName("com.mysql.cj.jdbc.Driver"); 
			  Connection con=DriverManager.getConnection("jdbc:mysql://db4free.net:3306/notificationapi","marwaomar","123456789");

		      Statement stmt = con.createStatement();
		      String sql = "SELECT * FROM notifications WHERE channel = 'email'";
		      ResultSet rs = stmt.executeQuery(sql);
		      while(rs.next()){
		    	 Notification notification = new Notification();
		         notification.setSubject(rs.getString("subject"));
		         notification.setChannel(rs.getString("channel"));
		         notification.setReceiver(rs.getString("receiver"));
		         notification.setMessage(rs.getString("message"));
		         notification.setStatus(rs.getString("status"));
		         mailNotifications.add(notification);
		      }
		      rs.close();
		      con.close();
	   } catch(SQLException se){
	      //Handle errors for JDBC
	      se.printStackTrace();
	   }catch(Exception e){
	      //Handle errors for Class.forName
	      e.printStackTrace();
	   }
    	return mailNotifications;
    }
    public ArrayList<Notification> GetSMSNotifications() throws IOException{
    	ArrayList<Notification> smsNotifications = new ArrayList<Notification>();
		try{
			  Class.forName("com.mysql.cj.jdbc.Driver"); 
			  Connection con=DriverManager.getConnection("jdbc:mysql://db4free.net:3306/notificationapi","marwaomar","123456789");

		      Statement stmt = con.createStatement();
		      String sql = "SELECT * FROM notifications WHERE channel = 'sms'";
		      ResultSet rs = stmt.executeQuery(sql);
		      while(rs.next()){
		    	 Notification notification = new Notification();
		         notification.setSubject(rs.getString("subject"));
		         notification.setChannel(rs.getString("channel"));
		         notification.setReceiver(rs.getString("receiver"));
		         notification.setMessage(rs.getString("message"));
		         notification.setStatus(rs.getString("status"));
		         smsNotifications.add(notification);
		      }
		      rs.close();
		      con.close();
	   } catch(SQLException se){
	      //Handle errors for JDBC
	      se.printStackTrace();
	   }catch(Exception e){
	      //Handle errors for Class.forName
	      e.printStackTrace();
	   }
    	return smsNotifications;
    }
}
