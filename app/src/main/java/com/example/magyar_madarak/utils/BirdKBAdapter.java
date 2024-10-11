package com.example.magyar_madarak.utils;

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

import java.util.ArrayList;
import java.util.List;

public class BirdKBAdapter extends RecyclerView.Adapter<BirdKBAdapter.BirdViewHolder> implements View.OnClickListener, Filterable {
    Context mContext;

    List<Bird> mBirdsAll;
    List<Bird> mBirds;

    private int lastPosition;

    public BirdKBAdapter(Context context, List<Bird> birds) {
        this.mContext = context;

        this.mBirdsAll = birds;
        this.mBirds = birds;

        this.lastPosition = -1;
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        Bird bird = mBirds.get(position);
        Log.d("ADAPTER", "--Clicked bird: " + bird);

        // TODO: Vezessen át a madár leíró oldalára (paraméterben átadja az itt kiválasztott madarat)
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
        private ImageView ic_arrow_right;

        public BirdViewHolder(@NonNull View itemView) {
            super(itemView);

            birdPicture = itemView.findViewById(R.id.item_image);
            birdName = itemView.findViewById(R.id.item_text);
            ic_arrow_right = itemView.findViewById(R.id.item_arrow);
        }

        public void bindTo(Bird bird) {
//            birdPicture;
            birdName.setText(bird.getName());
            ic_arrow_right.setImageResource(R.drawable.ic_arrow_right);
        }
    }
}

//public class BirdKBAdapter extends ArrayAdapter<Bird> implements Filterable {
//    private Context mContext;
//
//    private List<Bird> mBirds;
//    private List<Bird> mBirdsAll;
//
//    public BirdKBAdapter(Context context, List<Bird> birds) {
//        super(context, R.layout.list_item_bird, birds);
//
//        this.mContext = context;
//
//        this.mBirdsAll = birds;
//        this.mBirds = birds;
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
//        if (position < 0 || position >= mBirds.size()) {
//            return convertView;
//        }
//        if (convertView == null) {
//            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = inflater.inflate(R.layout.list_item_bird, parent, false);
//        }
//
//        Bird bird = mBirds.get(position);
//
//        ImageView birdIcon = convertView.findViewById(R.id.item_image);
//        TextView birdName = convertView.findViewById(R.id.item_text);
//
////        birdIcon.setImageResource(/*...*/); // TODO: Madárhoz hozzákötni a képet
//        birdName.setText(bird.getName());
//
//        convertView.setOnClickListener(v -> openBirdPage(bird));
//
//        return convertView;
//    }
//
//    private void openBirdPage(Bird bird) {
//        Log.d("ADAPTER", "--Clicked bird: " + bird);
//        // TODO: Vezessen át a madár leíró oldalára
////        NavigationUtils.redirect(mContext, ???);
////        bird???
//    }
//
//    @NonNull
//    @Override
//    public Filter getFilter() {
//        return birdFilter;
//    }
//
//    private Filter birdFilter = new Filter() {
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            FilterResults results = new FilterResults();
//
//            if (constraint == null || constraint.length() == 0) {
//                results.count = mBirdsAll.size();
//                results.values = mBirdsAll;
//            } else {
//                List<Bird> filteredBirds = new ArrayList<>();
//                String filterPattern = constraint.toString().toLowerCase().trim();
//
//                for (Bird bird : mBirdsAll) {
//                    if (bird.getName().toLowerCase().trim().contains(filterPattern)) {
//                        filteredBirds.add(bird);
//                    }
//                }
//                results.count = filteredBirds.size();
//                results.values = filteredBirds;
//            }
//            return results;
//        }
//
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//            mBirds = (List<Bird>) results.values;
//            notifyDataSetChanged();
//        }
//    };
//}
