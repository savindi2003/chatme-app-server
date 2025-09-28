/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.User;
//import entity.User;
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
@WebServlet(name = "SignIn", urlPatterns = {"/SignIn"})
public class SignIn extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject responsejson = new JsonObject();
        responsejson.addProperty("success", false);
        

        JsonObject requestJson = gson.fromJson(req.getReader(), JsonObject.class);
        String mobile = requestJson.get("mobile").getAsString();
        String password = requestJson.get("password").getAsString();
        
        

        if (mobile.isEmpty()) {
            responsejson.addProperty("message", "Please fill Mobile Number");

        } else if (password.isEmpty()) {
            responsejson.addProperty("message", "Please fill Password");
        } else {
        System.out.println(mobile);
        System.out.println(password);
            Session session = HibernateUtil.getSessionFactory().openSession();

            Criteria criteria1 = session.createCriteria(User.class);
            criteria1.add(Restrictions.eq("mobile", mobile));
            criteria1.add(Restrictions.eq("password", password));
            if (!criteria1.list().isEmpty()) {

                User user = (User) criteria1.uniqueResult();

                responsejson.addProperty("success", true);
                responsejson.addProperty("message", "Sign In Success");
                responsejson.add("user", gson.toJsonTree(user));
            } else {
                responsejson.addProperty("message", "Invalid username or Password");
            }

            session.close();

    }
        //resp.setContentType("application/json");
        resp.setHeader("Access-Control-Allow-Origin", "*");
resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
resp.setHeader("Access-Control-Allow-Headers", "Content-Type");

        resp.getWriter().write(gson.toJson(responsejson));
    }

}
