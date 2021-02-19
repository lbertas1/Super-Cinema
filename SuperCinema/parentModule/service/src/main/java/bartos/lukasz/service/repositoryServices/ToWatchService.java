package bartos.lukasz.service.repositoryServices;

import bartos.lukasz.dto.createModel.CreateToWatch;
import bartos.lukasz.dto.getModel.GetMovie;
import bartos.lukasz.dto.getModel.GetToWatch;
import bartos.lukasz.enums.FilmGenre;
import bartos.lukasz.exception.ServiceException;
import bartos.lukasz.mappers.CreateModelMappers;
import bartos.lukasz.mappers.GetModelMappers;
import bartos.lukasz.model.ToWatch;
import bartos.lukasz.repository.MovieRepository;
import bartos.lukasz.repository.ToWatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ToWatchService {

    private final ToWatchRepository toWatchRepository;

    private final MovieRepository movieRepository;

    public GetToWatch save(CreateToWatch createToWatch) {
        return GetModelMappers.toGetToWatch(toWatchRepository.save(CreateModelMappers.toWatch(createToWatch)).orElseThrow(() -> new ServiceException("Request doesn't saved")));
    }

    public GetToWatch remove(String movieName, Long userId) {
        Long movieId = GetModelMappers.toGetMovie(movieRepository.findByName(movieName).orElseThrow(() -> new ServiceException("Movie doesn't found"))).getId();
        return GetModelMappers.toGetToWatch(toWatchRepository.deleteToWatch(userId, movieId).orElseThrow(() -> new ServiceException("Movie doesn't removed")));
    }

    public List<GetMovie> getAllMovies(Long userID) {
        List<Long> ids = toWatchRepository
                .findAllByUserId(userID)
                .stream()
                .map(ToWatch::getMovieId)
                .collect(Collectors.toList());

        System.out.println(ids);

        return movieRepository
                .getAllByIdList(ids)
                .stream()
                .map(GetModelMappers::toGetMovie)
                .collect(Collectors.toList());
    }

    public List<GetMovie> getAllByType(FilmGenre filmGenre, Long userId) {
        List<Long> ids = toWatchRepository
                .findAllByUserId(userId)
                .stream()
                .map(ToWatch::getMovieId)
                .collect(Collectors.toList());

        return movieRepository
                .getMoviesByTypeAndMovieId(filmGenre.toString(), ids)
                .stream()
                .map(GetModelMappers::toGetMovie)
                .collect(Collectors.toList());
    }
}
