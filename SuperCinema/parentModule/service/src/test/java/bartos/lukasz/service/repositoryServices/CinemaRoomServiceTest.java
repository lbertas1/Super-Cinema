package bartos.lukasz.service.repositoryServices;

import bartos.lukasz.dto.createModel.CreateCinemaRoom;
import bartos.lukasz.dto.getModel.GetCinema;
import bartos.lukasz.dto.getModel.GetCinemaRoom;
import bartos.lukasz.mappers.CreateModelMappers;
import bartos.lukasz.mappers.GetModelMappers;
import bartos.lukasz.model.CinemaRoom;
import bartos.lukasz.repository.CinemaRoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CinemaRoomServiceTest {

    @Mock
    private CinemaRoomRepository cinemaRoomRepository;

    @InjectMocks
    private CinemaRoomService cinemaRoomService;

    private CreateCinemaRoom createCinemaRoom1;
    private CreateCinemaRoom createCinemaRoom2;

    private CinemaRoom cinemaRoom1;
    private CinemaRoom cinemaRoom2;
    private CinemaRoom cinemaRoom3;
    private CinemaRoom cinemaRoom4;

    private GetCinemaRoom getCinemaRoom1;
    private GetCinemaRoom getCinemaRoom2;

    private List<CreateCinemaRoom> createCinemaRooms;
    private List<CinemaRoom> cinemaRooms;
    private List<CinemaRoom> cinemaRooms2;
    private List<GetCinemaRoom> getCinemaRooms;

    @BeforeEach
    private void prepareData() {
        createCinemaRoom1 = CreateCinemaRoom.builder().roomNumber(1).cinemaId(1L).placesInRow(2).quantityOfRows(2).build();
        createCinemaRoom2 = CreateCinemaRoom.builder().roomNumber(1).cinemaId(1L).placesInRow(2).quantityOfRows(2).build();

        cinemaRoom1 = CreateModelMappers.toCinemaRoom(createCinemaRoom1);
        cinemaRoom2 = CreateModelMappers.toCinemaRoom(createCinemaRoom2);
        cinemaRoom3 = CreateModelMappers.toCinemaRoom(createCinemaRoom1);
        cinemaRoom3.setId(1L);
        cinemaRoom3.setCinemaId(1L);
        cinemaRoom4 = CreateModelMappers.toCinemaRoom(createCinemaRoom2);
        cinemaRoom4.setId(2L);
        cinemaRoom4.setCinemaId(2L);

        getCinemaRoom1 = GetModelMappers.toGetCinemaRoom(cinemaRoom3);
        getCinemaRoom2 = GetModelMappers.toGetCinemaRoom(cinemaRoom4);

        createCinemaRooms = List.of(createCinemaRoom1, createCinemaRoom2);
        cinemaRooms = List.of(cinemaRoom1, cinemaRoom2);
        cinemaRooms2 = List.of(cinemaRoom3, cinemaRoom4);
        getCinemaRooms = List.of(getCinemaRoom1, getCinemaRoom2);
    }

    @Test
    public void save() {
        CreateCinemaRoom createCinemaRoom = CreateCinemaRoom.builder().cinemaId(1L).roomNumber(1).placesInRow(2).quantityOfRows(2).build();
        CinemaRoom cinemaRoom = CreateModelMappers.toCinemaRoom(createCinemaRoom);
        CinemaRoom cinemaRoom2 = CreateModelMappers.toCinemaRoom(createCinemaRoom);
        cinemaRoom2.setId(1L);
        GetCinemaRoom getCinemaRoom = GetModelMappers.toGetCinemaRoom(cinemaRoom2);
        when(cinemaRoomRepository.save(cinemaRoom)).thenReturn(Optional.of(cinemaRoom2));

        assertAll(() -> {
            assertEquals(getCinemaRoom, cinemaRoomService.save(createCinemaRoom));
            assertNotNull(getCinemaRoom);
        });
    }

    @Test
    public void saveAll() {
        CreateCinemaRoom createCinemaRoom = CreateCinemaRoom.builder().cinemaId(1L).roomNumber(1).placesInRow(2).quantityOfRows(2).build();
        CinemaRoom cinemaRoom = CreateModelMappers.toCinemaRoom(createCinemaRoom);

        CreateCinemaRoom createCinemaRoom2 = CreateCinemaRoom.builder().cinemaId(1L).roomNumber(1).placesInRow(2).quantityOfRows(2).build();
        CinemaRoom cinemaRoom2 = CreateModelMappers.toCinemaRoom(createCinemaRoom2);

        List<CreateCinemaRoom> createCinemaRooms = List.of(createCinemaRoom, createCinemaRoom2);
        List<CinemaRoom> cinemaRooms = List.of(cinemaRoom, cinemaRoom2);

        when(cinemaRoomRepository.saveAll(cinemaRooms)).thenReturn(cinemaRooms);

        assertNotNull(cinemaRoomService.saveAll(createCinemaRooms));
    }

    @Test
    public void findById() {
        when(cinemaRoomRepository.findById(1L)).thenReturn(Optional.of(cinemaRoom3));

        assertAll(() -> {
            assertEquals(getCinemaRoom1, cinemaRoomService.findById(1L));
            assertEquals(1L, cinemaRoomService.findById(1L).getId());
        });
    }

    @Test
    public void getCinemaRoomsByCinemaId() {
        GetCinema getCinema = GetCinema.builder().id(1L).cityId(1L).name("Helios").build();

        List<CinemaRoom> result = cinemaRooms2
                .stream()
                .filter(cinemaRoom -> cinemaRoom.getCinemaId().equals(getCinema.getId()))
                .collect(Collectors.toList());

        List<GetCinemaRoom> getResult = result
                .stream()
                .map(GetModelMappers::toGetCinemaRoom)
                .collect(Collectors.toList());

        when(cinemaRoomRepository.getCinemaRoomsByCinemaId(getCinema.getId())).thenReturn(result);

        assertEquals(getResult, cinemaRoomService.getCinemaRoomsByCinemaId(getCinema));
    }

    @TestFactory
    public Stream<DynamicTest> getCinemaRoomsByCinemaId_DynamicTest() {
        GetCinema getCinema = GetCinema.builder().id(1L).cityId(1L).name("Helios").build();

        List<CinemaRoom> result = cinemaRooms2
                .stream()
                .filter(cinemaRoom -> cinemaRoom.getCinemaId().equals(getCinema.getId()))
                .collect(Collectors.toList());

        List<GetCinemaRoom> getResult = result
                .stream()
                .map(GetModelMappers::toGetCinemaRoom)
                .collect(Collectors.toList());


        when(cinemaRoomRepository.getCinemaRoomsByCinemaId(getCinema.getId())).thenReturn(result);

        return List
                .of(getCinema)
                .stream()
                .flatMap(cinemaDto1 -> List.of(
                        DynamicTest.dynamicTest("dynamicTestVersion1", () -> assertEquals(getResult, cinemaRoomService.getCinemaRoomsByCinemaId(cinemaDto1)))
                )
                        .stream());
    }

    @Test
    public void getCinemaRoomsFromCinemaList() {
        GetCinema getCinema = GetCinema.builder().id(1L).cityId(1L).name("Helios").build();
        GetCinema getCinema2 = GetCinema.builder().id(2L).cityId(1L).name("Helios").build();

        List<GetCinema> getCinemas = List.of(getCinema, getCinema2);

        List<Long> ids = getCinemas
                .stream()
                .map(GetCinema::getId)
                .collect(Collectors.toList());

        when(cinemaRoomRepository.findAllById(ids)).thenReturn(cinemaRooms2);

        assertEquals(getCinemaRooms, cinemaRoomService.getCinemaRoomsFromCinemaList(getCinemas));
    }

    @Test
    public void getCinemaRoomsByCinemaName() {
        GetCinema getCinema = GetCinema.builder().id(1L).name("Helios").build();
        when(cinemaRoomRepository.getCinemaRoomsByCinemaName(getCinema.getName())).thenReturn(cinemaRooms2);

        assertEquals(getCinemaRooms, cinemaRoomService.getCinemaRoomsByCinemaName(getCinema.getName()));
    }

    @TestFactory
    public Stream<DynamicTest> getCinemaRoomsByCinemaName_DynamicTestVersion() {
        GetCinema getCinema = GetCinema.builder().id(1L).name("Helios").build();
        when(cinemaRoomRepository.getCinemaRoomsByCinemaName(getCinema.getName())).thenReturn(cinemaRooms2);

        return List
                .of(getCinema)
                .stream()
                .flatMap(cinemaDto1 -> List
                        .of(
                                DynamicTest.dynamicTest("DynamicTest3", () -> assertEquals(getCinemaRooms, cinemaRoomService.getCinemaRoomsByCinemaName(cinemaDto1.getName())))
                        )
                        .stream());
    }
}