package com.consumerwatersystem.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.consumerwatersystem.app.Includes.Api;
import com.consumerwatersystem.app.Includes.Config;
import com.consumerwatersystem.app.Includes.Payment;
import com.consumerwatersystem.app.Includes.Reading;
import com.consumerwatersystem.app.Includes.User;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import static com.consumerwatersystem.app.Includes.Config.getUrl;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TableLayout data_table;

    TableRow row;
    TextView t1,t2,t3,t4,t5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        data_table=(TableLayout)findViewById(R.id.data_table);

        readingTable();
        //paymentTable();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Api api = Api.getApi("1");
        User user_active = User.getUser_Status("1");

        Config config = new Config();
        config.setUrl_User_data(api.getIp());

        if(id == R.id.nav_billing){
            data_table.removeAllViews();
            config.setUrl_Payment_data(api.getIp());

            Get_Payment get_payment = new Get_Payment(MainActivity.this);
            get_payment.execute(config.getUrl_Payment_data()+"?consumerid="+user_active.getUser_id());

        }else if(id == R.id.nav_payment){
            data_table.removeAllViews();
            config.setUrl_Reading_data(api.getIp());

            Get_Billing get_billing = new Get_Billing(MainActivity.this);
            get_billing.execute(config.getUrl_Reading_data()+"?consumerid="+user_active.getUser_id());

       }else if (id == R.id.nav_logout){
           User user = new User();
           user.update_User_Status();

           Intent intent = new Intent(MainActivity.this, LoginActivity.class);
           startActivity(intent);
           finish();
       }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void readingTable() {

        setTitle("METER BILLING");

        addTableRows(
            "ID",
            "CONSUMED",
            "AMOUNT",
            "STATUS",
            "DATE"
        );


        Reading reading = new Reading();

        ArrayList<ArrayList<Object>> data = reading.get_Billing_Rows();

        for (int current = 0; current < reading.get_Billing_Rows().size(); current++) {

            ArrayList<Object> row = data.get(current);

            addTableRows(
                row.get(0).toString(), //ID
                row.get(1).toString(), //CONSUMED
                row.get(2).toString(), //AMOUNT
                row.get(3).toString(), //STATUS
                row.get(4).toString()  //DATE
            );
        }
    }

    public void paymentTable() {

        setTitle("PAYMENT");

        addTableRows(
            "ID",
            "TYPE",
            "TOTAL",
            "PAID",
            "DATE"
        );


        Payment payment = new Payment();

        ArrayList<ArrayList<Object>> data = payment.get_Payment_Rows();

        for (int current = 0; current < payment.get_Payment_Rows().size(); current++) {

            ArrayList<Object> row = data.get(current);

            addTableRows(
                row.get(0).toString(), //ID
                row.get(2).toString(), //TYPE
                row.get(3).toString(), //TOTAL
                row.get(4).toString(), //PAID
                row.get(1).toString()  //DATE
            );
        }
    }

    public void addTableRows (String s1, String s2, String s3, String s4, String s5){

        int dip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                (float) 1, getResources().getDisplayMetrics());

        row = new TableRow(this);

        t1 = new TextView(this);
        t2 = new TextView(this);
        t3 = new TextView(this);
        t4 = new TextView(this);
        t5 = new TextView(this);

        t1.setText(s1);
        t2.setText(s2);
        t3.setText(s3);
        t4.setText(s4);
        t5.setText(s5);

        //t1.setTypeface(null, 1);
        //t2.setTypeface(null, 1);
        //t3.setTypeface(null, 1);
        //t4.setTypeface(null, 1);
        //t5.setTypeface(null, 1);

        t1.setWidth(40 * dip);
        t2.setWidth(100 * dip);
        t3.setWidth(100 * dip);
        t4.setWidth(100 * dip);
        t5.setWidth(120 * dip);

        row.addView(t1);
        row.addView(t2);
        row.addView(t3);
        row.addView(t4);
        row.addView(t5);

        data_table.addView(row);
    }

    public class Get_Billing  extends AsyncTask<String, Void, String> {

        private ProgressDialog dialog;

        public Get_Billing(MainActivity activity){ dialog = new ProgressDialog(activity);}

        @Override
        protected String doInBackground(String... urls) {
            return getUrl(urls[0]);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Synch ...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            try {

                JSONArray rows = new JSONArray(result);

                for(int i = 0; i < rows.length(); i++) {

                    String id =  rows.getJSONObject(i).getString("id");
                    String consume =  rows.getJSONObject(i).getString("consume");
                    String amount =  rows.getJSONObject(i).getString("amount");
                    String status =  rows.getJSONObject(i).getString("status");
                    String transdate =  rows.getJSONObject(i).getString("transdate");

                    Reading reading = new Reading(id,consume,amount,status,transdate);
                    reading.add_Reading();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (dialog.isShowing()) {
                dialog.dismiss();

                paymentTable();
            }
        }

        @Override
        protected void onCancelled() {
        }
    }

    public class Get_Payment  extends AsyncTask<String, Void, String> {

        private ProgressDialog dialog;

        public Get_Payment(MainActivity activity){ dialog = new ProgressDialog(activity);}

        @Override
        protected String doInBackground(String... urls) {
            return getUrl(urls[0]);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Synch ...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            try {

                JSONArray rows = new JSONArray(result);

                for(int i = 0; i < rows.length(); i++) {

                    String id  =  rows.getJSONObject(i).getString("id");
                    String date  =  rows.getJSONObject(i).getString("date");
                    String type =  rows.getJSONObject(i).getString("type");
                    String total =  rows.getJSONObject(i).getString("total");
                    String amountpaid =  rows.getJSONObject(i).getString("amountpaid");

                    Payment payment = new Payment(id,date,type,total,amountpaid);
                    payment.add_Payment();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (dialog.isShowing()) {
                dialog.dismiss();

                readingTable();
            }
        }

        @Override
        protected void onCancelled() {
        }
    }

}
