package com.udacity.popularmovies2.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.udacity.popularmovies2.R;
import com.udacity.popularmovies2.model.Video;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class VideosAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Video> videos;

    public VideosAdapter(Context context, ArrayList<Video> videos) {
        this.context = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.videos = videos;
    }

    @Override
    public int getCount() {
        return videos.size();
    }

    @Override
    public Video getItem(int position) {
        return videos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void refill(List<Video> videos) {
        videos.clear();
        this.videos.addAll(videos);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
//        VideoHolder holder;
//        if (convertView == null) {
//            convertView = inflater.inflate(R.layout.videos_list_item, viewGroup, false);
//            holder = new VideoHolder();
//            convertView.setTag(holder);
//        }else{
//            holder = (VideoHolder) convertView.getTag();
//        }
//        holder.videoTitle.setText(videos.get(position).getName());
//        return convertView;
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.videos_list_item, viewGroup, false);
        }
        ((TextView) view.findViewById(R.id.textview_video_title)).setText(videos.get(position).getName());
        return view;
    }

    public static class VideoHolder {
        @BindView(R.id.textview_video_title)
        TextView videoTitle;
    }
}
