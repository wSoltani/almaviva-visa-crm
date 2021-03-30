package com.almaviva.vs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.almaviva.vs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmailActivationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmailActivation.class);
        EmailActivation emailActivation1 = new EmailActivation();
        emailActivation1.setId(1L);
        EmailActivation emailActivation2 = new EmailActivation();
        emailActivation2.setId(emailActivation1.getId());
        assertThat(emailActivation1).isEqualTo(emailActivation2);
        emailActivation2.setId(2L);
        assertThat(emailActivation1).isNotEqualTo(emailActivation2);
        emailActivation1.setId(null);
        assertThat(emailActivation1).isNotEqualTo(emailActivation2);
    }
}
