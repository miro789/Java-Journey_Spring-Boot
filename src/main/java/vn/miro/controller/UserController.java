package vn.miro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.miro.configuration.Translator;
import vn.miro.dto.request.SampleDTO;
import vn.miro.dto.request.UserRequestDTO;
import vn.miro.dto.response.ResponseData;
import vn.miro.dto.response.ResponseError;
import vn.miro.dto.response.ResponseSuccess;
import vn.miro.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/user")
@Validated
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    // Summary for Post mapping
//    @Operation(summary = "summary", description = "description", responses = {
//            @ApiResponse(responseCode = "201", description = "User added successfully",
//                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
//                            examples = @ExampleObject(name = "ex name", summary = "ex summary",
//                            value = """
//                                    {
//                                        "status": 201,
//                                        "message": "User added successfully",
//                                        "data": 1
//                                    }
//                                    """
//                            )))
//    })

    // @RequestMapping(method = RequestMethod.POST, path = "/", headers = "apiKey=v1.0")
    @PostMapping(value = "/", headers = "apiKey=v1.0")
    // @ResponseStatus(HttpStatus.CREATED)
//    public String addUser(@Valid @RequestBody UserRequestDTO userDTO){
//
//        return "User added";
//
//    }

    public ResponseData<Integer> addUser(@Valid @RequestBody UserRequestDTO userDTO){
        log.info("Request add user = {} {}", userDTO.getFirstName(), userDTO.getLastName());

//        SampleDTO dto = SampleDTO.builder()
//                .id(1)
//                .name("Miro")
//                .build();

        try {
            userService.addUser(userDTO);
            return new ResponseData<>(HttpStatus.CREATED.value(), Translator.toLocale("user.add.success"), 1);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Save fail");
        }

    }

    @GetMapping ("/{userId}")
    // @ResponseStatus(HttpStatus.OK)
    public ResponseData<UserRequestDTO> getUser(@PathVariable int userId) {
        log.info("Request get user detail by userId = {}", userId);
        return new ResponseData<>(HttpStatus.OK.value(), "user", new UserRequestDTO("Miro", "Doan", "phone", "email"));
    }

    @GetMapping("/list")
    // @ResponseStatus(HttpStatus.OK)
    public ResponseData<List<UserRequestDTO>> getAllUser(
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        log.info("Request get user list");
        return new ResponseData<>(HttpStatus.OK.value(), "users", List.of(new UserRequestDTO("Miro", "Doan", "phone", "email"),
                new UserRequestDTO("Miro", "Doan", "phone", "email")));
    }


        // Summary for PUT mapping
//    @Operation(summary = "summary", description = "description", responses = {
//            @ApiResponse(responseCode = "202", description = "User added successfully",
//                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
//                            examples = @ExampleObject(name = "ex name", summary = "ex summary",
//                                    value = """
//                                    {
//                                        "status": 202,
//                                        "message": "User updated successfully",
//                                        "data": null
//                                    }
//                                    """
//                            )))
//    })

    @PutMapping("/{userId}")
    // @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseData<?> updateUser(@PathVariable int userId, @Valid @RequestBody UserRequestDTO userDTO){
        log.info("Request update userId = {}", userId);
        return new ResponseData<>(HttpStatus.ACCEPTED.value(), Translator.toLocale("user.upd.success"));
    }

    @PatchMapping("/{userId}")
    // @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseData<?> updateStatus(@Min(1) @PathVariable int userId, @RequestParam(required = false) boolean status)
    {
        log.info("Request change user status, userId = {}", userId);
        return new ResponseData<>(HttpStatus.ACCEPTED.value(), "User status changed");
    }

    @DeleteMapping("/{userId}")
    // @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseData<?> deleteUser(@PathVariable int userId)
    {
        System.out.println("Request delete userId=" + userId);
        return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "User deleted");
    }



}
