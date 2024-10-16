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

public class BirdShapeFragment extends Fragment {
    private static final String ARG_FRAGMENT_TAG = "mFragmentTag";
    private String mFragmentTag;

    private RecyclerView mRecyclerView;
    private BirdIdentificationAdapter mAdapter;

    private BirdViewModel mBirdViewModel;
    private LiveData<List<String>> birdShapes;
    private ArrayList<String> selectedBirdShapes;

    public BirdShapeFragment() { }

    public static BirdShapeFragment newInstance(String fragmentTag) {
        BirdShapeFragment fragment = new BirdShapeFragment();
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

        selectedBirdShapes = new ArrayList<>();
        if (savedInstanceState != null) {
            Log.i("FRAGMENT", "Load instances.");
            selectedBirdShapes = savedInstanceState.getStringArrayList("selectedShapes");
        }

        initializeData();
    }

    private void initializeData() {
        mBirdViewModel = new ViewModelProvider(this).get(BirdViewModel.class);
        birdShapes = mBirdViewModel.getAllShapes();

        if (selectedBirdShapes.isEmpty()) {
            mAdapter = new BirdIdentificationAdapter(getActivity());
        } else {
            mAdapter = new BirdIdentificationAdapter(getActivity(), selectedBirdShapes);
        }

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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("selectedShapes", (ArrayList<String>) mAdapter.getSelectedItems());
    }

    @Override
    public void onPause() {
        super.onPause();
        selectedBirdShapes = (ArrayList<String>) mAdapter.getSelectedItems();
        if (getActivity() instanceof BirdIdentificationActivity) {
            ((BirdIdentificationActivity) getActivity()).receiveSelectedListFromFragment(mFragmentTag, selectedBirdShapes);
            Log.i("FRAGMENT", "--Selected shapes sent to activity. " + selectedBirdShapes);
        }
    }

}