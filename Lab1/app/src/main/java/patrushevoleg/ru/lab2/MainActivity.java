package patrushevoleg.ru.lab2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    JsonAdapter jsonAdapter;
    FloatingActionButton startActivityBtn;
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        jsonAdapter = new JsonAdapter();
        jsonAdapter.parseJSON(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new RecyclerViewAdapter(jsonAdapter.getRecords(), this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(itemAnimator);

        startActivityBtn = (FloatingActionButton) findViewById(R.id.fab);
        startActivityBtn.setOnClickListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        jsonAdapter.saveData(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(this, AddItemActivity.class);
        startActivityForResult(intent, 1);
        intent.getExtras();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data == null) {
            return;
        }

        Record record = new Record();
        record.setId(data.getLongExtra("id", 11));
        record.setTitle(data.getStringExtra("title"));
        try {
            record.setDate(new SimpleDateFormat("dd MMM yyyy", Locale.US).parse(data.getStringExtra("date")));
        } catch (Exception ignored) {}
        record.setPriority(Integer.parseInt(data.getStringExtra("priority")));
        record.setDescription(data.getStringExtra("description"));
        adapter.add(record);
    }

}
