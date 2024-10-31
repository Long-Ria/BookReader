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
    private boolean isImage;
    public PageContentAdapter(Context mContext, boolean isImage){
        this.mContext = mContext;
        this.isImage = isImage;
    }

    public void setData(List<Pages> listPages){
        this.listPages = listPages;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PageContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(isImage){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.page_content_image, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.page_content_text, parent, false);
        }


        return new PageContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PageContentViewHolder holder, int position) {
        Pages page = listPages.get(position);
        if(page == null){
            return;
        }
        String imageUrl = page.getContent();
        if(imageUrl.startsWith("http") || imageUrl.startsWith("content")){
            // Sử dụng Glide để tải ảnh
            Glide.with(mContext)
                    .load(imageUrl)
                    .error(R.drawable.error_image)
                    .placeholder(R.drawable.loading_image)
                    .into(holder.ivPage);
        } else {
            holder.tvPage.setText(page.getContent());
        }


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
        private TextView tvPage;
        public PageContentViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPage = itemView.findViewById(R.id.page_content_image);
            tvPage = itemView.findViewById(R.id.page_content_text);
        }
    }
}
