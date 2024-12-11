package vn.miro.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.miro.dto.request.UserRequestDTO;

@RestController
@RequestMapping("/user")
public class UserController {

    @PostMapping("/")
    public String addUser(@RequestBody UserRequestDTO userDTO){

        return "User added";

    }
}
