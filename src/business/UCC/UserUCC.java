package business.UCC;

import business.DTO.UserDTO;

public interface UserUCC {
    UserDTO login(UserDTO userDTO);

    UserDTO register(UserDTO userDTO);
}
