/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.User;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Session;

/**
 *
 * @author Savindi
 */
@WebServlet(name = "LoadAccount", urlPatterns = {"/LoadAccount"})
public class LoadAccount extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    
        Gson gson = new Gson();

        JsonObject responsejson = new JsonObject();
        responsejson.addProperty("success", false);
        responsejson.addProperty("message", "Unable to process your request");
        
        
            
            Session session = HibernateUtil.getSessionFactory().openSession();

            String userId = req.getParameter("id");
            User user = (User) session.get(User.class, Integer.parseInt(userId));
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MMM dd");
            
            JsonObject userObject = new JsonObject();
            userObject.addProperty("user_name", user.getFirst_name()+" "+user.getLast_name());
            userObject.addProperty("user_mobile", user.getMobile());
            
            Date registerd_date = user.getRegistered_date_time();
            userObject.addProperty("user_date",dateFormat.format(registerd_date));
            
//            String serverPath = req.getServletContext().getRealPath("");
//            String userImagePath = serverPath + File.separator + "UserImages" + File.separator + user.getMobile() + ".png";
//                    
//            File otherImageFile = new File(userImagePath);
//                    if (otherImageFile.exists()) {
//                        //image found
//                        userObject.addProperty("user_image_found", true);
//
//                    } else {
//                        //image not found
//                        userObject.addProperty("user_image_found", false);
//                        userObject.addProperty("user_avatar_letters", user.getFirst_name().charAt(0) + "" + user.getLast_name().charAt(0));
//
//                    }
                    
                    
            if (user.getDp() != null && !user.getDp().trim().isEmpty()) {
                        
                        userObject.addProperty("user_image_found", true);
                        userObject.addProperty("dp_path", user.getDp().trim());
                        System.out.println(user.getDp().trim());
                        
                    }else{
                        userObject.addProperty("user_image_found", false);
                        userObject.addProperty("user_avatar_letters", user.getFirst_name().charAt(0) + "" + user.getLast_name().charAt(0));

                    }
            
            // everyone - nulll
            //friends - 1
            //only me - 2
                    
            if(user.getMobile_visibility() == 1){
                userObject.addProperty("user_mobile_visibility", "Friends");
            }else if(user.getMobile_visibility() == 2){
                userObject.addProperty("user_mobile_visibility", "Only me");
            }else{
                userObject.addProperty("user_mobile_visibility", "Everyone");
            }
            
            if(user.getOnline_visibility()== 1){
                userObject.addProperty("user_online_visibility", "Friends");
            }else if(user.getOnline_visibility() == 2){
                userObject.addProperty("user_online_visibility", "Only me");
            }else{
                userObject.addProperty("user_online_visibility", "Everyone");
            }
            
            if(user.getDp_visibility()== 1){
                userObject.addProperty("user_dp_visibility", "Friends");
            }else if(user.getDp_visibility() == 2){
                userObject.addProperty("user_dp_visibility", "Only me");
            }else{
                userObject.addProperty("user_dp_visibility", "Everyone");
            }
            
            responsejson.addProperty("success", true);
            responsejson.addProperty("message", "success");
                    
            responsejson.add("jsonUserArray", gson.toJsonTree(userObject));
            
            
        
        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(responsejson));
    
    }

    
    
}
