package com.example.kinobilet;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class PremiereFragment extends Fragment {
    private RecyclerView recyclerView;
    private FilmAdapter adapter;
    private List<Film> filmList = new ArrayList<>();
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_premiere, container, false);
        recyclerView = view.findViewById(R.id.films_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FilmAdapter(getContext(), filmList, film -> {
            Fragment filmFragment = FilmDetailsFragment.newInstance(film);
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_activity_fragment_container, filmFragment)
                    .addToBackStack(null)
                    .commit();
        });
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        loadFilms();
        return view;
    }

    private void loadFilms() {
        db.collection("films")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    filmList.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Film film = doc.toObject(Film.class);
                        filmList.add(film);
                    }
                    adapter.notifyDataSetChanged();
                });
    }
}