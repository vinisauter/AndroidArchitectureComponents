package com.vas.architecture;

import android.app.AlertDialog;
import android.content.Intent;

import com.vas.architecture.components.coordinator.BaseNavigator;
import com.vas.architecture.components.coordinator.NavigationOwner;
import com.vas.architecture.github.presentation.GitHubSearchActivity;

public class RootNavigator extends BaseNavigator {
    public RootNavigator(NavigationOwner owner) {
        super(owner);
    }

    public void showGitHubRepoSearch() {
        Intent intent = new Intent(owner.getNavigationOwner(), GitHubSearchActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        owner.getNavigationOwner().startActivity(intent);
    }

    public void showError(String errorText) {
        new AlertDialog.Builder(owner.getNavigationOwner())
                .setTitle("Erro!")
                .setMessage(errorText)
                .show();
    }

//    void showSomeActivity() {
//        Intent intent = new Intent(owner.getNavigationOwner(), SomeActivity.class);
//        owner.getNavigationOwner().startActivity(intent);
//    }
//
//    void showSomeFragment() {
//        owner.getNavigationOwner().getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.container, SomeFragment())
//                .commit();
//    }
//
//    void showSomeNavigationFragment() {
//        Navigation.findNavController(owner.getNavigationOwner(), R.id.container).navigate(R.id.viewSomeAction);
//    }
//
}
