/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import entity.Chat;
import entity.User;
import entity.User_Status;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Savindi
 */
@WebServlet(name = "LoadOnlineUsers", urlPatterns = {"/LoadOnlineUsers"})
public class LoadOnlineUsers extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Gson gson = new Gson();

        JsonObject responsejson = new JsonObject();
        responsejson.addProperty("status", false);

        responsejson.addProperty("message", "Unable to process your request");

        try {

            Session session = HibernateUtil.getSessionFactory().openSession();

            String userId = req.getParameter("id");

            User user = (User) session.get(User.class, Integer.parseInt(userId));

            

            //get other users
            Criteria criteria1 = session.createCriteria(User.class);
            criteria1.add(Restrictions.ne("id", user.getId()));
            List<User> otherUserlist = criteria1.list();

            JsonArray jsonCharArray = new JsonArray();

            for (User otherUser : otherUserlist) {

                //get chat list
                Criteria criteria2 = session.createCriteria(Chat.class);
                criteria2.add(
                        Restrictions.or(
                                Restrictions.and(
                                        Restrictions.eq("from_user", user),
                                        Restrictions.eq("to_user", otherUser)
                                ),
                                Restrictions.and(
                                        Restrictions.eq("from_user", otherUser),
                                        Restrictions.eq("to_user", user)
                                )
                        )
                );
                criteria2.addOrder(Order.desc("id"));
                criteria2.setMaxResults(1);

                //create chat item to send fronted data
                List<Chat> dbChatList = criteria2.list();

                if (dbChatList.isEmpty()) {

                } else {
                    
                    int userStatusId = otherUser.getUser_Status().getId();
                    int onlyMe = otherUser.getOnline_visibility();

                    if (userStatusId == 1 && onlyMe != 2) {

                    JsonObject jsonChatItem = new JsonObject();
                    jsonChatItem.addProperty("other_user_id", otherUser.getId());
                    jsonChatItem.addProperty("other_user_name", otherUser.getFirst_name());
                    jsonChatItem.addProperty("other_user_status", otherUser.getUser_Status().getId());
                    jsonChatItem.addProperty("other_user_mobile", otherUser.getMobile());
                    
                    

                    //check avatar image
//                    String serverPath = req.getServletContext().getRealPath("");
//                    String otherUserAvatarImagePath = serverPath + File.separator + "UserImages" + File.separator + otherUser.getMobile() + ".png";
//
//                    File otherUserAvatarImageFile = new File(otherUserAvatarImagePath);
//                    if (otherUserAvatarImageFile.exists()) {
//                        //image found
//                        jsonChatItem.addProperty("avatar_image_found", true);
//                        jsonChatItem.addProperty("dp_path", otherUser.getDp().trim());
//
//                    } else {
//                        //image not found
//                        jsonChatItem.addProperty("avatar_image_found", false);
//                        jsonChatItem.addProperty("other_user_avatar_letters", otherUser.getFirst_name().charAt(0) + "" + otherUser.getLast_name().charAt(0));
//
//                    }
                    
                    
                    if (otherUser.getDp() != null && !otherUser.getDp().trim().isEmpty()) {
                        
                        jsonChatItem.addProperty("avatar_image_found", true);
                        jsonChatItem.addProperty("dp_path", otherUser.getDp().trim());
                        System.out.println(otherUser.getDp().trim());
                        
                    }else{
                        jsonChatItem.addProperty("avatar_image_found", false);
                        jsonChatItem.addProperty("other_user_avatar_letters", otherUser.getFirst_name().charAt(0) + "" + otherUser.getLast_name().charAt(0));

                    }
                    
                    
                    
                    otherUser.setPassword(null);
                    jsonCharArray.add(jsonChatItem);

                    responsejson.addProperty("success", true);
                    responsejson.addProperty("message", "success");
                    //responsejson.add("user", gson.toJsonTree(user));
                    responsejson.add("jsonOnlineUsersArray", gson.toJsonTree(jsonCharArray));
                }
                }

            }

            //send users
            
            session.close();
            
            

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        
        
        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(responsejson));

    }

}
