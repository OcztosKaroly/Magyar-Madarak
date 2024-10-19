package com.example.magyar_madarak.ui.Fragments.BirdIdentificationFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class BirdShapeFragment extends Fragment {

    private SharedPreferences mSharedPreferences;

    private RecyclerView mRecyclerView;
    private BirdIdentificationAdapter mAdapter;

    private BirdViewModel mBirdViewModel;
    private LiveData<List<String>> birdShapes;
    private ArrayList<String> selectedBirdShapes;

    public BirdShapeFragment() { }

    public static BirdShapeFragment newInstance() {
        BirdShapeFragment fragment = new BirdShapeFragment();
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
        birdShapes = mBirdViewModel.getAllShapes();

        mSharedPreferences = getActivity().getSharedPreferences("birdIdentification", Context.MODE_PRIVATE);

        mAdapter = new BirdIdentificationAdapter(getActivity());

        initializeListeners();
    }

    private void initializeListeners() {
        birdShapes.observe(this, shapes -> {
            mAdapter.setItems(shapes);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bird_shape, container, false);

        mRecyclerView = view.findViewById(R.id.recyclerViewBirdIdentificationShape);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        selectedBirdShapes = new ArrayList<>(mSharedPreferences.getStringSet("selectedShapes", new HashSet<>()));
        Log.i("FRAGMENT", "--Get selected shapes from shared preferences. " + selectedBirdShapes);
        ArrayList<String> selectedItems = (ArrayList<String>) mAdapter.getSelectedItems();

        if (selectedBirdShapes.size() != selectedItems.size() ||
                !(new HashSet<>(selectedBirdShapes).containsAll(selectedItems))) {
            mAdapter.setSelectedItems(selectedBirdShapes);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        selectedBirdShapes = (ArrayList<String>) mAdapter.getSelectedItems();
        mSharedPreferences.edit().putStringSet("selectedShapes", new HashSet<>(selectedBirdShapes)).apply();
        Log.i("FRAGMENT", "--Saving selected shapes to shared preferences. " + selectedBirdShapes);
    }
}