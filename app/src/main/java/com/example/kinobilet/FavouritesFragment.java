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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FavouritesFragment extends Fragment {

    private RecyclerView recyclerView;
    private FilmAdapter adapter;
    private List<Film> filmList = new ArrayList<>();
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourites, container, false);
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
        auth = FirebaseAuth.getInstance();
        loadFavourites();
        return view;
    }

    private void loadFavourites() {
        String uid = auth.getUid();
        if (uid == null) return;

        db.collection("users").document(uid).collection("favourites")
                .get()
                .addOnSuccessListener(snapshot -> {
                    filmList.clear();
                    List<DocumentSnapshot> docs = snapshot.getDocuments();
                    if (docs.isEmpty()) {
                        adapter.notifyDataSetChanged();
                        return;
                    }

                    final int[] loaded = {0};
                    for (DocumentSnapshot doc : docs) {
                        String filmId = doc.getId();

                        db.collection("films").document(filmId).get()
                                .addOnSuccessListener(filmDoc -> {
                                    if (filmDoc.exists()) {
                                        Film film = filmDoc.toObject(Film.class);
                                        film.setId(filmDoc.getId());
                                        filmList.add(film);
                                    }
                                    loaded[0]++;
                                    if (loaded[0] == docs.size()) {
                                        adapter.notifyDataSetChanged();
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    loaded[0]++;
                                    if (loaded[0] == docs.size()) {
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                    }
                });
    }
}
