package vn.miro.dto.request;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
public class SampleDTO implements Serializable {
    private Integer id;

    @NonNull
    private String name;

}
