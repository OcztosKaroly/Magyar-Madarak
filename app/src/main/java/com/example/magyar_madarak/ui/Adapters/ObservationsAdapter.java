package com.example.magyar_madarak.ui.Adapters;

import static com.example.magyar_madarak.utils.NavigationUtils.startActivity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.magyar_madarak.R;
import com.example.magyar_madarak.data.model.Observation;
import com.example.magyar_madarak.ui.Pages.ObservationPageActivity;

import java.util.List;

public class ObservationsAdapter extends RecyclerView.Adapter<ObservationsAdapter.ObservationsViewHolder> implements View.OnClickListener {
    Context mContext;

    List<Observation> mObservations;

    public static Observation selectedObservation = null;

    public ObservationsAdapter(Context context, List<Observation> observations) {
        this.mContext = context;

        this.mObservations = observations;
    }

    public void setObservations(List<Observation> observations) {
        for (Observation observation: observations) {
            if (!mObservations.contains(observation)) {
                mObservations.add(observation);
                notifyItemInserted(getItemCount() - 1);
            }
        }
    }

    public List<Observation> getObservations() {
        return mObservations;
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        Observation observation = mObservations.get(position);
        Log.i("ADAPTER", "--Selected observation: " + observation + "--");
        selectedObservation = observation;

        startActivity(mContext, ObservationPageActivity.class);
    }

    @NonNull
    @Override
    public ObservationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ObservationsViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item_observation, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ObservationsViewHolder holder, int position) {
        Observation observation = mObservations.get(position);

        holder.bindTo(observation);
    }

    @Override
    public int getItemCount() {
        return mObservations.size();
    }

    static class ObservationsViewHolder extends RecyclerView.ViewHolder {
        private TextView date;
        private TextView name;

        public ObservationsViewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.item_date);
            name = itemView.findViewById(R.id.item_name);
        }

        public void bindTo(Observation observation) {
            date.setText(observation.getLastModificationDate());
            name.setText(observation.getName());
        }
    }
}
