package com.consumerwatersystem.app.Includes;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import java.util.ArrayList;

import static com.consumerwatersystem.app.Includes.DataHandler.db;

public class Payment {
    //PAYMENT TABLE
    private final String PAYMENT_TABLE = "payment";
    private final String PAYMENT_ID = "id";
    private final String PAYMENT_DATE = "date";
    private final String PAYMENT_TYPE = "type";
    private final String PAYMENT_TOTAL = "total";
    private final String PAYMENT_AMOUNTPAID = "amountpaid";

    private String id;
    private String date;
    private String type;
    private String total;
    private String amountpaid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getAmountpaid() {
        return amountpaid;
    }

    public void setAmountpaid(String amountpaid) {
        this.amountpaid = amountpaid;
    }


    public Payment() {
    }

    public Payment(String id, String date, String type, String total, String amountpaid) {
        this.id = id;
        this.date = date;
        this.type = type;
        this.total = total;
        this.amountpaid = amountpaid;
    }

    public void add_Payment() {

        ContentValues values = new ContentValues();

        values.put(PAYMENT_ID,getId());
        values.put(PAYMENT_DATE,getDate());
        values.put(PAYMENT_TYPE,getType());
        values.put(PAYMENT_TOTAL,getTotal());
        values.put(PAYMENT_AMOUNTPAID,getAmountpaid());

        try{
            db.insert(PAYMENT_TABLE, null, values);
        }
        catch(Exception e)
        {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
        }
    }

    public ArrayList<ArrayList<Object>> get_Payment_Rows()
    {
        ArrayList<ArrayList<Object>> dataArrays = new ArrayList<ArrayList<Object>>();
        Cursor cursor;

        try
        {

            cursor = db.query(
                    PAYMENT_TABLE,
                    new String[] {
                            PAYMENT_ID,
                            PAYMENT_DATE,
                            PAYMENT_TYPE,
                            PAYMENT_TOTAL,
                            PAYMENT_AMOUNTPAID
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
