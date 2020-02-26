package business.UCC;

import business.DTO.UserDTO;
import persistence.DALServices;
import persistence.DAO;

public class UserUCCImpl implements UserUCC {

    private final DALServices dalServices;
    private final DAO<UserDTO> userDAO;

    public UserUCCImpl(DALServices dalServices, DAO<UserDTO> userDAO) {
        this.dalServices = dalServices;
        this.userDAO = userDAO;
    }


    @Override
    public UserDTO login(UserDTO userDTO) {
        return null;
    }

    @Override
    public UserDTO register(UserDTO userDTO) {
        //add to DTO/UserImpl a method that check if fields are valid (or do that in javafx, as u wish)
        dalServices.startTransaction(); //we could start the transaction later since it's desktop, but if it was a website we would nee to do it before the checks to avoid race conditions
            //check if email already exists
            //userDTO.setPassword(       hash(userDTO.getPassword())         );
            int id = userDAO.create(userDTO);
            // if error
                // dalServices.rollback();
            //else
                dalServices.commit();
        userDTO.setUser_id(id);
        userDTO.setAuthorized(true);

        return  userDTO;
    }
}
