package app.infrastructure.kafka.producer;

import app.domain.PlatoMenu;
import app.infrastructure.controller.comida.ComidaController;
import app.infrastructure.item.menu.dto.MenuDto;
import app.infrastructure.item.reserva.dto.PlatoMenuKafkaDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComidaController.class);

    private static final String CRMSCREATE_TOPIC = "crmscreate";
    private static final String CRMSEDIT_TOPIC = "crmsedit";

    private static final String PMMS_MDT = "pmms-mdt";

    private static final  String PMMS_RESERVATIONS = "pmms-reservations";



    @Autowired
    private KafkaTemplate<String, PlatoMenu> platomenukafkaTemplate;

    @Autowired
    private KafkaTemplate<String, MenuDto> menukafkaTemplate;

    @Autowired
    private KafkaTemplate<String, PlatoMenuKafkaDto> platoMenuKafkaDtoKafkaTemplate;

    public void sendPlatoMenuToTopic(PlatoMenu platoMenu) {
        platomenukafkaTemplate.send(CRMSCREATE_TOPIC, platoMenu);
        LOGGER.info("Enviando nuevo plato al topico: {}", CRMSCREATE_TOPIC);
    }

    public void sendPlatoMenuToEditTopic(PlatoMenu platoMenu) {
        platomenukafkaTemplate.send(CRMSEDIT_TOPIC, platoMenu);
        LOGGER.info("Enviando plato actualizado al topico: {}", CRMSEDIT_TOPIC);
    }

    public void sendMenuToPmmsMdtTopic(MenuDto menuDto) {
        menukafkaTemplate.send(PMMS_MDT, menuDto);
        LOGGER.info("Enviando menu detalle actualizado al topico: {}", PMMS_MDT);

    }

    public void sendPlatoMenuToTopic(PlatoMenuKafkaDto platoMenuKafkaDto) {
        platoMenuKafkaDtoKafkaTemplate.send(PMMS_RESERVATIONS, platoMenuKafkaDto);
        LOGGER.info("Enviando nuevo stock al topico: {}", PMMS_RESERVATIONS);
    }
}
