package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookapp.R;

import java.util.List;

import Models.Pages;

public class PageContentAdapter extends RecyclerView.Adapter<PageContentAdapter.PageContentViewHolder>{

    private Context mContext;
    private List<Pages> listPages;

    public PageContentAdapter(Context mContext){
        this.mContext = mContext;
    }

    public void setData(List<Pages> listPages){
        this.listPages = listPages;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PageContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.page_content, parent, false);
        return new PageContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PageContentViewHolder holder, int position) {
        Pages page = listPages.get(position);
        if(page == null){
            return;
        }
        String imageUrl = page.getContent();

        // Sử dụng Glide để tải ảnh
        Glide.with(mContext)
                .load(imageUrl)
                .error(R.drawable.error_image)
                .placeholder(R.drawable.loading_image)
                .into(holder.ivPage);

    }

    @Override
    public int getItemCount() {
        if(listPages != null){
            return listPages.size();
        }
        return 0;
    }

    public class PageContentViewHolder extends RecyclerView.ViewHolder{

        private ImageView ivPage;

        public PageContentViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPage = itemView.findViewById(R.id.page_content);
        }
    }
}
