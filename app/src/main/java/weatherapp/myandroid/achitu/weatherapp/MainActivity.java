package weatherapp.myandroid.achitu.weatherapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;

import data.CityPreference;
import data.JSONWeatherParser;
import data.WeatherHTTPClient;
import model.Weather;
import util.Utils;

public class MainActivity extends AppCompatActivity {


    private TextView cityName;
    private TextView temp;
    private TextView cloud;
    private TextView humidity;
    private TextView sunrise;
    private TextView sunset;
    private TextView pressure;
    private TextView wind;
    private TextView updated;
    private ImageView iconView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName = (TextView) findViewById(R.id.cityTitleId);
        temp = (TextView) findViewById(R.id.tempTextId);
        cloud = (TextView) findViewById(R.id.cloudTextId);
        humidity = (TextView) findViewById(R.id.humidityTextId);
        sunrise = (TextView) findViewById(R.id.sunriseTextId);
        sunset = (TextView) findViewById(R.id.sunsetTextId);
        pressure = (TextView) findViewById(R.id.pressureTextId);
        wind = (TextView) findViewById(R.id.windTextId);
        updated = (TextView) findViewById(R.id.lastUpdatedTxtid);
        iconView = (ImageView) findViewById(R.id.imageViewId);


        CityPreference cityPreference= new CityPreference(MainActivity.this);
        renderWeatherData(cityPreference.getCity());


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void renderWeatherData(String city) {

        WeatherTask weatherTask = new WeatherTask();
        weatherTask.execute(new String[]{city});
    }

    private class DownloadImageAsyncTask extends AsyncTask<String , Void, Bitmap>{
        @Override
        protected Bitmap doInBackground(String... params) {

            return downloadImage(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            iconView.setImageBitmap(bitmap);
        }

        private Bitmap downloadImage(String code){
           final DefaultHttpClient client = new DefaultHttpClient();

            final HttpGet getRequest= new HttpGet(Utils.ICON_URL+ code+".png");

            try {
                HttpResponse response=client.execute(getRequest);

                final HttpEntity entity= response.getEntity();

                if(entity!=null){

                    InputStream inputStream=null;
                    inputStream=entity.getContent();

                    //decode contents from stream

                    final Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                    return bitmap;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class WeatherTask extends AsyncTask<String, Void, Weather> {

        @Override
        protected Weather doInBackground(String... params) {

            String data = ((new WeatherHTTPClient()).getweatherData(params[0]));

            Weather weather = JSONWeatherParser.getWeather(data);

            weather.iconData=weather.currentCondition.getIcon();

            Log.v("Data : ", weather.currentCondition.getDescription());

            new DownloadImageAsyncTask().execute(weather.iconData);

            return weather;
        }

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);

            DateFormat df= DateFormat.getTimeInstance();
            String sunriseDate= df.format(new Date(weather.place.getSunrise()));
            String sunsetDate= df.format(new Date((weather.place.getSunset())));
            String updateDate= df.format(new Date(weather.place.getLastUpdate()));

            DecimalFormat decimalFormat= new DecimalFormat("#.#");
            String tempFormat= decimalFormat.format(weather.currentCondition.getTemperature());


            cityName.setText(weather.place.getCity() + " , " + weather.place.getCountry());
            temp.setText("" +tempFormat + " C");
            humidity.setText("Humidity: " + weather.currentCondition.getHumidity() + "%");
            wind.setText("Wind: " + weather.wind.getSpeed() + "mps");
            pressure.setText("Pressure: " + weather.currentCondition.getPressure() + "hPa");
            sunrise.setText("Sunrise: " + sunriseDate);
            sunset.setText("Sunset: " + sunsetDate);
            updated.setText("Last Updated: " + updateDate);
            cloud.setText("Clouds: "+weather.currentCondition.getCondition() +"(" +weather.currentCondition.getDescription()+")");

        }
    }


    //change city on click button

    private void showInputDialog(){

                AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Change city");

        final EditText cityInput= new EditText(MainActivity.this);
        cityInput.setInputType(InputType.TYPE_CLASS_TEXT);
        cityInput.setHint("Baltimore,US");
        builder.setView(cityInput);
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                CityPreference cityPreference= new CityPreference(MainActivity.this);
                cityPreference.setCity(cityInput.getText().toString());

                String newCity= cityPreference.getCity();
                renderWeatherData(newCity);

            }
        });
        builder.show();

    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.changeCityId) {
            showInputDialog();
        }

        return super.onOptionsItemSelected(item);
    }
}
