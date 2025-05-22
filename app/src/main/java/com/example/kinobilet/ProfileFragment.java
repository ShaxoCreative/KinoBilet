package com.example.kinobilet;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {

    private FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        TextView emailText = view.findViewById(R.id.email_text);
        if (user != null) {
            emailText.setText(user.getEmail());
        }

        Button logoutButton = view.findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(v -> {
            auth.signOut();
            startActivity(new Intent(getContext(), LogInActivity.class));
            requireActivity().finish();
        });

        LinearLayout changePasswordLayout = view.findViewById(R.id.change_password_layout);
        changePasswordLayout.setOnClickListener(v -> {
            Fragment changePassFragment = new ChangePasswordFragment();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_activity_fragment_container, changePassFragment)
                    .addToBackStack(null)
                    .commit();
        });

        LinearLayout ticketsLayout = view.findViewById(R.id.tickets_layout);
        ticketsLayout.setOnClickListener(v -> {
            Fragment ticketsFragment = new TicketsFragment();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_activity_fragment_container, ticketsFragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }
}
