/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import entity.Chat;
import entity.Chat_Status;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
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
@WebServlet(name = "LoadChat", urlPatterns = {"/LoadChat"})
public class LoadChat extends HttpServlet {

    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//
//        Gson gson = new Gson();
//        Session session = HibernateUtil.getSessionFactory().openSession();
//
//        String logged_user_id = req.getParameter("logged_user_id");
//        String other_user_id = req.getParameter("other_user_id");
//
//        //get user
//        User logged_user = (User) session.get(User.class, Integer.parseInt(logged_user_id));
//
//        //get other user
//        User other_user = (User) session.get(User.class, Integer.parseInt(other_user_id));
//
//        Criteria criteria1 = session.createCriteria(Chat.class);
//        criteria1.add(
//                Restrictions.or(
//                        Restrictions.and(
//                                Restrictions.eq("from_user", other_user), Restrictions.eq("to_user", logged_user)
//                        ),
//                        Restrictions.and(
//                                Restrictions.eq("from_user", logged_user), Restrictions.eq("to_user", other_user)
//                        )
//                )
//        );
//
//        //sort chats
//        criteria1.addOrder(Order.asc("date_time"));
//
//        List<Chat> chat_liist = criteria1.list();
//
//        Chat_Status chat_Status = (Chat_Status) session.get(Chat_Status.class, 1);
//
//        //cretate chat array
//        JsonArray chatArray = new JsonArray();
//
//        //create date time format
//        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, hh:mm: a");
//
//        for (Chat chat : chat_liist) {
//
//            //create chat object
//            JsonObject chatObject = new JsonObject();
//            chatObject.addProperty("message", chat.getMessage());
//            chatObject.addProperty("datetime", dateFormat.format(chat.getDate_time()));
//            
//           
//
//            //get chats only from other user
//            if (chat.getFrom_user().getId() == other_user.getId()) {
//                
//                //add side to chat object
//                chatObject.addProperty("side", "left");
//
//                //get only unseen chats(chat_status_id = 2)
//                if (chat.getChat_Status().getId() == 2) {
//                    //update chat status - > seen
//                    chat.setChat_Status(chat_Status);
//                    session.update(chat);
//                }
//
//            }else{
//                //get chat from logged user
//                
//                chatObject.addProperty("side", "right");
//                chatObject.addProperty("status", chat.getChat_Status().getId());
//            }
//           //add chat object into chat array
//            chatArray.add(chatObject);
//        }
//
//        session.beginTransaction().commit();
//        
//        resp.setContentType("application/json");
//        resp.getWriter().write(gson.toJson(chatArray));
//    }
    
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    Gson gson = new Gson();
    
    
        
    
    
    Session session = HibernateUtil.getSessionFactory().openSession();

    String logged_user_id = req.getParameter("logged_user_id");
    String other_user_id = req.getParameter("other_user_id");

    // Get user objects
    User logged_user = (User) session.get(User.class, Integer.parseInt(logged_user_id));
    User other_user = (User) session.get(User.class, Integer.parseInt(other_user_id));

    Criteria criteria1 = session.createCriteria(Chat.class);
    criteria1.add(
        Restrictions.or(
            Restrictions.and(
                Restrictions.eq("from_user", other_user), Restrictions.eq("to_user", logged_user)
            ),
            Restrictions.and(
                Restrictions.eq("from_user", logged_user), Restrictions.eq("to_user", other_user)
            )
        )
    );

    // Sort chats by date
    criteria1.addOrder(Order.asc("date_time"));

    List<Chat> chat_list = criteria1.list();
    Chat_Status chat_Status = (Chat_Status) session.get(Chat_Status.class, 1);

    // Create chat array to hold JSON responses
    JsonArray chatArray = new JsonArray();

    // Date formatter for message timestamp and date labels
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, hh:mm a");
    
    SimpleDateFormat timeFo = new SimpleDateFormat("hh:mm a");
    SimpleDateFormat dateLabelFormat = new SimpleDateFormat("yyyy MMM dd");

    String lastDate = ""; // To track when the date changes

    for (Chat chat : chat_list) {
        String currentChatDate = dateLabelFormat.format(chat.getDate_time());

        // Check if the date has changed to insert a date label
        if (!lastDate.equals(currentChatDate)) {
            lastDate = currentChatDate;

            // Create a date label object
            JsonObject dateLabelObject = new JsonObject();
            if (currentChatDate.equals(dateLabelFormat.format(new Date()))) {
                dateLabelObject.addProperty("dateLabel", "Today");
            } else {
                dateLabelObject.addProperty("dateLabel", currentChatDate);
            }

            // Add the date label to the chat array
            chatArray.add(dateLabelObject);
        }

        // Create the chat message object
        JsonObject chatObject = new JsonObject();
        
        if(chat.getType() == 0){
            chatObject.addProperty("type", "text");
            chatObject.addProperty("message", chat.getMessage());
        }else if(chat.getType() == 1){
            chatObject.addProperty("type", "image");
            chatObject.addProperty("imagePath",chat.getMessage());
        }
        
        
        chatObject.addProperty("chatDate", dateLabelFormat.format(chat.getDate_time()));
        chatObject.addProperty("chatTime", timeFo.format(chat.getDate_time()));
        chatObject.addProperty("id", chat.getId());
        
        

        // Determine the side and status of the message
        if (chat.getFrom_user().getId() == other_user.getId()) {
            chatObject.addProperty("side", "left");

            if (chat.getChat_Status().getId() == 2) {
                chat.setChat_Status(chat_Status); // Update unseen messages to seen
                session.update(chat);
                
            }
        } else {
            chatObject.addProperty("side", "right");
            chatObject.addProperty("status", chat.getChat_Status().getId());
        }
        
        if(chat.getChat_action() == 0){
            chatObject.addProperty("action", "send");
        }else if(chat.getChat_action() == 1){
            chatObject.addProperty("action", "unsend");
        }
        
        
        // Add the chat message to the chat array
        chatArray.add(chatObject);
    }

    
    session.beginTransaction().commit();
    // Send response as JSON
    
    
    session.close();
    


    resp.setContentType("application/json");
    resp.getWriter().write(gson.toJson(chatArray));
    
}


}
