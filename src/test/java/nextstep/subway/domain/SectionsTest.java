package nextstep.subway.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

class SectionsTest {

    private Sections emptySections;

    private Sections sections;
    private Station 상행역;
    private Station 하행역;
    private Line line;

    @BeforeEach
    void setUp() {
        상행역 = new Station(1L, "상행역");
        하행역 = new Station(2L, "하행역");
        line = new Line("노선", "색상", 100L);
        line.setStations(상행역, 하행역);
        emptySections = Sections.of(line);
    }

    @Test
    @DisplayName("상행역에서 시작하고, 거리가 100이내이면 추가할 수 있다고 판단함")
    void canInsert() {
        boolean b = emptySections.canInsert(1L, 3L, 50L);

        assertThat(b).isTrue();
    }

    @Test
    @DisplayName("하행역에서 시작하고, 거리가 100이내이면 추가할 수 있다고 판단함")
    void canInsert2() {
        boolean b = emptySections.canInsert(3L, 2L, 50L);

        assertThat(b).isTrue();
    }

    @ParameterizedTest
    @ValueSource(longs = {100, 101})
    @DisplayName("상행역에서 시작할 때 거리가 같거나 크면 추가할 수 없음")
    void canInsert3(Long wrongDistance) {
        boolean b = emptySections.canInsert(1L, 3L, wrongDistance);

        assertThat(b).isFalse();
    }

    @ParameterizedTest
    @ValueSource(longs = {100, 101})
    @DisplayName("하행역에서 시작할 때 거리가 같거나 크면 추가할 수 없음")
    void canInsert4(Long wrongDistance) {
        boolean b = emptySections.canInsert(3L, 2L, wrongDistance);

        assertThat(b).isFalse();
    }

    @Test
    @DisplayName("상행역에서 시작하고, 거리가 100이내이면 구간이 추가되고, 2개의 구간을 갖게 됨")
    void canInsert5() {
        Station 중간역 = new Station(3L, "중간역");
        emptySections.insertInsideFromUpStation(상행역, 중간역, 60L);

        assertThat(emptySections.hasSection(Section.of(line, 상행역, 중간역, 60L))).isTrue();
        assertThat(emptySections.hasSection(Section.of(line, 중간역, 하행역, 40L))).isTrue();
    }

    @Test
    @DisplayName("하행역에서 시작하고, 거리가 100이내이면 구간이 추가되고, 2개의 구간을 갖게 됨")
    void canInsert6() {
        Station 중간역 = new Station(3L, "중간역");
        emptySections.insertInsideFromDownStation(하행역, 중간역, 60L);

        assertThat(emptySections.hasSection(Section.of(line, 상행역, 중간역, 40L))).isTrue();
        assertThat(emptySections.hasSection(Section.of(line, 중간역, 하행역, 60L))).isTrue();
    }
}