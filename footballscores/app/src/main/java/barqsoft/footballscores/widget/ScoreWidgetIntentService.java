package barqsoft.footballscores.widget;

/**
 * Created by incon on 17/08/15.
 */
public class ScoreWidgetIntentService {
    private static ScoreWidgetIntentService ourInstance = new ScoreWidgetIntentService();

    public static ScoreWidgetIntentService getInstance() {
        return ourInstance;
    }

    private ScoreWidgetIntentService() {
    }
}
