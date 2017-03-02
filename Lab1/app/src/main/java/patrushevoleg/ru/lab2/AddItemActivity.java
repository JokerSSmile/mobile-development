package patrushevoleg.ru.lab2;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class AddItemActivity extends AppCompatActivity implements View.OnClickListener, OnDateSetListener {

    EditText showPicker;
    Button addItem;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        Spinner spinner = (Spinner) findViewById(R.id.prioritySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.priority, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        showPicker = (EditText) findViewById(R.id.showDatePicker);
        showPicker.setOnClickListener(this);
        addItem = (Button) findViewById(R.id.addItemBtn);
        addItem.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.addItemBtn:
                EditText addTitle = (EditText) findViewById(R.id.addTitle);
                EditText addDate = (EditText) findViewById(R.id.showDatePicker);
                Spinner addPriority = (Spinner) findViewById(R.id.prioritySpinner);
                EditText addDescription = (EditText) findViewById(R.id.addDescription);

                if (!areElementsFilled(addTitle, addDate)) {
                    break;
                }

                Intent intent = new Intent();
                intent.putExtra("title", addTitle.getText().toString());
                intent.putExtra("date", addDate.getText().toString());
                intent.putExtra("priority", String.valueOf(addPriority.getSelectedItemPosition()));
                intent.putExtra("description", addDescription.getText().toString());
                setResult(1, intent);
                finish();
                break;
            case R.id.showDatePicker:
                calendar = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(this, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
                break;
        }
    }

    boolean areElementsFilled(EditText title, EditText date) {
        if (Objects.equals(title.getText().toString(), "")){
            Toast toast = Toast.makeText(this, "Cannot add item: title must be filled!", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        else if (Objects.equals(date.getText().toString(), "")) {
            Toast toast = Toast.makeText(this, "Cannot add item: date must be filled!", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }

        return true;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

        calendar.set(Calendar.YEAR, i);
        calendar.set(Calendar.MONTH, i1);
        calendar.set(Calendar.DAY_OF_MONTH, i2);
        updateLabel();
    }

    private void updateLabel() {

        String myFormat = "dd MMM yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        showPicker.setText(sdf.format(calendar.getTime()));
    }
}
