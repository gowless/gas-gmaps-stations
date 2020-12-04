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

import com.example.myapplication.helper.PostViewModel;
import com.example.myapplication.helper.PostsAdapter;
import com.example.myapplication.R;
import com.example.myapplication.db.Post;
import com.example.myapplication.listener.DeleteButtonClickListener;


public class FirstTabFragment extends Fragment implements DeleteButtonClickListener {

    private PostsAdapter postsAdapter;
    private PostViewModel postViewModel;
    RecyclerView recyclerView;

    public FirstTabFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first_tab, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        postsAdapter = new PostsAdapter(getContext(), this);
        setAdapter();
        //get Viewmodel
        postViewModel = ViewModelProviders.of(this).get(PostViewModel.class);
        // Create the observer which updates the UI.
        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        postViewModel.getAllPosts().observe(this, posts -> postsAdapter.setData(posts));
        // Inflate the layout for this fragment
        return view;
    }

    void setAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(postsAdapter);
    }

    @Override
    public void onDeleteButtonClicked(Post post) {
        postViewModel.deletePost(post);
    }
}