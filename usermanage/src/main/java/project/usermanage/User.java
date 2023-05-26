package project.usermanage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

@Path("/user")
public class User {
	public static class Connect {
		public static Connection connect() throws Exception {
			Class.forName("com.mysql.cj.jdbc.Driver");// server to app
			String url = "jdbc:mysql://localhost:3306/", user = "root", password = "Saitej@19";// data carry
			Connection c = DriverManager.getConnection(url, user, password);
			return c;
		}
	}
	// ctrl+shift+f -- code alignment
	// ctrl+shift+W --- closing all windows
	// ctrl+shift+o -- import and remove of unwanted imports

	Password pas = new Password();

	@GET
	@Path("verify")
	public String verifyuser(@Context HttpServletRequest rq, @Context HttpServletResponse rs) throws Exception {
		Connection c = Connect.connect();
		PreparedStatement useDb = c.prepareStatement("use batch4");
		useDb.execute();
		HttpSession s = rq.getSession();
		String username = rq.getParameter("name");
		String data = "";
		PreparedStatement readData = c.prepareStatement("select * from userdata where username='" + username + "'");
		ResultSet res = readData.executeQuery();
		s.setAttribute("username", username);
		if(res.next()) 
			if (res.getString(1).equals(username)) 
			{
				if (res.getString(4).equals("admin")) 
				{
					data = "<h1>Admin is	"+username+"</h1><br>"
							+ "<a href='http://localhost:8080/usermanage/webresources/user/details'>My Details</a><br>"
							+"<a href='http://localhost:8080/usermanage/ChangePassword.jsp'>Change Password</a><br>"
							+ "<a href='http://localhost:8080/usermanage/service.jsp'>Create Services</a><br>"
							+ "<a href='http://localhost:8080/usermanage/DeleteServices.jsp'>Delete Services</a><br>"
							+"<a href='http://localhost:8080/usermanage/webresources/user/pendingrequests'>Pending Requests</a><br>"
							+"<a href='http://localhost:8080/usermanage/webresources/user/managerslist'>Managers List</a><br>"
							+"<a href='http://localhost:8080/usermanage/webresources/user/listofservices'>Services List</a><br>"
							+"<a href='http://localhost:8080/usermanage/webresources/user/assignaservice'>Assign a Service</a><br>"
							+"<a href='http://localhost:8080/usermanage/webresources/user/deleteaccount'>Delete My Account</a><br>"
							+"<a href='http://localhost:8080/usermanage/webresources/user/logout'>Logout</a><br>";
				} 
				else if(res.getString(4).equals("manager")){
					s.setAttribute("username", rq.getParameter("name"));
					data="<h1>Manager is	"+username+"</h1><br>"
							+"<a href='http://localhost:8080/usermanage/webresources/user/details'>My Details</a><br>"
					+"<a href='http://localhost:8080/usermanage/ChangePassword.jsp'>Change Password</a><br>"
							+"<a href='http://localhost:8080/usermanage/webresources/user/requestservices'>Service Request</a><br>"
							+"<a href='http://localhost:8080/usermanage/webresources/user/intoteam'>Create a New User in Team</a><br>"
							+"<a href='http://localhost:8080/usermanage/webresources/user/teamdetails'>Team Details</a><br>"
							+"<a href='http://localhost:8080/usermanage/webresources/user/deleteaccount'>Delete My Account</a><br>"
							+"<a href='http://localhost:8080/usermanage/webresources/user/logout'>Logout</a><br>";;
				}
				else 
				{
					s.setAttribute("username", rq.getParameter("name"));
					data = "<h1>user name is 	"+username+"</h1><br><a href='http://localhost:8080/usermanage/webresources/user/details'>Userdetails</a><br>"
							+ "<a href='http://localhost:8080/usermanage/webresources/user/listofservices'>list of Services</a><br>"
							+ "<a href='http://localhost:8080/usermanage/ChangePassword.jsp'>Change Password</a><br>"
							+ "<a href='http://localhost:8080/usermanage/webresources/user/requestservices'>Request Services</a><br>"
							+ "<a href='http://localhost:8080/usermanage/webresources/user/requestmanager'>Request Manager</a><br>"
							+ "<a href='http://localhost:8080/usermanage/webresources/user/requestadmin'>Request Admin</a><br>"
							+ "<a href='http://localhost:8080/usermanage/webresources/user/deleteaccount'>Delete my account</a><br>"
							+ "<a href='http://localhost:8080/usermanage/webresources/user/logout'>Logout</a><br>";

				}
			} 
			else {
				data = "user doesn't exit";
			
			}
		else {
			rs.sendRedirect("http://localhost:8080/usermanage/registration.jsp");
		}
	
		return data;
	}

	@GET
	@Path("register")
	public String register(@Context HttpServletRequest rq, @Context HttpServletResponse rs) throws Exception {
		Connection c = Connect.connect();
		PreparedStatement useDb = c.prepareStatement("use batch4");
		useDb.execute();
		PreparedStatement readData = c.prepareStatement("select * from userdata");
		ResultSet res = readData.executeQuery();
		if(rq.getParameter("passwd").equals(rq.getParameter("confirmpwd")))
		{
			if(pas.passwordConditions(rq.getParameter("passwd")))
			{
				PreparedStatement insertdata = c.prepareStatement("insert into userdata values(?,?,?,?)");
				insertdata.setString(1, rq.getParameter("username"));
				insertdata.setString(2, rq.getParameter("name"));
				insertdata.setString(3, pas.passwordEncryption(rq.getParameter("passwd")));
				if (res.next()) 
				{
					insertdata.setString(4, "user");
				}
				else
				insertdata.setString(4, "admin");
				insertdata.execute();
			}
			else
				return "Password Conditions mismatch";
		}
		
		else 
			return "Password  mismatch";
		
		return "Registration successfully";
	
	}

	@GET
	@Path("details")
	public String userdetails(@Context HttpServletRequest rq, @Context HttpServletResponse rs) throws Exception {
		String details = "";
		Connection c = Connect.connect();
		PreparedStatement useDb = c.prepareStatement("use batch4");
		useDb.execute();
		HttpSession s = rq.getSession();
		
		PreparedStatement readData = c.prepareStatement(
				"select * from userdata where username='" + s.getAttribute("username").toString() + "'");
		ResultSet res = readData.executeQuery();
		if (res.next())

		{

			details += "User Name is" + res.getString(1) + "<br>" + "Name is" + res.getString(2) + "<br>" + "User Type"
					+ res.getString(4);
		}

		else
			details += "user_name not matched.....";

		return details;
	}

	@GET
	@Path("createservices")
	public String createServices(@Context HttpServletRequest rq, @Context HttpServletResponse rs) throws Exception {
		Connection c = Connect.connect();
		PreparedStatement useDb = c.prepareStatement("use batch4");
		useDb.execute();
		PreparedStatement p = c.prepareStatement("insert into service_table values(?,?,?)");
		p.setString(1, rq.getParameter("services"));
		p.setString(2, rq.getParameter("accesslevel"));
		p.setString(3, rq.getParameter("no_of_users"));
		p.execute();
		return "Data Saved";

	}

	@GET
	@Path("listofservices")
	public String requestServices(@Context HttpServletRequest rq, @Context HttpServletResponse rs) throws Exception {
		Connection c = Connect.connect();
		PreparedStatement useDb = c.prepareStatement("use batch4");
		useDb.execute();
		PreparedStatement p = c.prepareStatement("select services from service_table ");
		ResultSet res = p.executeQuery();
		List<String> li = new ArrayList<String>();
		String html = "<html><title>Services</title><body><form action=''><h1>List of Services : </h1><select name='services' id='services'";
		while (res.next()) {
			li.add(res.getString(1));
		}
		for (int i = 0; i < li.size(); i++) {
			html += "<option value =" + li.get(i) + ">" + li.get(i) + "</option>";
		}
		html += "</select>";
		return html;
	}
	
	@GET
	@Path("/changePassword")
	public String changePassword(@Context HttpServletRequest rq, @Context HttpServletResponse rs) throws Exception {
		Connection c = Connect.connect();
		PreparedStatement useDb = c.prepareStatement("use batch4");
		useDb.execute();
		PreparedStatement p = c.prepareStatement("select * from userdata where username='"+rq.getParameter("username")+"' and password='"+pas.passwordEncryption(rq.getParameter("oldpwd"))+"'");
		ResultSet res = p.executeQuery();
		if(res.next())
		{
			if(rq.getParameter("newpwd").equals(rq.getParameter("confirmpwd")))
			{
				if(pas.passwordConditions(rq.getParameter("newpwd")))
				{
					p=c.prepareStatement("update userdata set password='"+pas.passwordEncryption(rq.getParameter("newpwd"))+"' where username='"+rq.getParameter("username")+"'");
					p.execute();
					return "Password updated successfully";
				}
				else
					return "Password conditions not matched";
			}
			else
				return "Password mismatch";
		}
		else
			return "Password was not updated";
	}
		@GET
		@Path("deleteservices")
		public String deleteServices(@Context HttpServletRequest rq, @Context HttpServletResponse rs) throws Exception {
			Connection c = Connect.connect();
			PreparedStatement useDb = c.prepareStatement("use batch4");
			useDb.execute();
			PreparedStatement p = c.prepareStatement("delete from service_table where services='"+rq.getParameter("services")+"'");
			p.execute();
			return "Services deleted successfully";
		}
		
		@GET
		@Path("requestservices")
		public String requestservice(@Context HttpServletRequest rq, @Context HttpServletResponse rs) throws Exception
		{
			Connection c = Connect.connect();
			PreparedStatement useDb = c.prepareStatement("use batch4");
			useDb.execute();
			HttpSession s = rq.getSession();
			String uname =s.getAttribute("username").toString();
			List<String> l1 = new ArrayList<String>();//to store services from "request" table for a particular user
			List<String> l2 = new ArrayList<String>();//to store services of manager and user from service table
			List<String> l3 = new ArrayList<String>();//to store services of user from service table
			PreparedStatement ps = c.prepareStatement("select services from request_services where requester='"+uname+"' and requeststatus= 'Pending'");
			ResultSet rr = ps.executeQuery();
			String html="<html><title>Service List</title><body><form action='http://localhost:8080/usermanage/webresources/user/insertrequest'><h2>Services Available :</h2><select name='services'>";
				while(rr.next()) {
				l1.add(rr.getString(1));
				}
		
			PreparedStatement p1 = c.prepareStatement("select * from service_table where accesslevel in ('manager','user')");
			ResultSet rsm = p1.executeQuery();
				while(rsm.next()) {
					l2.add(rsm.getString(1));
				}
			PreparedStatement p2 = c.prepareStatement("select * from service_table where accesslevel='user'");
			ResultSet rsu = p2.executeQuery();
				while(rsu.next()) {
				l3.add(rsu.getString(1));
				}
			PreparedStatement p3 = c.prepareStatement("select * from userdata where username='"+uname+"'");
			ResultSet rsuser = p3.executeQuery();
				while(rsuser.next()) {
				if(rsuser.getString(4).equals("manager")) 
				{
					l2.removeAll(l1);
					//l4.addAll(l2);
					for(int i=0;i<l2.size();i++) {
						html+="<option value="+l2.get(i)+">"+l2.get(i)+"</option>";
									}
				}
				else if(rsuser.getString(4).equals("user")) {
					l3.removeAll(l1);
					//l4.addAll(l3);
					for(int i=0;i<l3.size();i++) {
						html+="<option value="+l3.get(i)+">"+l3.get(i)+"</option>";
									}
				}}
		
			
		html+="<input type='submit' value='submit'>";
		
				return html;
		
}
		
		@GET
		 @Path("insertrequest")
		 public String reqInsert(@Context HttpServletRequest rq,@Context HttpServletResponse rsp) throws Exception {
			try {
				 Connection c = Connect.connect();
				 PreparedStatement usedb = c.prepareStatement("use batch4");
				 usedb.execute();
				 PreparedStatement ps = c.prepareStatement("insert into request_services values(?,?,?)");

				 HttpSession s = rq.getSession();
				String uname = s.getAttribute("username").toString();

				ps.setString(1,rq.getParameter("services") );
				ps.setString(2, uname);
				ps.setString(3, "pending");
				ps.execute();
				} catch (Exception e) {
				 rsp.sendRedirect("http://localhost:8080/usermanage/loginpage.jsp");
				}
				
			return "Request Submitted";
		}
		
		@GET
		@Path("insrtrqst/{name}")
			public String insertRequest(@PathParam("name") String name, @Context HttpServletRequest rq,@Context HttpServletResponse rsp) throws Exception {
				Connection c = Connect.connect();
				 PreparedStatement usedb = c.prepareStatement("use batch4");
				 usedb.execute();
				 PreparedStatement ps = c.prepareStatement("insert into request_services values(?,?,?)");

				ps.setString(1,rq.getParameter("services") );
				ps.setString(2, name);
				ps.setString(3, "Approved");
				ps.execute();
				return "Service Approved";
			}
	

		@GET
		@Path("requestmanager")
			public String requestManager(@Context HttpServletRequest rq,@Context HttpServletResponse rsp) throws Exception {
			Connection c = Connect.connect();
			PreparedStatement useDb = c.prepareStatement("use batch4");
			useDb.execute();
			HttpSession s = rq.getSession();
			String uname = s.getAttribute("username").toString();
			
			PreparedStatement ps = c.prepareStatement("insert into request_services values(?,?,?)");
			
			ps.setString(1,"manager");
			ps.setString(2, uname);
			ps.setString(3,"pending");
			ps.execute();
			return "User Request has sent successfuly";
		}
		@GET
		@Path("requestadmin")
		public String requestAdmin(@Context HttpServletRequest rq,@Context HttpServletResponse rsp) throws Exception {
		Connection c = Connect.connect();
		PreparedStatement useDb = c.prepareStatement("use batch4");
		useDb.execute();
		HttpSession s = rq.getSession();
		String uname = s.getAttribute("username").toString();
		
		PreparedStatement ps = c.prepareStatement("insert into request_services values(?,?,?)");
		
		ps.setString(1,"admin");
		ps.setString(2, uname);
		ps.setString(3,"pending");
		ps.execute();
		return "User Request has sent successfuly";
	}
		
		
		@GET
		@Path("pendingrequests")
		public String pendingRequests(@Context HttpServletRequest rq,@Context HttpServletResponse rsp) throws Exception {
			
				Connection c = Connect.connect();
				PreparedStatement useDb = c.prepareStatement("use batch4");
				useDb.execute();
				PreparedStatement ps = c.prepareStatement("select * from request_services where requeststatus='pending'");
				ResultSet res = ps.executeQuery();
				String requests="";
				while(res.next()) {
				requests+=res.getString(1)+"	"+res.getString(2)+"	"+res.getString(3)+"<br>"+"<html><body><form action='http://localhost:8080/usermanage/webresources/user/requestdecision'>"
						+ "<input type='hidden' name='htmlForm' value='"+res.getString(1)+"_"+res.getString(2)+"'>"
						+ "<input type='submit' name='des' value='Approve'>"+"<input type='submit' name='des' value='Deny'></form>"+"<br><br>"+"</body></html>";
				}
				return requests;
		}
		
			@GET
			@Path("requestdecision")
			public String requestDecision(@Context HttpServletRequest rq,@Context HttpServletResponse rsp) throws Exception {
			Connection c = Connect.connect();
			PreparedStatement useDb = c.prepareStatement("use batch4");
			useDb.execute();
			String des=rq.getParameter("des");
			String formname=rq.getParameter("htmlForm");
			String form[]=formname.split("_");
			String serv=form[0];
			String user=form[1];
			
			if(des.equals("Approve")) {
			useDb = c.prepareStatement("update request_services set requeststatus='Approve' where services='"+serv+"' and requester='"+user+"'");
			useDb.execute();
			if(serv.equals("manager")) {
				useDb = c.prepareStatement("update userdata set usertype='manager' where username='"+user+"'");
				useDb.execute();
				
			 }
			if(serv.equals("admin")) {
				useDb = c.prepareStatement("update userdata set usertype='admin' where username='"+user+"'");
				useDb.execute();
			PreparedStatement p = c.prepareStatement("select * from service_table");
			ResultSet rs = p.executeQuery();
			while(rs.next()) {
			 p = c.prepareStatement("select distinct count(*) from request_services where services='"+rs.getString(1)+"' and requeststatus='Approve'");
			ResultSet res = p.executeQuery();
			if(res.next()) 
			p = c.prepareStatement("update service_table set no_of_users="+res.getInt(1)+" where services='"+rs.getString(1)+"'");
			p.execute();
			
			}
			}
		
		
			else {
			useDb = c.prepareStatement("update request_services set requeststatus='"+des+"' where services='"+serv+"' and requester='"+user+"'");
			useDb.execute();
	     }
			}

		return "Decision Updated";
		}
			@GET
			@Path("managerslist")
			public String managerslist(@Context HttpServletRequest rq,@Context HttpServletResponse rsp) throws Exception {
				Connection c = Connect.connect();
				PreparedStatement useDb = c.prepareStatement("use batch4");
				useDb.execute();
				PreparedStatement ps = c.prepareStatement("select username from userdata where  usertype='manager'");
				ResultSet rs=ps.executeQuery();
				String data="";
				while(rs.next()) {
					 data=rs.getString(1);
					
				}
				return data;
			}
			
			@GET
			@Path("deleteaccount")
			public String deleteAccount(@Context HttpServletRequest rq,@Context HttpServletResponse rsp) throws Exception {
				Connection c = Connect.connect();
				PreparedStatement useDb = c.prepareStatement("use batch4");
				useDb.execute();
				String name="";
				HttpSession s = rq.getSession();
				name=s.getAttribute("username").toString();
				PreparedStatement ps = c.prepareStatement("delete from userdata where username='"+name+"'");
				ps.execute();
				
				return name+"	account deleted";
			}
			@GET
			@Path("assignaservice")
			public String assignaservice(@Context HttpServletRequest rq,@Context HttpServletResponse rsp) throws Exception {
				Connection c = Connect.connect();
				PreparedStatement useDb = c.prepareStatement("use batch4");
				useDb.execute();
				PreparedStatement ps = c.prepareStatement("select username from userdata where usertype!='admin'");
				ResultSet rs=ps.executeQuery();
				String data="";
				List<String> l = new ArrayList<String>();
				while(rs.next()) {
					 data+=rs.getString(1)+"<a href='http://localhost:8080/usermanage/webresources/user/reqservice/"+rs.getString(1)+"'>services available</a>";
					 
					
				}
				
				return data;
			}
			
			@GET
			@Path("reqservice/{username}")
			public String reqService(@PathParam("username") String uname ,@Context HttpServletRequest rq,@Context HttpServletResponse rsp) throws Exception {
				Connection c = Connect.connect();
				PreparedStatement useDb = c.prepareStatement("use batch4");
				useDb.execute();
				List<String> l1 = new ArrayList<String>();//to store services from "request" table for a particular user
				List<String> l2 = new ArrayList<String>();//to store services of manager and user from service table
				List<String> l3 = new ArrayList<String>();//to store services of user from service table
				PreparedStatement ps = c.prepareStatement("select services from request_services where requester='"+uname+"' and requeststatus= 'Pending'");
				ResultSet rr = ps.executeQuery();
				String html="<html><title>Service List</title><body><form action='http://localhost:8080/usermanage/webresources/user/insrtrqst/"+uname+"'><h2>Services Available :</h2><select name='services'>";
					while(rr.next()) {
					l1.add(rr.getString(1));
					}
			
				PreparedStatement p1 = c.prepareStatement("select * from service_table where accesslevel in ('manager','user')");
				ResultSet rsm = p1.executeQuery();
					while(rsm.next()) {
						l2.add(rsm.getString(1));
					}
				PreparedStatement p2 = c.prepareStatement("select * from service_table where accesslevel='user'");
				ResultSet rsu = p2.executeQuery();
					while(rsu.next()) {
					l3.add(rsu.getString(1));
					}
				PreparedStatement p3 = c.prepareStatement("select * from userdata where username='"+uname+"'");
				ResultSet rsuser = p3.executeQuery();
					while(rsuser.next()) {
					if(rsuser.getString(4).equals("manager")) 
					{
						l2.removeAll(l1);
						for(int i=0;i<l2.size();i++) {
							html+="<option value="+l2.get(i)+">"+l2.get(i)+"</option>";
										}
					}
					else if(rsuser.getString(4).equals("user")) {
						l3.removeAll(l1);
						for(int i=0;i<l3.size();i++) {
							html+="<option value="+l3.get(i)+">"+l3.get(i)+"</option>";
										}
					}
			
					}
			html+="<input type='submit' value='submit'>";
			
					return html;
			
			}
					@GET
					@Path("intoteam")
					public String intoTeam(@Context HttpServletRequest rq,@Context HttpServletResponse rsp) throws Exception {
						Connection c = Connect.connect();
						PreparedStatement useDb = c.prepareStatement("use batch4");
						useDb.execute();
						String name="";
						HttpSession s = rq.getSession();
						name=s.getAttribute("username").toString();
						PreparedStatement ps = c.prepareStatement("select username from userdata where usertype='user' and manager is null");
						ResultSet rs=ps.executeQuery();
						String data="";
						while(rs.next()) {
							data+=rs.getString(1)+"<a href='http://localhost:8080/usermanage/webresources/user/updateteam/"+rs.getString(1)+"'>Add to Team</a><br>";
						}
						return data;
					}
					
					@GET
					@Path("updateteam/{name}")
					public String updateteam(@PathParam("name") String name,@Context HttpServletRequest rq,@Context HttpServletResponse rsp) throws Exception {
						Connection c = Connect.connect();
						PreparedStatement useDb = c.prepareStatement("use batch4");
						useDb.execute();
						String n="";
						HttpSession s = rq.getSession();
						n=s.getAttribute("username").toString();
						PreparedStatement ps = c.prepareStatement("update userdata set manager='"+n+"' where username='"+name+"'");
						ps.execute();
						return "data updated";
					}
			
					@GET
					@Path("teamdetails")
					public String teamdetails(@PathParam("name") String name,@Context HttpServletRequest rq,@Context HttpServletResponse rsp) throws Exception {
						Connection c = Connect.connect();
						PreparedStatement useDb = c.prepareStatement("use batch4");
						useDb.execute();
						String n="";
						HttpSession s = rq.getSession();
						n=s.getAttribute("username").toString();
						PreparedStatement ps = c.prepareStatement("select username from userdata where manager='"+n+"'");
						ResultSet rs=ps.executeQuery();
						String data="";
						while(rs.next()) {
							data+=rs.getString(1)+"<br>";
						}
						return "Team Details:"+"<br>"+data;
					}
					
					@GET
					@Path("logout")
					public Response logout(@Context HttpServletRequest rq,@Context HttpServletResponse rsp) {
						rq.getSession().invalidate();
						return Response.ok("logged out successfully").build();
					}
}


