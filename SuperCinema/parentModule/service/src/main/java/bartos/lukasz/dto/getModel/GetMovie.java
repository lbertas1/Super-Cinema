package bartos.lukasz.dto.getModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetMovie {
    private Long id;
    private String title;
    private String filmGenre;
}
