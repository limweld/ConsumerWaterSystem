package com.consumerwatersystem.app.Includes;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataHandler {

    Context context;
    public static SQLiteDatabase db;

    private final String DBNAME = "consumer_billing";
    private final int DBVER = 1;

    //PAYMENT TABLE
    private final String PAYMENT_TABLE = "payment";
    private final String PAYMENT_ID = "id";
    private final String PAYMENT_DATE = "date";
    private final String PAYMENT_TYPE = "type";
    private final String PAYMENT_TOTAL = "total";
    private final String PAYMENT_AMOUNTPAID = "amountpaid";

    //READING TABLE
    private final String READING_TABLE = "reading";
    private final String READING_ID = "id";
    private final String READING_CONSUMED = "consume";
    private final String READING_AMOUNT = "amount";
    private final String READING_STATUS = "status";
    private final String READING_TRANSLATE = "transdate";

    //USER TABLE
    private final String USER_TABLE = "user";
    private final String USER_ID = "employee_id";
    private final String USER_PASSWORD = "user_password";
    private final String USER_FIRSTNAME = "firstname";
    private final String USER_LASTNAME = "lastname";
    private final String USER_LOGIN_STATUS = "status";

    //API TABLE
    private final String API_TABLE = "api";
    private final String API_ID = "id";
    private final String API_IP = "ip";

    public DataHandler(){

    }

    public DataHandler(Context context)
    {
        this.context = context;
        CustomHelper helper = new CustomHelper(context);
        this.db = helper.getWritableDatabase();

    }

    private class CustomHelper extends SQLiteOpenHelper {

        public CustomHelper(Context context) {
            super(context, DBNAME, null, DBVER);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                String reading_table = "create table " + READING_TABLE +
                        " ("
                        + READING_ID + " text primary key not null,"
                        + READING_CONSUMED + " TEXT,"
                        + READING_AMOUNT + " TEXT,"
                        + READING_STATUS + " TEXT,"
                        + READING_TRANSLATE + " TEXT "
                        + ");";
                db.execSQL(reading_table);

                String payment_table = "create table " + PAYMENT_TABLE +
                        " ("
                        + PAYMENT_ID + " text primary key not null,"
                        + PAYMENT_DATE + " TEXT,"
                        + PAYMENT_TYPE + " TEXT,"
                        + PAYMENT_TOTAL + " TEXT,"
                        + PAYMENT_AMOUNTPAID + " TEXT "
                        + ");";
                db.execSQL(payment_table);

                String user_table = "create table " + USER_TABLE +
                        " ("
                        + USER_ID + "  text primary key not null,"
                        + USER_PASSWORD + " TEXT,"
                        + USER_FIRSTNAME + " TEXT,"
                        + USER_LASTNAME + " TEXT,"
                        + USER_LOGIN_STATUS + " TEXT "
                        + ");";
                db.execSQL(user_table);

                String api_table = "create table " + API_TABLE +
                        " ("
                        + API_ID + " text primary key not null,"
                        + API_IP + " TEXT "
                        + ");";
                db.execSQL(api_table);

                String api_insert_default_table = "INSERT INTO api (id,ip) VALUES ('1' , '192.168.254.200:8080')";
                db.execSQL(api_insert_default_table);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {

                db.execSQL("DROP TABLE IF EXISTS " + READING_TABLE);

                db.execSQL("DROP TABLE IF EXISTS " + PAYMENT_TABLE);

                db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);

                db.execSQL("DROP TABLE IF EXISTS " + API_TABLE);

                onCreate(db);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
