package com.example.kinobilet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.FilmViewHolder> {

    public interface OnFilmClickListener {
        void onFilmClick(Film film);
    }

    private Context context;
    private List<Film> filmList;
    private OnFilmClickListener listener;

    public FilmAdapter(Context context, List<Film> filmList, OnFilmClickListener listener) {
        this.context = context;
        this.filmList = filmList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FilmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_film, parent, false);
        return new FilmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilmViewHolder holder, int position) {
        Film film = filmList.get(position);
        holder.title.setText(film.getTitle());
        holder.genreCountry.setText(film.getGenre() + " • " + film.getCountry());

        Glide.with(context)
                .load(film.getPosterUrl())
                .into(holder.poster);

        holder.itemView.setOnClickListener(v -> listener.onFilmClick(film));
    }

    @Override
    public int getItemCount() {
        return filmList.size();
    }

    static class FilmViewHolder extends RecyclerView.ViewHolder {
        ImageView poster;
        TextView title, genreCountry;

        FilmViewHolder(View view) {
            super(view);
            poster = view.findViewById(R.id.film_poster);
            title = view.findViewById(R.id.film_title);
            genreCountry = view.findViewById(R.id.film_genre_country);
        }
    }
}
