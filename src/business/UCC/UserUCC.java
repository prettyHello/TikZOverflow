package business.UCC;

import business.DTO.UserDTO;
import exceptions.BizzException;
import exceptions.FatalException;

public interface UserUCC {
    UserDTO getUserInfo(UserDTO user) throws BizzException, FatalException;

    void login(UserDTO userDTO) throws BizzException, FatalException;

    void register(UserDTO userDTO) throws BizzException, FatalException;

    void updateUserInfo(UserDTO userDTO) throws BizzException, FatalException;
}
