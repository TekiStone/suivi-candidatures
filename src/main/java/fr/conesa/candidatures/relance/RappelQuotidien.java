package fr.conesa.candidatures.relance;

import fr.conesa.candidatures.candidature.Candidature;
import fr.conesa.candidatures.candidature.CandidatureService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RappelQuotidien {

    private static final Logger log = LoggerFactory.getLogger(RappelQuotidien.class);
    private final CandidatureService service;

    public RappelQuotidien(CandidatureService service) { this.service = service; }

    @Scheduled(cron = "0 0 8 * * *", zone = "Europe/Paris")
    public void rappeler() {
        List<Candidature> relances = service.aRelancer();
        List<Candidature> echeances = service.echeancesProches();
        if (relances.isEmpty() && echeances.isEmpty()) {
            log.info("Rappel : rien à relancer aujourd'hui");
            return;
        }
        relances.forEach(c -> log.info("A relancer : {} ({}), relance prévue le {}",
            c.getEntreprise(), c.getPoste(), c.getDateRelance()));
        echeances.forEach(c -> log.info("Date limite proche : {} ({}), le {}",
            c.getEntreprise(), c.getPoste(), c.getDateLimite()));
    }
}
