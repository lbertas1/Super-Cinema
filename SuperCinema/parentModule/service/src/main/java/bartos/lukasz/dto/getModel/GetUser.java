package bartos.lukasz.dto.getModel;

import bartos.lukasz.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetUser {
    private Long id;
    private String username;
    private String name;
    private String surname;
    private String email;
    private Integer age;
    private Role role;
}
