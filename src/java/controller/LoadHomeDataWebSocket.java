package controller;

//
//import com.google.gson.Gson;
//import com.google.gson.JsonArray;
//import com.google.gson.JsonObject;
//import entity.Chat;
//import entity.User;
//import entity.User_Status;
//import java.io.File;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//import org.hibernate.SessionFactory;
//import org.hibernate.cfg.Configuration;
//
//import javax.websocket.OnClose;
//import javax.websocket.OnMessage;
//import javax.websocket.OnOpen;
//import javax.websocket.Session;
//import javax.websocket.server.ServerEndpoint;
//import model.HibernateUtil;
//import org.hibernate.Criteria;
//import org.hibernate.criterion.Order;
//import org.hibernate.criterion.Restrictions;
//
//@ServerEndpoint("/LoadHomeDataWebSocket")
//public class LoadHomeDataWebSocket {
//
//    private static SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
//    private org.hibernate.Session hibernateSession;
//
//    @OnOpen
//    public void onOpen(Session session) {
//        hibernateSession = HibernateUtil.getSessionFactory().openSession();
//        System.out.println("WebSocket connection opened.");
//    }
//
//    @OnMessage
//    public void onMessage(String message, Session session) {
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
//            String userId = message;
//
//            User user = (User) hibernateSession.get(User.class, Integer.parseInt(userId));
//
//            User_Status user_status = (User_Status) hibernateSession.get(User_Status.class, 1);
//
//            //update user status
//            user.setUser_Status(user_status);
//            hibernateSession.update(user);
//
//            //get other users
//            Criteria criteria1 = hibernateSession.createCriteria(User.class);
//            criteria1.add(Restrictions.ne("id", user.getId()));
//            List<User> otherUserlist = criteria1.list();
//
//            JsonArray jsonCharArray = new JsonArray();
//
//            for (User otherUser : otherUserlist) {
//
//                //get chat list
//                Criteria criteria2 = hibernateSession.createCriteria(Chat.class);
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
//                    //check avatar image
//                    String serverPath = ("");
//                    String otherUserAvatarImagePath = serverPath + File.separator + "AvaterImages" + File.separator + otherUser.getMobile() + ".png";
//
//                    File otherUserAvatarImageFile = new File(otherUserAvatarImagePath);
//                    if (otherUserAvatarImageFile.exists()) {
//                        //image found
//                        jsonChatItem.addProperty("avatar_image_found", true);
//
//                    } else {
//                        //image not found
//                        jsonChatItem.addProperty("avatar_image_found", false);
//                        jsonChatItem.addProperty("other_user_avatar_letters", otherUser.getFirst_name().charAt(0) + "" + otherUser.getLast_name().charAt(0));
//
//                    }
//
//                    if (dbChatList.get(0).getFrom_user() == user) {
//                        jsonChatItem.addProperty("messageStatus", true);
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
//                    jsonChatItem.addProperty("message", dbChatList.get(0).getMessage());
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
//                    jsonChatItem.addProperty("chat_status_id", dbChatList.get(0).getChat_Status().getId());
//
//                    otherUser.setPassword(null);
//                    jsonCharArray.add(jsonChatItem);
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
//            hibernateSession.beginTransaction().commit();
//            session.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        // Send results back to client
//        try {
//
//          session.getBasicRemote().sendText(responsejson.toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        hibernateSession.getTransaction().commit();
//        System.out.println("Message received and processed: " + message);
//    }
//
//    @OnClose
//    public void onClose(Session session) {
//        if (hibernateSession != null) {
//            hibernateSession.close();
//        }
//        System.out.println("WebSocket connection closed.");
//    }
//}
