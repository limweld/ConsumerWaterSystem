package com.consumerwatersystem.app.Includes;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import static com.consumerwatersystem.app.Includes.DataHandler.db;

public class Api {

    private final String API_TABLE = "api";
    private final String API_ID = "id";
    private final String API_IP = "ip";

    private String id;
    private String ip;

    public Api() {
    }

    public Api(String id) {
        this.id = id;
    }

    public Api(String id, String ip) {
        this.id = id;
        this.ip = ip;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void update_Api(){
        ContentValues values = new ContentValues();

        values.put(API_IP, getIp());
        try {
            db.update(API_TABLE, values, API_ID+"='" + getId()+ "'", null);
        }
        catch (Exception e)
        {
            Log.e("DB Error", e.toString());
            e.printStackTrace();
        }
    }

    public static Api getApi(String id)
    {
        Api api = null;
        Cursor cursor;

        try
        {
            cursor = db.query
                    (
                        "api",
                        new String[] { "id", "ip"},
                        "id='" + id+ "'",
                        null, null, null, null, null
                    );

            cursor.moveToFirst();

            if (!cursor.isAfterLast())
            {
                do
                {
                    api = new Api();
                    api.setId(cursor.getString(0));
                    api.setIp(cursor.getString(1));
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
        return api;
    }


}
