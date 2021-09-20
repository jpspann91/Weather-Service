package com.grossmont.ws;

// Classes for reading web service.
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

// Classes for JSON conversion to java objects using Google's gson.
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class WeatherServiceManager{

    private Weather m_oWeather = null;

    private String m_sWeatherJson;



    // Gets the overall weather JSON string from the 3rd party web service.
    public void callWeatherWebService(String sCity){

    	String sServiceReturnJson = "";

    	try {

            // Call weather API.
            URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" +
                    sCity + "&appid=1868f2463a960613c0a78b66a99b5e5f&units=imperial");
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String strTemp = "";
            while (null != (strTemp = br.readLine())) {
                    sServiceReturnJson += strTemp;
            }

            // sServiceReturnJson now looks something like this:
            /*
            {"coord":{"lon":-116.96,"lat":32.79},
            "weather":[{"id":802,"main":"Clouds","description":"scattered clouds","icon":"03n"}],
            "base":"cmc stations",
            "main":{"temp":62.65,"pressure":1007.4,"humidity":93,"temp_min":62.65,"temp_max":62.65,"sea_level":1028.19,"grnd_level":1007.4},
            "wind":{"speed":7.29,"deg":310.501},"clouds":{"all":32},"dt":1463026609,
            "sys":{"message":0.0078,"country":"US","sunrise":1463057430,"sunset":1463107097},
            "id":5345529,"name":"El Cajon","cod":200}
            */

            // *****************
            // UNCOMMENT THIS if you wish to print out raw json that came back from web service during testing.
            // System.out.println("Returned json:");
            // System.out.println(sServiceReturnJson);
            // *****************


        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("An error occurred in callWeatherWebService() in WeatherServiceManager: " + ex.toString());
        }

        m_sWeatherJson = sServiceReturnJson;

        // Turn raw json into java object heirarchy using Google's gson.
        convertJsonToJavaObject();
    }




	// Uses Google's gson library to convert json into filled java objects
	// using the java object hierarchy that you already created.
    private void convertJsonToJavaObject(){

        Gson gson = new GsonBuilder().create();

        m_oWeather = gson.fromJson(m_sWeatherJson, Weather.class);
    }




    // This uses Google's gson library for parsing json.
    public float getCurrentTemp(){

        return m_oWeather.main.temp;
    }




	// ###################################
    // ###### BEGIN: YOUR CODE HERE ######

    public String getCityName()
    {
        return m_oWeather.name;
    }

    public float getHighTemp()
    {
        return m_oWeather.main.temp_max;
    }
    public float getLowTemp()
    {
        return m_oWeather.main.temp_min;
    }

    // ###### END: YOUR CODE HERE ######
    // #################################




    // If you are running this in Tomcat, then this main method
    // can be used when developing if you want to test the functions directly
    // in your IDE to make sure these classes work first before calling from JSP...
    // which is quicker than restarting Tomcat every time
    // you make an adjustment to your class.
    // Also, it's handy to use the System.out.println tool to print out values
    // to the console when testing or use break points and run in debug mode.
    public static void main(String[] args){

		// If you are NOT incorporating these classes into Tomcat, then do the following for the lab:

		// ###### BEGIN: YOUR CODE HERE ######
		// 1. Instantiate two instances of this class: WeatherServiceManager
		// 		- Each object will represent each city.
        WeatherServiceManager oCity1 = new WeatherServiceManager();
        WeatherServiceManager oCity2 = new WeatherServiceManager();
		// 2. Get user input two different times to get 2 cities.
        Scanner input = new Scanner(System.in);
        System.out.print("Enter the name of city number 1");
        String sCity1 = input.nextLine();
        System.out.println();
        System.out.print("Enter the name for city number 2");
        String sCity2 = input.nextLine();
        System.out.println();

		// 3. IMPORTANT: Take any space in the city of user input with %20 (e.g. "san diego, california" becomes "san%20diego,california").
		//		- To do this, simply use the replaceAll method on your city string like this:
		//			sCity1 = sCity1.replaceAll(" ","%20");
        sCity1 = sCity1.replaceAll(" ", "%20");
        sCity2 = sCity2.replaceAll(" ", "%20");
		// 3. Call callWeatherWebService on each WeatherServiceManager instance passing in each city.
        oCity1.callWeatherWebService(sCity1);
        oCity2.callWeatherWebService(sCity2);
		// 4. Then make comparisons of temps between cities on each WeatherServiceManager instance by using the get methods created above:
		//		- Print out which city has the HIGHEST CURRENT TEMP (NOTE: you can get city name from your m_oWeather.
        //If current temp for city 1 is greater then city 2
        if(oCity1.getCurrentTemp() > oCity2.getCurrentTemp())
        {
            //then print out the name of city 1 by calling getCityName()
            System.out.println("Highest Current Temp in: " + oCity1.getCityName());
        }
        //If the temp is equal in both cities
        else if(oCity1.getCurrentTemp() == oCity2.getCurrentTemp())
        {
            //Prompt the user that the temps are the same
            System.out.println("Current temperature in " + oCity1.getCityName() + " and " + oCity2.getCityName() + " are the same");
        }
        //if city 2 is greater
        else
        {
            //then print out the name for city 2 by calling getCityName()
            System.out.println("Highest Current Temp in: " + oCity2.getCityName());
        }
		// 		- Print out which city has the GREATEST RANGE between low and high.
        //Call getHighTemp and subtract getLowTemp to get the range. If range for city 1 is greater
        if((oCity1.getHighTemp() - oCity1.getLowTemp()) > (oCity2.getHighTemp() - oCity2.getLowTemp()))
        {
            //then print out the name for city1 by calling GetCityName()
            System.out.println("Greatest Temp Range in: " + oCity1.getCityName());
        }
        //If the ranges are equal to each other
        else if( (oCity1.getHighTemp() - oCity1.getLowTemp()) == (oCity2.getHighTemp() - oCity2.getLowTemp()))
        {
            //Let the user know they are the same temperature range
            System.out.println("Temperature Range in " + oCity1.getCityName() + " and " + oCity2.getCityName() + " are the same");
        }
        //if city 2's range is greater
        else
        {
            //Then print the name for city 2 by calling getCityName()
            System.out.println("Greatest Temp Range in: " + oCity2.getCityName());
        }
		// ###### END: YOUR CODE HERE ######
    }




	// ------------------------------------------------------------------------------------------------------------

    // ***********************************
	// *** BEGIN: NOT PART OF THIS LAB ***
	// Only included here just as an example of how the raw json
	// could be parsed directly w/o using 3rd party library like gson.
	public float getTempManualParse(){

		String sTemp = "";
		float fTemp;

		// Parse "temp" out of JSON reply.
		int iTempIndex = m_sWeatherJson.indexOf("\"temp\":") + 7;
		sTemp = m_sWeatherJson.substring(iTempIndex);
		sTemp = sTemp.substring(0, sTemp.indexOf(","));
		fTemp = Float.parseFloat(sTemp);

		return fTemp;
	}
    // *** END: NOT part of lab ***

}
