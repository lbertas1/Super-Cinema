package bartos.lukasz.service.repositoryServices;

import bartos.lukasz.dto.createModel.CreateUser;
import bartos.lukasz.dto.getModel.GetUser;
import bartos.lukasz.enums.Role;
import bartos.lukasz.exception.ServiceException;
import bartos.lukasz.mappers.CreateModelMappers;
import bartos.lukasz.mappers.GetModelMappers;
import bartos.lukasz.model.User;
import bartos.lukasz.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private CreateUser createUser1;
    private CreateUser createUser2;

    private User user1;
    private User user2;
    private User user3;
    private User user4;
    private User user5;
    private User user6;

    private GetUser getUser1;
    private GetUser getUser2;

    private List<CreateUser> createUsers;
    private List<User> users;
    private List<User> users1;
    private List<GetUser> getUsers;

    @BeforeEach
    private void prepareData() {
        createUser1 = CreateUser.builder().name("name1").surname("surname").password("password").username("username").age(23).email("lala").build();
        createUser2 = CreateUser.builder().username("lbertas12").password("password").name("Lukas").surname("Surname").email("email1").age(26).build();

        user1 = CreateModelMappers.toUser(createUser1);
        user2 = CreateModelMappers.toUser(createUser2);
        user3 = CreateModelMappers.toUser(createUser1);
        user3.setPassword(userService.encodePassword(user3.getPassword()));
        user4 = CreateModelMappers.toUser(createUser2);
        user4.setPassword(userService.encodePassword(user4.getPassword()));
        user5 = CreateModelMappers.toUser(createUser1);
        user5.setId(1L);
        user5.setPassword(userService.encodePassword(user3.getPassword()));
        user6 = CreateModelMappers.toUser(createUser2);
        user6.setId(2L);
        user6.setPassword(userService.encodePassword(user4.getPassword()));

        getUser1 = GetModelMappers.toGetUser(user5);
        getUser2 = GetModelMappers.toGetUser(user6);

        createUsers = List.of(createUser1, createUser2);
        users = List.of(user3, user4);
        users1 = List.of(user5, user6);
        getUsers = List.of(getUser1, getUser2);
    }

    @Test
    public void save() {
        when(userRepository.save(user3)).thenReturn(Optional.of(user5));
        assertEquals(getUser1, userService.save(createUser1));
    }

    @Test
    public void save_throwExceptionWhenUsernameIsNotEmpty() {
        when(userRepository.findUserByUsername("username")).thenReturn(Optional.of(user1));
        assertThrows(ServiceException.class, () -> userService.save(createUser1));
    }

    @ParameterizedTest()
    @ValueSource(strings = {"username", "lbertas12"})
    public void saveAll(String username) {
        User user7 = users
                .stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElseThrow();

        User user8 = users1
                .stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElseThrow();

        GetUser getUser = getUsers
                .stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElseThrow();

        CreateUser createUser = createUsers
                .stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElseThrow();

        when(userRepository.save(user7)).thenReturn(Optional.of(user8));
        assertThat(userService.save(createUser), equalTo(getUser));
    }

    @Test
    public void getUserByUsername() {
        String username = "username";
        when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(user5));
        assertEquals(getUser1, userService.getUserByUsername(username));
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2})
    public void getUserById(long userId) {
        GetUser getUser3 = getUsers
                .stream()
                .filter(getUser -> getUser.getId().equals(userId))
                .findFirst()
                .orElseThrow();

        when(userRepository.findById(userId)).thenReturn(Optional.of(GetModelMappers.toUser(getUser3)));
        assertEquals(getUser3, userService.getUserById(userId));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    public void checkUsernameInDatabase(int userIndex) {
        when(userRepository.findUserByUsername(getUsers.get(userIndex).getUsername())).thenReturn(Optional.of(GetModelMappers.toUser(getUsers.get(userIndex))));
        assertTrue(userService.checkUsernameInDatabase(getUsers.get(userIndex).getUsername()));
    }

    @Test
    public void checkUsernameInDatabase_returnFalseWhenUsernameNull() {
        assertThrows(ServiceException.class, () -> userService.checkUsernameInDatabase(null));
    }

    @Test
    public void checkUsernameInDatabase_returnFalseWhenUsernameDoesntExistsInDatabase() {
        when(userRepository.findUserByUsername("lbertas1970")).thenReturn(Optional.empty());
        assertFalse(userService.checkUsernameInDatabase("lbertas1970"));
    }

    @Test
    public void getUserByUsernameAndPassword() {
        when(userRepository.findUserByUsernameAndPassword(user3.getUsername(), user3.getPassword())).thenReturn(Optional.of(user5));
        assertEquals(getUser1.getUsername(), userService.getUserByUsernameAndPassword(user1.getUsername(), user1.getPassword()).getUsername());
    }

    @Test
    public void createUser() {
        String name = "name";
        String surname = "name";
        String password = "password";
        String username = "username";
        String email = "email";
        int age = 23;
        Role role = Role.USER;

        String encodedPassword = userService.encodePassword(password);

        User user = User
                .builder()
                .name(name)
                .surname(surname)
                .username(username)
                .password(encodedPassword)
                .age(age)
                .email(email)
                .role(Role.USER.name())
                .build();

        User user5 = User
                .builder()
                .id(1L)
                .name(name)
                .surname(surname)
                .username(username)
                .password(encodedPassword)
                .password(password)
                .age(age)
                .email(email)
                .role(Role.USER.name())
                .build();

        GetUser getUser = GetModelMappers.toGetUser(user5);

        when(userRepository.save(user)).thenReturn(Optional.of(user5));

        userService.createUser(username, password, name, surname, email, age);

        assertAll(() -> {
            assertEquals(getUser, userService.createUser(username, password, name, surname, email, age));
            assertNotNull(getUser.getId());
            assertThat(getUser.getAge(), greaterThan(17));
        });
    }

    @Test
    public void getAllUsers() {
        when(userRepository.findAll()).thenReturn(users1);
        assertEquals(getUsers, userService.getAllUsers());
    }

    @Test
    public void createUser_throwsExceptionWhenUserIsToYoung() {
        String name = "name";
        String surname = "name";
        String password = "password";
        String username = "username";
        String email = "email";
        int age = 23;
        Role role = Role.USER;

        assertThrows(ServiceException.class, () -> userService.createUser(username, password, name, surname, email, age));
    }

    @Test
    public void encodePassword() {
        String password = "password";
        String encodedPassword = userService.encodePassword(password);

        assertAll(() -> {
            assertNotNull(encodedPassword);
            assertNotEquals(password, encodedPassword);
        });
    }
}