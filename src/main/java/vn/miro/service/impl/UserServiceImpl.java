package vn.miro.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import vn.miro.dto.request.UserRequestDTO;
import vn.miro.dto.response.PageResponse;
import vn.miro.dto.response.UserDetailResponse;
import vn.miro.exception.ResourceNotFoundException;
import vn.miro.model.User;
import vn.miro.repository.SearchRepository;
import vn.miro.repository.UserRepository;
import vn.miro.repository.specification.UserSpec;
import vn.miro.repository.specification.UserSpecification;
import vn.miro.repository.specification.UserSpecificationsBuilder;
import vn.miro.service.UserService;
import vn.miro.util.Gender;
import vn.miro.util.UserStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final SearchRepository repository;

    @Override
    public long saveUser(UserRequestDTO requestDTO) {
        User user = User.builder()
                .firstName(requestDTO.getFirstName())
                .lastName(requestDTO.getLastName())
                .dateOfBirth(requestDTO.getDateOfBirth())
                .phone(requestDTO.getPhone())
                .email(requestDTO.getEmail())
                .build();

        userRepository.save(user);
        log.info("User has saved!");
        return user.getId();
    }

    @Override
    public void updateUser(long userId, UserRequestDTO requestDTO) {
        User user = getUserById(userId);
        user.setFirstName(requestDTO.getFirstName());
//        if (StringUtils.hasLength(requestDTO.getPhone())) {
//            // check duplicate
//            // send sms
//        }

        userRepository.save(user);
        log.info("User updated successfully");

    }

    @Override
    public void changeStatus(long userId, UserStatus status) {
        User user = getUserById(userId);
       //  user.setStatus(status);
        userRepository.save(user);
        log.info("status changed");
    }

    @Override
    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
        log.info("User deleted, userId = ");

    }

    @Override
    public UserDetailResponse getUser(long userId) {
        User user = getUserById(userId);
        UserDetailResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .email(user.getEmail())
                .build();
       return null;
    }

    @Override
    public PageResponse<?> getAllUsersWithSortBy(int pageNo, int pageSize, String sortBy) {int p = 0;
        if (pageNo > 0) {
            pageNo = pageNo - 1;
        }

        List<Sort.Order> sorts = new ArrayList<>();

        // neu co gia tri
        if (StringUtils.hasLength(sortBy)) {
            // firstName:asc|desc
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sortBy);
            if (matcher.find()) {
                if (matcher.group(3).equalsIgnoreCase("asc")) {
                    sorts.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                } else {
                    sorts.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                }
            }
        }

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sorts));

        Page<User> users = userRepository.findAll(pageable);


        List<UserDetailResponse> response = users.stream().map(user -> UserDetailResponse.builder()
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .phone(user.getPhone())
                        .build())
                .toList();

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(users.getTotalPages())
                .items(response)
                .build();
    }

    @Override
    public PageResponse<?> getAllUsersWithSortByMultipleColumn(int pageNo, int pageSize, String... sorts) {
        int p = 0;
        if (pageNo > 0) {
            p = pageNo - 1;
        }

        List<Sort.Order> orders = new ArrayList<>();

        if (sorts != null) {
            for (String sortBy : sorts) {
                // firstName:asc|desc
                Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
                Matcher matcher = pattern.matcher(sortBy);
                if (matcher.find()) {
                    if (matcher.group(3).equalsIgnoreCase("asc")) {
                        orders.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                    } else {
                        orders.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                    }
                }
            }
        }

        Pageable pageable = PageRequest.of(p, pageSize, Sort.by(sorts));

        Page<User> users = userRepository.findAll(pageable);

        List<UserDetailResponse> response = users.stream().map(user -> UserDetailResponse.builder()
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .phone(user.getPhone())
                        .build())
                .toList();

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(users.getTotalPages())
                .items(response)
                .build();
    }

    @Override
    public PageResponse<?> getAllUsersWithSortByColumnAndSearch(int pageNo, int pageSize, String search, String sortBy) {
        return repository.getAllUsersWithSortByColumnAndSearch(pageNo, pageSize, search, sortBy);
    }

    @Override
    public PageResponse<?> advanceSearchByCriteria(int pageNo, int pageSize, String sortBy, String... search) {
        return repository.advanceSearchUser(pageNo, pageSize, sortBy, search);
    }

    @Override
    public PageResponse<?> advanceSearchBySpecification(Pageable pageable, String[] user, String[] address) {

        Page<User> users = null;

        List<User> list = null;

        if (user != null && address != null) {
            // tim kiem tren user va address -> join table



        } else if (user != null && address == null) {
            // tim kiem tren bang user -> khong can join sang bang address
//            Specification<User> spec = UserSpec.hasFirstName("M");
//
//            Specification<User> genderSpec = UserSpec.notEqualGender(Gender.FEMALE);
//
//            Specification<User> finalSpec = spec.and(genderSpec);

            UserSpecificationsBuilder builder = new UserSpecificationsBuilder();

            for (String s: user) {
                Pattern pattern = Pattern.compile("(\\w+?)([:<>~!])(.*)(\\p{Punct}?)(.*)(\\p{Punct}?)");
                Matcher matcher = pattern.matcher(s);
                if (matcher.find()) {
                    builder.with(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4), matcher.group(5));


                }
            }

            list = userRepository.findAll(builder.build());
            return PageResponse.builder()
                    .pageNo(pageable.getPageNumber())
                    .pageSize(pageable.getPageSize())
                    .totalPage(10)
                    .items(list)
                    .build();
        } else {
            users = userRepository.findAll(pageable);
        }

        return PageResponse.builder()
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalPage(users.getTotalPages())
                .items(list)
                .build();
    }

    private User getUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

    }


//    @Override
//    public int addUser(UserRequestDTO requestDTO) {
//        System.out.println("Save user to db");
//        if (!requestDTO.getFirstName().equals("Halo")){
//            throw new ResourceNotFoundException("Halo is not exist");
//        }
//        return 0;
//    }
//
//    public static interface UserService {
//        // int addUser(UserRequestDTO requestDTO);
//
//        long saveUser(UserRequestDTO requestDTO);
//        void updateUser(long userId, UserRequestDTO requestDTO);
//        void changeStatus(long userId, UserStatus status);
//        void deleteUser(long userId);
//
//        UserDetailResponse getUser(long userId);
//
//        List<UserDetailResponse> getAllUsers(int pageNo, int pageSize);
//
//
//
//    }
}
