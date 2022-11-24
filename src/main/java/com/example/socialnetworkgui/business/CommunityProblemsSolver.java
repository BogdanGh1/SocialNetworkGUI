package com.example.socialnetworkgui.business;

import com.example.socialnetworkgui.domain.Friendship;
import com.example.socialnetworkgui.domain.User;
import com.example.socialnetworkgui.exceptions.RepoException;
import com.example.socialnetworkgui.infrastructure.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommunityProblemsSolver {

    private final Repository<Long, User> userRepository;

    private final Repository<Long, Friendship> friendshipRepository;

    private int maxRoadLength = 0;

    private List<User> mostSociableCommunity;

    public CommunityProblemsSolver(Repository<Long, User> userRepository, Repository<Long, Friendship> friendshipRepository) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
    }

    private void dfs(User user, HashMap<Long, Boolean> visited) throws RepoException {
        visited.put(user.getId(), true);
        List<Friendship> friendships = user.getFriendshipList();
        for (Friendship fr : friendships) {
            if (!visited.get(fr.getIdFriend())) {
                dfs(userRepository.findOne(fr.getIdFriend()), visited);
            }
        }
    }

    public int getNumberOfCommunities() throws RepoException {
        int nr = 0;
        HashMap<Long, Boolean> visited = new HashMap<>();
        Iterable<User> users = userRepository.findAll();
        for (User user : users) {
            visited.put(user.getId(), false);
        }
        for (User user : users) {
            if (!visited.get(user.getId())) {
                dfs(user, visited);
                nr++;
            }
        }
        return nr;
    }

    private boolean visited(List<User> currentUsers, long id) {
        for (User user : currentUsers) {
            if (user.getId() == id)
                return true;
        }
        return false;
    }

    /**
     * Finds the most sociable community
     *
     * @param currentUsers - list of the users, mast contain only one user
     * @throws RepoException
     */
    private void bkt(List<User> currentUsers) throws RepoException {
        if (currentUsers.size() > maxRoadLength) {
            maxRoadLength = currentUsers.size();
            mostSociableCommunity = new ArrayList<>();
            mostSociableCommunity.addAll(currentUsers);
        }
        User user = currentUsers.get(currentUsers.size() - 1);
        List<Friendship> friendships = user.getFriendshipList();
        for (Friendship fr : friendships) {
            if (!visited(currentUsers, fr.getIdFriend())) {
                currentUsers.add(userRepository.findOne(fr.getIdFriend()));
                bkt(currentUsers);
                currentUsers.remove(userRepository.findOne(fr.getIdFriend()));
            }
        }
    }

    public List<User> getMostSociableCommunity() throws RepoException {
        Iterable<User> users = userRepository.findAll();
        for (User user : users) {
            List<User> currentUsers = new ArrayList<>();
            currentUsers.add(user);
            bkt(currentUsers);
        }
        return mostSociableCommunity;
    }
}
