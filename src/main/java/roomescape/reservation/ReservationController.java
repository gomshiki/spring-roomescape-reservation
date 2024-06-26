package roomescape.reservation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponseDto>> getReservations() {
        final List<ReservationResponseDto> reservationResponseDtos = reservationService.findAll();
        return ResponseEntity.ok().body(reservationResponseDtos);

    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponseDto> createReservation(@RequestBody ReservationRequestDto reservationRequestDto) {
        final ReservationResponseDto responseDto = reservationService.save(reservationRequestDto);
        return ResponseEntity.ok().body(responseDto);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.delete(id);
        return ResponseEntity.ok().build();
    }

}
