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
import com.example.myapplication.activities.fragments.SecontTabFragment;
import com.example.myapplication.db.Post;
import com.example.myapplication.db.PostDao;
import com.example.myapplication.listener.DeleteButtonClickListener;

import java.util.ArrayList;
import java.util.List;



public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostViewHolder> {


    PostDao postDao;
    PostViewModel postViewModel;
    private List<Post> data;
    private Context context;
    private LayoutInflater layoutInflater;
    private DeleteButtonClickListener onDeleteButtonClickListener;


    public PostsAdapter(Context context, DeleteButtonClickListener listener) {
        this.data = new ArrayList<>();
        this.context = context;
        this.onDeleteButtonClickListener = listener;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public PostsAdapter(Context context, SecontTabFragment secontTabFragment) {
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.layout_post_item, parent, false);
        return new PostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
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



              /*  btnUpdate.setOnClickListener(v ->  {
                   // if (updateButtonClickListener != null)

                  postDao = PostsDatabase.getInstance(context).postDao();
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(v.getContext());
                    builder1.setMessage("Select quantity");
                    builder1.setCancelable(true);
                    Context context = v.getContext();
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View sell_dialog_layout = inflater.inflate(R.layout.dialog_layout_custom,null);
                    stationTitle1 = sell_dialog_layout.findViewById(R.id.stationTitle);
                    fuelType1 = sell_dialog_layout.findViewById(R.id.fuelType);
                    value1 = sell_dialog_layout.findViewById(R.id.value);
                    cost1 = sell_dialog_layout.findViewById(R.id.cost);
                    builder1.setView(sell_dialog_layout);
                    Post post1 = new Post(stationTitle1.getText().toString(), fuelType1.getText().toString(), value1.getText().toString(), cost1.getText().toString());

                    builder1.setPositiveButton(
                            "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id)
                                {
                                    List<Post> dataList = postDao.getAllData();
                                    postDao.nukeTable();
                                    for (int i=0; i < dataList.size(); i++){
                                        if (dataList.get(i).equals(post)){
                                            dataList.get(i).setCost(post1.getCost());
                                            dataList.get(i).setFuel_type(post1.getFuel_type());
                                            dataList.get(i).setTitle(post1.getTitle());
                                            dataList.get(i).setValue(post1.getValue());

                                        }
                                    }
                                    postDao.saveAll(dataList);
                                }
                            });

                    builder1.setNegativeButton(
                            "Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();

                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                        //updateButtonClickListener.onUpdateButtonClicked(post);
                });
*/

                btnDelete.setOnClickListener(v -> {
                    if (onDeleteButtonClickListener != null)
                        onDeleteButtonClickListener.onDeleteButtonClicked(post);
                });

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
