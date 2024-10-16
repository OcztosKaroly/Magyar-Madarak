package com.example.magyar_madarak.ui.Adapters;

import static com.example.magyar_madarak.utils.CommonUtils.capitalizeFirstLetter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.magyar_madarak.R;
import com.example.magyar_madarak.data.model.Bird;

import java.util.ArrayList;
import java.util.List;


public class BirdIdentificationAdapter extends RecyclerView.Adapter<BirdIdentificationAdapter.CheckBoxViewHolder> {
    Context mContext;

    List<String> checkboxItems;
    List<String> checkedItems;

    public BirdIdentificationAdapter(Context context) {
        this(context, new ArrayList<>());
    }

    public BirdIdentificationAdapter(Context context, List<String> selectedItems) {
        this.mContext = context;

        this.checkboxItems = new ArrayList<>();
        this.checkedItems = selectedItems;
    }

    public void setItems(List<String> items) {
        this.checkboxItems.clear();
        this.checkboxItems = items;
        notifyDataSetChanged();
    }

    public List<String> getSelectedItems() {
        return this.checkedItems;
    }

    @NonNull
    @Override
    public CheckBoxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CheckBoxViewHolder(LayoutInflater.from(mContext).inflate(R.layout.checkbox_bird_identification, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CheckBoxViewHolder holder, int position) {
        String item = checkboxItems.get(position);

        holder.bindTo(item, checkedItems.contains(item));

        holder.mCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkedItems.add(item);
            } else {
                checkedItems.remove(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return checkboxItems.size();
    }

    static class CheckBoxViewHolder extends RecyclerView.ViewHolder {
         CheckBox mCheckBox;

        public CheckBoxViewHolder(@NonNull View itemView) {
            super(itemView);

            mCheckBox = itemView.findViewById(R.id.checkBox);
        }

        public void bindTo(String text, boolean isChecked) {
            mCheckBox.setText(text);
            mCheckBox.setTag(text);
            mCheckBox.setChecked(isChecked);
        }
    }
}