package com.example.kinobilet;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class SessionTicketFragment extends Fragment {

    private static final String ARG_TICKET = "ticket";
    private Ticket ticket;

    public static SessionTicketFragment newInstance(Ticket ticket) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TICKET, ticket);
        SessionTicketFragment fragment = new SessionTicketFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ticket = (Ticket) getArguments().getSerializable(ARG_TICKET);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_session_ticket, container, false);

        TextView title = view.findViewById(R.id.ticket_film_title);
        TextView date = view.findViewById(R.id.ticket_date);
        TextView time = view.findViewById(R.id.ticket_time);
        TextView seats = view.findViewById(R.id.ticket_seats);
        TextView total = view.findViewById(R.id.ticket_total);
        ImageView qrCode = view.findViewById(R.id.ticket_qr_code);

        title.setText(ticket.getFilmTitle());
        date.setText(ticket.getDate());
        time.setText(ticket.getTime());
        seats.setText(formatSeats(ticket.getSeats()));
        total.setText(ticket.getTotal() + " ₽");

        Glide.with(this).load(ticket.getCode()).into(qrCode);

        return view;
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

}
