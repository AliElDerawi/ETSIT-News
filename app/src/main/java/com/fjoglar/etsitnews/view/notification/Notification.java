/*
 * Copyright (C) 2016 Felipe Joglar Santos
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fjoglar.etsitnews.view.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.fjoglar.etsitnews.R;
import com.fjoglar.etsitnews.model.entities.NewsItem;
import com.fjoglar.etsitnews.model.repository.NewsDataSource;
import com.fjoglar.etsitnews.model.repository.NewsRepository;
import com.fjoglar.etsitnews.model.repository.datasource.NewsSharedPreferences;
import com.fjoglar.etsitnews.view.activities.NewsListActivity;

import java.util.List;

public class Notification {

    private static final int RSS_NOTIFICATION_ID = 8008;

    private static List<NewsItem> mUpdatedNewsList;
    private static int mNewsCount;
    private static String mNotificationText = "";

    public static void createNotification (Context context) {
        if (needToNotify()) {
            String endingText = (mNewsCount > 1) ? " noticias nuevas." : " noticia nuevas.";

            // Create the notification.
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_notification)
                            .setContentTitle("Tienes " + mNewsCount + endingText)
                            .setContentText(mNotificationText)
                            .setColor(context.getResources().getColor(R.color.colorPrimary))
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .setAutoCancel(true)
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(mNotificationText));

            Intent resultIntent = new Intent(context, NewsListActivity.class);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);

            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(RSS_NOTIFICATION_ID, mBuilder.build());
        }

        // Clear values for next notifications.
        mNewsCount = 0;
        mNotificationText = "";
    }

    private static boolean needToNotify() {
        NewsSharedPreferences newsSharedPreferences = NewsSharedPreferences.getInstance();
        long lastUpdatedTime = newsSharedPreferences.get(
                newsSharedPreferences.getStringFromResId(R.string.pref_last_updated_key), 0L);

        NewsDataSource newsDataSource = NewsRepository.getInstance();
        newsDataSource.getAllNews(new NewsDataSource.LoadNewsCallback() {
            @Override
            public void onNewsLoaded(List<NewsItem> newsItemList) {
                mUpdatedNewsList = newsItemList;
            }

            @Override
            public void onDataNotAvailable() {

            }
        });

        for (NewsItem newsItem : mUpdatedNewsList) {
            if (newsItem.getFormattedPubDate() > lastUpdatedTime) {
                mNewsCount += 1;
                mNotificationText = mNotificationText + newsItem.getTitle() + "\n";
            }
        }

        return mNewsCount > 0;
    }

}
