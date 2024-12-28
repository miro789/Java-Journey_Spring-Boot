package vn.miro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.miro.configuration.Translator;
import vn.miro.dto.request.UserRequestDTO;
import vn.miro.dto.response.ResponseData;
import vn.miro.dto.response.ResponseError;
import vn.miro.dto.response.UserDetailResponse;
import vn.miro.exception.ResourceNotFoundException;
import vn.miro.service.UserService;
import vn.miro.service.impl.UserServiceImpl;
import vn.miro.util.UserStatus;

import java.util.List;

@RestController
@RequestMapping("/user")
@Validated
@Slf4j

@Tag(name = "User Controller")
@RequiredArgsConstructor
public class UserController {

//    @Autowired
//    private UserServiceImpl.UserService userService;
    private final UserService userService;

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

    @Operation(summary = "Add user", description = "API create new user")
    // @RequestMapping(method = RequestMethod.POST, path = "/", headers = "apiKey=v1.0")
    @PostMapping(value = "/", headers = "apiKey=v1.0")
    // @ResponseStatus(HttpStatus.CREATED)
//    public String addUser(@Valid @RequestBody UserRequestDTO userDTO){
//
//        return "User added";
//
//    }

    public ResponseData<Long> addUser(@Valid @RequestBody UserRequestDTO userDTO){
        log.info("Request add user = {} {}", userDTO.getFirstName(), userDTO.getLastName());

//        SampleDTO dto = SampleDTO.builder()
//                .id(1)
//                .name("Miro")
//                .build();

        try {
            long userId = userService.saveUser(userDTO);
            return new ResponseData<>(HttpStatus.CREATED.value(), Translator.toLocale("user.add.success"), userId);
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Add user fail");
        }

    }

    @Operation(summary = "Get user detail", description = "API get user detail")
    @GetMapping ("/{userId}")
    // @ResponseStatus(HttpStatus.OK)
    public ResponseData<UserDetailResponse> getUser(@PathVariable long userId) {
        log.info("Request get user detail by userId = {}", userId);
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "user", userService.getUser(userId));

        } catch (ResourceNotFoundException e) {
            log.error("errorMessage{}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());

        }

    }

    @Operation(summary = "Get user list per page", description = "Return user by pageNo and pageSize")
    @GetMapping("/list")
    // @ResponseStatus(HttpStatus.OK)
    public ResponseData<?> getAllUser(
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @Min(10) @RequestParam(defaultValue = "20", required = false) int pageSize,
            @RequestParam(required = false) String sortBy
            ) {
        log.info("Request get user list");
        return new ResponseData<>(HttpStatus.OK.value(), "users", userService.getAllUsersWithSortBy(pageNo, pageSize, sortBy));
    }

    @Operation(summary = "Get list of users with sort by multiple columns", description = "Send a request via this API to get user list by multiple columns ")
    @GetMapping("/list-with-sort-by-multiple-columns")
    // @ResponseStatus(HttpStatus.OK)
    public ResponseData<?> getAllUsersWithSortByMultipleColumn(
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @Min(10) @RequestParam(defaultValue = "20", required = false) int pageSize,
            @RequestParam(required = false) String... sort
    ) {
        log.info("Request get all of users with sort by multiple column");
        return new ResponseData<>(HttpStatus.OK.value(), "users", userService.getAllUsersWithSortByMultipleColumn(pageNo, pageSize, sort));
    }


    @Operation(summary = "Get list of users with sort by columns and search", description = "Return user by pageNo, pageSize and sort by multiple column ")
    @GetMapping("/list-with-sort-by-multiple-columns-search")
    // @ResponseStatus(HttpStatus.OK)
    public ResponseData<?> getAllUsersWithSortByColumnAndSearch(
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @Min(10) @RequestParam(defaultValue = "20", required = false) int pageSize,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sortBy
    ) {
        log.info("Request get all of users with sort by column and search");
        return new ResponseData<>(HttpStatus.OK.value(), "users", userService.getAllUsersWithSortByColumnAndSearch(pageNo, pageSize, search, sortBy));
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

    @Operation(summary = "Update user", description = "API update user")
    @PutMapping("/{userId}")
    // @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseData<?> updateUser(@PathVariable long userId, @Valid @RequestBody UserRequestDTO userDTO){
        log.info("Request update userId = {}", userId);
        try {
            userService.updateUser(userId, userDTO);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), Translator.toLocale("user.upd.success"));

        } catch (Exception e) {
            log.error("errorMessage{}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Update user fail");

        }

    }

    @Operation(summary = "Change status of user", description = "API change status of user")
    @PatchMapping("/{userId}")
    // @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseData<?> updateStatus(@Min(1) @PathVariable long userId, @RequestParam UserStatus status)
    {
        log.info("Request change user status, userId = {}", userId);

        try {
            userService.changeStatus(userId, status);
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "User status changed");

        } catch (Exception e) {
            log.error("errorMessage{}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Change status fail");

        }


    }

    @Operation(summary = "Delete user", description = "API delete user")
    @DeleteMapping("/{userId}")
    // @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseData<?> deleteUser(@PathVariable int userId)
    {
        System.out.println("Request delete userId=" + userId);

        try {
            userService.deleteUser(userId);
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), Translator.toLocale("user.del.success"));
        } catch (Exception e) {
            log.error("errorMessage{}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Delete user fail");
        }

    }


}
