package Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import Model.Category;
import hcmute.thach.group15mediaplayer.R;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private List<Category> categoryList;
    private Context context;

    public CategoryAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Category> categoryList){
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent,false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        if (category == null)
            return;
        holder.textView.setText(category.getTitle());
        SongAdapter songAdapter = new SongAdapter();
        songAdapter.setData(category.getSongList());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL,false);
        holder.recyclerView.setLayoutManager(linearLayoutManager);
        holder.recyclerView.setAdapter(songAdapter); // add this line to set the adapter to the RecyclerView
    }



    @Override
    public int getItemCount() {
        if(categoryList!=null){
            return categoryList.size();
        }
        return 0;
    }


    public class CategoryViewHolder extends RecyclerView.ViewHolder{
        private TextView textView;
        private RecyclerView recyclerView;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_name_category);
            recyclerView = itemView.findViewById(R.id.recycle_view_category);
        }
    }
}
