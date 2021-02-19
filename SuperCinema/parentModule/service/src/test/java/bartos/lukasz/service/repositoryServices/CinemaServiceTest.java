package bartos.lukasz.service.repositoryServices;

import bartos.lukasz.dto.createModel.CreateCinema;
import bartos.lukasz.dto.getModel.GetCinema;
import bartos.lukasz.dto.getModel.GetCity;
import bartos.lukasz.mappers.CreateModelMappers;
import bartos.lukasz.mappers.GetModelMappers;
import bartos.lukasz.model.Cinema;
import bartos.lukasz.repository.CinemaRepository;
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

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CinemaServiceTest {

    @Mock
    private CinemaRepository cinemaRepository;

    @InjectMocks
    private CinemaService cinemaService;

    @Test
    public void save() {
        CreateCinema createCinema = CreateCinema.builder().cityId(1L).name("Helios").build();
        Cinema cinema = CreateModelMappers.toCinema(createCinema);
        Cinema cinema1 = CreateModelMappers.toCinema(createCinema);
        cinema1.setId(1L);
        GetCinema getCinema = GetModelMappers.toGetCinema(cinema1);

        when(cinemaRepository.save(cinema)).thenReturn(Optional.of(cinema1));

        assertAll(() -> {
            assertEquals(getCinema, cinemaService.save(createCinema));
            assertNotNull(getCinema.getId());
        });
    }

    @Test
    public void saveAll() {
        CreateCinema createCinema1 = CreateCinema.builder().cityId(1L).name("Helios").build();
        CreateCinema createCinema2 = CreateCinema.builder().cityId(1L).name("Multikino").build();

        Cinema cinema1 = CreateModelMappers.toCinema(createCinema1);
        Cinema cinema2 = CreateModelMappers.toCinema(createCinema2);
        Cinema cinema3 = CreateModelMappers.toCinema(createCinema2);
        cinema3.setId(1L);
        Cinema cinema4 = CreateModelMappers.toCinema(createCinema2);
        cinema4.setId(2L);

        GetCinema getCinema1 = GetModelMappers.toGetCinema(cinema3);
        GetCinema getCinema2 = GetModelMappers.toGetCinema(cinema4);

        List<CreateCinema> createCinemas = List.of(createCinema1, createCinema2);
        List<Cinema> cinemas = List.of(cinema1, cinema2);
        List<Cinema> cinemas1 = List.of(cinema3, cinema4);
        List<GetCinema> getCinemas = List.of(getCinema1, getCinema2);

        when(cinemaRepository.saveAll(cinemas)).thenReturn(cinemas1);

        assertAll(() -> {
            assertEquals(getCinemas, cinemaService.saveAll(createCinemas));
            assertThat(getCinemas.stream().map(GetCinema::getId).collect(Collectors.toList()), not(empty()));
            assertThat(getCinemas.stream().map(GetCinema::getId).collect(Collectors.toList()), hasSize(getCinemas.size()));
        });
    }

    @Test
    public void findById() {
        Long cinemaId = 1L;
        GetCinema getCinema = GetCinema.builder().id(cinemaId).name("Helios").build();

        when(cinemaRepository.findById(cinemaId)).thenReturn(Optional.of(GetModelMappers.toCinema(getCinema)));

        assertAll(() -> {
            assertEquals(getCinema, cinemaService.findCinemaById(cinemaId));
            assertEquals(cinemaId, getCinema.getId());
        });
    }

    @Test
    public void getCinemaByName() {
        Cinema cinema = Cinema.builder().id(1L).cityId(1L).name("Helios").build();
        when(cinemaRepository.getCinemaByName("Helios")).thenReturn(cinema);

        assertEquals(GetModelMappers.toGetCinema(cinema), cinemaService.getCinemaByName("Helios"));
    }

    @Test
    public void getCinemaFromCity() {
        GetCity cityDto = GetCity.builder().id(1L).name("Toronto").build();
        GetCinema cinemaDto = GetCinema.builder().id(1L).cityId(1L).name("Helios").build();
        List<Cinema> cinemas = List.of(GetModelMappers.toCinema(cinemaDto));
        List<GetCinema> getCinemas = List.of(cinemaDto);

        when(cinemaRepository.getCinemaByCityName(cityDto.getName())).thenReturn(cinemas);

        assertEquals(getCinemas, cinemaService.getCinemaFromCity(cityDto.getName()));
    }

    @Test
    public void getAllCinemas() {
        GetCinema cinemaDto = GetCinema.builder().id(1L).cityId(1L).name("Helios").build();
        GetCinema cinemaDto2 = GetCinema.builder().id(2L).cityId(2L).name("Helios2").build();
        List<Cinema> cinemas = List.of(GetModelMappers.toCinema(cinemaDto), GetModelMappers.toCinema(cinemaDto2));
        List<GetCinema> cinemaDtos = List.of(cinemaDto, cinemaDto2);
        when(cinemaRepository.findAll()).thenReturn(cinemas);

        assertEquals(cinemaDtos, cinemaService.getAllCinemas());
    }
}