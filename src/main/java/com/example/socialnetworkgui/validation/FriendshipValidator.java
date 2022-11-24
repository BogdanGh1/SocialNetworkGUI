package com.example.socialnetworkgui.validation;

import com.example.socialnetworkgui.domain.Friendship;
import com.example.socialnetworkgui.exceptions.ValidationException;

public class FriendshipValidator implements Validator<Friendship> {

    public void validate(Friendship friendship) throws ValidationException {
        String errorMsg = "";
        if (friendship.getIdUser() < 0) {
            errorMsg += "The user id is invalid\n";
        }
        if (friendship.getIdFriend() < 0) {
            errorMsg += "The friend id is invalid\n";
        }
        if (errorMsg.length() > 0)
            throw new ValidationException(errorMsg);
    }
}
