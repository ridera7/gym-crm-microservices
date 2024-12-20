package com.gym.crm.application.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.ConnectionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class ActiveMQConfigTest {

    @InjectMocks
    private ActiveMQConfig activeMQConfig;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ConnectionFactory connectionFactory;

    @Test
    void shouldCreateJmsMessageConverterBean() throws NoSuchFieldException, IllegalAccessException {
        MessageConverter converter = activeMQConfig.jacksonJmsMessageConverter(objectMapper);

        assertNotNull(converter);

        Field typeIdPropertyNameField = MappingJackson2MessageConverter.class.getDeclaredField("typeIdPropertyName");
        typeIdPropertyNameField.setAccessible(true);
        String typeIdPropertyName = (String) typeIdPropertyNameField.get(converter);
        assertEquals("_type", typeIdPropertyName);

        Field objectMapperField = MappingJackson2MessageConverter.class.getDeclaredField("objectMapper");
        objectMapperField.setAccessible(true);
        Object actualObjectMapper = objectMapperField.get(converter);
        assertSame(objectMapper, actualObjectMapper);    }

    @Test
    void shouldCreateJmsTemplateBean() {
        JmsTemplate jmsTemplate = activeMQConfig.jmsTemplate(connectionFactory, objectMapper);

        assertNotNull(jmsTemplate);
        assertTrue(jmsTemplate.isSessionTransacted());
        assertTrue(jmsTemplate.isExplicitQosEnabled());
        assertEquals(5000, jmsTemplate.getTimeToLive());
    }
}