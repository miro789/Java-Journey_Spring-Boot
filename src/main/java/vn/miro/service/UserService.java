package vn.miro.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import vn.miro.dto.request.UserRequestDTO;
import vn.miro.dto.response.PageResponse;
import vn.miro.dto.response.UserDetailResponse;
import vn.miro.model.User;
import vn.miro.util.UserStatus;

import java.util.Date;
import java.util.List;

public interface UserService {
    long saveUser(UserRequestDTO requestDTO);
    void updateUser(long userId, UserRequestDTO requestDTO);
    void changeStatus(long userId, UserStatus status);
    void deleteUser(long userId);



    UserDetailResponse getUser(long userId);

    PageResponse<?> getAllUsersWithSortBy(int pageNo, int pageSize, String sortBy);
    PageResponse<?> getAllUsersWithSortByMultipleColumn(int pageNo, int pageSize, String... sorts);

    PageResponse<?> getAllUsersWithSortByColumnAndSearch(int pageNo, int pageSize, String search, String sortBy);
    PageResponse<?> advanceSearchByCriteria(int pageNo, int pageSize, String sortBy, String... search);

    PageResponse<?> advanceSearchBySpecification(Pageable pageable, String[] user, String[] address);
}
