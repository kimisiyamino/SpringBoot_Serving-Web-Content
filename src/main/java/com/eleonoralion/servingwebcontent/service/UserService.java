package com.eleonoralion.servingwebcontent.service;

import com.eleonoralion.servingwebcontent.entity.Role;
import com.eleonoralion.servingwebcontent.entity.User;
import com.eleonoralion.servingwebcontent.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final MailSender mailSender;

    private static final String MESSAGE = "Hello, %s! \n" +
            "Welcome to Serving Web Content. Please, visit next link: http://localhost:8080/activate/%s";

    @Autowired
    public UserService(UserRepository userRepository, MailSender mailSender){
        this.userRepository = userRepository;
        this.mailSender = mailSender;
    }

    @Override
    public UserDetails loadUserByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public void addUser(User user){
        userRepository.save(user);
    }

    public void updateUser(User user){
        user.getUserInfo().setId(user.getId());
        userRepository.save(user);
    }

    public void sendActivationCode(User user){

        // Генерируем случайный код
        user.setActivationCode(UUID.randomUUID().toString());

        // Формируем сообщение
        String message = String.format(MESSAGE,
                user.getUsername(),
                user.getActivationCode());

        // Отправляем письмо
        mailSender.send(user.getEmail(), "Activation Code", message);
    }

    public boolean activateCode(String code) {

        // Ищем в БД пользователя с имеющимся кодом
        User user = userRepository.findByActivationCode(code);

        // Если пользователь не найден
        if(user == null){
            return false;
        }

        // Удаляем код у User и делаем юзера активным
        user.setActivationCode(null);
        user.setActive(true);
        user.setConfirmEmail(true);

        // Обновляем данные User
        userRepository.save(user);

        return true;
    }

    public boolean checkByUsername(String username){
        return userRepository.findByUsername(username) == null;
    }

    public boolean checkByEmail(String email){
        return userRepository.findByEmail(email) == null;
    }

    public boolean checkByUsernameAndNotThisId(String newUsername, Long id){
        return userRepository.findByUsernameAndNotThisId(newUsername, id) == null;
    }
    public boolean checkByEmailAndNotThisId(String newEmail, Long id){
        return userRepository.findByEmailAndNotThisId(newEmail, id) == null;
    }

    public List<User> findAllByOrderById() {
        return userRepository.findAllByOrderById();
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void fillUserRolesFromMap(User user, Map<String, String> map) {
        // Создаём Set из String наименований ролей из enum Role
        Set<String> roles = Arrays.stream(Role.values()).map(role -> role.name()).collect(Collectors.toSet());
        // Очищаем текущего User от roles
        user.getRoles().clear();
        // Ищем ключи из формы по именам ролей созданного нами Set, и добавляем нашему User, если таковые ключи равны
        for (String key : map.keySet()) {
            if(roles.contains(key)){
                user.getRoles().add(Role.valueOf(key));
            }
        }
    }
}
