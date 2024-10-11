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
import com.example.magyar_madarak.data.model.Bird;
import com.example.magyar_madarak.data.viewModel.BirdViewModel;
import com.example.magyar_madarak.utils.BirdKBAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class KnowledgeBaseActivity extends AppCompatActivity {
    private static final String LOG_TAG = KnowledgeBaseActivity.class.getName();

    private SearchView mSearchBar;
    private RecyclerView mRecyclerView;
//    private ArrayAdapter<String> mListViewAdapter;
//    private ArrayList<String> mList;
    private BirdKBAdapter mBirdAdapter;
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
        mBottomNavigationView.getMenu().getItem(0).setChecked(true);

        mBirdViewModel = new ViewModelProvider(this).get(BirdViewModel.class);

        mSearchBar = findViewById(R.id.searchViewKnowledgeBase);
        mRecyclerView = findViewById(R.id.recyclerViewKnowledgeBase);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadBirdData();

        initializeListeners();
    }

    private void initializeListeners() {
        navigationBarRedirection(mBottomNavigationView, this);

        mBirds.observe(this, birds -> {
            mBirdAdapter = new BirdKBAdapter(this, birds);
            mRecyclerView.setAdapter(mBirdAdapter);
        });

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

    private void loadBirdData() {
        mBirds = mBirdViewModel.getAllBirds();
    }
}