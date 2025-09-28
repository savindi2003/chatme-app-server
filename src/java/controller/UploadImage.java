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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import model.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Savindi
 */
@MultipartConfig
@WebServlet(name = "UploadImage", urlPatterns = {"/UploadImage"})
public class UploadImage extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject responseJson = new JsonObject();
        responseJson.addProperty("success", false);

        // Get parameters
        String userMobile = req.getParameter("userMob");
        Part profileImage = req.getPart("profileImg");

        if (userMobile == null || userMobile.isEmpty()) {
            responseJson.addProperty("message", "Please sign up first");
        } else {

            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = null;

            try {
                // 🔍 Step 1: Find user by mobile using HQL
                Query query = session.createQuery("FROM User WHERE mobile = :mob");
                query.setParameter("mob", userMobile);
                User user = (User) query.uniqueResult();

                if (user == null) {
                    responseJson.addProperty("message", "User not found");
                } else {

                    // 📁 Step 2: Save image to /UserImages/<mobile>.png
                    if (profileImage != null) {

                        String serverPath = req.getServletContext().getRealPath("/") + "UserImages";
                        File folder = new File(serverPath);
                        if (!folder.exists()) {
                            folder.mkdirs();
                        }

                        // Step 2: Generate unique file name
                        UUID uuid = UUID.randomUUID();
                        String fileName = userMobile + "_" + uuid + ".png";

                        // Step 3: Save the image
                        String filePath = serverPath + File.separator + fileName;
                        File file = new File(filePath);
                        Files.copy(profileImage.getInputStream(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);

                        // 📝 Step 3: Update user dp
                        tx = session.beginTransaction();
                        user.setDp(fileName);
                        session.update(user);
                        tx.commit();

                        // ✅ Step 4: Response
                        responseJson.addProperty("success", true);
                        responseJson.addProperty("message", "Image uploaded and user updated successfully");
                        responseJson.addProperty("imagepath", fileName);
                    } else {
                        responseJson.addProperty("message", "No image received");
                    }
                }

            } catch (Exception e) {
                if (tx != null) {
                    tx.rollback();
                }
                responseJson.addProperty("message", "Server error: " + e.getMessage());
                e.printStackTrace();
            } finally {
                session.close();
            }
        }

        // 🔚 Final JSON Response
        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(responseJson));
    }
}
