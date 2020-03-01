package persistence;

import business.DTO.UserDTO;

public class UserDAOMock implements  UserDAO {
    @Override
    public UserDTO find(UserDTO obj) {
        return null;
    }

    @Override
    public int create(UserDTO obj) {
        return 0;
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
    public boolean updateUser(UserDTO userDTO) {
        return false;
    }
}
