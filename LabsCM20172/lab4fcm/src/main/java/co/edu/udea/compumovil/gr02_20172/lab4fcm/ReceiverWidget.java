package co.edu.udea.compumovil.gr02_20172.lab4fcm;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.io.IOException;
import java.util.List;

import co.edu.udea.compumovil.gr02_20172.lab4fcm.Interface.RestClient;
import co.edu.udea.compumovil.gr02_20172.lab4fcm.entities.Apartament;
import retrofit2.Call;

public class ReceiverWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

        // Get all ids
        // Get all ids
        ComponentName thisWidget = new ComponentName(context,
                ReceiverWidget.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        for (int widgetId : allWidgetIds) {

            RestClient restClient = RestClient.retrofit.create(RestClient.class);
            Call<Apartament> call = restClient.getApartament(lastApartment());
            Apartament currentApto = null;
            try {
                currentApto = call.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget_view);

            // Set the text
            remoteViews.setTextViewText(R.id.widget_nombre, currentApto.getName());
            remoteViews.setTextViewText(R.id.widget_tipo, currentApto.getType());
            remoteViews.setTextViewText(R.id.widget_valor, String.valueOf(currentApto.getValue()));
            remoteViews.setTextViewText(R.id.widget_area, String.valueOf(currentApto.getArea()));
            remoteViews.setTextViewText(R.id.widget_descripcion, currentApto.getDescription());


            // Register an onClickListener
            Intent intent = new Intent(context, ReceiverWidget.class);

            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.widget_Photo, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);

        }
    }

    private int lastApartment(){
        int id=0;
        RestClient restClient = RestClient.retrofit.create(RestClient.class);
        Call<List<Apartament>> call = restClient.getApartaments();
        List<Apartament> apartaments;
        try {
            apartaments = call.execute().body();
            id = apartaments.get(apartaments.size()-1).getId();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return id;
    }

}
