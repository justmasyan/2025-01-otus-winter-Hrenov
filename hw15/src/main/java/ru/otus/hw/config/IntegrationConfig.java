package ru.otus.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import ru.otus.hw.service.KindergartenService;
import ru.otus.hw.service.SchoolService;
import ru.otus.hw.service.UniversityService;

@Configuration
public class IntegrationConfig {

    @Bean
    public MessageChannelSpec<?, ?> babiesChannel() {
        return MessageChannels.queue(10);
    }

    @Bean
    public MessageChannelSpec<?, ?> adultsChannel() {
        return MessageChannels.queue(10);
    }

    @Bean
    public IntegrationFlow studyFlow(KindergartenService kindergartenService,
                                     SchoolService schoolService,
                                     UniversityService universityService) {
        return IntegrationFlow.from(babiesChannel())
                .split()
                .handle(kindergartenService, "teachBaseSkills")
                .handle(schoolService, "techBaseSciences")
                .handle(universityService, "awardScientificDegree")
                .aggregate()
                .channel(adultsChannel())
                .get();
    }
}
