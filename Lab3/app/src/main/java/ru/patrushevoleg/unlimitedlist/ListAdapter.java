package ru.patrushevoleg.unlimitedlist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private List<String> posts;

    ListAdapter(List<String> records) {
        this.posts = records;
    }

    ListAdapter() {
        this.posts = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String record = posts.get(position);

        holder.text.setText(record);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void addItems(List<String> items) {
        posts.addAll(items);
    }

    public List<String> getPosts() {
        return posts;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView text;

        ViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.item_text);
        }
    }
}
