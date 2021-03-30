package com.almaviva.vs.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.almaviva.vs.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VisaDocumentsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VisaDocuments.class);
        VisaDocuments visaDocuments1 = new VisaDocuments();
        visaDocuments1.setId(1L);
        VisaDocuments visaDocuments2 = new VisaDocuments();
        visaDocuments2.setId(visaDocuments1.getId());
        assertThat(visaDocuments1).isEqualTo(visaDocuments2);
        visaDocuments2.setId(2L);
        assertThat(visaDocuments1).isNotEqualTo(visaDocuments2);
        visaDocuments1.setId(null);
        assertThat(visaDocuments1).isNotEqualTo(visaDocuments2);
    }
}
