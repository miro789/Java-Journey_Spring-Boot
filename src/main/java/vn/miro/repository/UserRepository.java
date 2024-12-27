package vn.miro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.miro.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
