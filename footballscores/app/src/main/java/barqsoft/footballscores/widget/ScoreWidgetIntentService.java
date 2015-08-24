package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.widget.RemoteViews;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilies;
import barqsoft.footballscores.scoresAdapter;
import barqsoft.footballscores.service.myFetchService;

/**
 * Created by incon on 17/08/15.
 */
public class ScoreWidgetIntentService extends IntentService {


    public ScoreWidgetIntentService() {
        super("ScoreWidgetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Intent service_start = new Intent(getApplicationContext(), myFetchService.class);
        getApplicationContext().startService(service_start);

        Cursor scores = getApplicationContext().getContentResolver().query(DatabaseContract.scores_table.buildScores(),
                null, null, null, null);

        scores.moveToFirst();
        String lastestHome = scores.getString(scoresAdapter.COL_HOME);
        String lastestAway = scores.getString(scoresAdapter.COL_AWAY);
        Integer lastestScoreHome = scores.getInt(scoresAdapter.COL_HOME_GOALS);
        Integer lastestScoreAway = scores.getInt(scoresAdapter.COL_AWAY_GOALS);
        String lastestMatchTime = scores.getString(scoresAdapter.COL_MATCHTIME);


        // Retrieve all of the Today widget ids: these are the widgets we need to update
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                ScoreWidgetProvider.class));

        for (int appWidgetId : appWidgetIds) {
            int layoutId = R.layout.widget_score_small;
            RemoteViews views = new RemoteViews(getPackageName(), layoutId);

            //views.widget_home_name.setText(cursor.getString(COL_HOME));
            views.setTextViewText(R.id.widget_home_name, lastestHome);
            views.setTextViewText(R.id.widget_away_name, lastestAway);
            views.setTextViewText(R.id.widget_date_textview, lastestMatchTime);
            views.setTextViewText(R.id.widget_score_textview, Utilies.getScores(lastestScoreHome, lastestScoreAway));
            views.setImageViewResource(R.id.widget_home_crest, Utilies.getTeamCrestByTeamName(lastestHome));
            views.setImageViewResource(R.id.widget_away_crest, Utilies.getTeamCrestByTeamName(lastestAway));

            // Add the data to the RemoteViews
            // Content Descriptions for RemoteViews were only added in ICS MR1
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                views.setContentDescription(R.id.widget_home_crest, lastestHome);
                views.setContentDescription(R.id.widget_away_crest, lastestAway);
            }

            // Create an Intent to launch MainActivity
            Intent launchIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, 0);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    private void setRemoteContentDescription(RemoteViews views, String description) {
        //views.setContentDescription(R.id.widget_icon, description);
    }
}
