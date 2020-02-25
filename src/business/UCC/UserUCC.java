package business.UCC;

import business.DTO.UserDTO;

public interface UserUCC {
    boolean login(UserDTO userDTO);

    UserDTO register(UserDTO userDTO);
}
