package bartos.lukasz.dto.createModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUser {
    private String username;
    private String password;
    private String name;
    private String surname;
    private String email;
    private Integer age;
}
