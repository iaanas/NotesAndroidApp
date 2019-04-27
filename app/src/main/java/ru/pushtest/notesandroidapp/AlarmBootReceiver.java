package ru.pushtest.notesandroidapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmBootReceiver extends BroadcastReceiver {
	@Override
	public void onReceive( Context context, Intent intent) {
		if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
			//для демонстрации запустим только один тип уведомлений при перезапуске
			NotificationHelper.scheduleRepeatingRTCNotification( context, "22", "37" );
		}
	}
}
