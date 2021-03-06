package com.parag.localboundservice;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Intent serviceIntent;
    Button btnStartService,btnStopService,btnBindService,btnUnbindService;
    TextView textViewNumber,textViewBindStatus;
    MyService myService;
    boolean isServiceBound;
    ServiceConnection serviceConnection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serviceIntent = new Intent(this, MyService.class);

        btnStartService = (Button)findViewById(R.id.btn_start_service);
        btnStopService = (Button)findViewById(R.id.btn_stop_service);
        btnBindService = (Button)findViewById(R.id.btn_bind_service);
        btnUnbindService = (Button)findViewById(R.id.btn_unbind_service);

        textViewNumber = (TextView)findViewById(R.id.txt_number);
        textViewBindStatus = (TextView)findViewById(R.id.txt_bind_status);

        btnStartService.setOnClickListener(this);
        btnStopService.setOnClickListener(this);
        btnBindService.setOnClickListener(this);
        btnUnbindService.setOnClickListener(this);
        registerReceiver(broadcastReceiver,new IntentFilter("broadcast"));
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String num = "Random Number : "+intent.getStringExtra("number");
            textViewNumber.setText(num);
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_start_service:startService(serviceIntent);break;
            case R.id.btn_stop_service:
                stopService(serviceIntent);break;
            case R.id.btn_bind_service:
                bindService();
                textViewBindStatus.setText(getString(R.string.bound_status));
                break;
            case R.id.btn_unbind_service:
                unbindService();
                textViewBindStatus.setText(getString(R.string.unbound_status));
                break;
            default:Toast.makeText(this, "Something went terribly wrong", Toast.LENGTH_SHORT).show();
        }
    }

    private void bindService()
    {
        if(serviceConnection == null)
        {
            serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    MyService.MyServiceBinder myServiceBinder = (MyService.MyServiceBinder)iBinder;
                    myService = myServiceBinder.getService();
                    isServiceBound = true;
                }

                @Override
                public void onServiceDisconnected(ComponentName componentName) {
                    isServiceBound = false;
                }
            };
        }
        bindService(serviceIntent,serviceConnection,BIND_AUTO_CREATE);
    }

    private void unbindService()
    {
        if(isServiceBound)
        {
            unbindService(serviceConnection);
            isServiceBound = false;
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }
}
