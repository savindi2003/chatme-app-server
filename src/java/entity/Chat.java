/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "chat")
public class Chat implements Serializable{
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "from_user_id")
    private User from_user;

    @ManyToOne
    @JoinColumn(name = "to_user_id")
    private User to_user;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "date_time", nullable = false)
    private Date date_time;
    
    @Column(name = "chat_type", nullable = false)
    private int type; 
    
    @Column(name = "chat_action", nullable = false)
    private int chat_action;

    @ManyToOne
    @JoinColumn(name = "chat_status_id")
    private Chat_Status chat_Status;

    public Chat(){} 

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the from_user
     */
    public User getFrom_user() {
        return from_user;
    }

    /**
     * @param from_user the from_user to set
     */
    public void setFrom_user(User from_user) {
        this.from_user = from_user;
    }

    /**
     * @return the to_user
     */
    public User getTo_user() {
        return to_user;
    }

    /**
     * @param to_user the to_user to set
     */
    public void setTo_user(User to_user) {
        this.to_user = to_user;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the date_time
     */
    public Date getDate_time() {
        return date_time;
    }

    /**
     * @param date_time the date_time to set
     */
    public void setDate_time(Date date_time) {
        this.date_time = date_time;
    }

    /**
     * @return the chat_Status
     */
    public Chat_Status getChat_Status() {
        return chat_Status;
    }

    /**
     * @param chat_Status the chat_Status to set
     */
    public void setChat_Status(Chat_Status chat_Status) {
        this.chat_Status = chat_Status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getChat_action() {
        return chat_action;
    }

    public void setChat_action(int chat_action) {
        this.chat_action = chat_action;
    }

    
}
