/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.User;
import entity.User_Status;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import model.HibernateUtil;
import model.Validation;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Savindi
 */
@WebServlet(name = "SignUp", urlPatterns = {"/SignUp"})
public class SignUp extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject responsejson = new JsonObject();
        responsejson.addProperty("success", false);

        JsonObject requestJson = gson.fromJson(req.getReader(), JsonObject.class);
        String mobile = requestJson.get("mobile").getAsString();
        String password = requestJson.get("password").getAsString();
        String fname = requestJson.get("firstname").getAsString();
        String lname = requestJson.get("lastname").getAsString();

        if (mobile.isEmpty()) {
            responsejson.addProperty("message", "Please fill the Mobile number");
        } else if (fname.isEmpty()) {
            responsejson.addProperty("message", "Please fill your First name");
        } else if (lname.isEmpty()) {
            responsejson.addProperty("message", "Please fill your Last name");
        } else if (password.isEmpty()) {
            responsejson.addProperty("message", "Please fill the Password");
        } else if (Validation.isMobileNumberValid(mobile)) {
            responsejson.addProperty("message", "Invalid mobile number");
        } else if (Validation.isValidPassword(password)) {
            responsejson.addProperty("message", "Invalid Password");
        } else {
            Session session = HibernateUtil.getSessionFactory().openSession();

            Criteria criteria1 = session.createCriteria(User.class);
            criteria1.add(Restrictions.eq("mobile", mobile));

            if (!criteria1.list().isEmpty()) {
                responsejson.addProperty("message", "Moble number alreay Used");
            } else {
                User user = new User();
                user.setMobile(mobile);
                user.setFirst_name(fname);
                user.setLast_name(lname);
                user.setPassword(password);
                user.setRegistered_date_time(new Date());

                User_Status user_Status = (User_Status) session.get(User_Status.class, 2);
                user.setUser_Status(user_Status);

                session.save(user);
                session.beginTransaction().commit();

                responsejson.addProperty("success", true);
                responsejson.addProperty("message", "Registration Complete!");

            }

            session.close();
        }
        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(responsejson));
    }

}
