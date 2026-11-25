package fr.conesa.candidatures.contact;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findByCandidature_IdOrderByNomAsc(Long candidatureId);
}
