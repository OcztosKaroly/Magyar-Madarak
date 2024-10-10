package com.example.magyar_madarak;

import static com.example.magyar_madarak.utils.NavigationUtils.navigationBarRedirection;
import static com.example.magyar_madarak.utils.NavigationUtils.redirect;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class KnowledgeBaseActivity extends AppCompatActivity {
    private static final String LOG_TAG = KnowledgeBaseActivity.class.getName();

    SearchView mSearchBar;
    ListView mListView;
    ArrayAdapter<String> mListViewAdapter;
    ArrayList<String> mList;

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

        mListView = findViewById(R.id.listViewKnowledgeBase);
        mSearchBar = findViewById(R.id.searchViewKnowledgeBase);
        mList = new ArrayList<>();
        mList.add("C");
        mList.add("C++");
        mList.add("C#");
        mList.add("Java");
        mList.add("Advanced java");
        mList.add("Interview prep with c++");
        mList.add("Interview prep with java");
        mList.add("data structures with c");
        mList.add("data structures with java");
        mList.add("data structures with jadfgva");
        mList.add("data structures with jxdffava");
        mList.add("data stghjhructures with java");
        mList.add("data structureghjkgs with java");
        mList.add("data structjhjkjures with java");
        mList.add("data structughjres with java");
        mList.add("data structurjghkes with java");
        mList.add("data structudfghres with java");
        mListViewAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mList);
        mListView.setAdapter(mListViewAdapter);

        initializeListeners();
    }

    private void initializeListeners() {
        navigationBarRedirection(mBottomNavigationView, this);

        mSearchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (mList.contains(query)) {
                    mListViewAdapter.getFilter().filter(query);
                } else {
                    Toast.makeText(KnowledgeBaseActivity.this, "Nincs találat!", Toast.LENGTH_LONG).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mListViewAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }
}