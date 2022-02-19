package de.mm20.launcher2.badges.providers

import android.app.Notification
import de.mm20.launcher2.badges.Badge
import de.mm20.launcher2.notifications.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NotificationBadgeProvider : BadgeProvider, KoinComponent {
    private val notificationRepository: NotificationRepository by inject()

    override fun getBadge(badgeKey: String): Flow<Badge?> = channelFlow {
        if (badgeKey.startsWith("app://")) {
            val packageName = badgeKey.substring(6)
            notificationRepository.notifications.map {
                it.filter { it.packageName == packageName }
            }.collectLatest {
                if (it.isEmpty()) {
                    send(null)
                } else {
                    val badge = Badge(
                        number = it.sumOf {
                            it.notification.number
                        },
                        progress = it.mapNotNull {
                            if (!it.notification.extras.containsKey(Notification.EXTRA_PROGRESS)) return@mapNotNull null
                            val progress = it.notification.extras.getInt(Notification.EXTRA_PROGRESS)
                            val progressMax = it.notification.extras.getInt(Notification.EXTRA_PROGRESS_MAX).takeIf { it > 0 } ?: return@mapNotNull null
                            return@mapNotNull progress.toFloat() / progressMax.toFloat()
                        }
                            .takeIf { it.isNotEmpty() }
                            ?.let {
                                it.sumOf { it.toDouble() }.toFloat() / it.size
                            }
                    )
                    send(badge)
                }
            }
        } else {
            send(null)
        }
    }
}