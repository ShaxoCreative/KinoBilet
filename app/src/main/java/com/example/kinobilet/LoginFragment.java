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

public class LoginFragment extends Fragment {

    private EditText emailInput, passwordInput;
    private TextView errorMessage;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mAuth = FirebaseAuth.getInstance();

        emailInput = view.findViewById(R.id.login_email);
        passwordInput = view.findViewById(R.id.login_password);
        Button loginBtn = view.findViewById(R.id.login_button);
        TextView switchToRegister = view.findViewById(R.id.to_register);
        errorMessage = view.findViewById(R.id.error_message);
        TextView forgotPasswordText = view.findViewById(R.id.forgot_password_text);


        forgotPasswordText.setOnClickListener(v -> {
            ((LogInActivity) requireActivity()).loadFragment(new ResetPasswordFragment());
        });

        loginBtn.setOnClickListener(v -> loginUser());
        switchToRegister.setOnClickListener(v ->
                ((LogInActivity) getActivity()).loadFragment(new RegisterFragment()));

        return view;
    }

    private void loginUser() {
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            errorMessage.setText("Все поля авторизации обязаны быть заполнены.");
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ((LogInActivity) getActivity()).goToMainActivity();
                    } else {
                        errorMessage.setText("Неверный ввод данных. Пожалуйста, попробуйте попытку авторизации снова. ");
                    }
                });
    }
}
