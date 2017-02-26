package patrushevoleg.ru.lab2;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Record> records = new ArrayList<>();
        populateRecords(records);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(records);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(itemAnimator);
    }

    private void populateRecords(List<Record> records){
        for (int i = 0; i < 50; i++){
            Record record = new Record();
            record.setName("Item №" + i);
            record.setPriority(Record.Priority.values()[i%3]);
            records.add(record);
        }
    }

}
