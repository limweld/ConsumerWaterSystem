package com.consumerwatersystem.app;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.consumerwatersystem.app.Includes.Api;
import com.consumerwatersystem.app.Includes.Config;
import com.consumerwatersystem.app.Includes.DataHandler;
import com.consumerwatersystem.app.Includes.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.consumerwatersystem.app.Includes.Config.getUrl;
import static com.consumerwatersystem.app.R.id.config_ip;

public class LoginActivity extends AppCompatActivity {
    final Context context = this;
    private Button login;
    private EditText userID;
    private EditText password;

    private static DataHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userID = (EditText)findViewById(R.id.userid);
        password = (EditText)findViewById(R.id.password);

        login = (Button) findViewById(R.id.email_sign_in_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                secondary_Open(
                        userID.getText().toString(),
                        password.getText().toString()
                );

            }
        });

        db = new DataHandler(this);

        //Check Active
        if(cheack_status()){
            open_intent_main();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //Toast.makeText(MainActivity.this,"Test", Toast.LENGTH_LONG).show();
            try{
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.prompt_connection, null);

                final EditText config_ip = promptsView.findViewById(R.id.config_ip);
                final Button btn_SaveIp = promptsView.findViewById(R.id.btn_SaveIp);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                // create alert dialog
                final AlertDialog alertDialog = alertDialogBuilder.create();

                try {

                    Api api = Api.getApi("1");
                    config_ip.setText(api.getIp());

                }catch(Exception ex){
                    Toast.makeText(getApplicationContext() , ex.toString()  , Toast.LENGTH_LONG).show();

                }

                alertDialog.show();

                btn_SaveIp.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Api ip_update = new Api("1",config_ip.getText().toString());
                        ip_update.update_Api();

                        Toast.makeText(getApplicationContext() , "IP updated!" , Toast.LENGTH_LONG).show();
                        alertDialog.cancel();

                    }
                });



            }catch (Exception ex){

            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void primary_Open(String username, String password){
        user_api_connect(username, password);
    }

    private void secondary_Open(String username, String password){
        if(check_database(username, password) == true){
            open_intent_main();

            //Current Status
            User user = new User(username,password);
            user.update_Current_User_Status();

            Toast.makeText(getApplicationContext(), "Login Phone Success ! ", Toast.LENGTH_LONG).show();
        }else{
            primary_Open(username, password);
        }
    }

    private  void open_intent_main(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void user_api_connect(String username, String password){
        Api api = Api.getApi("1");

        Config config = new Config();
        config.setUrl_User_data(api.getIp());

        Get_User get_user = new Get_User(LoginActivity.this);
        get_user.execute(config.getUrl_User_data()+"?user="+username+"&pass="+password);
    }

    private boolean check_database(String username, String password){
        try {
            User user = User.getUser(
                    username,
                    password
            );
            if (user.getUser_id() != null) {
                return true;
            }
        }catch(Exception ex){
            return false;
        }
        return false;
    }

    public boolean cheack_status(){
        User user = User.getUser_Status("1");

        if (user != null) {
            return true;
        }

        return false;
    }

    public class Get_User   extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog;

        public Get_User(LoginActivity activity) { dialog = new ProgressDialog(activity);}

        @Override
        protected String doInBackground(String... urls) {
            return getUrl(urls[0]);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Login");
            dialog.show();
        }

        @Override
        protected void onPostExecute(String result) {

            try {
                JSONObject obj = new JSONObject(result);

                String username = (String) obj.get("id");
                String password = (String) obj.get("password");
                String firstname = (String) obj.get("firstname");
                String lastname = (String) obj.get("lastname");

                User user = new User(
                        username,
                        password,
                        firstname,
                        lastname,
                        "1"
                );

                user.add_User_Info();

                Toast.makeText(getApplicationContext(), "Login Success ! ", Toast.LENGTH_LONG).show();

                open_intent_main();
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Unmatch Account or Try Again !", Toast.LENGTH_LONG).show();
            }

            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

        @Override
        protected void onCancelled() {
        }
    }




}
