package be.ac.ulb.infof307.g09.model.business.UCC;

import be.ac.ulb.infof307.g09.model.business.DTO.UserDTO;
import be.ac.ulb.infof307.g09.model.persistence.DALServices;
import be.ac.ulb.infof307.g09.model.persistence.DAO;

import be.ac.ulb.infof307.g09.model.business.User;

public class UserUCCImpl implements UserUCC {

    private final DALServices dalServices;
    private final DAO<UserDTO> userDAO;

    public UserUCCImpl(DALServices dalServices, DAO<UserDTO> userDAO) {
        this.dalServices = dalServices;
        this.userDAO = userDAO;
    }


    public boolean login(UserDTO user) {
        User usr = (User) user;
        try {
            this.dalServices.startTransaction();
            User userDb = (User) userDAO.getUser(user);
            /**
             * @TODO Implement the password verification
             **/
            /*
            if (userDb == null || !userDb.checkPassword(user.getPassword())) {
                Map<String, Object> errorsLog = new HashMap<String, Object>();
                errorsLog.put("password", "Le pseudo et le mot de passe ne correspondent pas.");
                throw new BizzException(UtilResponse.errorResponseMap(errorsLog));
            }
            */

            this.dalServices.commit();
        } finally {
            this.dalServices.rollback();
            return true;
        }
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
