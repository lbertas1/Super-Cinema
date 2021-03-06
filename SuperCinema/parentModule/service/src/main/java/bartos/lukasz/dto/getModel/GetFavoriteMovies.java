package bartos.lukasz.dto.getModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetFavoriteMovies {
    private Long id;
    private Long userId;
    private Long movieId;
}
