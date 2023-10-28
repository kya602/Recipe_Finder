import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class RecipeApp {
    private static JTextArea inputTextArea;
    private static JList<String> mealList;
    private static DefaultListModel<String> listModel;
    private static JTextArea recipeTextArea;
    private static JLabel imageLabel;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Recipe App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Create a panel for user input
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());

        JLabel inputLabel = new JLabel("Enter Ingredients:");
        inputTextArea = new JTextArea(3, 20);
        JButton searchButton = new JButton("Search");

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchRecipes();
            }
        });

        inputPanel.add(inputLabel, BorderLayout.NORTH);
        inputPanel.add(inputTextArea, BorderLayout.CENTER);
        inputPanel.add(searchButton, BorderLayout.SOUTH);

        // Create a panel for displaying the list of meals
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        mealList = new JList<>(listModel);
        mealList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScrollPane = new JScrollPane(mealList);

        mealList.addListSelectionListener(e -> showRecipeDetails());

        listPanel.add(new JLabel("Meals:"), BorderLayout.NORTH);
        listPanel.add(listScrollPane, BorderLayout.CENTER);

        // Create a panel for displaying the recipe details
        JPanel recipePanel = new JPanel();
        recipePanel.setLayout(new BorderLayout());

        imageLabel = new JLabel();
        recipeTextArea = new JTextArea(10, 30);
        recipeTextArea.setWrapStyleWord(true);
        recipeTextArea.setLineWrap(true);
        recipeTextArea.setEditable(false);

        recipePanel.add(imageLabel, BorderLayout.NORTH);
        recipePanel.add(new JScrollPane(recipeTextArea), BorderLayout.CENTER);

        // Add panels to the main frame
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(listPanel, BorderLayout.WEST);
        frame.add(recipePanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private static void searchRecipes() {
        // TODO: Implement API request and update the listModel with meal names based on the API response.
    }

    private static void showRecipeDetails() {
        int selectedIndex = mealList.getSelectedIndex();
        if (selectedIndex >= 0) {
            // TODO: Implement displaying the image and recipe details based on the selected meal.
        }
    }
}
