package roomescape.reservation;

import roomescape.time.ReservationTime;
import roomescape.time.ReservationTimeResponseDto;

public class ReservationResponseDto {
    private Long id;
    private String name;
    private String date;
    private ReservationTimeResponseDto reservationTimeResponseDto;

    public ReservationResponseDto(Long id, String name, String date, ReservationTimeResponseDto reservationTimeResponseDto) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.reservationTimeResponseDto = reservationTimeResponseDto;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public ReservationTimeResponseDto getReservationTimeResponseDto() {
        return reservationTimeResponseDto;
    }

    public static class Builder {
        private Long id;
        private String name;
        private String date;
        private ReservationTimeResponseDto reservationTimeResponseDto;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder date(String date) {
            this.date = date;
            return this;
        }

        public Builder reservationTimeResponseDto(ReservationTimeResponseDto reservationTimeResponseDto) {
            this.reservationTimeResponseDto = reservationTimeResponseDto;
            return this;
        }

        public ReservationResponseDto build() {
            return new ReservationResponseDto(id, name, date, reservationTimeResponseDto);
        }
    }
}
