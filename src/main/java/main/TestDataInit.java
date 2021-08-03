//package main;
//
//import main.entity.Good;
//import main.entity.User;
//import main.repository.GoodRepository;
//import main.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import java.util.Collections;
//
//@Component
//public class TestDataInit implements CommandLineRunner {
//
//    @Autowired
//    GoodRepository goodRepository;
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    PasswordEncoder pwdEncoder;
//
//    @Override
//    public void run(String... args) throws Exception {
////        goodRepository.save(new Good("Chicha", 8f));
////        goodRepository.save(new Good("Muchacha", 9f));
//
//        userRepository.save(new User("user", pwdEncoder.encode("user"), Collections.singletonList("ROLE_USER")));
//        userRepository.save(new User("admin", pwdEncoder.encode("admin"), Collections.singletonList("ROLE_ADMIN")));
//    }
//}
