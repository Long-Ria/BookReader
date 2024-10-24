package Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import com.example.bookapp.ChapterContent;
import com.example.bookapp.R;

import java.util.List;

import Models.Chapters;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder>{
    private Context mContext;
    private List<Chapters> listChapter;

    public ChapterAdapter(Context mContext){
        this.mContext = mContext;
    }

    public void setData(List<Chapters> listChapter){
        this.listChapter = listChapter;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chapter, parent, false);

        return new ChapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder holder, int position) {
            final Chapters chapter = listChapter.get(position);
            if(chapter == null){
                return;
            }

            holder.tvChapterId.setText(chapter.getChapterId() + "");
            holder.tvChapterName.setText(chapter.getChapterName());
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    chapterItemOnClick(chapter);
                }
            });
    }
    private void chapterItemOnClick(Chapters chapter) {
        Intent intent = new Intent(mContext, ChapterContent.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("chapter", chapter);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }
    @Override
    public int getItemCount() {

        if(listChapter != null){
            return listChapter.size();
        }

        return 0;
    }

    public class ChapterViewHolder extends RecyclerView.ViewHolder{

        private TextView tvChapterId;
        private TextView tvChapterName;
        private LinearLayout linearLayout;

        public ChapterViewHolder(@NonNull View itemView) {
            super(itemView);
            tvChapterId = itemView.findViewById(R.id.tv_chapterId);
            tvChapterName = itemView.findViewById(R.id.tv_chapterName);
            linearLayout = itemView.findViewById(R.id.chapter_recycler_view);
        }
    }

}
