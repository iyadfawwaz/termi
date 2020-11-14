package sy.iyad.sybox.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.messaging.FirebaseMessaging;
import org.json.JSONException;
import org.json.JSONObject;
import sy.iyad.mikrotik.MikrotikServer;
import sy.iyad.mikrotik.Models.ConnectionEventListener;
import sy.iyad.mikrotik.Utils.Api;
import sy.iyad.sybox.R;
import sy.iyad.sybox.ServerInformations;
import sy.iyad.sybox.Token;
import sy.iyad.sybox.Utils.ServerInfo;

import static sy.iyad.sybox.ServerInformations.ADMIN;
import static sy.iyad.sybox.ServerInformations.IP;
import static sy.iyad.sybox.ServerInformations.PASSWORD;


public class LoginActivity extends AppCompatActivity {


    EditText admin,ip,pass;
    Button button;
    TextView textView;
    FloatingActionButton actionButton,registerToken;

    @SuppressLint("SetTextI18n")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_login);

        button =  findViewById(R.id.connect);
        ip =  findViewById(R.id.ip);
        admin =  findViewById(R.id.admin);
        pass =  findViewById(R.id.password);
        textView =  findViewById(R.id.textView);
        actionButton = findViewById(R.id.floatingActionButton);
        registerToken = findViewById(R.id.regis);

        laodInfo();

        registerToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerId();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                String ips = ip.getText().toString();
                String admins = admin.getText().toString();
                String passs = pass.getText().toString();
                IP = ips;
                ADMIN = admins;
                PASSWORD = passs;

                doLogin(ips, admins, passs);

            }
        });

        button.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View view) {

                registerId();
                return true;

            }
        });

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ip.setText(IP);
                admin.setText(ADMIN);
                pass.setText("995x");

            }
        });
    }

    private void init(){

    }

    private void laodInfo() {
        ip.setText(ServerInformations.IP);
        admin.setText(ServerInformations.ADMIN);
        pass.setText(ServerInformations.PASSWORD);
    }

    private void doLogin(String aips, String aadmins, String apasss) {
        MikrotikServer.connect(aips, aadmins, apasss)
                .addConnectionEventListener(new ConnectionEventListener() {
                    public void onConnectionSuccess(Api api) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }

                    public void onConnectionFailed(Exception exp) {
                        textView.setText(exp.getMessage());
                    }
                });
    }

    private void registerId() {

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {

            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    ServerInfo.tokenReference.push().setValue(new Token( task.getResult()));
                    ServerInfo.isRegistered = true;
                }
            }
        });

    }
    private void loadSetupInfo(@NonNull String ip,@NonNull String username,@NonNull String password){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ip",ip);
            jsonObject.put("username",username);
            jsonObject.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (MikrotikServer.getApi() != null && MikrotikServer.isConnected()){
            startActivity(new Intent(this,MainActivity.class));
        }

    }
}