package ru.patrushevoleg.unlimitedlist;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiPost;
import com.vk.sdk.api.model.VKList;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ListAdapter adapter;
    JsonAdapter jsonAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VKSdk.initialize(this);
        setContentView(R.layout.activity_main);
        adapter = new ListAdapter();
        jsonAdapter = new JsonAdapter();
        init();
    }

    @Override
    public void onPause() {
        super.onPause();
        jsonAdapter.saveData(this, adapter.getPosts());
    }

    private void init() {

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(itemAnimator);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                final List<String> postsText = new ArrayList<>();
                VKRequest request = VKApi.wall().get(VKParameters.from(VKApiConst.OWNER_ID, -52833601, VKApiConst.EXTENDED, 1, VKApiConst.OFFSET, adapter.getItemCount(), VKApiConst.COUNT, 10));
                System.out.println("COUNT!!!" + adapter.getItemCount());
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {

                        VKList<VKApiPost> posts = (VKList<VKApiPost>) response.parsedModel;
                        for (VKApiPost post : posts) {
                            if (!post.text.contains("club") && !Objects.equals(post.text, "")) {
                                postsText.add(post.text);
                            }
                        }
                        adapter.addItems(postsText);
                        adapter.notifyDataSetChanged();
                        loading = false;
                    }
                });
            }
        });

        //if (isNetworkConnected()) {
        //    firstLoadData();
        //}
        //else {
            loadSavedPosts();
        //}
    }

    private void firstLoadData() {

        final List<String> postsText = new ArrayList<>();

        VKRequest request = VKApi.wall().get(VKParameters.from(VKApiConst.OWNER_ID, -52833601, VKApiConst.EXTENDED, 1, VKApiConst.OFFSET, 1, VKApiConst.COUNT, 10));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {

                VKList<VKApiPost> posts = (VKList<VKApiPost>) response.parsedModel;
                for (VKApiPost post : posts) {
                    if (!post.text.contains("club") && !Objects.equals(post.text, "")) {
                        postsText.add(post.text);
                    }
                }
                adapter.addItems(postsText);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void loadSavedPosts() {
        jsonAdapter.parseJSON(this);
        adapter.addItems(jsonAdapter.GetSavedPosts());
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
}