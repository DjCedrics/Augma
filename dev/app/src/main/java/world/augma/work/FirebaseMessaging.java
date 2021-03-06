package world.augma.work;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import world.augma.R;
import world.augma.ui.main.UIMain;

public class FirebaseMessaging extends FirebaseMessagingService {

    private final String NOTE_POSTED = "AugmaNotePosted";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //super.onMessageReceived(remoteMessage);
        initChannels(this);
        Log.d("FireBase Message From:", remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0){


            int id =0;
            Object obj = remoteMessage.getData().get("id");
            if(obj !=null){
                id = Integer.valueOf(obj.toString());
            }



            String msg = remoteMessage.getData().get("default");
            String alarmName = "";
            try {
                JSONObject alarm = new JSONObject(msg);
                Log.d("FireBase Message Alarm:", ""+alarm.get("AlarmName"));
                alarmName = alarm.get("AlarmName").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }

           // Log.e("FIREBASE type:", ""+type);
            // Log.d("FireBase Message Data:", ""+remoteMessage.getData());

           // Log.d("FireBase Message notification body:", text);

            Intent intent = new Intent(this, UIMain.class);
            intent.putExtra("TEXT","test"/*text*/);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,
                    PendingIntent.FLAG_ONE_SHOT);

            NotificationCompat.Builder notificationBuilder = null;

            if(alarmName.equals(NOTE_POSTED)) {

                try {
                    notificationBuilder = new NotificationCompat.Builder(this, "default")
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(URLDecoder.decode("A Wild Note Appeared!", "UTF-8"))
                            .setContentText(URLDecoder.decode("There seems to be a new note nearby. Check it out.", "UTF-8"))
                            .setAutoCancel(true)
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .setContentIntent(pendingIntent);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
            else {
                try {
                    notificationBuilder = new NotificationCompat.Builder(this, "default")
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(URLDecoder.decode("Beacons are lit!", "UTF-8"))
                            .setContentText(URLDecoder.decode("Somebody just light a beacon nearby.This must be interesting!", "UTF-8"))
                            .setAutoCancel(true)
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .setContentIntent(pendingIntent);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            if(notificationBuilder !=null){
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify( id,notificationBuilder.build());
            }


        }
    }

    public void initChannels(Context context) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("default",
                "default_channel",
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("Channel description");
        notificationManager.createNotificationChannel(channel);
    }



}
