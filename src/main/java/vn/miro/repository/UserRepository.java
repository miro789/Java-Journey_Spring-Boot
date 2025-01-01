package vn.miro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.miro.model.User;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    // @Query(value = "select * from User u inner join Address a on u.id = a.userId where a.city=:city)
    List<User> getAllUser(String city);

    // -- Distinct --
    // @Query(value = "select disctinct from User u where u.firstName=:fistName and u.lastName=:lastname ")


    // --Single field --
    // @Query(value = "select * from User u where u.email= ?1")
    List<User> findByEmail(String email);

    // --OR --
    // Query(value = "select * from User u where u.firstName:name or u.lastName:name")
    List<User> findByFirstNameOrLastName(String name);

    // -- Is, Equals --
    // @Query(value = "select * from User u where u.fistName=:name")
    List<User> findByFirstNameIs(String name);
    List<User> findByFirstNameEqual(String name);
    List<User> findByFirstName(String name);

    // -- Between --
    // @Query(value = "select * from User u where u.createdAt between ?1 and 2?")
    List<User> findByCreatedAtBetween(Date startDate, Date endDate);

    // -- Less than --
    // @Query(value = "select * from User u where u.age < :age")
    List<User> findByAgeLessThan(int age);
    List<User> findByAgeLessThanEquals();
    List<User> findByAgeGreaterThan();
    List<User> findByAgeGreaterThanEquals();

    // -- Before and After --

    // IsNull, Null
    // @Query(value = "select * from User u where u.age is null")
    List<User> findByAgeIsNull();

    // Like
    // @Query(value = "select * from User u where u.fistName like %:fistName%")
    List<User> findByLastNameLike(String firstName);

    // StatingWith
    // @Query(value = "select * from User u where u.lastName not like :lastName%")
    List<User> findByLastNameStartingWith(String lastName);

    // Containing
    List<User> findByLastNameContaining(String name);

    // Not
    List<User> findByLastNameNot(String name);

    // In
    List<User> findByAgeIn(Collection<Integer> ages);


    // True/ False
    List<User> findByActivatedTrue();


}
