package fr.conesa.candidatures.candidature;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidatureRepository extends JpaRepository<Candidature, Long> {
    List<Candidature> findByStatutOrderByDateLimiteAsc(Statut statut);
}
