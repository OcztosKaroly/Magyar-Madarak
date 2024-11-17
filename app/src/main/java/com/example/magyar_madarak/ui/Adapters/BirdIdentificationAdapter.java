package com.example.magyar_madarak.ui.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.magyar_madarak.R;

import java.util.ArrayList;
import java.util.List;


public class BirdIdentificationAdapter extends RecyclerView.Adapter<BirdIdentificationAdapter.CheckBoxViewHolder> {
    Context mContext;

    List<String> checkboxItems;
    List<String> checkedItems;

    public BirdIdentificationAdapter(Context context) {
        this.mContext = context;

        this.checkboxItems = new ArrayList<>();
        this.checkedItems = new ArrayList<>();
    }

    public void setItems(List<String> items) {
        for (String item: items) {
            if (!checkboxItems.contains(item)) {
                checkboxItems.add(item);
                notifyItemInserted(getItemCount() - 1);
            }
        }
    }

    public List<String> getSelectedItems() {
        return this.checkedItems;
    }

    public void setSelectedItems(List<String> selectedItems) {
        for (String item: checkedItems) {
            if (!selectedItems.contains(item)) {
                checkedItems.remove(item);

                notifyItemChanged(getItemPositionByName(item));
            }
        }

        for (String item: selectedItems) {
            if (!checkedItems.contains(item)) {
                checkedItems.add(item);

                notifyItemChanged(getItemPositionByName(item));
            }
        }
    }

    private int getItemPositionByName(String itemName) {
        return checkboxItems.indexOf(itemName);
    }

    // onCreateViewHolder is called when a new view needs to be created.
    // It's responsible for inflating the layout and creating a ViewHolder instance.
    @NonNull
    @Override
    public CheckBoxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CheckBoxViewHolder(LayoutInflater.from(mContext).inflate(R.layout.checkbox_bird_identification, parent, false));
    }


    // onBindViewHolder is called when an existing view is reused for a new item.
    // It binds the data to the view holder, allowing you to update the content of the view.
    // Should not be used to bind click listeners and we should be mindful of what anonymous object or task we do within it.
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

    public static class CheckBoxViewHolder extends RecyclerView.ViewHolder {
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