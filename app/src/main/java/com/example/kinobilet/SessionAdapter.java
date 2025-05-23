package com.example.kinobilet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.SessionViewHolder> {

    public interface OnSessionClickListener {
        void onClick(Session session);
    }

    private List<Session> sessions;
    private final OnSessionClickListener listener;

    public SessionAdapter(List<Session> sessions, OnSessionClickListener listener) {
        this.sessions = sessions;
        this.listener = listener;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SessionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_session, parent, false);
        return new SessionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SessionViewHolder holder, int position) {
        Session session = sessions.get(position);
        holder.textTime.setText(session.getTime());
        holder.textPrice.setText(session.getPrice() + "₽");
        holder.itemView.setOnClickListener(v -> listener.onClick(session));
    }

    @Override
    public int getItemCount() {
        return sessions != null ? sessions.size() : 0;
    }

    static class SessionViewHolder extends RecyclerView.ViewHolder {
        TextView textTime, textPrice;

        SessionViewHolder(View itemView) {
            super(itemView);
            textTime = itemView.findViewById(R.id.text_session_time);
            textPrice = itemView.findViewById(R.id.text_session_price);
        }
    }
}
