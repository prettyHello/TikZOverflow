package be.ac.ulb.infof307.g09.controller.UCC;

import be.ac.ulb.infof307.g09.controller.DTO.UserDTO;

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
