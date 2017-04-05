package patrushevoleg.ru.lab2;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.test.espresso.core.deps.guava.collect.ComparisonChain;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Record> records;
    private Activity context;
    private Record lastDelitedRecord;

    public RecyclerViewAdapter(List<Record> records, Activity context) {
        this.records = records;
        this.context = context;
        lastDelitedRecord = null;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recyclerview, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Record record = records.get(i);
        int iconPriority = 0;
        switch (record.getPriority()) {
            case LOW:
                iconPriority = R.drawable.low_priority;
                break;
            case MAX:
                iconPriority = R.drawable.max_priority;
                break;
            case NORMAL:
                iconPriority = R.drawable.normal_priority;
                break;
        }
        viewHolder.icon.setImageResource(iconPriority);
        viewHolder.name.setText(record.getTitle());
        viewHolder.description.setText(record.getDescription());
        viewHolder.date.setText(new SimpleDateFormat("dd MMM yyyy", Locale.US).format(record.getDate()));
        //viewHolder.isDone.setChecked(record.isDone());
        viewHolder.deleteButtonListener.setRecord(record);
        //viewHolder.checkBoxListener.setRecord(record);
        viewHolder.itemClickListener.setRecord(record);
        viewHolder.itemView.setOnClickListener(viewHolder.itemClickListener);
        //sort();
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    private void delete(Record record) {
        int position = records.indexOf(record);
        if (position < 0) {
            return;
        }
        lastDelitedRecord = records.get(position);
        records.remove(position);
        notifyItemRemoved(position);
    }

    private void sort() {
        Collections.sort(records, new Comparator<Record>() {
            @Override
            public int compare(Record lhs, Record rhs) {
                return ComparisonChain.start().compare(rhs.getPriority(), lhs.getPriority()).compare(rhs.getDate(), lhs.getDate()).result();
            }
        });
        notifyDataSetChanged();
    }

    public void add(Record record) {
        int pos = getRecordPosition(record);
        if (pos == -1) {
            records.add(record);
            notifyItemInserted(records.indexOf(record));
        }
        else {
            records.set(pos, record);
            notifyItemChanged(records.indexOf(record));
        }
        sort();
    }

    private int getRecordPosition(Record record) {
        for (Record rec : records){
            if (rec.getId() == record.getId()) {
                return records.indexOf(rec);
            }
        }
        return -1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private ImageView icon;
        private Button deleteButton;
        private TextView description;
        private TextView date;
        //private CheckBox isDone;
        //CheckBoxListener checkBoxListener;
        DeleteButtonListener deleteButtonListener;
        OnItemClickListener itemClickListener;

        ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.recyclerViewItemName);
            icon = (ImageView) itemView.findViewById(R.id.recyclerViewItemIcon);
            description = (TextView) itemView.findViewById(R.id.recyclerViewDescription);
            date = (TextView) itemView.findViewById(R.id.recyclerViewItemDate);
            //isDone = (CheckBox) itemView.findViewById(R.id.recyclerViewItemCheckbox);
            deleteButton = (Button) itemView.findViewById(R.id.recyclerViewItemDeleteButton);
            deleteButtonListener = new DeleteButtonListener();
            deleteButton.setOnClickListener(deleteButtonListener);
            //checkBoxListener = new CheckBoxListener();
            //isDone.setOnClickListener(checkBoxListener);
            itemClickListener = new OnItemClickListener();
        }
    }

    private class DeleteButtonListener implements View.OnClickListener {
        private Record record;

        @Override
        public void onClick(View v) {
            delete(record);
            sort();
            Snackbar snackbar = Snackbar.make(v, "Deleted", Snackbar.LENGTH_INDEFINITE).setAction("Cancel", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //System.out.println(lastDelitedRecord.getTitle());
                    //records.add(lastDelitedRecord);
                    //notifyItemInserted(records.indexOf(lastDelitedRecord));
                    add(lastDelitedRecord);
                    sort();
                }
            });
            snackbar.setDuration(5000);
            snackbar.show();
        }

        void setRecord(Record record) {
            this.record = record;
        }
    }

    /*
    private class CheckBoxListener implements View.OnClickListener {
        private Record record;

        @Override
        public void onClick(View v) {
            record.setDone(!record.isDone());
            notifyDataSetChanged();
            //sort();
        }

        void setRecord(Record record) {
            this.record = record;
        }
    }
    */

    private class OnItemClickListener implements View.OnClickListener {
        private Record record;

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, AddItemActivity.class);
            intent.putExtra("id", record.getId());
            intent.putExtra("title", record.getTitle());
            intent.putExtra("date", record.getDate().toString());
            intent.putExtra("priority", record.getPriorityInt());
            intent.putExtra("description", record.getDescription());
            context.startActivityForResult(intent, 2);
            intent.getExtras();
        }

        void setRecord(Record record) {
            this.record = record;
        }
    }
}