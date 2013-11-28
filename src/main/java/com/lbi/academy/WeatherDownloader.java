package com.lbi.academy;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class WeatherDownloader {

	public static void main(String[] args) throws IOException {
		WeatherRetriever weatherRetriever = new WeatherRetriever();

		String weather = weatherRetriever.getWeatherJsonForCities();
		FileWriter fileWriter = new FileWriter(new File("weather.txt"));

		fileWriter.write(weather);
		fileWriter.flush();
	}


}
