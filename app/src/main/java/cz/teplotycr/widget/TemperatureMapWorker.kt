package cz.teplotycr.widget

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.graphics.BitmapFactory
import android.widget.RemoteViews
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import java.net.HttpURLConnection
import java.net.URL

class TemperatureMapWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        return try {
            val url = URL("https://meteo.amut.net/images/pocasi-cr-teplota.png?refresh=${System.currentTimeMillis()}")
            val connection = (url.openConnection() as HttpURLConnection).apply {
                connectTimeout = 12000
                readTimeout = 12000
                setRequestProperty("User-Agent", "TeplotyCR-AndroidWidget/1.0")
            }
            connection.inputStream.use { stream ->
                val bitmap = BitmapFactory.decodeStream(stream) ?: return Result.retry()
                val manager = AppWidgetManager.getInstance(applicationContext)
                val component = ComponentName(applicationContext, TemperatureWidgetProvider::class.java)
                manager.getAppWidgetIds(component).forEach { id ->
                    val views = RemoteViews(applicationContext.packageName, R.layout.widget_temperature)
                    views.setImageViewBitmap(R.id.widget_map, bitmap)
                    val intent = android.content.Intent(applicationContext, MainActivity::class.java)
                    val pending = android.app.PendingIntent.getActivity(
                        applicationContext, 1000 + id, intent,
                        android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
                    )
                    views.setOnClickPendingIntent(R.id.widget_root, pending)
                    manager.updateAppWidget(id, views)
                }
            }
            connection.disconnect()
            Result.success()
        } catch (_: Exception) {
            Result.retry()
        }
    }
}
