package com.almaviva.vs.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link EmailActivationSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class EmailActivationSearchRepositoryMockConfiguration {

    @MockBean
    private EmailActivationSearchRepository mockEmailActivationSearchRepository;
}
