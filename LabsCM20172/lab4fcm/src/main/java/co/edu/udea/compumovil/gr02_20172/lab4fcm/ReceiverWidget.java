package co.edu.udea.compumovil.gr02_20172.lab4fcm;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import co.edu.udea.compumovil.gr02_20172.lab4fcm.entities.Apartament;
public class ReceiverWidget extends AppWidgetProvider {

    private DatabaseReference databaseReference;//REFERENCIAS A LA BASE DE DATOS DE FIREBASE
    private DatabaseReference apartamentosReference;//REFERENCIA A UN HIJO EN LA BASE DE DATOS.
    private List<Apartament> apartamentoList;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

        databaseReference = FirebaseDatabase.getInstance().getReference();//INSTANCIA LA BASE DE DATOS DE FIREBASE
        apartamentosReference = databaseReference.child("Apartamentos");//SE PARA EN EL HIJO USUARIO
        apartamentoList = new ArrayList<>();

        apartamentosReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Apartament a = dataSnapshot.getValue(Apartament.class);
                apartamentoList.add(a);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Get all ids
        // Get all ids
        ComponentName thisWidget = new ComponentName(context,
                ReceiverWidget.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        for (int widgetId : allWidgetIds) {

            Apartament currentApto = apartamentoList.get(apartamentoList.size()-1);

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

    private void prepareApartamentos() {

    }

}
