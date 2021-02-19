package bartos.lukasz.service.repositoryServices;

import bartos.lukasz.dto.createModel.CreateCity;
import bartos.lukasz.dto.getModel.GetCity;
import bartos.lukasz.mappers.CreateModelMappers;
import bartos.lukasz.mappers.GetModelMappers;
import bartos.lukasz.model.City;
import bartos.lukasz.repository.CityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CityServiceTest {

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private CityService cityService;


    @Test
    public void getCityByName() {
        City city = City.builder().id(1L).name("Radom").build();
        when(cityRepository.getByName("Radom")).thenReturn(city);

        assertEquals(GetModelMappers.toGetCity(city), cityService.getCityByName("Radom"));
    }

    @Test
    public void findById() {
        City city = City.builder().id(1L).name("Radom").build();
        when(cityRepository.findById(1L)).thenReturn(Optional.of(city));

        assertEquals(GetModelMappers.toGetCity(city), cityService.findById(1L));
    }

    @Test
    public void getCities() {
        CreateCity city = CreateCity.builder().name("Radom").build();
        CreateCity city2 = CreateCity.builder().name("Radomsko").build();
        List<CreateCity> createCities = List.of(city, city2);

        List<City> cities = createCities
                .stream()
                .map(CreateModelMappers::toCity)
                .collect(Collectors.toList());

        List<GetCity> getCities = cities
                .stream()
                .map(GetModelMappers::toGetCity)
                .collect(Collectors.toList());

        when(cityRepository.findAll()).thenReturn(cities);

        assertAll(() -> {
         assertEquals(getCities, cityService.getCities());
         assertEquals(2, cityService.getCities().size());
        });
    }

    @Test
    public void save() {
        CreateCity createCity = CreateCity.builder().name("Radom").build();
        GetCity getCity = GetModelMappers.toGetCity(CreateModelMappers.toCity(createCity));
        when(cityRepository.save(CreateModelMappers.toCity(createCity))).thenReturn(Optional.of(CreateModelMappers.toCity(createCity)));

        assertEquals(getCity, cityService.save(createCity));
    }

    @Test
    public void saveAll() {
        CreateCity city = CreateCity.builder().name("Radom").build();
        CreateCity city2 = CreateCity.builder().name("Radomsko").build();
        List<CreateCity> createCities = List.of(city, city2);

        List<City> cities = createCities
                .stream()
                .map(CreateModelMappers::toCity)
                .collect(Collectors.toList());

        List<GetCity> getCities = cities
                .stream()
                .map(GetModelMappers::toGetCity)
                .collect(Collectors.toList());

        when(cityRepository.saveAll(cities)).thenReturn(cities);

        assertEquals(getCities, cityService.saveAll(createCities));
    }
}