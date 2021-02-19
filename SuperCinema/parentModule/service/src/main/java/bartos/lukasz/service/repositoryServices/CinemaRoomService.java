package bartos.lukasz.service.repositoryServices;

import bartos.lukasz.dto.createModel.CreateCinemaRoom;
import bartos.lukasz.dto.getModel.GetCinema;
import bartos.lukasz.dto.getModel.GetCinemaRoom;
import bartos.lukasz.exception.ServiceException;
import bartos.lukasz.mappers.CreateModelMappers;
import bartos.lukasz.mappers.GetModelMappers;
import bartos.lukasz.repository.CinemaRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CinemaRoomService {

    private final CinemaRoomRepository cinemaRoomRepository;

    public List<GetCinemaRoom> saveAll(List<CreateCinemaRoom> cinemaRoomDtos) {
        return cinemaRoomRepository
                .saveAll(cinemaRoomDtos
                        .stream()
                        .map(CreateModelMappers::toCinemaRoom)
                        .collect(Collectors.toList()))
                .stream()
                .map(GetModelMappers::toGetCinemaRoom)
                .collect(Collectors.toList());
    }

    public GetCinemaRoom findById(Long cinemaRoomId) {
        return GetModelMappers
                .toGetCinemaRoom(cinemaRoomRepository
                        .findById(cinemaRoomId)
                        .orElseThrow(() -> new ServiceException("Cinema room not found")));
    }

    public List<GetCinemaRoom> getCinemaRoomsByCinemaId(GetCinema getCinema) {
        return cinemaRoomRepository
                .getCinemaRoomsByCinemaId(getCinema.getId())
                .stream()
                .map(GetModelMappers::toGetCinemaRoom)
                .collect(Collectors.toList());
    }

    public List<GetCinemaRoom> getCinemaRoomsFromCinemaList(List<GetCinema> cinemaDtos) {
        List<Long> ids = cinemaDtos
                .stream()
                .map(GetCinema::getId)
                .collect(Collectors.toList());

        return cinemaRoomRepository
                .findAllById(ids)
                .stream()
                .map(GetModelMappers::toGetCinemaRoom)
                .collect(Collectors.toList());
    }

    public GetCinemaRoom save(CreateCinemaRoom createCinemaRoom) {
        return GetModelMappers.toGetCinemaRoom(cinemaRoomRepository.save(CreateModelMappers.toCinemaRoom(createCinemaRoom)).orElseThrow(() -> new ServiceException("Cinema room doesn't saved")));
    }

    public List<GetCinemaRoom> getCinemaRoomsByCinemaName(String cinemaName) {
        return cinemaRoomRepository
                .getCinemaRoomsByCinemaName(cinemaName)
                .stream()
                .map(GetModelMappers::toGetCinemaRoom)
                .collect(Collectors.toList());
    }
}
