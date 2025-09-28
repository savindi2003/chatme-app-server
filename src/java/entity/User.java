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
@Table(name = "user")
public class User implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "mobile", length = 10, nullable = false)
    private String mobile;

    @Column(name = "fname", length = 45, nullable = false)
    private String first_name;

    @Column(name = "lname", length = 45, nullable = false)
    private String last_name;
    
    @Column(name = "password", length = 20, nullable = false)
    private String password;
    
    @Column(name = "registerd_date_time", nullable = false)
    private Date registered_date_time;

    @ManyToOne
    @JoinColumn(name = "user_status_id")
    private User_Status user_Status;
    
    @Column(name = "mobile_visibility")
    private int mobile_visibility;
    
    @Column(name = "online_visibility")
    private int online_visibility;
    
    @Column(name = "dp_visibility")
    private int dp_visibility;
    
    @Column(name = "dp")
    private String dp;
    
    

    public User(){}
    
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
     * @return the mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile the mobile to set
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * @return the first_name
     */
    public String getFirst_name() {
        return first_name;
    }

    /**
     * @param first_name the first_name to set
     */
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    /**
     * @return the last_name
     */
    public String getLast_name() {
        return last_name;
    }

    /**
     * @param last_name the last_name to set
     */
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the registered_date_time
     */
    public Date getRegistered_date_time() {
        return registered_date_time;
    }

    /**
     * @param registered_date_time the registered_date_time to set
     */
    public void setRegistered_date_time(Date registered_date_time) {
        this.registered_date_time = registered_date_time;
    }

    /**
     * @return the user_Status
     */
    public User_Status getUser_Status() {
        return user_Status;
    }

    /**
     * @param user_Status the user_Status to set
     */
    public void setUser_Status(User_Status user_Status) {
        this.user_Status = user_Status;
    }

    public int getMobile_visibility() {
        return mobile_visibility;
    }

    public void setMobile_visibility(int mobile_visibility) {
        this.mobile_visibility = mobile_visibility;
    }

    public int getOnline_visibility() {
        return online_visibility;
    }

    public void setOnline_visibility(int online_visibility) {
        this.online_visibility = online_visibility;
    }

    public int getDp_visibility() {
        return dp_visibility;
    }

    public void setDp_visibility(int dp_visibility) {
        this.dp_visibility = dp_visibility;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

}
