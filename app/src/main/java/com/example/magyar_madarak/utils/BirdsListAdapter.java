package com.example.magyar_madarak.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.magyar_madarak.R;

import java.util.List;

public class BirdsListAdapter extends ArrayAdapter<String> implements View.OnClickListener {
    Context mContext;
    List<String> birds; // List<Bird> birds;
    private int lastPosition;
//    private int lastPosition = -1;

    private static class ViewHolder {
        ImageView birdPicture;
        TextView birdName;
    }

    public BirdsListAdapter(Context context, List<String> birds) {
        super(context, R.layout.list_item, birds);
        this.mContext = context;
        this.birds = birds;
        this.lastPosition = -1;
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        Object object = getItem(position);
        String bird = (String)object;
//        Bird bird = (Bird)object;

        // TODO: Vezessen át a madár leíró oldalára
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String bird = getItem(position);
        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            viewHolder.birdPicture = convertView.findViewById(R.id.item_image);
            viewHolder.birdName = convertView.findViewById(R.id.item_text);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        lastPosition = position;

//        viewHolder.birdName.setText(bird.getName());
//        viewHolder.birdPicture.setImageIcon(bird.getPicture());
        viewHolder.birdPicture.setTag(position);

        return convertView;
    }
}
