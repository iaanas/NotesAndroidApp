package ru.pushtest.notesandroidapp.notifications;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import ru.pushtest.notesandroidapp.MainActivity;

public class AlarmReceiver extends BroadcastReceiver {
	@Override
	public void onReceive( Context context , Intent intent ) {
		
		Intent intentToRepeat = new Intent( context , MainActivity.class );
	
		intentToRepeat.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );

		PendingIntent pendingIntent =
				PendingIntent.getActivity( context , NotificationHelper.ALARM_TYPE_RTC , intentToRepeat , PendingIntent.FLAG_UPDATE_CURRENT );

		Notification repeatedNotification = buildLocalNotification( context , pendingIntent ).build( );
	
		NotificationHelper.getNotificationManager( context ).notify( NotificationHelper.ALARM_TYPE_RTC , repeatedNotification );
	}
	
	public NotificationCompat.Builder buildLocalNotification( Context context , PendingIntent pendingIntent ) {
		NotificationCompat.Builder builder =
				new NotificationCompat.Builder( context )
						.setContentIntent( pendingIntent )
						.setSmallIcon( android.R.drawable.ic_dialog_info)
						.setContentTitle( "Напоминание" )
						.setAutoCancel( true );
		
		return builder;
	}
}