package com.example.kinobilet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.*;

public class SessionsFragment extends Fragment {

    private static final String ARG_FILM_ID = "film_id";

    private String filmId;
    private RecyclerView recyclerView;
    private SessionAdapter adapter;
    private TextView textDate;
    private TextView textNoSessions;
    private Button btnPrevDay, btnNextDay;

    private int dayOffset = 0;

    public static SessionsFragment newInstance(String filmId) {
        SessionsFragment fragment = new SessionsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FILM_ID, filmId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sessions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        filmId = getArguments().getString(ARG_FILM_ID);

        recyclerView = view.findViewById(R.id.recycler_sessions);
        textDate = view.findViewById(R.id.text_date);
        textNoSessions = view.findViewById(R.id.text_no_sessions);
        btnPrevDay = view.findViewById(R.id.btn_prev_day);
        btnNextDay = view.findViewById(R.id.btn_next_day);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SessionAdapter(new ArrayList<>(), session -> {});
        recyclerView.setAdapter(adapter);

        updateDayButtons();
        loadSessions();

        btnPrevDay.setOnClickListener(v -> {
            if (dayOffset > 0) {
                dayOffset--;
                updateDayButtons();
                loadSessions();
            }
        });

        btnNextDay.setOnClickListener(v -> {
            if (dayOffset < 6) {
                dayOffset++;
                updateDayButtons();
                loadSessions();
            }
        });
    }

    private void updateDayButtons() {
        btnPrevDay.setEnabled(dayOffset > 0);
        btnNextDay.setEnabled(dayOffset < 6);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, dayOffset);
        Date date = calendar.getTime();

        String dateString = new SimpleDateFormat("EEEE", Locale.getDefault()).format(date);
        if (dateString.equals("Monday")) {
            dateString = "Понедельник";
        } else if (dateString.equals("Tuesday")) {
            dateString = "Вторник";
        } else if (dateString.equals("Wednesday")) {
            dateString = "Среда";
        } else if (dateString.equals("Thursday")) {
            dateString = "Четверг";
        } else if (dateString.equals("Friday")) {
            dateString = "Пятница";
        } else if (dateString.equals("Saturday")) {
            dateString = "Суббота";
        } else if (dateString.equals("Sunday")) {
            dateString = "Воскресенье";
        }
        dateString = dateString + new SimpleDateFormat(", dd ", Locale.getDefault()).format(date);
        String month = new SimpleDateFormat("MMMM", Locale.getDefault()).format(date);
        if (month.equals("January")) {
            month = "Января";
        } else if (month.equals("February")) {
            month = "Февраля";
        } else if (month.equals("March")) {
            month = "Марта";
        } else if (month.equals("April")) {
            month = "Апреля";
        } else if (month.equals("May")) {
            month = "Мая";
        } else if (month.equals("June")) {
            month = "Июня";
        } else if (month.equals("July")) {
            month = "Июля";
        } else if (month.equals("August")) {
            month = "Августа";
        } else if (month.equals("September")) {
            month = "Сентября";
        } else if (month.equals("October")) {
            month = "Октября";
        } else if (month.equals("November")) {
            month = "Ноября";
        } else if (month.equals("December")) {
            month = "Декабря";
        }
        textDate.setText(dateString + month);
    }

    private void loadSessions() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, dayOffset);
        String dayOfWeek = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(calendar.getTime()).toLowerCase();

        FirebaseFirestore.getInstance()
                .collection("weekly_schedule")
                .document(dayOfWeek)
                .collection("sessions")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Session> filteredSessions = new ArrayList<>();
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        String film = doc.getString("filmId");
                        if (filmId.equals(film)) {
                            String time = doc.getString("time");
                            Long priceLong = doc.getLong("price");
                            int price = priceLong != null ? priceLong.intValue() : 0;
                            filteredSessions.add(new Session(time, price));
                        }
                    }

                    if (filteredSessions.isEmpty()) {
                        showEmpty();
                    } else {
                        textNoSessions.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        adapter.setSessions(filteredSessions);
                    }
                })
                .addOnFailureListener(e -> {
                    showEmpty();
                });
    }

    private void showEmpty() {
        recyclerView.setVisibility(View.GONE);
        textNoSessions.setVisibility(View.VISIBLE);
    }
}
