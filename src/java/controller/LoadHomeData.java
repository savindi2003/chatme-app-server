package controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import entity.Chat;
import entity.User;
import entity.User_Status;
import java.io.File;
import java.io.IOException;

import java.text.SimpleDateFormat;

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

import org.hibernate.criterion.Restrictions;


/**
 *
 * @author Savindi
 */
@WebServlet(name = "LoadHomeData", urlPatterns = {"/LoadHomeData"})
public class LoadHomeData extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Gson gson = new Gson();

        JsonObject responsejson = new JsonObject();
        responsejson.addProperty("status", false);
        responsejson.addProperty("today", false);

        responsejson.addProperty("message", "Unable to process your request");

        try {

            Session session = HibernateUtil.getSessionFactory().openSession();

            String userId = req.getParameter("id");
            
            System.out.println(userId);

            User user = (User) session.get(User.class, Integer.parseInt(userId));

            User_Status user_status = (User_Status) session.get(User_Status.class, 1);

            //update user status
            user.setUser_Status(user_status);
            session.update(user);
            session.beginTransaction().commit();
            

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
                SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy, MM dd hh:mm a");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy, MM dd");
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");

                if (dbChatList.isEmpty()) {

                } else {

                    JsonObject jsonChatItem = new JsonObject();
                    jsonChatItem.addProperty("other_user_id", otherUser.getId());
                    jsonChatItem.addProperty("other_user_mobile", otherUser.getMobile());
                    jsonChatItem.addProperty("other_user_name", otherUser.getFirst_name() + " " + otherUser.getLast_name());
                    jsonChatItem.addProperty("other_user_status", otherUser.getUser_Status().getId());
                    
                    if(otherUser.getMobile_visibility() == 0){
                        jsonChatItem.addProperty("mobile_visibility",0);
                    }else if(otherUser.getMobile_visibility() == 1){
                        jsonChatItem.addProperty("mobile_visibility",1);
                    }else if(otherUser.getMobile_visibility() == 2){
                        jsonChatItem.addProperty("mobile_visibility",2);
                    }
                    
                    if(otherUser.getOnline_visibility() ==0){
                        jsonChatItem.addProperty("online_visibility",0);
                    }else if(otherUser.getOnline_visibility() ==1){
                        jsonChatItem.addProperty("online_visibility",1);
                    }else if(otherUser.getOnline_visibility() ==2){
                        jsonChatItem.addProperty("online_visibility",2);
                    }
                    
                    if(otherUser.getDp_visibility()==0){
                        jsonChatItem.addProperty("dp_visibility",0);
                    }else if(otherUser.getDp_visibility() ==1){
                        jsonChatItem.addProperty("dp_visibility",1);
                    }else if(otherUser.getDp_visibility() ==2){
                        jsonChatItem.addProperty("dp_visibility",2);
                    }

                    //check avatar image
                    String serverPath = req.getServletContext().getRealPath("");
                    String otherUserAvatarImagePath = serverPath + File.separator + "UserImages" + File.separator + otherUser.getMobile() + ".png";
                    
                    System.out.println(otherUserAvatarImagePath);

                    File otherUserAvatarImageFile = new File(otherUserAvatarImagePath);
                    if (otherUserAvatarImageFile.exists()) {
                        //image found
                        jsonChatItem.addProperty("avatar_image_found", true);
                        jsonChatItem.addProperty("other_user_avatar_letters", otherUser.getFirst_name().charAt(0) + "" + otherUser.getLast_name().charAt(0));

                    } else {
                        //image not found
                        jsonChatItem.addProperty("avatar_image_found", false);
                        jsonChatItem.addProperty("other_user_avatar_letters", otherUser.getFirst_name().charAt(0) + "" + otherUser.getLast_name().charAt(0));

                    }

                    if (dbChatList.get(0).getFrom_user() == user) {
                        jsonChatItem.addProperty("messageStatus", true);
                        

                        

                    } else {
                        jsonChatItem.addProperty("messageStatus", false);
                        //get unseen chat count
                        int chatStatusId = dbChatList.get(0).getChat_Status().getId();
                        if (chatStatusId == 2) {
                            int count = 0; // Initialize the counter
                            count++; // Increment for each chat with status 2
                            jsonChatItem.addProperty("chat_status_count", count); // Add to response
                        }
                    }
                    
                    if(dbChatList.get(0).getType()==0){
                        
                        jsonChatItem.addProperty("type", "text");
                        jsonChatItem.addProperty("message", dbChatList.get(0).getMessage());
                        
                    }else if(dbChatList.get(0).getType()==1){
                        jsonChatItem.addProperty("type", "image");
                    }

                    if(dbChatList.get(0).getChat_action() == 0){
                        jsonChatItem.addProperty("action", "send");
                    }else if(dbChatList.get(0).getChat_action() == 1){
                        jsonChatItem.addProperty("action", "unsend");
                    }
                    

                    Date chatDate = dbChatList.get(0).getDate_time();

                    Date today = new Date();

                    if (dateFormat.format(chatDate).equals(dateFormat.format(today))) {
                        jsonChatItem.addProperty("today", true);
                        jsonChatItem.addProperty("chatTime", timeFormat.format(chatDate));

                    } else {
                        jsonChatItem.addProperty("today", false);
                        jsonChatItem.addProperty("chatDate", dateFormat.format(chatDate));
                        jsonChatItem.addProperty("chatTime", timeFormat.format(chatDate));
                    }
                    
                    
                    

                    jsonChatItem.addProperty("chat_status_id", dbChatList.get(0).getChat_Status().getId());

                    otherUser.setPassword(null);
                    jsonCharArray.add(jsonChatItem);
                    
                    System.out.println(jsonChatItem);

                    responsejson.addProperty("success", true);
                    responsejson.addProperty("message", "success");
                    //responsejson.add("user", gson.toJsonTree(user));
                    responsejson.add("jsonChatArray", gson.toJsonTree(jsonCharArray));
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
//package controller;
//
//import com.google.gson.Gson;
//import com.google.gson.JsonArray;
//import com.google.gson.JsonObject;
//import entity.Chat;
//import entity.User;
//import entity.User_Status;
//import java.io.File;
//import java.io.IOException;
//
//import java.text.SimpleDateFormat;
//
//import java.util.Date;
//import java.util.List;
//
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import model.HibernateUtil;
//import org.hibernate.Criteria;
//import org.hibernate.Session;
//import org.hibernate.criterion.Order;
//
//import org.hibernate.criterion.Restrictions;
//
//
///**
// *
// * @author Savindi
// */
//@WebServlet(name = "LoadHomeData", urlPatterns = {"/LoadHomeData"})
//public class LoadHomeData extends HttpServlet {
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//
//        Gson gson = new Gson();
//
//        JsonObject responsejson = new JsonObject();
//        responsejson.addProperty("status", false);
//        responsejson.addProperty("today", false);
//
//        responsejson.addProperty("message", "Unable to process your request");
//
//        try {
//
//            Session session = HibernateUtil.getSessionFactory().openSession();
//
//            String userId = req.getParameter("id");
//            
//            System.out.println(userId);
//
//            User user = (User) session.get(User.class, Integer.parseInt(userId));
//
//            User_Status user_status = (User_Status) session.get(User_Status.class, 1);
//
//            //update user status
//            user.setUser_Status(user_status);
//            session.update(user);
//            session.beginTransaction().commit();
//            
//
//            //get other users
//            Criteria criteria1 = session.createCriteria(User.class);
//            criteria1.add(Restrictions.ne("id", user.getId()));
//            List<User> otherUserlist = criteria1.list();
//
//            JsonArray jsonCharArray = new JsonArray();
//
//            for (User otherUser : otherUserlist) {
//
//                //get chat list
//                Criteria criteria2 = session.createCriteria(Chat.class);
//                criteria2.add(
//                        Restrictions.or(
//                                Restrictions.and(
//                                        Restrictions.eq("from_user", user),
//                                        Restrictions.eq("to_user", otherUser)
//                                ),
//                                Restrictions.and(
//                                        Restrictions.eq("from_user", otherUser),
//                                        Restrictions.eq("to_user", user)
//                                )
//                        )
//                );
//                criteria2.addOrder(Order.desc("id"));
//                criteria2.setMaxResults(1);
//                
//                
//
//                //create chat item to send fronted data
//                List<Chat> dbChatList = criteria2.list();
//                SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy, MM dd hh:mm a");
//                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy, MM dd");
//                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
//
//                if (dbChatList.isEmpty()) {
//
//                } else {
//
//                    JsonObject jsonChatItem = new JsonObject();
//                    jsonChatItem.addProperty("other_user_id", otherUser.getId());
//                    jsonChatItem.addProperty("other_user_mobile", otherUser.getMobile());
//                    jsonChatItem.addProperty("other_user_name", otherUser.getFirst_name() + " " + otherUser.getLast_name());
//                    jsonChatItem.addProperty("other_user_status", otherUser.getUser_Status().getId());
//                    
//                    if(otherUser.getMobile_visibility() == 0){
//                        jsonChatItem.addProperty("mobile_visibility",0);
//                    }else if(otherUser.getMobile_visibility() == 1){
//                        jsonChatItem.addProperty("mobile_visibility",1);
//                    }else if(otherUser.getMobile_visibility() == 2){
//                        jsonChatItem.addProperty("mobile_visibility",2);
//                    }
//                    
//                    if(otherUser.getOnline_visibility() ==0){
//                        jsonChatItem.addProperty("online_visibility",0);
//                    }else if(otherUser.getOnline_visibility() ==1){
//                        jsonChatItem.addProperty("online_visibility",1);
//                    }else if(otherUser.getOnline_visibility() ==2){
//                        jsonChatItem.addProperty("online_visibility",2);
//                    }
//                    
//                    if(otherUser.getDp_visibility()==0){
//                        jsonChatItem.addProperty("dp_visibility",0);
//                    }else if(otherUser.getDp_visibility() ==1){
//                        jsonChatItem.addProperty("dp_visibility",1);
//                    }else if(otherUser.getDp_visibility() ==2){
//                        jsonChatItem.addProperty("dp_visibility",2);
//                    }
//                    
//        
//                    if (otherUser.getDp() != null && !otherUser.getDp().trim().isEmpty()) {
//                        
//                        jsonChatItem.addProperty("avatar_image_found", true);
//                        jsonChatItem.addProperty("dp_path", otherUser.getDp().trim());
//                        System.out.println(otherUser.getDp().trim());
//                        
//                    }else{
//                        jsonChatItem.addProperty("avatar_image_found", false);
//                        jsonChatItem.addProperty("other_user_avatar_letters", otherUser.getFirst_name().charAt(0) + "" + otherUser.getLast_name().charAt(0));
//
//                    }
//                    
//                    
//                    
//                    
//
//                    if (dbChatList.get(0).getFrom_user() == user) {
//                        jsonChatItem.addProperty("messageStatus", true);
//                        
//
//                        
//
//                    } else {
//                        jsonChatItem.addProperty("messageStatus", false);
//                        //get unseen chat count
//                        int chatStatusId = dbChatList.get(0).getChat_Status().getId();
//                        if (chatStatusId == 2) {
//                            int count = 0; // Initialize the counter
//                            count++; // Increment for each chat with status 2
//                            jsonChatItem.addProperty("chat_status_count", count); // Add to response
//                        }
//                    }
//                    
//                    if(dbChatList.get(0).getType()==0){
//                        
//                        jsonChatItem.addProperty("type", "text");
//                        jsonChatItem.addProperty("message", dbChatList.get(0).getMessage());
//                        
//                    }else if(dbChatList.get(0).getType()==1){
//                        jsonChatItem.addProperty("type", "image");
//                    }
//
//                    if(dbChatList.get(0).getChat_action() == 0){
//                        jsonChatItem.addProperty("action", "send");
//                    }else if(dbChatList.get(0).getChat_action() == 1){
//                        jsonChatItem.addProperty("action", "unsend");
//                    }
//                    
//
//                    Date chatDate = dbChatList.get(0).getDate_time();
//
//                    Date today = new Date();
//
//                    if (dateFormat.format(chatDate).equals(dateFormat.format(today))) {
//                        jsonChatItem.addProperty("today", true);
//                        jsonChatItem.addProperty("chatTime", timeFormat.format(chatDate));
//
//                    } else {
//                        jsonChatItem.addProperty("today", false);
//                        jsonChatItem.addProperty("chatDate", dateFormat.format(chatDate));
//                        jsonChatItem.addProperty("chatTime", timeFormat.format(chatDate));
//                    }
//                    
//                    
//                    
//
//                    jsonChatItem.addProperty("chat_status_id", dbChatList.get(0).getChat_Status().getId());
//
//                    otherUser.setPassword(null);
//                    jsonCharArray.add(jsonChatItem);
//                    
//                    System.out.println(jsonChatItem);
//
//                    responsejson.addProperty("success", true);
//                    responsejson.addProperty("message", "success");
//                    //responsejson.add("user", gson.toJsonTree(user));
//                    responsejson.add("jsonChatArray", gson.toJsonTree(jsonCharArray));
//                }
//
//            }
//
//            //send users
//            
//            session.close();
//            
//            
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//              
//        
//        resp.setContentType("application/json");
//        resp.getWriter().write(gson.toJson(responsejson));
//        
//        
//        
//
//    }
//
//}

//package controller;
//
//import com.google.gson.Gson;
//import com.google.gson.JsonArray;
//import com.google.gson.JsonObject;
//import entity.Chat;
//import entity.User;
//import entity.User_Status;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.*;
//
//import model.HibernateUtil;
//import org.hibernate.Criteria;
//import org.hibernate.Session;
//import org.hibernate.criterion.Order;
//import org.hibernate.criterion.Restrictions;
//
//@WebServlet(name = "LoadHomeData", urlPatterns = {"/LoadHomeData"})
//public class LoadHomeData extends HttpServlet {
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//
//        Gson gson = new Gson();
//        JsonObject responsejson = new JsonObject();
//        responsejson.addProperty("status", false);
//        responsejson.addProperty("today", false);
//        responsejson.addProperty("message", "Unable to process your request");
//
//        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
//
//            String userId = req.getParameter("id");
//            System.out.println(userId);
//
//            User user = (User) session.get(User.class, Integer.parseInt(userId));
//            User_Status user_status = (User_Status) session.get(User_Status.class, 1);
//
//            // update user status
//            user.setUser_Status(user_status);
//            session.update(user);
//            session.beginTransaction().commit();
//
//            // get other users
//            Criteria criteria1 = session.createCriteria(User.class);
//            criteria1.add(Restrictions.ne("id", user.getId()));
//            List<User> otherUserlist = criteria1.list();
//
//            List<JsonObject> tempList = new ArrayList<>();
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy, MM dd");
//            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
//
//            for (User otherUser : otherUserlist) {
//
//                // get last message
//                Criteria criteria2 = session.createCriteria(Chat.class);
//                criteria2.add(
//                        Restrictions.or(
//                                Restrictions.and(
//                                        Restrictions.eq("from_user", user),
//                                        Restrictions.eq("to_user", otherUser)
//                                ),
//                                Restrictions.and(
//                                        Restrictions.eq("from_user", otherUser),
//                                        Restrictions.eq("to_user", user)
//                                )
//                        )
//                );
//                criteria2.addOrder(Order.desc("id"));
//                criteria2.setMaxResults(1);
//
//                List<Chat> dbChatList = criteria2.list();
//
//                if (!dbChatList.isEmpty()) {
//                    Chat chat = dbChatList.get(0);
//                    JsonObject jsonChatItem = new JsonObject();
//
//                    jsonChatItem.addProperty("other_user_id", otherUser.getId());
//                    jsonChatItem.addProperty("other_user_mobile", otherUser.getMobile());
//                    jsonChatItem.addProperty("other_user_name", otherUser.getFirst_name() + " " + otherUser.getLast_name());
//                    jsonChatItem.addProperty("other_user_status", otherUser.getUser_Status().getId());
//
//                    jsonChatItem.addProperty("mobile_visibility", otherUser.getMobile_visibility());
//                    jsonChatItem.addProperty("online_visibility", otherUser.getOnline_visibility());
//                    jsonChatItem.addProperty("dp_visibility", otherUser.getDp_visibility());
//
//                    if (otherUser.getDp() != null && !otherUser.getDp().trim().isEmpty()) {
//                        jsonChatItem.addProperty("avatar_image_found", true);
//                        jsonChatItem.addProperty("dp_path", otherUser.getDp().trim());
//                    } else {
//                        jsonChatItem.addProperty("avatar_image_found", false);
//                        jsonChatItem.addProperty("other_user_avatar_letters",
//                                otherUser.getFirst_name().charAt(0) + "" + otherUser.getLast_name().charAt(0));
//                    }
//
//                    jsonChatItem.addProperty("messageStatus", chat.getFrom_user().equals(user));
//
//                    if (!chat.getFrom_user().equals(user) && chat.getChat_Status().getId() == 2) {
//                        jsonChatItem.addProperty("chat_status_count", 1);
//                    }
//
//                    if (chat.getType() == 0) {
//                        jsonChatItem.addProperty("type", "text");
//                        jsonChatItem.addProperty("message", chat.getMessage());
//                    } else if (chat.getType() == 1) {
//                        jsonChatItem.addProperty("type", "image");
//                    }
//
//                    jsonChatItem.addProperty("action", chat.getChat_action() == 0 ? "send" : "unsend");
//
//                    Date chatDate = chat.getDate_time();
//                    Date today = new Date();
//
//                    if (dateFormat.format(chatDate).equals(dateFormat.format(today))) {
//                        jsonChatItem.addProperty("today", true);
//                        jsonChatItem.addProperty("chatTime", timeFormat.format(chatDate));
//                    } else {
//                        jsonChatItem.addProperty("today", false);
//                        jsonChatItem.addProperty("chatDate", dateFormat.format(chatDate));
//                        jsonChatItem.addProperty("chatTime", timeFormat.format(chatDate));
//                    }
//
//                    jsonChatItem.addProperty("chat_status_id", chat.getChat_Status().getId());
//
//                    // ✅ Store chat date-time as hidden field for sorting
//                    jsonChatItem.addProperty("chat_datetime_sort", chatDate.getTime());
//
//                    tempList.add(jsonChatItem);
//                }
//            }
//
//            // ✅ Sort by chat_datetime_sort descending
//            tempList.sort((o1, o2) -> {
//                long t1 = o1.get("chat_datetime_sort").getAsLong();
//                long t2 = o2.get("chat_datetime_sort").getAsLong();
//                return Long.compare(t2, t1); // descending order
//            });
//
//            // ✅ Convert to JsonArray
//            JsonArray jsonCharArray = new JsonArray();
//            for (JsonObject obj : tempList) {
//                obj.remove("chat_datetime_sort"); // optional: clean up
//                jsonCharArray.add(obj);
//            }
//
//            responsejson.addProperty("success", true);
//            responsejson.addProperty("message", "success");
//            responsejson.add("jsonChatArray", gson.toJsonTree(jsonCharArray));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        resp.setContentType("application/json");
//        resp.getWriter().write(gson.toJson(responsejson));
//    }
//}
//
