package com.baghira.ui.notification;

import com.intellij.notification.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;

public class NotificationsHelper {

    private static final String DISPLAY_ID = "Image Viewer";

    public static void showNotification(Project project, String message) {
        final NotificationGroup GROUP_DISPLAY_ID_INFO =
                new NotificationGroup(DISPLAY_ID, NotificationDisplayType.BALLOON, true);
        ApplicationManager.getApplication().invokeLater(() -> {
            Notification notification = GROUP_DISPLAY_ID_INFO.createNotification(message, NotificationType.INFORMATION);
            Notifications.Bus.notify(notification, project);
        });
    }

}