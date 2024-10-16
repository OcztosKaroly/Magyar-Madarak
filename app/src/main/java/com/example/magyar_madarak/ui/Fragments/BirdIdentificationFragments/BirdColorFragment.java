package com.example.magyar_madarak.ui.Fragments.BirdIdentificationFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.magyar_madarak.R;
import com.example.magyar_madarak.data.viewModel.BirdViewModel;
import com.example.magyar_madarak.ui.Adapters.BirdIdentificationAdapter;
import com.example.magyar_madarak.ui.BirdIdentificationActivity;

import java.util.ArrayList;
import java.util.List;

public class BirdColorFragment extends Fragment {
    private static final String ARG_FRAGMENT_TAG = "mFragmentTag";
    private String mFragmentTag;

    private RecyclerView mRecyclerView;
    private BirdIdentificationAdapter mAdapter;

    private BirdViewModel mBirdViewModel;
    private LiveData<List<String>> birdColors;
    private ArrayList<String> selectedBirdColors;

    public BirdColorFragment() { }

    public static BirdColorFragment newInstance(String fragmentTag) {
        BirdColorFragment fragment = new BirdColorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FRAGMENT_TAG, fragmentTag);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mFragmentTag = getArguments().getString(ARG_FRAGMENT_TAG);
        }

        if (savedInstanceState != null) {
            Log.i("FRAGMENT", "Load instances.");
            selectedBirdColors = savedInstanceState.getStringArrayList("selectedColors");
        } else {
            selectedBirdColors = new ArrayList<>();
        }

        initializeData();
    }

    private void initializeData() {
        mBirdViewModel = new ViewModelProvider(this).get(BirdViewModel.class);
        birdColors = mBirdViewModel.getAllColors();

        if (selectedBirdColors != null && !selectedBirdColors.isEmpty()) {
            mAdapter = new BirdIdentificationAdapter(getActivity(), selectedBirdColors);
        } else {
            mAdapter = new BirdIdentificationAdapter(getActivity());
        }

        initializeListeners();
    }

    private void initializeListeners() {
        birdColors.observe(this, colors -> {
            mAdapter.setItems(colors);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bird_color, container, false);

        mRecyclerView = view.findViewById(R.id.recyclerViewBirdIdentificationColor);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("FRAGMENT", "--Saving color instance state.");
        outState.putStringArrayList("selectedColors", (ArrayList<String>) mAdapter.getSelectedItems());
    }

    @Override
    public void onPause() {
        super.onPause();
        selectedBirdColors = (ArrayList<String>) mAdapter.getSelectedItems();
        if (getActivity() instanceof BirdIdentificationActivity) {
            ((BirdIdentificationActivity) getActivity()).receiveSelectedListFromFragment(mFragmentTag, selectedBirdColors);
            Log.i("FRAGMENT", "--Selected colors sent to activity. " + selectedBirdColors);
        }
    }
}
