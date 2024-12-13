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
import com.example.magyar_madarak.data.model.observation.Observation;
import com.example.magyar_madarak.ui.Pages.ObservationPageActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

public class ObservationsAdapter extends RecyclerView.Adapter<ObservationsAdapter.ObservationsViewHolder> implements View.OnClickListener {
    Context mContext;

    List<Observation> mObservations = new ArrayList<>();

    public static Observation selectedObservation = null;

    public ObservationsAdapter(Context context, List<Observation> observations) {
        this.mContext = context;

        setObservations(observations);
    }

    public void setObservations(List<Observation> observations) {
        List<Observation> observationsToRemove = new ArrayList<>(mObservations);
        observationsToRemove.removeAll(observations);

        for (Observation observation: observationsToRemove) {
            int index = mObservations.indexOf(observation);
            if (index != -1) {
                mObservations.remove(observation);
//                notifyItemRemoved(index);
            }
        }

        for (Observation observation: observations) {
            if (!mObservations.contains(observation)) {
                mObservations.add(observation);
//                notifyItemInserted(mObservations.size() - 1);
            }
        }

        mObservations.sort(Comparator.comparing(Observation::getLastModificationDate).reversed());
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        Observation observation = mObservations.get(position);
        Log.i("ADAPTER", "--Selected observation: " + observation.getName() + "--");
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
        Observation currentObservation = mObservations.get(position);

        holder.bindTo(currentObservation);

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return mObservations.size();
    }

    public static class ObservationsViewHolder extends RecyclerView.ViewHolder {
        private TextView lastModificationDate;
        private TextView observationName;

        public ObservationsViewHolder(@NonNull View itemView) {
            super(itemView);

            lastModificationDate = itemView.findViewById(R.id.item_date);
            observationName = itemView.findViewById(R.id.item_name);
        }

        public void bindTo(Observation observation) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(observation.getCreationDate());

            String date = calendar.get(Calendar.YEAR) + ". " + (calendar.get(Calendar.MONTH) + 1) +
                    ". " + calendar.get(Calendar.DAY_OF_MONTH) + ".";
            lastModificationDate.setText(date);
            observationName.setText(observation.getName());
        }
    }
}
