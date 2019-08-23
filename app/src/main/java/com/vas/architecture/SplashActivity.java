package com.vas.architecture;

import android.os.Bundle;

import com.vas.architecture.components.presentation.BaseActivity;

public class SplashActivity extends BaseActivity {
    RootCoordinator coordinator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        coordinator = new RootCoordinator(this);
        coordinator.start();
    }
}
