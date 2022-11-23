package com.example.socialnetworkgui.validation;

import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.exceptions.ValidationException;

import java.util.Objects;
import java.util.regex.Pattern;

public class UserValidator implements Validator<User> {

    public void validate(User user) throws ValidationException {
        String errorMsg = "";
        if(Objects.equals(user.getName(), "")){
            errorMsg += "The name can't be empty\n";
        }
        if(user.getName().contains(" "))
            errorMsg += "The name can't contain space\n";

        if(!Pattern.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", user.getEmail()))
            errorMsg += "The email is invalid\n";

        if(errorMsg.length() > 0)
            throw new ValidationException(errorMsg);
    }
}
