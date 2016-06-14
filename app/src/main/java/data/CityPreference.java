package data;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by achitu on 6/14/16.
 */
public class CityPreference {
    SharedPreferences prefs;

    public CityPreference(Activity activity){

        prefs=activity.getPreferences(Activity.MODE_PRIVATE);
    }

    public String getCity(){
        return prefs.getString("city","Fremont,US");
    }

    public void  setCity(String city){
        prefs.edit().putString("city",city).commit();
    }
}
