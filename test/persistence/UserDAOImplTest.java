package persistence;

import business.DTO.UserDTO;
import business.factories.UserFactory;
import business.factories.UserFactoryImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;
import utilities.DAOTestConfigurationHolder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserDAOImplTest {

    DALServices dalServices;
    UserFactory userFactory;
    DAO<UserDTO> userDAO;


    @BeforeEach
    void setUp() {
        /*this.dalServices =  DAOTestConfigurationHolder.getDalServices();
        this.userFactory = DAOTestConfigurationHolder.getUserFactory();
        this.userDAO = DAOTestConfigurationHolder.getUserDAO();*/
        dalServices = new DALServicesImpl();
        userFactory = new UserFactoryImpl();
        userDAO = new UserDAOImpl(dalServices, userFactory);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Rollback
    void create() {

        // Test1: simple, working insert
        UserDTO user = userFactory.createUser();
        user.setFirst_name("ben");
        user.setPassword("pass");
        user.setSalt("salt");
        user.setEmail("mail@mail.be");
        user.setLast_name("ber");
        user.setPhone("0032");
        user.setRegister_date(LocalDateTime.now().toString());

        userDAO.create(user);
        UserDTO result = userDAO.getUser(user);

        assertEquals(user.getFirst_name(), result.getFirst_name(), "First name different");
        assertEquals(user.getEmail(), result.getEmail(), "Email different");
        assertEquals(user.getPassword(),user.getPassword(), "Password different");




        // Test2: error same email

        // Test3: error same salt

        // Test4: error same phone number




        /*
        List<DepartmentEntity> departments = departmentDAO.getAllDepartments();
        List<EmployeeEntity> employees = employeeDAO.getAllEmployees();

        Assert.assertEquals(1, departments.size());
        Assert.assertEquals(1, employees.size());

        Assert.assertEquals(department.getName(), departments.get(0).getName());
        Assert.assertEquals(employee.getEmail(), employees.get(0).getEmail());
        */

    }

    @Test
    @Rollback
    void getUser() {
    }

    @Test
    @Rollback
    void find() {
    }

    @Test
    @Rollback
    void update() {
    }

    @Test
    @Rollback
    void delete() {
    }
}