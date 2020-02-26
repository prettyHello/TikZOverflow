package be.ac.ulb.infof307.g09.model.business.UCC;

import be.ac.ulb.infof307.g09.model.business.DTO.UserDTO;

public interface UserUCC {
    boolean login(UserDTO userDTO);

    UserDTO register(UserDTO userDTO);
}
