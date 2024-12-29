package vn.miro.repository.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import vn.miro.model.User;
import vn.miro.util.Gender;

public class UserSpec {

    public static Specification<User> hasFirstName(String firstName) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("firstName"), "%" + firstName + "%");
    }

    public static Specification<User> notEqualGender(Gender gender) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get("gender"), gender);
    }
}
