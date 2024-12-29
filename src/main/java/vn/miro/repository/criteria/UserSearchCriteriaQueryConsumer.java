package vn.miro.repository.criteria;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import vn.miro.model.User;

import java.util.function.Consumer;

public class UserSearchCriteriaQueryConsumer implements Consumer<SearchCriteria> {

    private CriteriaBuilder builder;
    private Predicate predicate;
    private Root root;


    public UserSearchCriteriaQueryConsumer(CriteriaBuilder builder, Predicate predicate, Root root) {
        this.builder = builder;
        this.predicate = predicate;
        this.root = root;
    }

    @Override
    public void accept(SearchCriteria param) {



    }

    public CriteriaBuilder getBuilder() {
        return builder;
    }

    public Root getRoot() {
        return root;
    }

    public Predicate getPredicate() {
        return predicate;
    }
}
