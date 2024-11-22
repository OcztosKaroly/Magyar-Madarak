package com.example.magyar_madarak.ui.Fragments.BirdIdentificationFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.magyar_madarak.R;
import com.example.magyar_madarak.data.model.bird.Bird;
import com.example.magyar_madarak.data.model.constants.Color;
import com.example.magyar_madarak.data.model.constants.Habitat;
import com.example.magyar_madarak.data.model.constants.Shape;
import com.example.magyar_madarak.data.viewModel.BirdViewModel;
import com.example.magyar_madarak.ui.Adapters.BirdIdentificationAdapter;
import com.example.magyar_madarak.ui.Adapters.ListBirdsAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class BirdIdentificationResultsFragment extends Fragment {

    private SharedPreferences mSharedPreferences;

    private RecyclerView mRecyclerView;
    private ListBirdsAdapter mBirdAdapter;

    private BirdViewModel mBirdViewModel;
    private MediatorLiveData<List<Bird>> birdIdentificationResults;

    private LiveData<List<Color>> selectedColors;
    private LiveData<List<Shape>> selectedShapes;
    private LiveData<List<Habitat>> selectedHabitats;

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

        mSharedPreferences = requireActivity().getSharedPreferences("birdIdentification", Context.MODE_PRIVATE);

        mBirdAdapter = new ListBirdsAdapter(getActivity());

        ArrayList<String> selectedColorNames = new ArrayList<>(mSharedPreferences.getStringSet("selectedColors", new HashSet<>()));
        selectedColors = mBirdViewModel.getAllColorsByNames(selectedColorNames);
        ArrayList<String> selectedShapeNames = new ArrayList<>(mSharedPreferences.getStringSet("selectedShapes", new HashSet<>()));
        selectedShapes = mBirdViewModel.getAllShapesByNames(selectedShapeNames);
        ArrayList<String> selectedHabitatNames = new ArrayList<>(mSharedPreferences.getStringSet("selectedHabitats", new HashSet<>()));
        selectedHabitats = mBirdViewModel.getAllHabitatsByNames(selectedHabitatNames);

        Log.d("RESULTS", "--All birds: " + loadBirds().getValue() + ".--");
        Log.d("RESULTS", "--Selected colors: " + selectedColorNames + ".--");
        Log.d("RESULTS", "--Selected shape: " + selectedShapeNames + ".--");
        Log.d("RESULTS", "--Selected habitat: " + selectedHabitatNames + ".--");

        birdIdentificationResults = new MediatorLiveData<>();

        birdIdentificationResults.addSource(loadBirds(), birds -> updateFilteredResults(birds));
        birdIdentificationResults.addSource(selectedColors, colors -> updateFilteredResults(getCurrentBirds()));
        birdIdentificationResults.addSource(selectedShapes, shapes -> updateFilteredResults(getCurrentBirds()));
        birdIdentificationResults.addSource(selectedHabitats, habitats -> updateFilteredResults(getCurrentBirds()));


        Log.d("RESULTS", "--Bird results: " + birdIdentificationResults.getValue() + ".--");

        initializeListeners();
    }

    private void initializeListeners() {
        birdIdentificationResults.observe(this, birds -> mBirdAdapter.setBirds(birds));
    }

    private List<Bird> getCurrentBirds() {
        return birdIdentificationResults.getValue() != null ? birdIdentificationResults.getValue() : new ArrayList<>();
    }

    private void updateFilteredResults(List<Bird> allBirds) {
        List<Bird> filteredBirds = allBirds.stream()
                .filter(bird -> new HashSet<>(getCurrentColors()).containsAll(bird.getColors()) &&
                        new HashSet<>(getCurrentShapes()).containsAll(bird.getShapes()) &&
                        new HashSet<>(getCurrentHabitats()).containsAll(bird.getHabitats()))
                .collect(Collectors.toList());

        birdIdentificationResults.setValue(filteredBirds);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bird_identification_results, container, false);

        mRecyclerView = view.findViewById(R.id.recyclerViewBirdIdentificationResults);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mBirdAdapter);

        return view;
    }

    private LiveData<List<Bird>> loadBirds() {
        return mBirdViewModel.getAllBirds();
    }

    private List<Color> getCurrentColors() {
        return selectedColors.getValue() != null ? selectedColors.getValue() : new ArrayList<>();
    }

    private List<Shape> getCurrentShapes() {
        return selectedShapes.getValue() != null ? selectedShapes.getValue() : new ArrayList<>();
    }

    private List<Habitat> getCurrentHabitats() {
        return selectedHabitats.getValue() != null ? selectedHabitats.getValue() : new ArrayList<>();
    }
}