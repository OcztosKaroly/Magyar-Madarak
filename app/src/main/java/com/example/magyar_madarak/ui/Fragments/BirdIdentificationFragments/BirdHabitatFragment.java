package com.example.magyar_madarak.ui.Fragments.BirdIdentificationFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.magyar_madarak.R;
import com.example.magyar_madarak.data.model.constants.Habitat;
import com.example.magyar_madarak.data.viewModel.BirdViewModel;
import com.example.magyar_madarak.ui.Adapters.BirdIdentificationAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class BirdHabitatFragment extends Fragment {

    private SharedPreferences mSharedPreferences;

    private RecyclerView mRecyclerView;
    private BirdIdentificationAdapter mAdapter;

    private BirdViewModel mBirdViewModel;
    private LiveData<List<String>> birdHabitatsNames;
    private List<String> selectedBirdHabitats;

    public BirdHabitatFragment() { }

    public static BirdHabitatFragment newInstance() {
        BirdHabitatFragment fragment = new BirdHabitatFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeData();
    }

    private void initializeData() {
        mBirdViewModel = new ViewModelProvider(this).get(BirdViewModel.class);
        birdHabitatsNames = Transformations.map(mBirdViewModel.getAllHabitats(), habitats ->
                habitats.stream().map(Habitat::getHabitatName).collect(Collectors.toList())
        );

        mSharedPreferences = requireActivity().getSharedPreferences("birdIdentification", Context.MODE_PRIVATE);

        mAdapter = new BirdIdentificationAdapter(getActivity());

        initializeListeners();
    }

    private void initializeListeners() {
        birdHabitatsNames.observe(this, habitats -> {
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
    public void onResume() {
        super.onResume();

        selectedBirdHabitats = new ArrayList<>(mSharedPreferences.getStringSet("selectedHabitats", new HashSet<>()));
        Log.i("FRAGMENT", "--Get selected habitats from shared preferences. " + selectedBirdHabitats);
        ArrayList<String> selectedItems = (ArrayList<String>) mAdapter.getSelectedItems();

        if (selectedBirdHabitats.size() != selectedItems.size() ||
                !(new HashSet<>(selectedBirdHabitats).containsAll(selectedItems))) {
            mAdapter.setSelectedItems(selectedBirdHabitats);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        selectedBirdHabitats = mAdapter.getSelectedItems();
        mSharedPreferences.edit().putStringSet("selectedHabitats", new HashSet<>(selectedBirdHabitats)).apply();
        Log.i("FRAGMENT", "--Saving selected habitats to shared preferences. " + selectedBirdHabitats);
    }
}