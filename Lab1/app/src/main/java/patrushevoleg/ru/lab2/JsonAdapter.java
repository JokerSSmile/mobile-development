package patrushevoleg.ru.lab2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

class JsonAdapter {

    private static final String FILENAME = "todolist.json";
    private List<Record> records;

    JsonAdapter(){

        records = new ArrayList<>();
    }

    void ParseJSON(Context context) {

        try {
            JSONObject obj = new JSONObject(getDataFromFile(context));

            JSONArray m_jArry = obj.getJSONArray("recipes");
            Record record;

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jsonObject = m_jArry.getJSONObject(i);
                String title = jsonObject.getString("title");
                String priority = jsonObject.getString("priority");
                String date = jsonObject.getString("date");
                String isDone = jsonObject.getString("isDone");
                String description = jsonObject.getString("description");

                record = new Record();
                record.setTitle(title);
                record.setPriority(Integer.parseInt(priority));
                record.setDate(stringToDate(date));
                record.setDone(Boolean.parseBoolean(isDone));
                record.setDescription(description);

                records.add(record);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast toast = Toast.makeText(context, "Can not read data file!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private Date stringToDate(String str) throws Exception {

        return new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US).parse(str);
    }

    List<Record> getRecords() {
        return records;
    }

    void saveData(Context context) {
        JSONArray recipes = new JSONArray();

        for (Record record : records) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("title", record.getTitle());
                obj.put("priority", record.getPriority().ordinal());
                obj.put("date", record.getDate());
                obj.put("isDone", record.isDone());
                obj.put("description", record.getDescription());
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            recipes.put(obj);
        }

        try {
            String stringToSave = "{\"recipes\":" + recipes + "}";
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(context.openFileOutput(FILENAME, Context.MODE_PRIVATE)));
            bw.write(stringToSave);
            bw.close();
        }
        catch (Exception e) {
            Toast toast = Toast.makeText(context, "Can not write data file!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private String getDataFromFile(Context context) {
        String result = "";

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    context.openFileInput(FILENAME)));
            String str = "";
            while ((str = br.readLine()) != null) {
                result += str;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast toast = Toast.makeText(context, "Can not load data file!", Toast.LENGTH_SHORT);
            toast.show();
            return null;
        }

        return result;
    }
}
