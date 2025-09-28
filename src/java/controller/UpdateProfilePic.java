/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 *
 * @author Savindi
 */
@MultipartConfig
@WebServlet(name = "UpdateProfilePic", urlPatterns = {"/UpdateProfilePic"})
public class UpdateProfilePic extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    
        Gson gson =new Gson();
        JsonObject responsejson = new JsonObject();
        responsejson.addProperty("success", false);
        
        String userMobile = req.getParameter("userMob");
       Part ProfileImage = req.getPart("profileImg");
        

            
        if(ProfileImage != null){
        String serverPath = req.getServletContext().getRealPath("");
        String ImagePath = serverPath + File.separator + "UserImages" + File.separator + userMobile + ".png";
        System.out.println(ImagePath);
        File file = new File(ImagePath);
        Files.copy(ProfileImage.getInputStream(), file.toPath(),StandardCopyOption.REPLACE_EXISTING);
        }
        
        responsejson.addProperty("success", true);
        responsejson.addProperty("message", "Registration Complete!");
            
        
        
        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(responsejson));
    
    }

    

}
