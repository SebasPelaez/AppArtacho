package co.edu.udea.compumovil.gr02_20172.lab3services.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class ServiceBoot extends Service {
    public ServiceBoot() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "Servicio creado", Toast.LENGTH_LONG).show();
        Log.d("SERVICEBOOT", "Servicio creado");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Servicio Destruido", Toast.LENGTH_LONG).show();
        Log.d("SERVICEBOOT", "Servicio Destruido");
    }
}
