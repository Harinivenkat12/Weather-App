package data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import model.Place;
import model.Weather;
import util.Utils;

/**
 * Created by achitu on 6/13/16.
 */
public class JSONWeatherParser {

    public static Weather getWeather(String data) {

        Weather weather = new Weather();

        try {
                JSONObject jsonObject = new JSONObject(data);
                Place place = new Place();

                JSONObject coordObj = Utils.getObject("coord", jsonObject);
                place.setLatitude(Utils.getFloat("lat", coordObj)); // get the longitude and latitude obj
                place.setLongitude(Utils.getFloat("lon", coordObj));

            // get the sys obj

                JSONObject sysObj = Utils.getObject("sys", jsonObject);
                place.setCity(Utils.getString("name", jsonObject));
                place.setLastUpdate(Utils.getInt("dt", jsonObject));
                place.setCountry(Utils.getString("country", sysObj));
                place.setSunrise(Utils.getInt("sunrise", sysObj));
                place.setSunset(Utils.getInt("sunset", sysObj));

                weather.place = place;

            //get the weather info

                JSONArray jsonArray = jsonObject.getJSONArray("weather");
                JSONObject jsonWeather = jsonArray.getJSONObject(0);
                weather.currentCondition.setWeatherId(Utils.getInt("id", jsonWeather));
                weather.currentCondition.setCondition(Utils.getString("main", jsonWeather));
                weather.currentCondition.setDescription(Utils.getString("description", jsonWeather));
                weather.currentCondition.setIcon(Utils.getString("icon", jsonWeather));

            //get the main obj

                JSONObject mainObj= Utils.getObject("main",jsonObject);
                weather.currentCondition.setHumidity(Utils.getInt("humidity", mainObj));
                weather.currentCondition.setPressure(Utils.getInt("pressure", mainObj));
                weather.currentCondition.setMinTemp(Utils.getFloat("temp_min", mainObj));
                weather.currentCondition.setMaxTemp(Utils.getFloat("temp_max", mainObj));
                weather.currentCondition.setTemperature(Utils.getDouble("temp",mainObj));



            //get the wind obj

                JSONObject windObj = Utils.getObject("wind", jsonObject);
                weather.wind.setDeg(Utils.getFloat("deg", windObj));
                weather.wind.setSpeed(Utils.getFloat("speed", windObj));

            // get the cloud obj

                JSONObject cloudObj = Utils.getObject("clouds", jsonObject);
                weather.cloud.setPrecipitation(Utils.getInt("all", cloudObj));

                return weather;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
