package xyz.deltacare.empresa.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.deltacare.empresa.domain.Beneficiario;
import xyz.deltacare.empresa.dto.NovoBeneficiarioDto;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

@Service
@Transactional
public class EventSenderKafka implements EventSender {

    public static final String HEADER_VALUE = "999";
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public EventSenderKafka(ObjectMapper objectMapper, KafkaTemplate<String, String> kafkaTemplate) {
        this.objectMapper = objectMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(Beneficiario beneficiario) {

        try {
            NovoBeneficiarioDto novoBeneficiarioDto = NovoBeneficiarioDto
                    .builder()
                    .mes(getMes())
                    .plano(beneficiario.getPlano())
                    .qtd(1)
                    .build();
            String payload = objectMapper.writeValueAsString(novoBeneficiarioDto);
            Message<String> message = MessageBuilder
                    .withPayload(payload)
                    .setHeader(KafkaHeaders.TOPIC, "beneficiario")
                    .setHeader(KafkaHeaders.MESSAGE_KEY, HEADER_VALUE)
                    .setHeader(KafkaHeaders.PARTITION_ID, 0)
                    .build();
            kafkaTemplate.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private String getMes() {
        String mes = LocalDate.now().getMonth().getDisplayName(TextStyle.FULL_STANDALONE,
                new Locale("pt", "BR")).substring(0,3);
        return mes.substring(0, 1).toUpperCase() + mes.substring(1);
    }

}
