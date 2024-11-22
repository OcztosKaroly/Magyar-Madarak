package com.example.magyar_madarak.ui;

import static com.example.magyar_madarak.utils.NavigationUtils.navigationBarRedirection;

import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.magyar_madarak.R;
import com.example.magyar_madarak.data.model.bird.Bird;
import com.example.magyar_madarak.data.viewModel.BirdViewModel;
import com.example.magyar_madarak.ui.Adapters.ListBirdsAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class KnowledgeBaseActivity extends AppCompatActivity {
    private static final String LOG_TAG = KnowledgeBaseActivity.class.getName();

    private SearchView mSearchBar;
    private RecyclerView mRecyclerView;
    private ListBirdsAdapter mBirdAdapter;
    private LiveData<List<Bird>> mBirds;

    private BirdViewModel mBirdViewModel;

    private View mView;
    private BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_knowledge_base);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.contentKnowledgeBase), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        initializeData();
    }

    private void initializeData() {
        mView = findViewById(R.id.contentKnowledgeBase);
        mBottomNavigationView = findViewById(R.id.bottomNavigationView);
        mBottomNavigationView.getMenu().findItem(R.id.nav_home).setChecked(true);

        mBirdViewModel = new ViewModelProvider(this).get(BirdViewModel.class);

        mSearchBar = findViewById(R.id.searchViewKnowledgeBase);
        mBirdAdapter = new ListBirdsAdapter(this);
        mRecyclerView = findViewById(R.id.recyclerViewKnowledgeBase);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mBirdAdapter);
        mBirds = loadBirds();

        initializeListeners();
    }

    private void initializeListeners() {
        navigationBarRedirection(mBottomNavigationView, this);

        mBirds.observe(this, birds -> mBirdAdapter.setBirds(birds));

        mSearchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mBirdAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mBirdAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private LiveData<List<Bird>> loadBirds() {
        return mBirdViewModel.getAllBirds();
    }
}