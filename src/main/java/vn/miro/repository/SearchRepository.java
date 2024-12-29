package vn.miro.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import vn.miro.dto.response.PageResponse;
import vn.miro.model.User;
import vn.miro.repository.criteria.SearchCriteria;
import vn.miro.repository.criteria.UserSearchCriteriaQueryConsumer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Repository
public class SearchRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public PageResponse<?> getAllUsersWithSortByColumnAndSearch(int pageNo, int pageSize, String search, String sortBy)
    {
        StringBuilder sqlQuery = new StringBuilder("select new vn.miro.dto.response.UserDetailResponse(u.id, u.firstName, u.lastName, u.email, u.phone) from User u where 1=1");
        if (StringUtils.hasLength(search)){
            sqlQuery.append(" and lower(u.firstName) like lower(:firstName)");
            sqlQuery.append(" or lower(u.lastName) like lower(:firstName)");
            sqlQuery.append(" or lower(u.email) like lower(:email)");
        }

        if (StringUtils.hasLength(sortBy)) {

            if (StringUtils.hasLength(sortBy)) {
                // firstName:asc
                Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
                Matcher matcher = pattern.matcher(sortBy);
                if (matcher.find()) {
                    sqlQuery.append(String.format(" order by u.%s %s", matcher.group(1), matcher.group(3)));
                }
            }

        }

        Query selectQuery = entityManager.createQuery(sqlQuery.toString());


        selectQuery.setFirstResult(pageNo);
        selectQuery.setMaxResults(pageSize);

        if (StringUtils.hasLength(search)){
            selectQuery.setParameter("firstName", String.format("%%%s%%", search));
            selectQuery.setParameter("firstName", "%" + search + "%");
            selectQuery.setParameter("email", "%" + search + "%");
        }

        List users = selectQuery.getResultList();

        System.out.println(users);
        // query list user

        // query the number of record
        StringBuilder sqlCountQuery = new StringBuilder("select count(*) from User u where 1=1");
        if (StringUtils.hasLength(search)){
            sqlCountQuery.append(" and lower(u.firstName) like lower(?1)");
            sqlCountQuery.append(" or lower(u.lastName) like lower(?2)");
            sqlCountQuery.append(" or lower(u.email) like lower(?3)");
        }

        Query selectCountQuery = entityManager.createQuery(sqlCountQuery.toString());
        if (StringUtils.hasLength(search)){
            selectCountQuery.setParameter(1, String.format("%%%s%%", search));
            selectCountQuery.setParameter(2, String.format("%%%s%%", search));
            selectCountQuery.setParameter(3, String.format("%%%s%%", search));
        }
        Long totalElements = (Long) selectCountQuery.getSingleResult();
        System.out.println(totalElements);

        Page<?> page = new PageImpl<Object>(users, PageRequest.of(pageNo, pageSize), totalElements);



        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(totalElements.intValue()/pageSize)
                .items(page.stream().toList())
                .build();
    }

    public PageResponse advanceSearchUser (int pageNo, int pageSize, String sortBy, String... search)
    {
        // firstName: T, lastName: T

        List<SearchCriteria> criteriaList = new ArrayList<>();
        // 1. lay ra danh sach user
        if (search != null) {
            for (String s : search) {
                // firstName:val
                Pattern pattern = Pattern.compile("(\\w+?)(:|>|<)(.*)");
                Matcher matcher = pattern.matcher(sortBy);
                if (matcher.find()) {
                    criteriaList.add(new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
                }
            }
        }

        // 2. lay ra so luong ban ghi
        List<User> users = getUsers(pageNo, pageSize, criteriaList, sortBy);


        Long totalElements = 1l;

        return PageResponse.builder()
                .pageNo(pageNo) // offset = vi tri cua ban ghi trong danh sach
                .pageSize(pageSize)
                .totalPage(0)
                .items(users)
                .build();

    }

    private List<User> getUsers(int pageNo, int pageSize, List<SearchCriteria> criteriaList, String sortBy) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User> root = query.from(User.class);

        // Xu ly cac dieu kien tim kiem
        Predicate predicate = criteriaBuilder.conjunction();
        UserSearchCriteriaQueryConsumer queryConsumer = new UserSearchCriteriaQueryConsumer(criteriaBuilder, predicate, root);


        criteriaList.forEach(queryConsumer);
        predicate = queryConsumer.getPredicate();

        query.where(predicate);

        // sort
        if (StringUtils.hasLength(sortBy)) {
            Pattern pattern = Pattern.compile("(\\w+?)(:)(asc|desc)");
            Matcher matcher = pattern.matcher(sortBy);
            if (matcher.find()) {
                String columnName = matcher.group(1);
                if (matcher.group(3).equalsIgnoreCase("desc")) {
                    query.orderBy(criteriaBuilder.desc(root.get(columnName)));
                } else {
                    query.orderBy(criteriaBuilder.asc(root.get(columnName)));
                }

            }
        }

        return entityManager.createQuery(query).setFirstResult(pageNo).setMaxResults(pageSize).getResultList();

    }

}
