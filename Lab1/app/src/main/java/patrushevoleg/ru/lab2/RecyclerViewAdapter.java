package patrushevoleg.ru.lab2;

import android.icu.text.DateFormat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Record> records;

    public RecyclerViewAdapter(List<Record> records) {
        this.records = records;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recyclerview, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Record record = records.get(i);
        int iconResourceId = 0;
        switch (record.getPriority()) {
            case LOW:
                iconResourceId = R.drawable.low_priority;
                break;
            case MAX:
                iconResourceId = R.drawable.max_priority;
                break;
            case NORMAL:
                iconResourceId = R.drawable.normal_priority;
                break;
        }
        viewHolder.icon.setImageResource(iconResourceId);
        viewHolder.name.setText(record.getName());
        viewHolder.description.setText(i + "kek" + i);
        viewHolder.date.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT).format(new Date()));
        viewHolder.isDone.setChecked(true);
        viewHolder.deleteButtonListener.setRecord(record);
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    private void delete(Record record) {
        int position = records.indexOf(record);
        records.remove(position);
        notifyItemRemoved(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private ImageView icon;
        private Button deleteButton;
        private TextView description;
        private TextView date;
        private CheckBox isDone;
        private DeleteButtonListener deleteButtonListener;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.recyclerViewItemName);
            icon = (ImageView) itemView.findViewById(R.id.recyclerViewItemIcon);
            description = (TextView) itemView.findViewById(R.id.recyclerViewDescription);
            date = (TextView) itemView.findViewById(R.id.recyclerViewItemDate);
            isDone = (CheckBox) itemView.findViewById(R.id.recyclerViewItemCheckbox);
            deleteButton = (Button) itemView.findViewById(R.id.recyclerViewItemDeleteButton);
            deleteButtonListener = new DeleteButtonListener();
            deleteButton.setOnClickListener(deleteButtonListener);
        }
    }

    private class DeleteButtonListener implements View.OnClickListener {
        private Record record;

        @Override
        public void onClick(View v) {
            delete(record);
        }

        public void setRecord(Record record) {
            this.record = record;
        }
    }
}