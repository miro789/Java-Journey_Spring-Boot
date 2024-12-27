package vn.miro.dto.response;

import lombok.Builder;
import lombok.Getter;
import vn.miro.util.PhoneNumber;

import java.io.Serializable;

@Getter
@Builder
public class UserDetailResponse implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;

}
