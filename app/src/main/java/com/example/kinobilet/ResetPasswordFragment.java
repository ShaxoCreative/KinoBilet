package com.example.kinobilet;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordFragment extends Fragment {

    private EditText emailInput;
    private TextView errorMessage;
    private Button resetButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);

        emailInput = view.findViewById(R.id.reset_email_input);
        resetButton = view.findViewById(R.id.reset_password_button);
        errorMessage = view.findViewById(R.id.error_message);

        resetButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            if (!TextUtils.isEmpty(email)) {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Письмо отправлено", Toast.LENGTH_SHORT).show();
                                ((LogInActivity) requireActivity()).loadFragment(new LoginFragment());
                            } else {
                                errorMessage.setText("Ошибка: "+ task.getException().getMessage());
                            }
                        });
            } else {
                errorMessage.setText("Введите адрес почты для восстановления доступа");
            }
        });

        return view;
    }
}
