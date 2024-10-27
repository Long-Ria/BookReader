package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bookapp.R;

import java.util.List;

import Models.Categories;

public class CategoryAdapter extends ArrayAdapter<Categories> {
    public CategoryAdapter(@NonNull Context context, int resource, @NonNull List<Categories> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected, parent, false);
        TextView tvSelected = convertView.findViewById(R.id.tv_selected);
        Categories categories = this.getItem(position);
        if (categories != null) {
            tvSelected.setText(categories.getCategoryName());
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        TextView tvCategory = convertView.findViewById(R.id.tv_category);
        Categories categories = this.getItem(position);
        if (categories != null) {
            tvCategory.setText(categories.getCategoryName());
        }
        return convertView;
    }

}
