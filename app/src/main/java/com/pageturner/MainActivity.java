package com.pageturner;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Toast;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.ChannelExec;

public class MainActivity extends Activity {

    private static final String HOST = "192.168.68.78";
    private static final String USER = "root";
    private static final String PASSWORD = "tVuO1w1qr0";
    private static final int PORT = 22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        findViewById(R.id.btnPrev).setOnClickListener(v -> runCommand("/usr/local/bin/prevpage.sh"));
        findViewById(R.id.btnNext).setOnClickListener(v -> runCommand("/usr/local/bin/nextpage.sh"));
    }

    private void runCommand(String command) {
        new Thread(() -> {
            try {
                JSch jsch = new JSch();
                Session session = jsch.getSession(USER, HOST, PORT);
                session.setPassword(PASSWORD);
                session.setConfig("StrictHostKeyChecking", "no");
                session.connect(5000);

                ChannelExec channel = (ChannelExec) session.openChannel("exec");
                channel.setCommand(command);
                channel.connect();
                Thread.sleep(500);
                channel.disconnect();
                session.disconnect();

            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}