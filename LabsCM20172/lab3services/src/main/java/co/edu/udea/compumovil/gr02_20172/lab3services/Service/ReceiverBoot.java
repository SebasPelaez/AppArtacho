package co.edu.udea.compumovil.gr02_20172.lab3services.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ReceiverBoot extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // LANZAR SERVICIO
        Intent serviceIntent = new Intent();
        serviceIntent.setAction("co.edu.udea.compumovil.gr02_2017.lab3services.Service.ServiceBoot");
        context.startService(serviceIntent);
    }
}
