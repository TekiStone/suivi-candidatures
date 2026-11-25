package fr.conesa.candidatures.candidature;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidatureRepository extends JpaRepository<Candidature, Long> {

    List<Candidature> findByStatutOrderByDateLimiteAsc(Statut statut);

    List<Candidature> findByStatutInAndDateRelanceLessThanEqualOrderByDateRelanceAsc(
            Collection<Statut> statuts, LocalDate date);

    List<Candidature> findByStatutAndDateLimiteLessThanEqualOrderByDateLimiteAsc(
            Statut statut, LocalDate date);
}
