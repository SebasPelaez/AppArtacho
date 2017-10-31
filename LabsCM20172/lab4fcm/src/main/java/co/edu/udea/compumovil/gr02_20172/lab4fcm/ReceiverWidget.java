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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import co.edu.udea.compumovil.gr02_20172.lab4fcm.entities.Apartament;
public class ReceiverWidget extends AppWidgetProvider {

    private FirebaseDatabase databaseReference;//REFERENCIAS A LA BASE DE DATOS DE FIREBASE
    private DatabaseReference apartamentosReference;//REFERENCIA A UN HIJO EN LA BASE DE DATOS.
    private List<Apartament> apartamentoList;

    private FirebaseStorage storage ;
    private StorageReference storageRef;

    private RemoteViews views;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        databaseReference = FirebaseDatabase.getInstance();//INSTANCIA LA BASE DE DATOS DE FIREBASE
        apartamentosReference = databaseReference.getReference().child("Apartamentos");//SE PARA EN EL HIJO USUARIO
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        for (int appWidgetId : appWidgetIds) {
            Log.d("onUpdate","onUpdate ciclo");
            updateAppWidget(context, appWidgetManager, appWidgetId);

            // Register an onClickListener
            Intent intent = new Intent(context, ReceiverWidget.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.widget_Photo, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    void updateAppWidget(final Context context, final AppWidgetManager appWidgetManager,
                         final int appWidgetId) {

        //final CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        views = new RemoteViews(context.getPackageName(),R.layout.widget_view);
        apartamentosReference.limitToLast(1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Apartament value = dataSnapshot.getValue(Apartament.class);
                //Picasso.with(context).load(value.getPhoto()).into(photo);
                mostrarDato(value,appWidgetManager,appWidgetId);
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

    }

    public void mostrarDato(Apartament currentApto,AppWidgetManager appWidgetManager,int appWidgetId)
    {
        // Set the text
        views.setTextViewText(R.id.widget_nombre, currentApto.getName());
        views.setTextViewText(R.id.widget_tipo, currentApto.getType());
        views.setTextViewText(R.id.widget_valor, String.valueOf(currentApto.getValue()));
        views.setTextViewText(R.id.widget_area, String.valueOf(currentApto.getArea()));
        views.setTextViewText(R.id.widget_descripcion, currentApto.getDescription());
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onEnabled(Context context) {
        databaseReference = FirebaseDatabase.getInstance();
        apartamentosReference = databaseReference.getReference("Apartment");
        storage= FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    }

    @Override
    public void onDisabled(Context context) {

    }
}
