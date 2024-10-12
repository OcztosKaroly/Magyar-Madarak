package com.example.magyar_madarak.utils;

import static com.example.magyar_madarak.utils.NavigationUtils.startActivity;
import static com.example.magyar_madarak.utils.CommonUtils.capitalizeFirstLetter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.magyar_madarak.R;
import com.example.magyar_madarak.data.model.Bird;
import com.example.magyar_madarak.ui.BirdPageActivity;

import java.util.ArrayList;
import java.util.List;

public class BirdKBAdapter extends RecyclerView.Adapter<BirdKBAdapter.BirdViewHolder> implements View.OnClickListener, Filterable {
    Context mContext;

    List<Bird> mBirdsAll;
    List<Bird> mBirds;

    private int lastPosition;

    public static Bird selectedBird = null;

    public BirdKBAdapter(Context context, List<Bird> birds) {
        this.mContext = context;

        this.mBirdsAll = birds;
        this.mBirds = birds;

        this.lastPosition = -1;
    }

    public void setBirds(List<Bird> birds) {
        this.mBirdsAll = birds;
        this.mBirds = birds;
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        Bird bird = mBirds.get(position);
        Log.i("ADAPTER", "--Selected bird: " + bird + "--");
        selectedBird = bird;

        startActivity(mContext, BirdPageActivity.class);
    }

    @NonNull
    @Override
    public BirdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BirdViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item_bird, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BirdViewHolder holder, int position) {
        Bird currentBird = mBirds.get(position);

        holder.bindTo(currentBird);

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(this);

    }

    @Override
    public int getItemCount() {
        return mBirds.size();
    }

    @Override
    public Filter getFilter() {
        return birdFilter;
    }

    private Filter birdFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Bird> filteredBirds = new ArrayList<>();
            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0) {
                results.count = mBirdsAll.size();
                results.values = mBirdsAll;
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Bird bird : mBirdsAll) {
                    if (bird.getName().toLowerCase().contains(filterPattern)) {
                        filteredBirds.add(bird);
                    }
                }
                results.count = filteredBirds.size();
                results.values = filteredBirds;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mBirds = (ArrayList<Bird>) results.values;
            notifyDataSetChanged();
        }
    };

    static class BirdViewHolder extends RecyclerView.ViewHolder {
        private ImageView birdPicture;
        private TextView birdName;

        public BirdViewHolder(@NonNull View itemView) {
            super(itemView);

            birdPicture = itemView.findViewById(R.id.item_image);
            birdName = itemView.findViewById(R.id.item_text);
        }

        public void bindTo(Bird bird) {
//            birdPicture;
            birdName.setText(capitalizeFirstLetter(bird.getName()));
        }
    }
}