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
    public void update(UserDTO obj) {
    }

    @Override
    public void delete(UserDTO obj) {

    }

    @Override
    public UserDTO getUser(UserDTO usrAuth) {
        return null;
    }


}
