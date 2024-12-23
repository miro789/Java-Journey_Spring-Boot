package vn.miro.service;

import vn.miro.dto.request.UserRequestDTO;

public interface UserService {
    int addUser(UserRequestDTO requestDTO);
}
