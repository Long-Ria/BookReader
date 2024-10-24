package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookapp.R;

import Models.Categories;
import java.util.List;

public class CategoryTagAdapter extends RecyclerView.Adapter<CategoryTagAdapter.CategoryViewHolder>{

    private Context mContext;
    private List<Categories> listC;

    public CategoryTagAdapter(Context mContext){
        this.mContext = mContext;
    }
    public void setData(List<Categories> listC){
        this.listC = listC;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag_categories, parent, false);

        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Categories category = listC.get(position);
        if(category == null){
            return;
        }
        holder.tvCategory.setText(category.getCategoryName());
    }

    @Override
    public int getItemCount() {
        if(listC != null){
            return listC.size();
        }
        return 0;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{

        private TextView tvCategory;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tv_categoryName_tag);
        }
    }


}
