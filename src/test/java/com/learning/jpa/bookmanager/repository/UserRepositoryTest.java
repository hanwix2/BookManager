package com.learning.jpa.bookmanager.repository;

import com.learning.jpa.bookmanager.domain.User;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("이름 역순으로 모든 user 출력")
    void findAllTest() {
        List<User> users = userRepository.findAll(Sort.by(Sort.Direction.DESC, "name"));

        users.forEach(System.out::println);
    }

    @Test
    @DisplayName("id 값으로 여러 user 찾기")
    void findAllByIdTest() {
        List<User> users = userRepository.findAllById(Lists.newArrayList(1L, 2L, 3L));

        users.forEach(System.out::println);
    }

    @Test
    @Transactional
    @DisplayName("id 값으로 한 명의 user 찾기 (getById 메소드)")
    void getByIdTest() {
        // getById()는 기본적으로 entity에 대해서 lazy한 로딩 지원 (Lazy Patch)
        // EntityManager에서 reference만 가지고 있고 실제 값을 구하는 시점에 세션을 통해 조회
        User user = userRepository.getById(1L);

        System.out.println(user);
    }

    @Test
    @DisplayName("id 값으로 한 명의 user 찾기 (findById 메소드)")
    void findByIdTest() {
        // findById는 EntityManager에서 직접 Entity 객체를 가져옴 (Eager patch)
        User user = userRepository.findById(1L).orElse(null);

        System.out.println(user);
    }

    @Test
    @DisplayName("user 한 명 추가")
    void saveTest() {
        User user1 = new User("jack", "jack@email.com");

        userRepository.save(user1);

        List<User> users = userRepository.findAll();

        users.forEach(System.out::println);
    }

    @Test
    @DisplayName("user 여러명 추가")
    void saveAllTest() {
        User user1 = new User("jack", "jack@email.com");
        User user2 = new User("steve", "steve@email.com");

        userRepository.saveAll(Lists.newArrayList(user1, user2));

        List<User> users = userRepository.findAll();

        users.forEach(System.out::println);
    }

}