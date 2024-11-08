package com.example.magyar_madarak.ui.Fragments.BirdIdentificationFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.magyar_madarak.R;
import com.example.magyar_madarak.data.model.constants.Color;
import com.example.magyar_madarak.data.viewModel.BirdViewModel;
import com.example.magyar_madarak.ui.Adapters.BirdIdentificationAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class BirdColorFragment extends Fragment {

    private SharedPreferences mSharedPreferences;

    private RecyclerView mRecyclerView;
    private BirdIdentificationAdapter mAdapter;

    private BirdViewModel mBirdViewModel;
    private LiveData<List<String>> birdColorsNames;
    private List<String> selectedBirdColors;

    public BirdColorFragment() { }

    public static BirdColorFragment newInstance() {
        BirdColorFragment fragment = new BirdColorFragment();
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
        birdColorsNames = Transformations.map(mBirdViewModel.getAllColors(), colors ->
                colors.stream().map(Color::getColorName).collect(Collectors.toList())
        );

        mSharedPreferences = requireActivity().getSharedPreferences("birdIdentification", Context.MODE_PRIVATE);

        mAdapter = new BirdIdentificationAdapter(getActivity());

        initializeListeners();
    }

    private void initializeListeners() {
        birdColorsNames.observe(this, colors -> {
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
    public void onResume() {
        super.onResume();

        selectedBirdColors = new ArrayList<>(mSharedPreferences.getStringSet("selectedColors", new HashSet<>()));
        Log.i("FRAGMENT", "--Get selected colors from shared preferences. " + selectedBirdColors);

        mAdapter.setSelectedItems(selectedBirdColors);
    }

    @Override
    public void onPause() {
        super.onPause();
        selectedBirdColors = mAdapter.getSelectedItems();
        mSharedPreferences.edit().putStringSet("selectedColors", new HashSet<>(selectedBirdColors)).apply();
        Log.i("FRAGMENT", "--Saving selected colors to shared preferences. " + selectedBirdColors);
    }
}