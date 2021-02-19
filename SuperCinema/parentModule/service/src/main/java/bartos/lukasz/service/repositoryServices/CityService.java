package bartos.lukasz.service.repositoryServices;

import bartos.lukasz.dto.createModel.CreateCity;
import bartos.lukasz.dto.getModel.GetCity;
import bartos.lukasz.exception.ServiceException;
import bartos.lukasz.mappers.CreateModelMappers;
import bartos.lukasz.mappers.GetModelMappers;
import bartos.lukasz.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CityService {
    private final CityRepository cityRepository;

    public List<GetCity> saveAll(List<CreateCity> cities) {
        return cityRepository
                .saveAll(cities
                        .stream()
                        .map(CreateModelMappers::toCity)
                        .collect(Collectors.toList()))
                .stream()
                .map(GetModelMappers::toGetCity)
                .collect(Collectors.toList());
    }

    public List<GetCity> getCities () {
        return cityRepository
                .findAll()
                .stream()
                .map(GetModelMappers::toGetCity)
                .collect(Collectors.toList());
    }

    public GetCity getCityByName(String name) {
        return GetModelMappers.toGetCity(cityRepository.getByName(name));
    }

    public GetCity findById(Long cityId) {
        return GetModelMappers.toGetCity(cityRepository.findById(cityId).orElseThrow(() -> new ServiceException("City not found")));
    }

    public GetCity save(CreateCity createCity) {
        return GetModelMappers.toGetCity(cityRepository.save(CreateModelMappers.toCity(createCity)).orElseThrow(() -> new ServiceException("City doesn't saved")));
    }
}
