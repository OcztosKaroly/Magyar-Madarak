package com.example.magyar_madarak.data.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.magyar_madarak.data.model.user.User;
import com.example.magyar_madarak.data.repository.UserRepository;

public class UserViewModel extends AndroidViewModel {
    private UserRepository userRepository;

    public UserViewModel(Application application) {
        super(application);

        userRepository = new UserRepository(application);
    }

    public void insertUser(User user) {
        userRepository.insertUser(user);
    }

    public LiveData<User> getUserById(String userId) {
        return userRepository.getUserById(userId);
    }

    public void updateUser(User user) {
        userRepository.updateUser(user);
    }

    public void deleteUserById(String string) {
        userRepository.deleteUserById(string);
    }

    public void deleteUser(User user) {
        userRepository.deleteUser(user);
    }
}
