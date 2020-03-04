package persistence;

import business.DTO.UserDTO;

public class UserDAOMock implements  UserDAO {
    @Override
    public UserDTO find(UserDTO obj) {
        return null;
    }

    @Override
    public void create(UserDTO obj) {
    }

    @Override
    public Long update(UserDTO obj) {
        return null;
    }

    @Override
    public void delete(UserDTO obj) {

    }

    @Override
    public UserDTO getUser(UserDTO usrAuth) {
        return null;
    }

    @Override
    public void updateUser(UserDTO userDTO){};
}
