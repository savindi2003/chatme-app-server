/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Savindi
 */
@WebServlet(name = "UpdateAccount", urlPatterns = {"/UpdateAccount"})
public class UpdateAccount extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    
        Gson gson = new Gson();
        JsonObject responseJson = new JsonObject();
        responseJson.addProperty("success", false);

        Session session = HibernateUtil.getSessionFactory().openSession();
       

        String userId = req.getParameter("user");
        String mobile = req.getParameter("mobile");
        String online = req.getParameter("online");
        String dp = req.getParameter("dp");
        
        // 0 - everyone
        // 1 - friends
        // 2 - only me
        
        int online_id = 0;
        int mobile_id = 0;
        int dp_id = 0;
        
        
        
        if(mobile.equals("1")){
            mobile_id = 0;
        }else if(mobile.equals("2")){
            mobile_id = 1;
        }else if(mobile.equals("3")){
            mobile_id = 2;
        }
        
        if(online.equals("1")){
            online_id = 0;
        }else if(online.equals("2")){
            online_id = 1;
        }else if(online.equals("3")){
            online_id = 2;
        }
        
        if(dp.equals("1")){
            dp_id = 0;
        }else if(dp.equals("2")){
            dp_id = 1;
        }else if(dp.equals("3")){
            dp_id = 2;
        }
       
        try{

        User user = (User) session.get(User.class, Integer.parseInt(userId));
        
        if(!mobile.equals("4")){
            user.setMobile_visibility(mobile_id);
        }
        
        if(!online.equals("4")){
            user.setOnline_visibility(online_id);
        }
        
        if(!dp.equals("4")){
            user.setDp_visibility(dp_id);
        }
        

        session.update(user);
        session.beginTransaction().commit();
        
        
        responseJson.addProperty("success", true);
        responseJson.addProperty("message", "update user settings");
        
        
        session.close();
        
        }catch(Exception e){
            e.printStackTrace();
        }
        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(responseJson));

        
        
    
    }

    

}
