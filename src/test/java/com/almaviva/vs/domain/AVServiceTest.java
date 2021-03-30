package com.almaviva.vs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.almaviva.vs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AVServiceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AVService.class);
        AVService aVService1 = new AVService();
        aVService1.setId(1L);
        AVService aVService2 = new AVService();
        aVService2.setId(aVService1.getId());
        assertThat(aVService1).isEqualTo(aVService2);
        aVService2.setId(2L);
        assertThat(aVService1).isNotEqualTo(aVService2);
        aVService1.setId(null);
        assertThat(aVService1).isNotEqualTo(aVService2);
    }
}
