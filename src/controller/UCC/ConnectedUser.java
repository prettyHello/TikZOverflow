package controller.UCC;

import controller.DTO.UserDTO;

/**
 * Singleton that contains the only connected user.
 */
public class ConnectedUser {

    /**
     * Container for the connected user
     */
    private static UserDTO connectedUser = null;

    static void setConnectedUser(UserDTO user) {
        connectedUser = user;
    }

    static void deleteConnectedUser() {
        connectedUser = null;
    }

    public static UserDTO getConnectedUser() {
        return connectedUser;
    }
}
