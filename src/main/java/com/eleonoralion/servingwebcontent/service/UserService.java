package com.eleonoralion.servingwebcontent.service;

import com.eleonoralion.servingwebcontent.entity.User;
import com.eleonoralion.servingwebcontent.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public boolean addUser(User user){
        userRepository.save(user);
        return true;
    }

    public boolean updateUser(User user){
        user.getUserInfo().setId(user.getId());
        userRepository.save(user);
        return true;
    }

    public boolean sendEmailMessage(User user){

        String message = String.format(MESSAGE,
                user.getUsername(),
                user.getActivationCode());

        mailSender.send(user.getEmail(), "Activation Code", message);
        return true;
    }

    public boolean activateCode(String code) {
        User user = userRepository.findByActivationCode(code);

        if(user == null){
            return false;
        }

        user.setActivationCode(null);
        user.setActive(true);
        user.setConfirmEmail(true);

        userRepository.save(user);
        return true;
    }

    public boolean checkUserAndEmail(String username, String email){
        if(userRepository.findByUsername(username) != null){
            return false;
        }
        if(userRepository.findByEmail(email) != null){
            return false;
        }
        return true;
    }

    public boolean findByUsernameAndNotThisId(String data, Long id){
        return userRepository.findByUsernameAndNotThisId(data, id) == null;
    }
    public boolean findByEmailAndNotThisId(String data, Long id){
        return userRepository.findByEmailAndNotThisId(data, id) == null;
    }
}
