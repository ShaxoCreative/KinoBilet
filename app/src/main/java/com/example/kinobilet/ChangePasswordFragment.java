package com.example.kinobilet;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordFragment extends Fragment {

    private EditText currentPassInput, newPassInput, confirmPassInput;
    private TextView errorMessage;
    private FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        currentPassInput = view.findViewById(R.id.current_password_input);
        newPassInput = view.findViewById(R.id.new_password_input);
        confirmPassInput = view.findViewById(R.id.confirm_password_input);
        Button confirmBtn = view.findViewById(R.id.confirm_button);
        Button cancelBtn = view.findViewById(R.id.cancel_button);
        errorMessage = view.findViewById(R.id.error_message);

        auth = FirebaseAuth.getInstance();

        confirmBtn.setOnClickListener(v -> handleChangePassword());
        cancelBtn.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }

    private void handleChangePassword() {
        String currentPass = currentPassInput.getText().toString().trim();
        String newPass = newPassInput.getText().toString().trim();
        String confirmPass = confirmPassInput.getText().toString().trim();

        if (TextUtils.isEmpty(currentPass) || TextUtils.isEmpty(newPass) || TextUtils.isEmpty(confirmPass)) {
            errorMessage.setText("Все поля обязаны быть заполнены.");
            return;
        }

        if (!newPass.equals(confirmPass)) {
            errorMessage.setText("Пароль повторно введен неверно. Попробуйте ввести пароль заново.");
            return;
        }

        if (newPass.length() < 6) {
            errorMessage.setText("Пароль должен быть не менее 6 символов.");
            return;
        }

        FirebaseUser user = auth.getCurrentUser();
        if (user == null || user.getEmail() == null) {
            errorMessage.setText("Ошибка: пользователь не найден.");
            return;
        }

        user.reauthenticate(EmailAuthProvider.getCredential(user.getEmail(), currentPass))
                .addOnSuccessListener(unused -> {
                    user.updatePassword(newPass)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(getContext(), "Пароль обновлён", Toast.LENGTH_SHORT).show();
                                requireActivity().getSupportFragmentManager().popBackStack();
                            })
                            .addOnFailureListener(e -> {
                                errorMessage.setText("Ошибка при обновлении.");
                            });
                })
                .addOnFailureListener(e -> {
                    errorMessage.setText("Текущий пароль введен неверно.");
                });
    }
}
