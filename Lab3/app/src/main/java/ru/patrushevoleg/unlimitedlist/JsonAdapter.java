package ru.patrushevoleg.unlimitedlist;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class JsonAdapter {

    private static final String FILENAME = "vkposts.json";
    private List<PostEntity> posts;

    public List<PostEntity> GetSavedPosts() {
        return posts;
    }

    private String getDataFromFile(Context context) {
        String result = "";

        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(
                    context.openFileInput(FILENAME)));
            String str;
            while ((str = br.readLine()) != null) {
                result += str;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast toast = Toast.makeText(context, "Can not load data file!", Toast.LENGTH_SHORT);
            toast.show();
            return null;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        return result;
    }

    void parseJSON(Context context) {

        posts = new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(getDataFromFile(context));

            JSONArray m_jArry = obj.getJSONArray("posts");
            //String post;

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jsonObject = m_jArry.getJSONObject(i);
                String text = jsonObject.getString("post");
                String date = jsonObject.getString("date");

                posts.add(new PostEntity(text, Long.parseLong(date)));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast toast = Toast.makeText(context, "Can not read data file!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    void saveData(Context context, List<PostEntity> posts) {
        JSONArray recipes = new JSONArray();

        for (PostEntity post : posts) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("post", post.text);
                obj.put("date", post.date);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            recipes.put(obj);
        }

        BufferedWriter bw = null;
        try {
            String stringToSave = "{\"posts\":" + recipes + "}";
            bw = new BufferedWriter(new OutputStreamWriter(context.openFileOutput(FILENAME, Context.MODE_PRIVATE)));
            bw.write(stringToSave);
        }
        catch (Exception e) {
            Toast toast = Toast.makeText(context, "Can not write data file!", Toast.LENGTH_SHORT);
            toast.show();
        }
        finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
