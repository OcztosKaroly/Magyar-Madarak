package com.example.magyar_madarak.ui.Fragments.BirdIdentificationFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.magyar_madarak.R;

public class BirdIdentificationResultsFragment extends Fragment {

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bird_identification_results, container, false);
    }
}