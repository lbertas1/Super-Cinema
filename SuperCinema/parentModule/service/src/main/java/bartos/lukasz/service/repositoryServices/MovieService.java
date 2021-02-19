package bartos.lukasz.service.repositoryServices;

import bartos.lukasz.dto.createModel.CreateMovie;
import bartos.lukasz.dto.getModel.GetMovie;
import bartos.lukasz.dto.getModel.GetSeance;
import bartos.lukasz.enums.FilmGenre;
import bartos.lukasz.exception.ServiceException;
import bartos.lukasz.mappers.CreateModelMappers;
import bartos.lukasz.mappers.GetModelMappers;
import bartos.lukasz.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    public GetMovie findMovieByName(String movieName) {
        return GetModelMappers.toGetMovie(movieRepository.findByName(movieName).orElseThrow(() -> new ServiceException("Movie not found")));
    }

    public GetMovie save(CreateMovie createMovie) {
        return GetModelMappers.toGetMovie(movieRepository.save(CreateModelMappers.toMovie(createMovie)).orElseThrow(() -> new ServiceException("Movie doesn't saved")));
    }

    public GetMovie findById(Long movieId) {
        return GetModelMappers.toGetMovie(movieRepository.findById(movieId).orElseThrow(() -> new ServiceException("Movie not found")));
    }

    public List<GetMovie> saveAll(List<CreateMovie> movieDtos) {
        return movieRepository
                .saveAll(movieDtos
                        .stream()
                        .map(CreateModelMappers::toMovie)
                        .collect(Collectors.toList()))
                .stream()
                .map(GetModelMappers::toGetMovie)
                .collect(Collectors.toList());
    }

    public List<GetMovie> getAllMovies() {
        return movieRepository
                .findAll()
                .stream()
                .map(GetModelMappers::toGetMovie)
                .collect(Collectors.toList());
    }

    public List<GetMovie> getMoviesByType(String type) {
        List<FilmGenre> types = List.of(FilmGenre.ACTION, FilmGenre.COMEDY, FilmGenre.DRAMA, FilmGenre.HORROR, FilmGenre.SCI_FI, FilmGenre.THRILLER);

        AtomicBoolean typeIsValid = new AtomicBoolean(false);

        types.forEach(filmGenre -> {
            if (filmGenre.toString().equalsIgnoreCase(type)) typeIsValid.set(true);
        });

        if (!typeIsValid.get()) throw new ServiceException("Invalid type");

        return movieRepository
                .getMoviesByType(type)
                .stream()
                .map(GetModelMappers::toGetMovie)
                .collect(Collectors.toList());
    }

    public List<GetMovie> getAllByMoviesIdList(List<GetSeance> seanceDtos) {
        return movieRepository
                .getAllByIdList(
                        seanceDtos
                                .stream()
                                .map(GetSeance::getMovieId)
                                .collect(Collectors.toList()))
                .stream()
                .map(GetModelMappers::toGetMovie)
                .collect(Collectors.toList());
    }

    public void showMovies(List<GetMovie> moviesDto) {
        if (moviesDto == null || moviesDto.isEmpty()) throw new ServiceException("Given list of movies are incorrect");

        int counter = 1;
        for (GetMovie movieDto : moviesDto) {
            System.out.println(counter + " - " + movieDto.getTitle());
            counter++;
        }
    }
}
