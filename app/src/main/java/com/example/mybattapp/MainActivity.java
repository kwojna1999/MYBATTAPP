package com.example.mybattapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView batteryInfos;
    ProgressBar poziomN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        batteryInfos = (TextView) findViewById(R.id.textView2);
        poziomN = (ProgressBar) findViewById(R.id.progressBar);
        //loadBatteryInfo();
    }
    private void loadBatteryInfo(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);

        registerReceiver(batteryInfoReceiver, intentFilter);
    }
    protected void onResume() {
        super.onResume();
        loadBatteryInfo();
    }
    private void updateBatteryData(Intent intent){
        boolean present = intent.getBooleanExtra(BatteryManager.EXTRA_PRESENT, false);

        if (present) {
            StringBuilder batteryInfo = new StringBuilder();
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            batteryInfo.append("Poziom naładowania : " + level).append("\n");
            poziomN.setProgress(level);
            int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH,-1);
            batteryInfo.append("Stan : " + health).append("\n");
            int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
            batteryInfo.append("Źródło zasilania : " + plugged).append("\n");
            batteryInfo.append("Dostępna? : Tak").append("\n");
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
            batteryInfo.append("Poziom : " + scale).append("\n");
            //if(level != -1 && scale != -1){
            ///    int batteryPct = (int) ((level/(float)scale)*100f);
            //    batteryInfo.append("Battery Pct : " + batteryPct).append("\n");
            //}


            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0);
            batteryInfo.append("Stan : " + status).append("\n");
            if (intent.getExtras() != null){
                String technology = intent.getExtras().getString(BatteryManager.EXTRA_TECHNOLOGY);
                batteryInfo.append("Technologia : " + technology).append("\n");
            }

            int temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
            if (temperature > 0){
                batteryInfo.append("Temperatura : " + ((float)temperature / 10f)).append("*C\n");
            }
            if (plugged == 0){
                batteryInfo.append("Napięcie : 0V").append("\n");
            } else {
                int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
                batteryInfo.append("Napięcie : " + voltage + "mV").append("\n");
            }
            batteryInfos.setText(batteryInfo.toString());
        } else {
            Toast.makeText(MainActivity.this, "No Battery Present", Toast.LENGTH_SHORT).show();
        }

    }


    private BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateBatteryData(intent);
        }
    };
}