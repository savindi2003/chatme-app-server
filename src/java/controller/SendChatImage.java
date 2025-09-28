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
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import model.HibernateUtil;
import org.hibernate.Session;

/**
 *
 * @author Savindi
 */
@MultipartConfig
@WebServlet(name = "SendChatImage", urlPatterns = {"/SendChatImage"})
public class SendChatImage extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject responseJson = new JsonObject();
        responseJson.addProperty("success", false);
        Session session = HibernateUtil.getSessionFactory().openSession();

        String userId = req.getParameter("logged_user_id");
        String otherUserId = req.getParameter("other_user_id");

        Part image1 = req.getPart("chatImg");

        UUID uniqueID = UUID.randomUUID();

        if (image1 != null) {

            String serverPath = req.getServletContext().getRealPath("");
            String chatImagePath = serverPath + File.separator + "chatImages" + File.separator + userId + "_" + otherUserId + "_" + uniqueID + ".png";
            
            String chatImagePath2 = userId + "_" + otherUserId + "_" + uniqueID + ".png";

            File file = new File(chatImagePath);
            Files.copy(image1.getInputStream(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);

            //get user
            User logged_user = (User) session.get(User.class, Integer.parseInt(userId));

            //get other user
            User other_user = (User) session.get(User.class, Integer.parseInt(otherUserId));
            
            
            //save chat
            Chat chat = new Chat();

            Chat_Status chat_status = (Chat_Status) session.get(Chat_Status.class, 2);
            chat.setChat_Status(chat_status);

            chat.setDate_time(new Date());
            chat.setFrom_user(logged_user);
            chat.setTo_user(other_user);
            chat.setMessage(chatImagePath2);
            chat.setType(1);
            chat.setChat_action(0);
            try {
            session.save(chat);

            
                session.beginTransaction().commit();
                session.close();
                responseJson.addProperty("success", true);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        
        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(responseJson));

    }

}
