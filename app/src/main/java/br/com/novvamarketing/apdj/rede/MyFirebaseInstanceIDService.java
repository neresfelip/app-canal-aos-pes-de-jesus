package br.com.novvamarketing.apdj.rede;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseInstanceIDService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        Log.i("teste", "MyFirebaseInstanceIDService");

        /*

        Map<String, String> data = remoteMessage.getData();

        Intent it = new Intent(this, SorteioActivity.class);

        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, it, 0);

        NotificationManager notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String notifChannelId = "Meu_ID";

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notifChannel = new NotificationChannel(notifChannelId, "Minha Notificação", NotificationManager.IMPORTANCE_DEFAULT);
            notifChannel.setDescription("Minha descrição");
            notifChannel.enableLights(true);
            notifChannel.setLightColor(Color.RED);
            notifManager.createNotificationChannel(notifChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), notifChannelId);
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(data.get("title"));
        builder.setContentText(data.get("body"));
        builder.setContentIntent(pi);

        notifManager.notify(1, builder.build());*/
    }
}
