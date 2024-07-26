/*
package com.dpt.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class Register {
	

	@Value("${spring.datasource.url}")
	private String url;

	@Value("${spring.datasource.username}")
	private String DBusername;

	@Value("${spring.datasource.password}")
	private String DBpassword;
	
	
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView registerform()
	{
		ModelAndView mv=new ModelAndView("register");
		
		return mv;		
	}
	
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ModelAndView register(String firstName,String lastName,String email,String userName,String password) throws ClassNotFoundException
	{
		Class.forName("com.mysql.jdbc.Driver");
		//Add employee here
		try (Connection con = DriverManager.getConnection(url, DBusername, DBpassword);
				Statement st = con.createStatement()) {

			String sql = "insert into Employee (first_name,last_name,email,username,password,regdate) values('"+firstName+"','"+lastName+"','"+email+"','"+userName+"','"+password+"',CURDATE());";
			st.execute(sql);

		} catch (SQLException ex) {

			ex.printStackTrace();

		}
				
		
		ModelAndView mv=new ModelAndView("register");		
		mv.addObject("message", "user account has been added for "+userName);
		return mv;		
	}
	

}
*/

package com.dpt.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class Register {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String DBusername;

    @Value("${spring.datasource.password}")
    private String DBpassword;

    private static final Logger LOGGER = Logger.getLogger(Register.class.getName());

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView registerForm() {
        ModelAndView mv = new ModelAndView("register");
        return mv;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView register(String firstName, String lastName, String email, String userName, String password) {
        ModelAndView mv = new ModelAndView("register");
        Connection con = null;
        PreparedStatement ps = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");  // Updated to the new MySQL driver class
            con = DriverManager.getConnection(url, DBusername, DBpassword);

            String sql = "INSERT INTO Employee (first_name, last_name, email, username, password, regdate) VALUES (?, ?, ?, ?, ?, CURDATE())";
            ps = con.prepareStatement(sql);
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, email);
            ps.setString(4, userName);
            ps.setString(5, password);  // Password should be hashed

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                mv.addObject("message", "User account has been added for " + userName);
            } else {
                mv.addObject("message", "Failed to add user account for " + userName);
            }
        } catch (ClassNotFoundException | SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error: ", e);
            mv.addObject("message", "An error occurred while processing your request. Please try again later.");
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error closing resources: ", e);
            }
        }

        return mv;
    }
}
