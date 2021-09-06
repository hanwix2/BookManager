package com.learning.jpa.bookmanager.repository;

import com.learning.jpa.bookmanager.domain.User;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.endsWith;

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
    void flushTest() {
        // flush는 DB 반영 시점을 조절 (log상으로는 확인 불가)
        userRepository.saveAndFlush(new User("new martin", "newMartin@email.com"));

//        // Or
//        userRepository.save(new User("new martin", "newMartin@email.com"));
//        userRepository.flush();

        userRepository.findAll().forEach(System.out::println);
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

    @Test
    @DisplayName("현재 저장되어 있는 레코드 수 출력")
    void countTest() {
        long count = userRepository.count();

        System.out.println(count);
    }

    @Test
    @DisplayName("1번 user가 존재하는지 출력")
    void existsTest() {
        // 실제 실행 쿼리를 확인해 보면 count 쿼리로 처리한다.
        boolean exists = userRepository.existsById(1L);

        System.out.println(exists);
    }

    @Test
    @DisplayName("1번 user 삭제 (delete)")
    void deleteTest() {
        userRepository.delete(userRepository.findById(1L).orElseThrow(RuntimeException::new));
    }

    @Test
    @DisplayName("1번 user 삭제 (deleteById)")
    void deleteByIdTest() {
        // 삭제하기 전 해당 엔티티가 실제로 존재하는지의 여부 확인으로 SELECT 쿼리 진행 된다.
        userRepository.deleteById(1L);
    }

    @Test
    @DisplayName("모든 user 삭제")
    void deleteAllTest01() {
        // deleteAll은 레코드가 존재하는지 먼저 확인(SELECT)하고
        // 각각의 모든 데이터를 삭제하는 쿼리(DELETE)를 수행한다. (레코드 수만큼)
        // -> 성능 이슈 발생 가능성이 높다.
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("지정한 여러 user 삭제")
    void deleteAllTest02() {
        // deleteAllTest01 와 마찬가지로 지우려는 데이터 수만큼 DELETE 쿼리 수행
        userRepository.deleteAll(userRepository.findAllById(Lists.newArrayList(1L, 3L)));
    }

    @Test
    @DisplayName("지정한 여러 user 삭제 (deleteAllInBatch)")
    void deleteAllInBatchTest() {
        // 데이터를 지우기 전 확인(SELECT) 작업을 수행하지 않고
        // DELETE 쿼리를 한번만 수행한다. (OR 연산)
        userRepository.deleteAllInBatch(userRepository.findAllById(Lists.newArrayList(1L, 3L)));
    }

    @Test
    @DisplayName("페이징을 적용하여 user 출력")
    void pagingTest() {
        Page<User> users = userRepository.findAll(PageRequest.of(1, 3));

        System.out.println("page: " + users);
        System.out.println("totalElements: " + users.getTotalElements());
        System.out.println("totalPages: " + users.getTotalPages());
        System.out.println("numberOfElements: " + users.getNumberOfElements());
        System.out.println("sort: " + users.getSort());
        System.out.println("size: " + users.getSize());

        users.getContent().forEach(System.out::println);
    }

    @Test
    void queryByExampleTest01() {
        // QueryByExample(QBE): Entity를 Example로 만들고 Matcher를 추가하여 선언함으로 필요한 쿼리를 만드는 방법
        // 단점: 문자열에 국한 / 복잡한 쿼리에 대해선 QueryDsl 사용
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("name")
                .withMatcher("email", endsWith());

        Example<User> example = Example.of(new User("ma", "email.com"), matcher);

        userRepository.findAll(example).forEach(System.out::println);
    }

}