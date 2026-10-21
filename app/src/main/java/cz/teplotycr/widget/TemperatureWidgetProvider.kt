package cz.teplotycr.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.widget.RemoteViews
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class TemperatureWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, manager: AppWidgetManager, ids: IntArray) {
        ids.forEach { id ->
            val views = RemoteViews(context.packageName, R.layout.widget_temperature)
            val intent = Intent(context, MainActivity::class.java)
            val pending = PendingIntent.getActivity(
                context, 1000 + id, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_root, pending)
            manager.updateAppWidget(id, views)
        }

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        WorkManager.getInstance(context).enqueue(OneTimeWorkRequestBuilder<TemperatureMapWorker>()
            .setConstraints(constraints).build())
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "temperature-map-refresh",
            ExistingPeriodicWorkPolicy.UPDATE,
            PeriodicWorkRequestBuilder<TemperatureMapWorker>(30, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build()
        )
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        onUpdate(context, AppWidgetManager.getInstance(context),
            AppWidgetManager.getInstance(context).getAppWidgetIds(ComponentName(context, javaClass)))
    }
}
