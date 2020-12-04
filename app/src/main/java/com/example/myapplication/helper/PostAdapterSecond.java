package com.example.myapplication.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.db.Post;


import java.util.ArrayList;
import java.util.List;
    public class PostAdapterSecond extends RecyclerView.Adapter<com.example.myapplication.helper.PostAdapterSecond.PostViewHolder> {

        private List<Post> data;
        private Context context;
        private LayoutInflater layoutInflater;

        public PostAdapterSecond(Context context) {
            this.data = new ArrayList<>();
            this.context = context;
            this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }



        @Override
        public com.example.myapplication.helper.PostAdapterSecond.PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.layout_post_item, parent, false);
            return new com.example.myapplication.helper.PostAdapterSecond.PostViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(com.example.myapplication.helper.PostAdapterSecond.PostViewHolder holder, int position) {
            holder.bind(data.get(position));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public void setData(List<Post> newData) {
            if (data != null) {


                data.clear();
                data.addAll(newData);
                notifyDataSetChanged();

            } else {
                data = newData;
            }
        }



        class PostViewHolder extends RecyclerView.ViewHolder {
            EditText stationTitle1, fuelType1, value1, cost1;
            private TextView stationTitle, fuelType, value, cost;
            private Button btnDelete, btnUpdate;

            PostViewHolder(View itemView) {
                super(itemView);


                stationTitle = itemView.findViewById(R.id.stationTitle);
                fuelType = itemView.findViewById(R.id.fuelType);
                value = itemView.findViewById(R.id.value);
                cost = itemView.findViewById(R.id.cost);


                btnDelete = itemView.findViewById(R.id.btnDelete);
            }

            void bind(final Post post) {
                if (post != null) {


                    stationTitle.setText(post.getTitle());
                    fuelType.setText(post.getContent());
                    value.setText(post.getValue());
                    cost.setText(post.getCost());



                }
            }

        }

        class PostDiffCallback extends DiffUtil.Callback {

            private final List<Post> oldPosts, newPosts;

            public PostDiffCallback(List<Post> oldPosts, List<Post> newPosts) {
                this.oldPosts = oldPosts;
                this.newPosts = newPosts;
            }

            @Override
            public int getOldListSize() {
                return oldPosts.size();
            }

            @Override
            public int getNewListSize() {
                return newPosts.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return oldPosts.get(oldItemPosition).getId() == newPosts.get(newItemPosition).getId();
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return oldPosts.get(oldItemPosition).equals(newPosts.get(newItemPosition));
            }
        }
    }

