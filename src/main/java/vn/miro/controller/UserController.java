package vn.miro.controller;

import org.springframework.web.bind.annotation.*;
import vn.miro.dto.request.UserRequestDTO;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

//    @PostMapping(value = "/", headers = "apiKey=v1.0")
    @RequestMapping(method = RequestMethod.POST, path = "/", headers = "apiKey=v1.0")
    public String addUser(@RequestBody UserRequestDTO userDTO){

        return "User added";

    }

    @GetMapping ("/{userId}")
    public UserRequestDTO getUser(@PathVariable int userId) {
        System.out.println("Request get user detail by userId");
        return new UserRequestDTO("Miro", "Doan", "phone", "email");
    }

    @GetMapping("/list")
    public List<UserRequestDTO> getAllUser(
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        System.out.println("Request get user list");
        return List.of(new UserRequestDTO("Miro", "Doan", "phone", "email"),
                new UserRequestDTO("Miro", "Doan", "phone", "email"));
    }


    @PutMapping("/{userId}")
    public String updateUser(@PathVariable int userId, @RequestBody UserRequestDTO userDTO){
        System.out.println("Request update userId=" + userId);
        return "User updated";
    }

    @PatchMapping("/{userId}")
    public String changeStatus(@PathVariable int userId, @RequestParam(required = false) boolean status)
    {
        System.out.println("Request change user status, userId="+ userId);
        return "User status changed";
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable int userId)
    {
        System.out.println("Request delete userId=" + userId);
        return "User deleted";
    }



}
