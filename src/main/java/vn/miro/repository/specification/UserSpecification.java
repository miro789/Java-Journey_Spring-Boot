package vn.miro.repository.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import vn.miro.model.User;

public class UserSpecification implements Specification<User> {

    private SpecSearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        return switch (criteria.getOperation()) {
            case EQUALITY -> builder.equal(root.get(criteria.getKey()), criteria.getValue());
            case NEGATION -> builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
            case GREATER_THAN -> builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case LESS_THAN -> builder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case CONTAINS -> builder.like(root.get(criteria.getKey()), "%" + criteria.getValue().toString() + "%");
            case STARTS_WITH -> builder.like(root.get(criteria.getKey()), criteria.getValue().toString() + "%");
            case ENDS_WITH -> builder.like(root.get(criteria.getKey()), "%" + criteria.getValue().toString());
            case LIKE -> builder.like(root.get(criteria.getKey()), criteria.getValue().toString());
        };
    }
}
