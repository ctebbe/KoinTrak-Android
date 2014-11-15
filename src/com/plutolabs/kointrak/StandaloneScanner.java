package com.plutolabs.kointrak;

import com.plutolabs.kointrak.qr.*;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
/**
 * Created by chipwasson on 11/15/14.
 */
public class StandaloneScanner {
    private Activity callingActivity;
    IntentIntegrator scanIntegrator;

    public StandaloneScanner(Activity callingActivity) {
        this.callingActivity = callingActivity;
    }

    public void scan(){
        scanIntegrator = new IntentIntegrator(callingActivity);
        scanIntegrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,intent);
        if(scanningResult != null){
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            Toast toast = Toast.makeText(callingActivity.getApplicationContext(),"Scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            Toast toast = Toast.makeText(callingActivity.getApplicationContext(),"No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
