package com.vas.architecture.github.presentation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.vas.architecture.R;
import com.vas.architecture.databinding.ActivityMainBinding;
import com.vas.architecture.github.presentation.components.RepoAdapter;
import com.vas.architecture.github.presentation.objects.Repo;

public class GitHubSearchActivity extends AppCompatActivity {
    static final String KEY_QUERY = "git_query";
    static final String DEFAULT_QUERY = "android";
    public ActivityMainBinding mainBinding;
    public GitHubViewModel mainVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainVM = new ViewModelProvider(this).get(GitHubViewModel.class);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainBinding.setMainVM(mainVM);

        RepoAdapter adapter = new RepoAdapter(new RepoAdapter.AdapterEvent() {
            // FIXME: Posso substituir este callback por DataBinding?
            @Override
            public void onQueryRetryRequest() {
                mainVM.onQueryRetryRequest();
            }

            // FIXME: Posso substituir este callback por DataBinding?
            @Override
            public void onItemClicked(Repo item) {
                mainVM.onRepoSelected(item);
            }
        });

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                if(positionStart == 0) {
                    mainBinding.recyclerView.scrollToPosition(0);
                }
            }
        });

        mainVM.posts.observe(this, adapter::submitList);
        mainBinding.recyclerView.setAdapter(adapter);

        mainVM.queryState.observe(this, adapter::setQueryState);

        mainVM.navigateToBrowser.observe(this, url -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });

        mainVM.errorToShow.observe(this, errorText ->
                Toast.makeText(this, errorText, Toast.LENGTH_LONG).show()
        );

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
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
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
