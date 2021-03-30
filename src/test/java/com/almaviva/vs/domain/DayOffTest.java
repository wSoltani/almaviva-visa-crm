package com.almaviva.vs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.almaviva.vs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DayOffTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DayOff.class);
        DayOff dayOff1 = new DayOff();
        dayOff1.setId(1L);
        DayOff dayOff2 = new DayOff();
        dayOff2.setId(dayOff1.getId());
        assertThat(dayOff1).isEqualTo(dayOff2);
        dayOff2.setId(2L);
        assertThat(dayOff1).isNotEqualTo(dayOff2);
        dayOff1.setId(null);
        assertThat(dayOff1).isNotEqualTo(dayOff2);
    }
}
