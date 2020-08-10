package ui;

import android.content.Context;
import android.media.Image;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diary.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.diary;

public class DiaryRecyclerAdapter extends RecyclerView.Adapter<DiaryRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<diary> diaryList;

    public DiaryRecyclerAdapter(Context context, List<diary> diaryList) {
        this.context = context;
        this.diaryList = diaryList;
    }

    @NonNull
    @Override
    public DiaryRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.diary_row, parent,false);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull DiaryRecyclerAdapter.ViewHolder holder, int position) {
diary diaryitem=diaryList.get(position);
String imageUrl;
holder.about.setText(diaryitem.getAbout());
holder.story.setText(diaryitem.getStory());
//holder.name.setText(diaryitem.getUsername());
        String timeago= (String) DateUtils.getRelativeTimeSpanString(diaryitem.getTimeadded().getSeconds()*1000);
        holder.date.setText(timeago);
        imageUrl=diaryitem.getImageUrl();
        Picasso.get().load(imageUrl).placeholder(R.drawable.year).fit().into(holder.image);
    }

    @Override
    public int getItemCount() {
        return diaryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView about,story,date,name;
        public ImageView image;
        String id;
        String username;
        public ViewHolder(@NonNull View itemView,Context ctx) {
            super(itemView);
            context=ctx;
            about=itemView.findViewById(R.id.row_about);
            story=itemView.findViewById(R.id.row_story);
            date=itemView.findViewById(R.id.row_timestamp);
            image=itemView.findViewById(R.id.row_image);
        }
    }
}
