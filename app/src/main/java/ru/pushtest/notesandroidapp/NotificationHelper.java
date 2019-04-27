package ru.pushtest.notesandroidapp;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class NotificationHelper {
	public static int ALARM_TYPE_RTC = 100;
	private static AlarmManager alarmManagerRTC;
	private static PendingIntent alarmIntentRTC;
	
	
	public static void scheduleRepeatingRTCNotification( Context context, String hour, String min ){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis( System.currentTimeMillis() );
		
		calendar.set( Calendar.HOUR_OF_DAY,
				Integer.getInteger( hour, 22 ),
				Integer.getInteger( min, 37 ));
		
		Intent intent = new Intent(context, AlarmReceiver.class);
		
		alarmIntentRTC = PendingIntent.getBroadcast(context, ALARM_TYPE_RTC, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		alarmManagerRTC = (AlarmManager)context.getSystemService(ALARM_SERVICE);
		
		alarmManagerRTC.setInexactRepeating(AlarmManager.RTC_WAKEUP,
				calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntentRTC);
		
	}
	
	public static void cancelAlarmRTC(){
		if(alarmManagerRTC != null){
			alarmManagerRTC.cancel( alarmIntentRTC );
		}
	}
	
	public static NotificationManager getNotificationManager(Context context){
		return (NotificationManager) context.getSystemService( Context.NOTIFICATION_SERVICE );
	}
	
	public static void enableBootReceiver(Context context){
		ComponentName receiver = new ComponentName( context, AlarmBootReceiver.class );
		PackageManager pm = context.getPackageManager();
		
		pm.setComponentEnabledSetting( receiver,
				PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
				PackageManager.DONT_KILL_APP);
	}
	
	public static void disableBootreceiver(Context context){
		ComponentName receiver = new ComponentName( context,AlarmBootReceiver.class );
		PackageManager pm = context.getPackageManager();
		
		pm.setComponentEnabledSetting( receiver,
				PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
				PackageManager.DONT_KILL_APP);
	}
}
