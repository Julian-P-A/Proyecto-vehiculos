package com.ppooii.proyecto_vehiculos.APIs.GoogleMaps;

import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;

import java.io.IOException;

public class Geocoder {
	
	private static final String GEOCODING_RESOURCE = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?fields=all&inputtype=textquery&key=";
	private static final String API_KEY = "";
	
	public Geocoder() {}
	
	public String GeoCodeSync(String query) throws IOException, InterruptedException {
		HttpClient httpClient = HttpClient.newHttpClient();
		String encodedQuery = URLEncoder.encode(query, "UTF-8");
		String requestUri = GEOCODING_RESOURCE + API_KEY + "&input=" + encodedQuery;
		HttpRequest geocodingRequest = HttpRequest.newBuilder().GET().uri(URI.create(requestUri))
				.timeout(Duration.ofMillis(2000)).build();
		HttpResponse geocodingResponse = httpClient.send(geocodingRequest, 
				HttpResponse.BodyHandlers.ofString());
		return (String) geocodingResponse.body();
	}
	
	public String getLatLng(String query) throws IOException, InterruptedException {
		ObjectMapper mapper = new ObjectMapper();
		String response = this.GeoCodeSync(query);
		JsonNode responseJsonNode = mapper.readTree(response);
		JsonNode items = responseJsonNode.get("candidates");
		String latitud = items.get(0).get("geometry").get("location").get("lat").asText();
		String longitud = items.get(0).get("geometry").get("location").get("lng").asText();
		return latitud + "," + longitud;
	}
}

