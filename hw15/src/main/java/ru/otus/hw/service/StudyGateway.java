package ru.otus.hw.service;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.hw.domain.Adult;
import ru.otus.hw.domain.Baby;

import java.util.Collection;

@MessagingGateway
public interface StudyGateway {

    @Gateway(requestChannel = "babiesChannel", replyChannel = "adultsChannel")
    Collection<Adult> process(Collection<Baby> babies);
}
