package lotus.ma.userservice.services;


import lombok.AllArgsConstructor;
import lotus.ma.userservice.dao.repositories.UserRepository;
import lotus.ma.userservice.dto.CustomUser;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository customerRepository;

    public List<CustomUser> allUsers() {
        List<CustomUser> users = customerRepository.findAll()
                .stream()
                .map(customer -> CustomUser.builder().user(customer).build())
                .collect(Collectors.toList());

        return users;
    }
}