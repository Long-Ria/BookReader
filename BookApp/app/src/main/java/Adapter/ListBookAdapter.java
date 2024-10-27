package Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookapp.BookDetail;
import com.example.bookapp.ChapterContent;
import com.example.bookapp.R;

import java.util.ArrayList;
import java.util.List;

import Models.Books;

public class ListBookAdapter extends RecyclerView.Adapter<ListBookAdapter.MyViewHolder> implements Filterable {
    Context context;
    List<Books> array;
    List<Books> arraySearch;

    public ListBookAdapter(List<Books> array, Context context) {
        this.array = array;
        this.context = context;
        this.arraySearch = array;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listbook, parent, false);

        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Books books = array.get(position);
        holder.txtName.setText(books.getBookName());
        holder.txtAuthor.setText(books.getBookAuthor());
        Glide.with(context).load(books.getImage()).into(holder.img);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookItemOnclick(books);
            }
        });
    }

    @Override
    public int getItemCount() {
        return array.size();  // Trả về kích thước danh sách sách
    }

    private void bookItemOnclick(Books book){
        Intent intent = new Intent(context, BookDetail.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("book", book);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtName, txtAuthor;
        ImageView img;
        private CardView cardView;
        @SuppressLint("WrongViewCast")
        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            txtName = itemView.findViewById(R.id.itembook_bookname);
            txtAuthor = itemView.findViewById(R.id.itembook_bookauthor);
            img = itemView.findViewById(R.id.itembook_bookimage);
            cardView = itemView.findViewById(R.id.book_card_view);
        }

    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                List<Books> filteredList = new ArrayList<>();

                if (strSearch.isEmpty()) {
                    filteredList = arraySearch;
                } else {
                    for (Books books: arraySearch) {
                        if (books.getBookName().toLowerCase().contains(strSearch.toLowerCase())) {
                            filteredList.add(books);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList; // Luôn trả về danh sách hợp lệ, dù là rỗng
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                // Kiểm tra filterResults và filterResults.values để đảm bảo không bị null
                if (filterResults != null && filterResults.values != null) {
                    array = (List<Books>) filterResults.values;
                    notifyDataSetChanged();
                }
            }
        };
    }




}
