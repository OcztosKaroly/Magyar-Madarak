package com.example.magyar_madarak.ui.Fragments.BirdIdentificationFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.magyar_madarak.R;
import com.example.magyar_madarak.data.viewModel.BirdViewModel;
import com.example.magyar_madarak.ui.Adapters.BirdIdentificationAdapter;
import com.example.magyar_madarak.ui.BirdIdentificationActivity;

import java.util.ArrayList;
import java.util.List;

public class BirdHabitatFragment extends Fragment {
    private static final String ARG_FRAGMENT_TAG = "mFragmentTag";
    private String mFragmentTag;

    private RecyclerView mRecyclerView;
    private BirdIdentificationAdapter mAdapter;

    private BirdViewModel mBirdViewModel;
    private LiveData<List<String>> birdHabitats;
    private ArrayList<String> selectedBirdHabitats;

    public BirdHabitatFragment() { }

    public static BirdHabitatFragment newInstance(String fragmentTag) {
        BirdHabitatFragment fragment = new BirdHabitatFragment();
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

        selectedBirdHabitats = new ArrayList<>();
        if (savedInstanceState != null) {
            Log.i("FRAGMENT", "Load instances.");
            selectedBirdHabitats = savedInstanceState.getStringArrayList("selectedHabitats");
        }

        initializeData();
    }

    private void initializeData() {
        mBirdViewModel = new ViewModelProvider(this).get(BirdViewModel.class);
        birdHabitats = mBirdViewModel.getAllHabitats();

        if (selectedBirdHabitats.isEmpty()) {
            mAdapter = new BirdIdentificationAdapter(getActivity());
        } else {
            mAdapter = new BirdIdentificationAdapter(getActivity(), selectedBirdHabitats);
        }

        initializeListeners();
    }

    private void initializeListeners() {
        birdHabitats.observe(this, habitats -> {
            mAdapter.setItems(habitats);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bird_habitat, container, false);

        mRecyclerView = view.findViewById(R.id.recyclerViewBirdIdentificationHabitat);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("selectedHabitats", (ArrayList<String>) mAdapter.getSelectedItems());
    }

    @Override
    public void onPause() {
        super.onPause();
        selectedBirdHabitats = (ArrayList<String>) mAdapter.getSelectedItems();
        if (getActivity() instanceof BirdIdentificationActivity) {
            ((BirdIdentificationActivity) getActivity()).receiveSelectedListFromFragment(mFragmentTag, selectedBirdHabitats);
            Log.i("FRAGMENT", "--Selected habitats sent to activity. " + selectedBirdHabitats);
        }
    }
}