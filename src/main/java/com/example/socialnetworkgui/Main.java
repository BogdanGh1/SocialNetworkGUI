package com.example.socialnetworkgui;

import com.example.socialnetworkgui.business.Service;
import com.example.socialnetworkgui.domain.Friendship;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.exceptions.RepoException;
import com.example.socialnetworkgui.exceptions.ValidationException;
import com.example.socialnetworkgui.infrastructure.Repository;
import com.example.socialnetworkgui.infrastructure.database.FriendshipDbRepository;
import com.example.socialnetworkgui.infrastructure.database.UserDbRepository;
import com.example.socialnetworkgui.presentation.UI;
import com.example.socialnetworkgui.validation.FriendshipValidator;
import com.example.socialnetworkgui.validation.UserValidator;
import com.example.socialnetworkgui.validation.Validator;

public class Main {
    public static void main(String[] args) throws ValidationException, RepoException {
        Validator<User> userValidator = new UserValidator();
        Repository<Long, User> userRepository = new UserDbRepository("jdbc:postgresql://localhost:5432/SocialNetwork","postgres","postgres",userValidator);
        Validator<Friendship> friendshipValidator = new FriendshipValidator();
        Repository<Long, Friendship> friendshipRepository = new FriendshipDbRepository("jdbc:postgresql://localhost:5432/SocialNetwork","postgres","postgres",friendshipValidator);
        Service service = new Service(userRepository,friendshipRepository);
        UI ui = new UI(service);
        ui.run();
    }
}