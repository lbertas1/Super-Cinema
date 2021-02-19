package bartos.lukasz.service.repositoryServices;

import bartos.lukasz.dto.createModel.CreateUser;
import bartos.lukasz.dto.getModel.GetUser;
import bartos.lukasz.enums.Role;
import bartos.lukasz.exception.ServiceException;
import bartos.lukasz.mappers.CreateModelMappers;
import bartos.lukasz.mappers.GetModelMappers;
import bartos.lukasz.model.User;
import bartos.lukasz.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<GetUser> saveAll(List<CreateUser> userDtos) {
        List<CreateUser> availableGetUser = userDtos
                .stream()
                .filter(createUser -> !checkUsernameInDatabase(createUser.getUsername()))
                .collect(Collectors.toList());

        if (!(availableGetUser.size() == userDtos.size())) throw new ServiceException("At least one from given logins is not available. The collection has not been saved");

        return userRepository
                .saveAll(userDtos
                        .stream()
                        .map(CreateModelMappers::toUser)
                        .peek(user -> user.setPassword(encodePassword(user.getPassword())))
                        .collect(Collectors.toList()))
                .stream()
                .map(GetModelMappers::toGetUser)
                .collect(Collectors.toList());
    }

    public GetUser save(CreateUser userDto) {
        userDto.setPassword(encodePassword(userDto.getPassword()));
        if (!checkUsernameInDatabase(userDto.getUsername())) {
            User user = CreateModelMappers.toUser(userDto);
            Optional<User> savedUser = userRepository.save(user);
            return GetModelMappers.toGetUser(savedUser.orElseThrow(() -> new ServiceException("User doesn't saved")));
        }
        else throw new ServiceException("Username isn't available");
    }

    public GetUser getUserById(Long id) {
        return GetModelMappers.toGetUser(userRepository.findById(id).orElseThrow(() -> new ServiceException("User not found")));
    }

    public GetUser getUserByUsernameAndPassword(String username, String password) {
        String encodedPassword = encodePassword(password);
        return GetModelMappers.toGetUser(userRepository.findUserByUsernameAndPassword(username, encodedPassword).orElseThrow(() -> new ServiceException("User doesn't found")));
    }

    public GetUser getUserByUsername(String username) {
        return GetModelMappers.toGetUser(userRepository.findUserByUsername(username).orElseThrow(() -> new ServiceException("User not found")));
    }

    public List<GetUser> getAllUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(GetModelMappers::toGetUser)
                .collect(Collectors.toList());
    }

    public GetUser createUser(String username, String password, String name, String surname, String email, int age) {
        User user = User
                .builder()
                .username(username)
                .password(encodePassword(password))
                .name(name)
                .surname(surname)
                .email(email)
                .age(age)
                .role(Role.USER.name())
                .build();

        if (user.getAge() < 18) throw new ServiceException("Sorry, only adults can set up an account");

        return GetModelMappers.toGetUser(userRepository.save(user).orElseThrow(() -> new ServiceException("User doesn't saved")));
    }

    public boolean checkUsernameInDatabase(String username) {
        if (Objects.isNull(username)) throw new ServiceException("Username is null");

        return userRepository
                .findUserByUsername(username)
                .isPresent();
    }

    public GetUser isPasswordValid(String username, String password) {
        String encodedPassword = encodePassword(password);
        return GetModelMappers
                .toGetUser(userRepository
                        .findUserByUsernameAndPassword(username, encodedPassword)
                        .orElseThrow(() -> new ServiceException("User has not been confirmed")));
    }

    public String encodePassword(String userPassword) {
        try {
            KeySpec spec = new PBEKeySpec(userPassword.toCharArray(), Objects.requireNonNull(getSecretFromProperties()), 10000, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return Arrays.toString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    private byte[] getSecretFromProperties() {

        try {
            Properties properties = new Properties();
            String propertiesFile = "application.properties";
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propertiesFile);
            if (inputStream != null) {
                properties.load(inputStream);
            }
            String secret = String.valueOf(properties.get("SECRET"));
            return secret.getBytes();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
