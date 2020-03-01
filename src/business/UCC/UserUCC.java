package business.UCC;

import business.DTO.UserDTO;

public interface UserUCC {
    UserDTO getUserInfo(UserDTO user);

    boolean login(UserDTO userDTO);

    UserDTO register(UserDTO userDTO);
}
