package roomescape.time;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.reservation.Reservation;
import roomescape.reservation.ReservationRequestDto;
import roomescape.reservation.ReservationResponseDto;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ReservationTimeControllerTest {

    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        reservationTimeRepository = new ReservationTimeRepository(jdbcTemplate);
        jdbcTemplate.execute("DROP TABLE IF EXISTS reservation");
        jdbcTemplate.execute("DROP TABLE IF EXISTS reservation_time");

        jdbcTemplate.execute("CREATE TABLE reservation_time (" +
                "id BIGINT NOT NULL AUTO_INCREMENT, " +
                "start_at VARCHAR(255) NOT NULL, " +
                "PRIMARY KEY (id))");

        jdbcTemplate.execute("CREATE TABLE reservation (" +
                "id BIGINT NOT NULL AUTO_INCREMENT, " +
                "name VARCHAR(255) NOT NULL, " +
                "date VARCHAR(255) NOT NULL, " +
                "time_id BIGINT, " +   // 컬럼 수정
                "PRIMARY KEY (id))");  // 외래키 제거
    }

    @DisplayName("전체 예약을 조회 합니다.")
    @Test
    void getTimes() {
        final ReservationTime request1 = new ReservationTime("15:40");
        final ReservationTime request2 = new ReservationTime("16:40");
        List<Object[]> reservationTimes = Arrays.asList(request1, request2).stream()
                .map(reservationTime -> new Object[]{reservationTime.getStartAt()})
                .collect(Collectors.toList());

        jdbcTemplate.batchUpdate("INSERT INTO reservation_time(start_at) VALUES (?)", reservationTimes);

        var response = RestAssured
                .given().log().all()
                .when().get("/times")
                .then().log().all().extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList(".", ReservationTimeResponseDto.class)).hasSize(2);
    }

    @DisplayName("시간 추가하기")
    @Test
    void addTime() {

        // given
        final ReservationTimeRequestDto request = new ReservationTimeRequestDto("15:40");

        // when
        var response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/times")
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        ReservationTimeResponseDto responseDto = response.as(ReservationTimeResponseDto.class);
        assertThat(responseDto.getStartAt()).isEqualTo(request.getStartAt());
    }

    @DisplayName("시간 삭제하기")
    @Test
    void deleteTime() {
        // given
        final ReservationTimeRequestDto request = new ReservationTimeRequestDto("15:40");

        // when
        var response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/times")
                .then().log().all().extract();



        final ReservationTimeResponseDto responseDto = response.as(ReservationTimeResponseDto.class);

        var response2 = RestAssured.given().log().all()
                .when().delete("/times/"+responseDto.getId())
                .then().log().all().extract();

        // then
        assertThat(response2.statusCode()).isEqualTo(HttpStatus.OK.value());

    }
}
