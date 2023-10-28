import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RecipeSearchGUI {
    private static JTextField inputTextField;
    private static JList<String> mealList;
    private static DefaultListModel<String> listModel;
    private static JTextArea recipeTextArea;
    private static JLabel imageLabel;
    private static final String appId = "d46dbf3e";
    private static final String appKey = "52f41532fd0eea5a6297a56b7d695c1d";

    public static void main(String[] args) {
        JFrame frame = new JFrame("Recipe Finder");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());

        JLabel inputLabel = new JLabel("Enter Ingredients:");
        inputTextField = new JTextField(100);
        JButton searchButton = new JButton("Search");

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchRecipes();
            }
        });

        inputPanel.add(inputLabel, BorderLayout.NORTH);
        inputPanel.add(inputTextField, BorderLayout.CENTER);
        inputPanel.add(searchButton, BorderLayout.SOUTH);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        mealList = new JList<>(listModel);
        mealList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScrollPane = new JScrollPane(mealList);

    

        listPanel.add(new JLabel("Meals:"), BorderLayout.NORTH);
        listPanel.add(listScrollPane, BorderLayout.CENTER);

        JPanel recipePanel = new JPanel();
        recipePanel.setLayout(new BorderLayout());

        imageLabel = new JLabel();
        recipeTextArea = new JTextArea(10, 30);
        recipeTextArea.setWrapStyleWord(true);
        recipeTextArea.setLineWrap(true);
        recipeTextArea.setEditable(false);

        recipePanel.add(imageLabel, BorderLayout.NORTH);
        recipePanel.add(new JScrollPane(recipeTextArea), BorderLayout.CENTER);

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(listPanel, BorderLayout.WEST);
        frame.add(recipePanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private static void searchRecipes() {
        try {
            String ingredients = inputTextField.getText();
            String encodedIngredients = URLEncoder.encode(ingredients, "UTF-8");
            String apiUrl = "https://api.edamam.com/search?q=" + encodedIngredients +
                    "&app_id=" + appId +
                    "&app_key=" + appKey;

            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder responseBuilder = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line);
                }
                reader.close();

                String responseBody = responseBuilder.toString();
                mealList.addListSelectionListener(e -> showRecipeDetails(responseBody));
                updateRecipeList(responseBody); // Update the list based on API response
            } else {
                System.err.println("Error: Failed to fetch recipes. Status code: " + responseCode);
            }

            connection.disconnect();
        } catch (IOException e) {
            System.err.println("Error making API request: " + e.getMessage());
        }
    }

    private static void updateRecipeList(String responseBody) {
        try {
            JSONObject jsonObject = new JSONObject(responseBody);
            JSONArray recipes = jsonObject.getJSONArray("hits");
            listModel.removeAllElements();

            for (int i = 0; i < recipes.length(); i++) {
                JSONObject recipe = recipes.getJSONObject(i).getJSONObject("recipe");
                String label = recipe.getString("label");
                listModel.addElement(label);
            }
        } catch (JSONException e) {
            System.err.println("Error parsing JSON: " + e.getMessage());
        }
    }

    private static void showRecipeDetails(String responseBody) {
        int selectedIndex = mealList.getSelectedIndex();
        if (selectedIndex >= 0) {
            String selectedLabel = listModel.getElementAt(selectedIndex);

            try {
                JSONObject jsonObject = new JSONObject(responseBody); 
                JSONArray recipes = jsonObject.getJSONArray("hits");

                for (int i = 0; i < recipes.length(); i++) {
                    JSONObject recipe = recipes.getJSONObject(i).getJSONObject("recipe");
                    if (recipe.getString("label").equals(selectedLabel)) {
                        String image = recipe.getString("image");
                        JSONArray ingredients = recipe.getJSONArray("ingredientLines");

                        // Display the image (you need to implement this part)
                        // imageLabel.setIcon(new ImageIcon(new URL(image));

                        // Display ingredients in recipeTextArea
                        StringBuilder ingredientText = new StringBuilder();
                        for (int j = 0; j < ingredients.length(); j++) {
                            ingredientText.append(ingredients.getString(j)).append("\n");
                        }
                        recipeTextArea.setText(ingredientText.toString());

                        break;
                    }
                }
            } catch (JSONException e) {
                System.err.println("Error parsing JSON: " + e.getMessage());
            }
        }
    }
}
