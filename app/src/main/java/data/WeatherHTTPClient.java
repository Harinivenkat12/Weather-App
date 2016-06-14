package data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import util.Utils;

/**
 * Created by achitu on 6/10/16.
 */
public class WeatherHTTPClient {

    public String getweatherData(String place) {
        HttpURLConnection connection = null;
        InputStream inputStream = null;


        try {
            connection = (HttpURLConnection) (new URL(Utils.BASE_URL +
                    URLEncoder.encode(place, "UTF-8") + "&units=metric")).openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoInput(true);
            connection.connect();

            //Read the response
            StringBuffer stringBuffer = new StringBuffer();
            inputStream = connection.getInputStream();// gets the input stream of bits
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));// gets the inputs stream of bits in readable form

            String line = null;

            while ((line = bufferedReader.readLine()) != null) { // when buffered reader reads data from internet and is true(has data)

                stringBuffer.append(line + "\r\n"); // \r\n = for well organising line by line

            }
            inputStream.close();
            connection.disconnect();
            return stringBuffer.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }
}
