package bartos.lukasz.service.repositoryServices;

import bartos.lukasz.dto.createModel.CreateSeance;
import bartos.lukasz.dto.createModel.CreateSeat;
import bartos.lukasz.dto.getModel.GetCinemaRoom;
import bartos.lukasz.dto.getModel.GetSeance;
import bartos.lukasz.exception.ServiceException;
import bartos.lukasz.mappers.CreateModelMappers;
import bartos.lukasz.mappers.GetModelMappers;
import bartos.lukasz.model.Seat;
import bartos.lukasz.repository.CinemaRoomRepository;
import bartos.lukasz.repository.MovieRepository;
import bartos.lukasz.repository.SeanceRepository;
import bartos.lukasz.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeanceService {
    private final SeanceRepository seanceRepository;
    private final CinemaRoomRepository cinemaRoomRepository;
    private final MovieRepository movieRepository;
    private final SeatRepository seatRepository;

    public List<GetSeance> findSeancesByGivenDate(String date) {
        return seanceRepository
                .findSeancesByDate(date)
                .stream()
                .map(seance -> {
                    String movieTitle = movieRepository.findById(seance.getMovieId()).orElseThrow(() -> new ServiceException("Movie not found")).getTitle();
                    GetSeance finalGetSeance = GetModelMappers.toGetSeance(seance);
                    finalGetSeance.setMovieName(movieTitle);
                    return finalGetSeance;
                })
                .collect(Collectors.toList());
    }

    public GetSeance getSeance(Long seanceId) {
        return GetModelMappers.toGetSeance(seanceRepository.findById(seanceId).orElseThrow(() -> new ServiceException("Seance doesn't found")));
    }

    public GetSeance save(CreateSeance createSeance) {
        GetCinemaRoom getCinemaRoom = GetModelMappers.toGetCinemaRoom(cinemaRoomRepository.findById(createSeance.getCinemaRoomId()).orElseThrow(() -> new ServiceException("Cinema room not found")));

        String title = movieRepository.findById(createSeance.getMovieId()).orElseThrow(() -> new ServiceException("Movie not found")).getTitle();
        GetSeance getSeance = GetModelMappers.toGetSeance(seanceRepository.save(CreateModelMappers.toSeance(createSeance)).orElseThrow(() -> new ServiceException("Seance doesn't saved")));
        getSeance.setMovieName(title);

        List<Seat> seatsForSeance = createSeatsForSeance(getCinemaRoom, getSeance.getId())
                .stream()
                .map(CreateModelMappers::toSeat)
                .collect(Collectors.toList());

        seatRepository.saveAll(seatsForSeance);

        return getSeance;
    }

    protected List<CreateSeat> createSeatsForSeance(GetCinemaRoom getCinemaRoom, Long seanceId) {
        int seatNumber = 1;
        List<CreateSeat> allSeats = new ArrayList<>();
        for (int i = 0; i < getCinemaRoom.getQuantityOfRows(); i++) {
            for (int integer = 0; integer < getCinemaRoom.getPlacesInRow(); integer++) {
                allSeats.add(CreateSeat.builder().roomId(getCinemaRoom.getId()).seanceId(seanceId).seatNumber(seatNumber).seatRow(i + 1).place(integer + 1).build());
                seatNumber++;
            }
        }
        return allSeats;
    }

    public List<GetSeance> saveAll(List<CreateSeance> seanceDtos) {
        return seanceRepository
                .saveAll(seanceDtos
                        .stream()
                        .map(CreateModelMappers::toSeance)
                        .collect(Collectors.toList()))
                .stream()
                .peek(System.out::println)
                .map(seance -> {
                            String movieTitle = movieRepository.findById(seance.getMovieId()).orElseThrow(() -> new ServiceException("Movie not found")).getTitle();
                            GetSeance finalGetSeance = GetModelMappers.toGetSeance(seance);
                            finalGetSeance.setMovieName(movieTitle);
                            return finalGetSeance;
                })
                .collect(Collectors.toList());
    }

    public List<GetSeance> getSeancesByCinemaRoomsId(List<GetCinemaRoom> getCinemaRooms) {
        if (getCinemaRooms.isEmpty() || Objects.isNull(getCinemaRooms)) throw new ServiceException("Given list is null");

        return seanceRepository
                .getSeancesByCinemaRoomId(getCinemaRooms
                        .stream()
                        .map(GetCinemaRoom::getId)
                        .collect(Collectors.toList()))
                .stream()
                .map(seance -> {
                    String movieTitle = movieRepository.findById(seance.getMovieId()).orElseThrow(() -> new ServiceException("Movie not found")).getTitle();
                    GetSeance finalGetSeance = GetModelMappers.toGetSeance(seance);
                    finalGetSeance.setMovieName(movieTitle);
                    return finalGetSeance;
                })
                .collect(Collectors.toList());
    }

    public void showSeances(List<GetSeance> seanceDtos) {
        if (seanceDtos == null || seanceDtos.isEmpty())
            throw new ServiceException("Given seances list is incorrect");

        System.out.println("Seances date with choosen movie:");
        seanceDtos.forEach(seanceDto -> System.out.println(seanceDto.getScreeningDate()));
    }

    public void chooseSeance(List<GetSeance> seances) {
        if (seances == null || seances.isEmpty())
            throw new ServiceException("Given seances list is incorrect");

        System.out.println("Select seance. Enter correct number");
        for (int i = 0; i < seances.size(); i++) {
            System.out.println(i + 1 + " - " + seances.get(i).getScreeningDate());
        }
    }

    public List<GetSeance> getSeancesByFilmName(String movieName) {
        if (movieName == null) throw new ServiceException("Movie name is null");

        Long movieId = movieRepository
                .findByName(movieName)
                .orElseThrow(() -> new ServiceException("Movie not found"))
                .getId();

        return seanceRepository
                .findSeancesByMovieId(movieId)
                .stream()
                .map(seance -> {
                    String movieTitle = movieRepository.findById(seance.getMovieId()).orElseThrow(() -> new ServiceException("Movie not found")).getTitle();
                    GetSeance finalGetSeance = GetModelMappers.toGetSeance(seance);
                    finalGetSeance.setMovieName(movieTitle);
                    return finalGetSeance;
                })
                .collect(Collectors.toList());
    }
}
