package bartos.lukasz.service.repositoryServices;

import bartos.lukasz.dto.createModel.CreateCinema;
import bartos.lukasz.dto.getModel.GetCinema;
import bartos.lukasz.exception.ServiceException;
import bartos.lukasz.mappers.CreateModelMappers;
import bartos.lukasz.mappers.GetModelMappers;
import bartos.lukasz.repository.CinemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CinemaService {
    private final CinemaRepository cinemaRepository;

    public List<GetCinema> saveAll(List<CreateCinema> cities) {
        return cinemaRepository
                .saveAll(cities
                        .stream()
                        .map(CreateModelMappers::toCinema)
                        .collect(Collectors.toList()))
                .stream()
                .map(GetModelMappers::toGetCinema)
                .collect(Collectors.toList());
    }

    public List<GetCinema> getAllCinemas() {
        return cinemaRepository
                .findAll()
                .stream()
                .map(GetModelMappers::toGetCinema)
                .collect(Collectors.toList());
    }

    public GetCinema findCinemaById(Long cinemaId) {
        return GetModelMappers.toGetCinema(cinemaRepository.findById(cinemaId).orElseThrow(() -> new ServiceException("Cinema not found")));
    }

    public GetCinema save(CreateCinema createCinema) {
        return GetModelMappers.toGetCinema(cinemaRepository.save(CreateModelMappers.toCinema(createCinema)).orElseThrow(() -> new ServiceException("Saving doesn't work")));
    }

    public List<GetCinema> getCinemaFromCity(String name) {
        return
                cinemaRepository
                        .getCinemaByCityName(name)
                        .stream()
                        .map(GetModelMappers::toGetCinema)
                        .collect(Collectors.toList());
    }

    public GetCinema getCinemaByName(String name) {
        return GetModelMappers.toGetCinema(cinemaRepository.getCinemaByName(name));
    }
}
