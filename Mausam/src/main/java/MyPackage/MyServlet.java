package MyPackage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Scanner;


import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;

/**
 * Servlet implementation class MyServlet
 */
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String apiKey = "63db5cfbb37e2e1435cf2da10d66ce38";
		// Get the city from the form input
        String city = request.getParameter("city"); 

        // Create the URL for the OpenWeatherMap API request
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;
        
        try {
        //API Integration
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        InputStream inputStream = connection.getInputStream();
        InputStreamReader reader = new InputStreamReader(inputStream);
               
        StringBuilder responseContent = new StringBuilder();
        
        Scanner scanner = new Scanner (reader);
        
        while(scanner.hasNext()) {
        	responseContent.append(scanner.nextLine());
        }
        scanner.close();
        
        Gson gson = new Gson();
       JsonObject jsonObject = gson.fromJson(responseContent.toString(), JsonObject.class);
       
       long dateTimestamp = jsonObject.get("dt").getAsLong() * 1000;
       String date = new Date(dateTimestamp).toString();
       
       //Temperature
       double temperatureKelvin = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
       int temperatureCelsius = (int) (temperatureKelvin - 273.15);
      
       //Humidity
       int humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
       
       //Wind Speed
       double windSpeed = jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
       
       //Weather Condition
       String weatherCondition = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
       
       request.setAttribute("date", date);
       request.setAttribute("city", city);
       request.setAttribute("temperature", temperatureCelsius);
       request.setAttribute("weatherCondition", weatherCondition); 
       request.setAttribute("humidity", humidity);    
       request.setAttribute("windSpeed", windSpeed);
       request.setAttribute("weatherData", responseContent.toString());
       
       connection.disconnect();
    } catch (IOException e) {
        e.printStackTrace();
    }

    // Forward the request to the weather.jsp page for rendering
    request.getRequestDispatcher("index.jsp").forward(request, response);
}
}