package com.vas.architecture.github.repositories.presentation.components;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.State;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.vas.architecture.R;
import com.vas.architecture.databinding.RepoItemBinding;
import com.vas.architecture.databinding.StateItemBinding;
import com.vas.architecture.github.repositories.objects.Repo;

import java.util.List;

public class RepoAdapter extends PagedListAdapter<Repo, RecyclerView.ViewHolder> {
    private final int REPO_ITEM_TYPE = R.layout.repo_item;
    private final int STATE_ITEM_TYPE = R.layout.state_item;
    private final AdapterEvent adapterEvent;

    private State queryState = null;

    public interface AdapterEvent {
        void onQueryRetryRequest();
    }

    public RepoAdapter(AdapterEvent adapterEvent) {
        super(new DiffUtil.ItemCallback<Repo>() {
            @Override
            public boolean areItemsTheSame(@NonNull Repo oldItem, @NonNull Repo newItem) {
                return oldItem == newItem;
            }

            @Override
            public boolean areContentsTheSame(@NonNull Repo oldItem, @NonNull Repo newItem) {
                return (newItem.id != null && newItem.id.equals(oldItem.id));
            }
        });
        this.adapterEvent = adapterEvent;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1 && hasExtraRow()) {
            return STATE_ITEM_TYPE;
        } else {
            return REPO_ITEM_TYPE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == REPO_ITEM_TYPE) {
            return RepositoryViewHolder.create(parent);
        } else if (viewType == STATE_ITEM_TYPE) {
            return StateItemViewHolder.create(parent, adapterEvent);
        } else {
            throw new IllegalArgumentException("unknown view type $viewType: " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == REPO_ITEM_TYPE) {
            ((RepositoryViewHolder) holder).bind(getItem(position));
        } else if (viewType == STATE_ITEM_TYPE) {
            ((StateItemViewHolder) holder).bind(queryState);
        }
    }

    private boolean hasExtraRow() {
        return queryState != null && (queryState.isLoading() || queryState.isError());
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + (hasExtraRow() ? 1 : 0);
    }

    public void setQueryState(State newState) {
        State previousState = this.queryState;
        boolean hadExtraRow = queryState != null && (queryState.isLoading() || queryState.isError());
        boolean hasExtraRow = newState != null && (newState.isLoading() || newState.isError());

        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount());
            } else {
                notifyItemInserted(super.getItemCount());
            }
        } else if (hasExtraRow && previousState != newState) {
            notifyItemChanged(super.getItemCount() - 1);
        }
        queryState = newState;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        if (!payloads.isEmpty()) {
            Repo item = getItem(position);
            ((RepositoryViewHolder) holder).bind(item);
        } else {
            onBindViewHolder(holder, position);
        }
    }

    static class RepositoryViewHolder extends RecyclerView.ViewHolder {

        private final RepoItemBinding repoItemBinding;

        static RepositoryViewHolder create(ViewGroup parent) {
            RepoItemBinding repoItemBinding = RepoItemBinding.inflate(LayoutInflater.from(parent.getContext()));
            return new RepositoryViewHolder(repoItemBinding);
        }

        private RepositoryViewHolder(@NonNull RepoItemBinding binding) {
            super(binding.getRoot());
            this.repoItemBinding = binding;
        }

        void bind(Repo item) {
            repoItemBinding.setItem(item);
        }
    }

    static class StateItemViewHolder extends RecyclerView.ViewHolder {

        private final StateItemBinding stateItemBinding;
        private AdapterEvent adapterEvent;

        static RecyclerView.ViewHolder create(ViewGroup parent, AdapterEvent adapterEvent) {
            StateItemBinding stateItemBinding = StateItemBinding.inflate(LayoutInflater.from(parent.getContext()));
            return new StateItemViewHolder(stateItemBinding, adapterEvent);
        }

        private StateItemViewHolder(@NonNull StateItemBinding binding, AdapterEvent adapterEvent) {
            super(binding.getRoot());
            this.stateItemBinding = binding;
            this.adapterEvent = adapterEvent;
        }

        void bind(State task) {
            stateItemBinding.setAdapterEvent(adapterEvent);
            stateItemBinding.setState(task);
        }
    }
}
