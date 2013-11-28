package com.lbi.academy;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lbi.academy.model.WeatherData;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class WeatherRetriever {

	private static final String WEATHER_API_URL = "http://api.openweathermap.org/data/2.5/weather?mode=json&units=metric&q=";
	private static final String CHARSET = "UTF-8";

	private HttpClient client;
	private Gson gson;

	public WeatherRetriever() {
		client = new DefaultHttpClient();
		gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
	}

	public String getWeatherJsonForCities() throws UnsupportedEncodingException {
		List<String> weather = new ArrayList<String>();
		String[] cities = getCityListFromFile();
		for(String city : cities){
			String weatherJson = getContent(generateUrl(city));
			if(StringUtils.isNotBlank(weatherJson)){
				WeatherData weatherData = gson.fromJson(weatherJson, WeatherData.class);
				if(weatherData.getCod() == 200){
					weather.add(gson.toJson(weatherData));
				}
			}
		}
		return gson.toJson(weather);
	}

	public String generateUrl(String city) throws UnsupportedEncodingException {
		return WEATHER_API_URL + URLEncoder.encode(city, CHARSET);
	}

	public String[] getCityListFromFile(){
		String citiesFile = getFileAsString("/cities.txt");
		return StringUtils.split(citiesFile, "\n");
	}

	public String getFileAsString(String filePath){
		InputStream is = getClass().getResourceAsStream(filePath);
		String fileString = "";

		try {
			fileString = IOUtils.toString(is, "UTF-8");
		} catch (IOException e) {
			System.out.println("Couldn't parse city file");
		}

		return fileString;
	}

	public String getContent(String resourceURI) {
		HttpGet get = new HttpGet(resourceURI);
		String body = "";

		HttpResponse response;
		HttpEntity entity = null;
		try {
			response = client.execute(get);
			entity = response.getEntity();
			if (entity != null && response.getStatusLine().getStatusCode() == 200) {
				body = EntityUtils.toString(entity, CHARSET);
			}
		} catch (IOException e) {
			System.out.println("Error getting weather data");
		} finally {
			try {
				EntityUtils.consume(entity);
				get.releaseConnection();
			} catch (IOException e) {
				System.out.println("Error getting weather data");
			}
		}

		return body;
	}
}
