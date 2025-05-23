package com.example.kinobilet;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class PurchaseActivity extends AppCompatActivity {

    public static final String EXTRA_FILM_ID = "film_id";
    public static final String EXTRA_FILM_TITLE = "film_title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        String filmId = getIntent().getStringExtra(EXTRA_FILM_ID);
        String filmTitle = getIntent().getStringExtra(EXTRA_FILM_TITLE);
        if (savedInstanceState == null && filmId != null) {
            Fragment sessionsFragment = SessionsFragment.newInstance(filmId, filmTitle);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, sessionsFragment);
            transaction.commit();
        }
    }
}
