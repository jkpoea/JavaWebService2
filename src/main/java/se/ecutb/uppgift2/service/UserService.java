package se.ecutb.uppgift2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import se.ecutb.uppgift2.entity.User;
import se.ecutb.uppgift2.repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Cacheable(value = "userCache")
    public List<User> findAll(String name, boolean sortOnDate){
        var users = userRepository.findAll();
        log.info("Searching for all users");
        log.info("Fresh data");

        if (name != null){
            users.stream()
                    .filter(user -> user.getFirstName().toLowerCase().contains(name) ||
                            user.getLastName().toLowerCase().contains(name))
                    .collect(Collectors.toList());
        }
        if (sortOnDate) {
            users.sort(Comparator.comparing(User::getBirthDate));
        }
        return users;
    }

    @Cacheable(value = "userCache", key = "#id")
    public User findById(String id){
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Could not find book by id %s", id)));
    }

    @CachePut(value = "userCache")
    public User save(User user){
        user.setPassWord(passwordEncoder.encode(user.getPassWord()));
        return userRepository.save(user);
    }

    @CachePut(value = "userCache", key = "#id")
    public void update(String id, User user){
        if (!userRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Could not find the user by id %s", id));
        }
        user.setId(id);
        user.setPassWord(passwordEncoder.encode(user.getPassWord()));
        userRepository.save(user);
    }

    @CacheEvict(value = "userCache", key = "#id")
    public void delete(String id){
        if (!userRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Can't find user %s by id", id));
        }
        userRepository.deleteById(id);
    }


    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Could not find the user by username %s", userName)));
    }
}
