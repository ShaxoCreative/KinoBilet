package com.example.kinobilet;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class RegisterFragment extends Fragment {

    private EditText emailInput, passwordInput, confirmPasswordInput;
    private TextView errorMessage;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        mAuth = FirebaseAuth.getInstance();

        emailInput = view.findViewById(R.id.register_email);
        passwordInput = view.findViewById(R.id.register_password);
        confirmPasswordInput = view.findViewById(R.id.register_confirm_password);
        Button registerBtn = view.findViewById(R.id.register_button);
        TextView switchToLogin = view.findViewById(R.id.to_login);
        errorMessage = view.findViewById(R.id.error_message);

        registerBtn.setOnClickListener(v -> registerUser());
        switchToLogin.setOnClickListener(v ->
                ((LogInActivity) getActivity()).loadFragment(new LoginFragment()));

        return view;
    }

    private void registerUser() {
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        String confirmPassword = confirmPasswordInput.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            errorMessage.setText("Все поля регистрации обязаны быть заполнены.");
            return;
        }

        if (password.length() < 6) {
            errorMessage.setText("Пароль должен быть не менее 6 символов.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            errorMessage.setText("Пароль повторно введен неверно. Попробуйте ввести пароль заново.");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ((LogInActivity) getActivity()).goToMainActivity();
                    } else {
                        errorMessage.setText("Аккаунт с такой почтой уже существует. Попробуйте войти в аккаунт.");
                    }
                });
    }
}