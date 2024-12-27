package vn.miro.service;

import vn.miro.dto.request.UserRequestDTO;
import vn.miro.dto.response.PageResponse;
import vn.miro.dto.response.UserDetailResponse;
import vn.miro.model.User;
import vn.miro.util.UserStatus;

import java.util.List;

public interface UserService {
    long saveUser(UserRequestDTO requestDTO);
    void updateUser(long userId, UserRequestDTO requestDTO);
    void changeStatus(long userId, UserStatus status);
    void deleteUser(long userId);

    UserDetailResponse getUser(long userId);

    PageResponse<?> getAllUsersWithSortBy(int pageNo, int pageSize, String sortBy);
    PageResponse<?> getAllUsersWithSortByMultipleColumn(int pageNo, int pageSize, String... sorts);

}
