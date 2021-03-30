package com.almaviva.vs.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link SiteConfigurationSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class SiteConfigurationSearchRepositoryMockConfiguration {

    @MockBean
    private SiteConfigurationSearchRepository mockSiteConfigurationSearchRepository;
}
