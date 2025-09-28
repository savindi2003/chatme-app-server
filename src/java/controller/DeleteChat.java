/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.Chat;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Savindi
 */
@WebServlet(name = "DeleteChat", urlPatterns = {"/DeleteChat"})
public class DeleteChat extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject responseJson = new JsonObject();
        responseJson.addProperty("success", false);
        Session session = HibernateUtil.getSessionFactory().openSession();

        String chat_id = req.getParameter("id");

        int chatid = Integer.parseInt(chat_id);

        System.out.println(chat_id);

        Criteria criteria1 = session.createCriteria(Chat.class);
        criteria1.add(Restrictions.eq("id", chatid));

        Chat chatItem = (Chat) criteria1.uniqueResult();

        if (chatItem.getChat_action() == 0) {
            chatItem.setChat_action(1);
            

            try {
                session.update(chatItem);
                session.beginTransaction().commit();
                
                session.close();
                
                responseJson.addProperty("success", true);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else{
            responseJson.addProperty("success", false);
        }
        
        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(responseJson));

    }

}
