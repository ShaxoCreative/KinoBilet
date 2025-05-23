package com.example.kinobilet;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collections;

public class FilmDetailsFragment extends Fragment {

    private static final String ARG_FILM = "film";

    private Film film;
    private ImageView poster, favouriteIcon;
    private TextView title, genreCountry, description;
    private boolean isFavourite = false;
    Button buyTicketButton;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    public static FilmDetailsFragment newInstance(Film film) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_FILM, film);
        FilmDetailsFragment fragment = new FilmDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_film_details, container, false);

        film = (Film) getArguments().getSerializable(ARG_FILM);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        poster = view.findViewById(R.id.details_poster);
        favouriteIcon = view.findViewById(R.id.favourite_icon);
        title = view.findViewById(R.id.details_title);
        genreCountry = view.findViewById(R.id.details_genre_country);
        description = view.findViewById(R.id.details_description);
        buyTicketButton = view.findViewById(R.id.buy_ticket_button);

        title.setText(film.getTitle());
        genreCountry.setText(film.getGenre() + " • " + film.getCountry());
        description.setText(film.getDescription());
        Glide.with(requireContext()).load(film.getPosterUrl()).into(poster);

        checkIfFavourite();

        buyTicketButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), PurchaseActivity.class);
            intent.putExtra(PurchaseActivity.EXTRA_FILM_ID, film.getId());
            startActivity(intent);
        });

        favouriteIcon.setOnClickListener(v -> toggleFavourite());

        return view;
    }

    private void checkIfFavourite() {
        db.collection("users").document(auth.getUid())
                .collection("favourites")
                .document(film.getId())
                .get()
                .addOnSuccessListener(snapshot -> {
                    isFavourite = snapshot.exists();
                    updateFavouriteIcon();
                });
    }

    private void toggleFavourite() {
        DocumentReference favRef = db.collection("users").document(auth.getUid())
                .collection("favourites")
                .document(film.getId());

        if (isFavourite) {
            favRef.delete();
            isFavourite = false;
        } else {
            favRef.set(Collections.singletonMap("favourite", true));
            isFavourite = true;
        }
        updateFavouriteIcon();
    }

    private void updateFavouriteIcon() {
        if (isFavourite) {
            favouriteIcon.setImageResource(R.drawable.ic_favourite_remove);
        } else {
            favouriteIcon.setImageResource(R.drawable.ic_favourite_add);
        }
    }
}
