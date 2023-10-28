
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class RecipeSearch {

    public static void main(String[] args) {
        // Replace 'YOUR_APP_ID' and 'YOUR_APP_KEY' with your Edamam API credentials
        String appId = "d46dbf3e";
        String appKey = "52f41532fd0eea5a6297a56b7d695c1d";

        try {
            // Define the ingredients you want to search for
            String ingredients = "chicken, tomato, onion"; // Replace with your desired ingredients

            // Encode the ingredients for the URL
            String encodedIngredients = URLEncoder.encode(ingredients, "UTF-8");

            // Create the API URL for recipes
            String apiUrl = "https://api.edamam.com/search?q=" + encodedIngredients +
                    "&app_id=" + appId +
                    "&app_key=" + appKey;

            // Create a URL object representing the API endpoint
            URL url = new URL(apiUrl);

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method to GET
            connection.setRequestMethod("GET");

            // Get the response code from the server
            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                // Read and process the response content (recipes in JSON format)
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder responseBuilder = new StringBuilder();
                String line;

                // Read the response line by line and build it into a string
                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line);
                }
                reader.close();

                // Extract the response body as a string (JSON format)
                String responseBody = responseBuilder.toString();
                System.out.println(responseBody); // This will contain recipe data in JSON format
            } else {
                System.err.println("Error: Failed to fetch recipes. Status code: " + responseCode);
            }

            // Close the connection when done
            connection.disconnect();
        } catch (IOException e) {
            System.err.println("Error making API request: " + e.getMessage());
        }
    }
}
