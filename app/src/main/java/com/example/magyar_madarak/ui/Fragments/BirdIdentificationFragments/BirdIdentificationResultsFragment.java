package com.example.magyar_madarak.ui.Fragments.BirdIdentificationFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.magyar_madarak.R;
import com.example.magyar_madarak.data.model.bird.Bird;
import com.example.magyar_madarak.data.viewModel.BirdViewModel;
import com.example.magyar_madarak.ui.Adapters.BirdIdentificationAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class BirdIdentificationResultsFragment extends Fragment {

    private SharedPreferences mSharedPreferences;

    private RecyclerView mRecyclerView;
    private BirdIdentificationAdapter mAdapter;

    private BirdViewModel mBirdViewModel;
    private LiveData<List<Bird>> birdIdentificationResults;

    private List<String> selectedColors;
    private List<String> selectedShapes;
    private List<String> selectedHabitats;

    public BirdIdentificationResultsFragment() { }

    public static BirdIdentificationResultsFragment newInstance() {
        BirdIdentificationResultsFragment fragment = new BirdIdentificationResultsFragment();
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
        List<String> todoList = new ArrayList<>();
        birdIdentificationResults = mBirdViewModel.getBirdsByNameList(todoList);

        mSharedPreferences = requireActivity().getSharedPreferences("birdIdentification", Context.MODE_PRIVATE);

        selectedColors = new ArrayList<>(mSharedPreferences.getStringSet("selectedColors", new HashSet<>()));
        selectedShapes = new ArrayList<>(mSharedPreferences.getStringSet("selectedShapes", new HashSet<>()));
        selectedHabitats = new ArrayList<>(mSharedPreferences.getStringSet("selectedHabitats", new HashSet<>()));

        Log.d("FRAGMENT", "--Results: selectedColors: " + selectedColors);
        Log.d("FRAGMENT", "--Results: selectedShapes: " + selectedShapes);
        Log.d("FRAGMENT", "--Results: selectedHabitats: " + selectedHabitats);

        // TODO: a ViewModel-ben nem jó a lekérdezés, név alapján ad vissza, de résztartalmazás[szín, alak, élőhely] alapján kellene legyen

        initializeListeners();
    }

    private void initializeListeners() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bird_identification_results, container, false);
    }
}