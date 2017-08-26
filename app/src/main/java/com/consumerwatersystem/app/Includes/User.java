package com.consumerwatersystem.app.Includes;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import static com.consumerwatersystem.app.Includes.DataHandler.db;


public class User {
    private final String USER_TABLE = "user";
    private final String USER_ID = "employee_id";
    private final String USER_PASSWORD = "user_password";
    private final String USER_FIRSTNAME = "firstname";
    private final String USER_LASTNAME = "lastname";
    private final String USER_LOGIN_STATUS = "status";

    private String user_id;
    private String password;
    private String firstname;
    private String lastname;
    private String status;

    public User() {
    }

    public User(String status) {
        this.status = status;
    }

    public User(String user_id, String password) {
        this.user_id = user_id;
        this.password = password;
    }

    public User(String user_id, String password, String firstname, String lastname, String status) {
        this.user_id = user_id;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.status = status;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void add_User(){
        ContentValues values = new ContentValues();

        values.put(USER_ID, getUser_id());
        values.put(USER_PASSWORD,getPassword());

        try{
            db.insert(USER_TABLE, null, values);
        }
        catch(Exception e)
        {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
        }
    }


    public void add_User_Info(){
        ContentValues values = new ContentValues();

        values.put(USER_ID, getUser_id());
        values.put(USER_PASSWORD, getPassword());
        values.put(USER_FIRSTNAME, getFirstname());
        values.put(USER_LASTNAME, getLastname());
        values.put(USER_LOGIN_STATUS, getStatus());

        try{
            db.insert(USER_TABLE, null, values);
        }
        catch(Exception e)
        {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
        }
    }

    public void update_User_Status() {
        ContentValues values = new ContentValues();
        values.put(USER_LOGIN_STATUS, "0");

        try {
            db.update(USER_TABLE, values, USER_LOGIN_STATUS + "='1'", null);
        } catch (Exception e) {
            Log.e("DB Error", e.toString());
            e.printStackTrace();
        }
    }

    public void update_Current_User_Status() {
        ContentValues values = new ContentValues();
        values.put(USER_LOGIN_STATUS, "1");

        try {
            db.update(USER_TABLE, values,
                    USER_ID + "='"+ getUser_id() +"' AND "+
                    USER_PASSWORD +"='" + getPassword() + "'",
                    null);
        } catch (Exception e) {
            Log.e("DB Error", e.toString());
            e.printStackTrace();
        }
    }

    public static User getUser(String user_id, String password)
    {
        User user = null;
        Cursor cursor;

        try
        {
            cursor = db.query
                    (
                            "user",
                            new String[] { "employee_id", "user_password" },
                            "employee_id='" + user_id+ "' AND "+
                                    "user_password='"+ password+ "'",
                            null, null, null, null, null
                    );

            cursor.moveToFirst();

            if (!cursor.isAfterLast())
            {
                do
                {
                    user = new User();
                    user.setUser_id(cursor.getString(0));
                    user.setPassword(cursor.getString(1));

                }
                while (cursor.moveToNext());
            }
            cursor.close();
        }
        catch (SQLException e)
        {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
        }
        return user;
    }


    public static User getUser_Status(String status)
    {
        User user = null;
        Cursor cursor;

        try
        {
            cursor = db.query
            (
                    "user",
                    new String[] { "employee_id", "user_password" ,"firstname", "lastname"},
                    "status='" + status+ "'",
                    null, null, null, null, null
            );

            cursor.moveToFirst();

            if (!cursor.isAfterLast())
            {
                do
                {
                    user = new User();
                    user.setUser_id(cursor.getString(0));
                    user.setPassword(cursor.getString(1));
                    user.setFirstname(cursor.getString(2));
                    user.setLastname(cursor.getString(3));
                }
                while (cursor.moveToNext());
            }
            cursor.close();
        }
        catch (SQLException e)
        {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id='" + user_id + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
