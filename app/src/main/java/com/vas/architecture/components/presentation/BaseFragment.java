package com.vas.architecture.components.presentation;

import android.content.Intent;

import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.vas.architecture.components.coordinator.NavigationOwner;

public abstract class BaseFragment extends Fragment implements NavigationOwner {
    private FragmentActivity getFragmentActivity() {
        return getActivity();
    }

    @NonNull
    @Override
    public FragmentActivity getNavigationOwner() {
        return getFragmentActivity();
    }

    @NonNull
    @Override
    public OnBackPressedDispatcher getOnBackPressedDispatcher() {
        return getFragmentActivity().getOnBackPressedDispatcher();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
