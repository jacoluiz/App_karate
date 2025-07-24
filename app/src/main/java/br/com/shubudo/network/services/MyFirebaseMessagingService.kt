package br.com.shubudo.network.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.app.NotificationCompat
import br.com.shubudo.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "Novo token: $token")
        saveFcmTokenToPrefs(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        remoteMessage.notification?.let {
            val titulo = it.title ?: "Nova mensagem"
            val corpo = it.body ?: ""
            mostrarNotificacao(titulo, corpo)
        }
    }

    private fun mostrarNotificacao(titulo: String, mensagem: String) {
        val canalId = "default_channel"

        val builder = NotificationCompat.Builder(this, canalId)
            .setSmallIcon(R.drawable.ic_notificacao)
            .setContentTitle(titulo)
            .setContentText(mensagem)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val canal =
            NotificationChannel(canalId, "Canal padrão", NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(canal)

        notificationManager.notify(0, builder.build())
    }

    private fun saveFcmTokenToPrefs(token: String) {
        val sharedPref: SharedPreferences = getSharedPreferences("fcm_prefs", Context.MODE_PRIVATE)
        sharedPref.edit().putString("fcm_token", token).apply()
    }
}
