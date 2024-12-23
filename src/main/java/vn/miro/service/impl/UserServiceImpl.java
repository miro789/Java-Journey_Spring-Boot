package vn.miro.service.impl;

import org.springframework.stereotype.Service;
import vn.miro.dto.request.UserRequestDTO;
import vn.miro.exception.ResourceNotFoundException;
import vn.miro.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public int addUser(UserRequestDTO requestDTO) {
        System.out.println("Save user to db");
        if (!requestDTO.getFirstName().equals("Halo")){
            throw new ResourceNotFoundException("Halo is not exist");
        }
        return 0;
    }
}
