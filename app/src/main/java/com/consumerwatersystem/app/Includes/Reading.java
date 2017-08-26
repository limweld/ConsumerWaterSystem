package com.consumerwatersystem.app.Includes;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import java.util.ArrayList;

import static com.consumerwatersystem.app.Includes.DataHandler.db;

public class Reading {
    //READING TABLE
    private final String READING_TABLE = "reading";
    private final String READING_ID = "id";
    private final String READING_CONSUMED = "consume";
    private final String READING_AMOUNT = "amount";
    private final String READING_STATUS = "status";
    private final String READING_TRANSLATE = "transdate";

    private String id;
    private String consume;
    private String amount;
    private String status;
    private String transdate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConsume() {
        return consume;
    }

    public void setConsume(String consume) {
        this.consume = consume;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransdate() {
        return transdate;
    }

    public void setTransdate(String transdate) {
        this.transdate = transdate;
    }

    public Reading() {
    }

    public Reading(String id, String consume, String amount, String status, String transdate) {
        this.id = id;
        this.consume = consume;
        this.amount = amount;
        this.status = status;
        this.transdate = transdate;
    }

    public void add_Reading() {

        ContentValues values = new ContentValues();

        values.put(READING_ID,getId());
        values.put(READING_CONSUMED,getConsume());
        values.put(READING_AMOUNT,getAmount());
        values.put(READING_STATUS,getStatus());
        values.put(READING_TRANSLATE,getTransdate());

        try{
            db.insert(READING_TABLE, null, values);
        }
        catch(Exception e)
        {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
        }
    }

    public ArrayList<ArrayList<Object>> get_Billing_Rows()
    {
        ArrayList<ArrayList<Object>> dataArrays = new ArrayList<ArrayList<Object>>();
        Cursor cursor;

        try
        {
            cursor = db.query(
                    READING_TABLE,
                    new String[] {
                            READING_ID,
                            READING_CONSUMED,
                            READING_AMOUNT,
                            READING_STATUS,
                            READING_TRANSLATE
                    },
                    null, null, null, null, null
            );

            cursor.moveToFirst();

            if (!cursor.isAfterLast())
            {
                do
                {
                    ArrayList<Object> dataList = new ArrayList<>();

                    dataList.add(cursor.getString(0));
                    dataList.add(cursor.getString(1));
                    dataList.add(cursor.getString(2));
                    dataList.add(cursor.getString(3));
                    dataList.add(cursor.getString(4));

                    dataArrays.add(dataList);
                }

                while (cursor.moveToNext());
            }
        }
        catch (SQLException e)
        {
            Log.e("DB Error", e.toString());
            e.printStackTrace();
        }

        return dataArrays;
    }
}
