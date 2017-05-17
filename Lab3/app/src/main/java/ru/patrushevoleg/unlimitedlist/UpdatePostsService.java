package ru.patrushevoleg.unlimitedlist;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiPost;
import com.vk.sdk.api.model.VKList;

public class UpdatePostsService extends Service {

    public static boolean isWorking = false;
    public static long oldLastPostDate = 0;
    public static long newLastPostDate = 0;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        if(!ListActivity.isWorking){
            getPosts(this);
        }
        setAlarm();
        isWorking = true;
        return START_STICKY;
    }

    private void setAlarm(){
        Intent intent = new Intent(this, UpdatePostsService.class);
        PendingIntent pIntent = PendingIntent.getService(this, 0, intent, 0);
        AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pIntent);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 5000, pIntent);
    }

    private void getPosts(final Context context)
    {
        VKRequest request = VKApi.wall().get(VKParameters.from(VKApiConst.OWNER_ID, -29506463, VKApiConst.EXTENDED, 1, VKApiConst.OFFSET, 1, VKApiConst.COUNT, 1));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {

                VKList<VKApiPost> lastPost = (VKList<VKApiPost>) response.parsedModel;
                newLastPostDate = lastPost.get(0).date;
                if (oldLastPostDate == 0) {
                    oldLastPostDate = newLastPostDate;
                    return;
                }
                if (oldLastPostDate < newLastPostDate) {
                    oldLastPostDate = newLastPostDate;
                    Intent notificationIntent = new Intent(context, ListActivity.class);
                    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    PendingIntent pendingSelectIntent = PendingIntent.getActivity(context, 0,
                            notificationIntent, 0);

                    Notification mBuilder = new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_ab_app)
                            .setContentTitle("ВКонтакте")
                            .setContentIntent(pendingSelectIntent)
                            .setAutoCancel(true)
                            .setContentText("Новые увлекательные цитаты из рэпа").build();

                    NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(1, mBuilder);
                }
            }
        });
    }
}
