package com.example.myapplication.activities.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.db.Post;
import com.example.myapplication.db.PostDao;
import com.example.myapplication.db.PostsDatabase;
import com.example.myapplication.helper.PostAdapterSecond;
import com.example.myapplication.helper.PostViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class SecontTabFragment extends Fragment {
    private PostAdapterSecond postsAdapter;
    PostDao postDao;
    private PostViewModel postViewModel;
    RecyclerView recyclerView;

    public SecontTabFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_secont_tab, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        postsAdapter = new PostAdapterSecond(getContext());

        postDao = PostsDatabase.getInstance(getContext()).postDao();
        List<Post> dataList = postDao.getAllData();
        //get Viewmodel
        postViewModel = ViewModelProviders.of(this).get(PostViewModel.class);
        if (dataList.size() > 0) {
            Collections.sort(dataList, new Comparator<Post>() {
                @Override
                public int compare(final Post object1, final Post object2) {
                    return object1.getTitle().compareTo(object2.getTitle());
                }
            });
        }

        postsAdapter.setData(dataList);
        setAdapter();
        // Inflate the layout for this fragment
        return view;
    }

    void setAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(postsAdapter);
    }


}
