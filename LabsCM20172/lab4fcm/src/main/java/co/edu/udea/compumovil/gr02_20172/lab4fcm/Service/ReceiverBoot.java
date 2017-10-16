package co.edu.udea.compumovil.gr02_20172.lab4fcm.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ReceiverBoot extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Se lanza el servicio
        Intent serviceIntent = new Intent(context,ServiceBoot.class);
        context.startActivity(serviceIntent);
        Log.d("TAG","Entre en el receiver boot!!!!!!!!!!!!!!!");

        /*
        Intent i = new Intent(context,MainActivity.class);
        context.startActivity(i);

        Se lanza la actividad
        Intent i = new Intent(context, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        */

    }
}
