package com.example.kinobilet;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SeatsFragment extends Fragment {

    private String filmId, filmTitle, date, time, sessionId;
    private int price;
    private List<String> bannedSeats;
    private List<String> selectedSeats = new ArrayList<>();

    private GridLayout seatGrid;
    private TextView selectedText, totalText;
    private Button buyButton;

    private String dayOfWeek;

    public static SeatsFragment newInstance(String dayOfWeek, String sessionId, String filmId, String filmTitle, String date, String time, int price, List<String> bannedSeats) {
        SeatsFragment fragment = new SeatsFragment();
        Bundle args = new Bundle();
        args.putString("dayOfWeek", dayOfWeek);
        args.putString("sessionId", sessionId);
        args.putString("filmId", filmId);
        args.putString("filmTitle", filmTitle);
        args.putString("date", date);
        args.putString("time", time);
        args.putInt("price", price);
        args.putStringArrayList("bannedSeats", new ArrayList<>(bannedSeats));
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_seats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dayOfWeek = getArguments().getString("dayOfWeek");
        sessionId = getArguments().getString("sessionId");
        filmId = getArguments().getString("filmId");
        filmTitle = getArguments().getString("filmTitle");
        date = getArguments().getString("date");
        time = getArguments().getString("time");
        price = getArguments().getInt("price");
        bannedSeats = getArguments().getStringArrayList("bannedSeats");

        seatGrid = view.findViewById(R.id.seat_grid);
        selectedText = view.findViewById(R.id.text_selected);
        totalText = view.findViewById(R.id.text_total);
        buyButton = view.findViewById(R.id.button_buy);

        setupSeats();

        buyButton.setOnClickListener(v -> buyTickets());

        updateUI();
    }

    private void setupSeats() {
        seatGrid.removeAllViews();
        seatGrid.setRowCount(15);
        seatGrid.setColumnCount(15);

        for (int row = 1; row <= 15; row++) {
            for (int col = 1; col <= 15; col++) {
                String seatId = row + "_" + col;
                View seatView = new View(getContext());
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 70;
                params.height = 70;
                params.setMargins(10, 10, 10, 10);
                seatView.setLayoutParams(params);
                seatView.setBackgroundColor(getResources().getColor(
                        bannedSeats.contains(seatId) ? android.R.color.holo_red_dark : android.R.color.darker_gray
                ));
                seatView.setEnabled(!bannedSeats.contains(seatId));

                seatView.setTag(seatId);
                seatView.setOnClickListener(v -> {
                    if (selectedSeats.contains(seatId)) {
                        selectedSeats.remove(seatId);
                        v.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                    } else {
                        selectedSeats.add(seatId);
                        v.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
                    }
                    updateUI();
                });

                seatGrid.addView(seatView);
            }
        }
    }

    private void updateUI() {
        selectedText.setText("Выбрано: " + formatSeats(selectedSeats));
        totalText.setText("Итого: " + (selectedSeats.size() * price) + "₽");
        buyButton.setVisibility(selectedSeats.isEmpty() ? View.GONE : View.VISIBLE);
    }

    private String formatSeats(List<String> seats) {
        List<String> formatted = new ArrayList<>();
        for (String seat : seats) {
            String[] parts = seat.split("_");
            if (parts.length == 2) {
                formatted.add("Ряд " + parts[0] + " – место " + parts[1]);
            }
        }
        return TextUtils.join(", ", formatted);
    }

    private void buyTickets() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Ticket ticket = new Ticket();
        ticket.setCode("https://i.imgur.com/oKcGuXl.png");
        ticket.setDate(date);
        ticket.setTime(time);
        ticket.setFilmId(filmId);
        ticket.setFilmTitle(filmTitle);
        ticket.setSeats(selectedSeats);
        ticket.setTotal(selectedSeats.size() * price);

        db.collection("users")
                .document(userId)
                .collection("tickets")
                .add(ticket)
                .addOnSuccessListener(documentReference -> {
                    DocumentReference sessionRef = db.collection("weekly_schedule")
                            .document(dayOfWeek)
                            .collection("sessions")
                            .document(sessionId);

                    sessionRef.update("banned", mergeLists(bannedSeats, selectedSeats));
                    Toast.makeText(getContext(), "Покупка прошла успешно. Проверьте свои билеты в профиле.", Toast.LENGTH_SHORT).show();
                    requireActivity().finish();
                });
    }

    private List<String> mergeLists(List<String> original, List<String> additions) {
        List<String> merged = new ArrayList<>(original);
        for (String seat : additions) {
            if (!merged.contains(seat)) {
                merged.add(seat);
            }
        }
        return merged;
    }
}
