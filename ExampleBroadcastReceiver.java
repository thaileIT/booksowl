package com.example.bookproject.model;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.ProgressBar;
import android.widget.Toast;

public class ExampleBroadcastReceiver extends BroadcastReceiver {
    ProgressDialog progressDialog;

    @Override
    public void onReceive(Context context, Intent intent) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Please wait ... connect to internet");
        progressDialog.setCanceledOnTouchOutside(false);
        if(ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
            boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY,false);
            if(noConnectivity){
                progressDialog.setMessage("Internet ... ");
                progressDialog.show();
            }
            else{
                progressDialog.dismiss();
            }
        }
    }
}
