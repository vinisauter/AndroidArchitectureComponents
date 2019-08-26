package com.vas.architecture.github.repositories.presentation;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.vas.architecture.R;
import com.vas.architecture.databinding.ActivityMainBinding;
import com.vas.architecture.github.repositories.presentation.components.RepoAdapter;

public class GitHubRepoSearchActivity extends AppCompatActivity {
    static final String KEY_QUERY = "git_query";
    static final String DEFAULT_QUERY = "android";
    public ActivityMainBinding mainBinding;
    public GitHubRepoViewModel mainVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainVM = new ViewModelProvider(this).get(GitHubRepoViewModel.class);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainBinding.setMainVM(mainVM);

        RepoAdapter adapter = new RepoAdapter(() -> mainVM.onQueryRetryRequest());
        mainVM.posts.observe(this, adapter::submitList);
        mainBinding.recyclerView.setAdapter(adapter);

        mainVM.queryState.observe(this, adapter::setQueryState);

        String savedQuery = null;
        if (savedInstanceState != null) {
            savedQuery = savedInstanceState.getString(KEY_QUERY);
        }
        String query = savedQuery == null ? DEFAULT_QUERY : savedQuery;
        mainVM.setQuery(query);
        mainBinding.query.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updatedQueryFromInput();
                return true;
            } else {
                return false;
            }
        });
        mainBinding.query.setOnKeyListener((view, keyCode, keyEvent) -> {
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updatedQueryFromInput();
                return true;
            } else {
                return false;
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_QUERY, mainVM.getCurrentQueryValue());
    }

    private void updatedQueryFromInput() {
        String query = mainBinding.query.getText().toString().trim();
        mainVM.setQuery(query);
    }
}
