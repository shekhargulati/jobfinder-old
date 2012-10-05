package com.jobsnearyou.scheduler;

import org.springframework.stereotype.Component;

import com.google.api.client.googleapis.GoogleHeaders;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.http.json.JsonHttpParser;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.gson.Gson;

@Component
public class GooglePlacesClient {

	// Create our transport.
	private static final HttpTransport transport = new ApacheHttpTransport();

	private static final String API_KEY = "AIzaSyA4hjzX6JXrP5DKftAkjnpH0gPABlWTi8E";
//	private static final String API_KEY =  "AIzaSyBdy37tvqqbQr_xWgHFOUdhZsXcrT4F4d8";
	private static final String PLACES_TEXT_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/textsearch/json?";

	public Places performTextSearch(String query) {
		try {
			System.out.println("Using Google Places to search ... " + query);
			HttpRequestFactory httpRequestFactory = createRequestFactory(transport);
			HttpRequest request = httpRequestFactory
					.buildGetRequest(new GenericUrl(PLACES_TEXT_SEARCH_URL));
			request.url.put("key", API_KEY);
			request.url.put("query", query);
			request.url.put("radius", 500);
			request.url.put("sensor", "false");

			String json = request.execute().parseAsString();
			Gson gson = new Gson();
			Places places = gson.fromJson(json, Places.class);
			return places;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static HttpRequestFactory createRequestFactory(
			final HttpTransport transport) {

		return transport.createRequestFactory(new HttpRequestInitializer() {
			public void initialize(HttpRequest request) {
				GoogleHeaders headers = new GoogleHeaders();
				headers.setApplicationName("JobsNearYou");
				request.headers = headers;
				JsonHttpParser parser = new JsonHttpParser();
				parser.jsonFactory = new JacksonFactory();
				request.addParser(parser);
			}
		});
	}
}
