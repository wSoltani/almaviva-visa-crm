package com.almaviva.vs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.almaviva.vs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SiteConfigurationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SiteConfiguration.class);
        SiteConfiguration siteConfiguration1 = new SiteConfiguration();
        siteConfiguration1.setId(1L);
        SiteConfiguration siteConfiguration2 = new SiteConfiguration();
        siteConfiguration2.setId(siteConfiguration1.getId());
        assertThat(siteConfiguration1).isEqualTo(siteConfiguration2);
        siteConfiguration2.setId(2L);
        assertThat(siteConfiguration1).isNotEqualTo(siteConfiguration2);
        siteConfiguration1.setId(null);
        assertThat(siteConfiguration1).isNotEqualTo(siteConfiguration2);
    }
}
