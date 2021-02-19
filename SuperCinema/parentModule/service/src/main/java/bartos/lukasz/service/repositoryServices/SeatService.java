package bartos.lukasz.service.repositoryServices;

import bartos.lukasz.dto.createModel.CreateSeat;
import bartos.lukasz.dto.getModel.GetSeat;
import bartos.lukasz.enums.SeatStatus;
import bartos.lukasz.exception.ServiceException;
import bartos.lukasz.mappers.CreateModelMappers;
import bartos.lukasz.mappers.GetModelMappers;
import bartos.lukasz.model.Seance;
import bartos.lukasz.repository.SeanceRepository;
import bartos.lukasz.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeatService {
    private final SeatRepository seatRepository;
    private final SeanceRepository seanceRepository;

    public GetSeat save(CreateSeat seatDto) {
        return GetModelMappers
                .toGetSeat(seatRepository
                        .save(CreateModelMappers
                                .toSeat(seatDto))
                        .orElseThrow(() -> new ServiceException("Seat cannot be save")));
    }

    public List<GetSeat> saveAll(List<CreateSeat> seatDtos) {
        return seatRepository
                .saveAll(seatDtos
                        .stream()
                        .map(CreateModelMappers::toSeat)
                        .collect(Collectors.toList()))
                .stream()
                .map(GetModelMappers::toGetSeat)
                .collect(Collectors.toList());
    }

    public GetSeat update(GetSeat getSeat) {
        return GetModelMappers
                .toGetSeat(seatRepository
                .update(getSeat.getId(), GetModelMappers.toSeat(getSeat))
                .orElseThrow(() -> new ServiceException("Seat cannot updated")));
    }

    public GetSeat getSeat(Long seatId) {
        return GetModelMappers.toGetSeat(seatRepository.findById(seatId).orElseThrow(() -> new ServiceException("Seat doesn't founded")));
    }

    public List<GetSeat> getSeats (List<Long> seats) {
        return seatRepository.findAllById(seats)
                .stream()
                .map(GetModelMappers::toGetSeat)
                .collect(Collectors.toList());
    }

    public List<GetSeat> getBusySeatsFromSeance(Long seanceId) {
        return seatRepository
                .getAllBusySeatsForSeance(seanceId)
                .stream()
                .map(GetModelMappers::toGetSeat)
                .collect(Collectors.toList());
    }

    public List<GetSeat> getSeatsBySeanceId(Long seanceId) {
        return seatRepository
                .getSeatsBySeanceId(seanceId)
                .stream()
                .map(GetModelMappers::toGetSeat)
                .collect(Collectors.toList());
    }

    public GetSeat removeById(Long seatId) {
        return GetModelMappers.toGetSeat(seatRepository.delete(seatId).orElseThrow(() -> new ServiceException("Seat removing failed")));
    }

    public List<GetSeat> getAllSeatsByStatus(Long seanceId, SeatStatus seatStatus) {
        return seatRepository
                .getAllSeatsByStatus(seanceId, seatStatus.toString())
                .stream()
                .map(GetModelMappers::toGetSeat)
                .collect(Collectors.toList());
    }

    public List<GetSeat> clearDatabase() {
        List<Long> seancesId = seanceRepository
                .findSeancesBeforeDate(LocalDate.now().toString())
                .stream()
                .map(Seance::getId)
                .collect(Collectors.toList());

        return seatRepository
                .removeAllBySeanceId(seancesId)
                .stream()
                .map(GetModelMappers::toGetSeat)
                .collect(Collectors.toList());
    }

    public GetSeat getSeatByNumber(Long seanceId, Integer seatNumber) {
        return GetModelMappers.toGetSeat(seatRepository
                .getSeatByNumber(seanceId, seatNumber)
                .orElseThrow(() -> new ServiceException("Seat not found")));
    }

    public void showSeats(SeatStatus seatStatus, Long seanceId) {
        if (Objects.isNull(seatStatus)) throw new ServiceException("Seat status is null");
        if (Objects.isNull(seanceId)) throw new ServiceException("seance id is null");

        List<GetSeat> getSeats = seatRepository
                .getAllSeatsByStatus(seanceId, seatStatus.toString())
                .stream()
                .map(GetModelMappers::toGetSeat)
                .collect(Collectors.toList());

        getSeats.forEach(System.out::println);
    }
}
