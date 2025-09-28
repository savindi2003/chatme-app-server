/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.Chat;
import entity.Chat_Status;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
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
@WebServlet(name = "SendChat", urlPatterns = {"/SendChat"})
public class SendChat extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject responseJson = new JsonObject();
        responseJson.addProperty("success", false);

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        String logged_user_id = req.getParameter("logged_user_id");
        String other_user_id = req.getParameter("other_user_id");
        String message = req.getParameter("message");
        try {
            //get user
            User logged_user = (User) session.get(User.class, Integer.parseInt(logged_user_id));

            //get other user
            User other_user = (User) session.get(User.class, Integer.parseInt(other_user_id));

            //save chat
            Chat chat = new Chat();

            Chat_Status chat_status = (Chat_Status) session.get(Chat_Status.class, 2);
            chat.setChat_Status(chat_status);

            chat.setDate_time(new Date());
            chat.setFrom_user(logged_user);
            chat.setTo_user(other_user);
            chat.setMessage(message);
            chat.setType(0);
            chat.setChat_action(0);

            session.save(chat);
            transaction.commit();

           
            responseJson.addProperty("success", true);
        } catch (Exception e) {

            if (transaction != null) {
                transaction.rollback();  // Rollback in case of exception
            }
            e.printStackTrace();
        } finally {
            session.close();  // Always close session in finally block
        }

        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(responseJson));

    }
    
    

}
