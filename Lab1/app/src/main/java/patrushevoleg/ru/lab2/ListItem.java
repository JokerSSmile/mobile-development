package patrushevoleg.ru.lab2;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

public class ListItem {

    public String title;
    public String description;
    public String priority;
    public String date;
    public String isDone;

    public static ArrayList<ListItem> getRecipesFromFile(String filename, Context context){
        final ArrayList<ListItem> recipeList = new ArrayList<>();

        try {
            String jsonString = loadJsonFromAsset("list.json", context);
            JSONObject json = new JSONObject(jsonString);
            JSONArray recipes = json.getJSONArray("items");

            for(int i = 0; i < recipes.length(); i++){
                ListItem recipe = new ListItem();

                recipe.title = recipes.getJSONObject(i).getString("title");
                recipe.description = recipes.getJSONObject(i).getString("description");
                recipe.priority = recipes.getJSONObject(i).getString("priority");
                recipe.date = recipes.getJSONObject(i).getString("date");
                recipe.isDone = recipes.getJSONObject(i).getString("status");

                recipeList.add(recipe);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return recipeList;
    }

    private static String loadJsonFromAsset(String filename, Context context) {

        String json;

        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        }
        catch (java.io.IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return json;
    }
}
