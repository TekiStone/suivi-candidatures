package fr.conesa.candidatures.candidature;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "candidature")
public class Candidature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank private String entreprise;
    @NotBlank private String poste;
    private String lieu;
    @Column(length = 1000) private String lien;
    private String source;
    private LocalDate dateCandidature;
    private LocalDate dateLimite;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Statut statut = Statut.A_ENVOYER;

    @Column(columnDefinition = "text") private String notes;
    @Column(nullable = false, updatable = false) private OffsetDateTime creeLe;
    @Column(nullable = false) private OffsetDateTime modifieLe;

    @PrePersist
    void avantCreation() { creeLe = OffsetDateTime.now(); modifieLe = creeLe; }

    @PreUpdate
    void avantMaj() { modifieLe = OffsetDateTime.now(); }

    /** Copie les champs modifiables depuis une autre candidature (utilisé par le PUT). */
    public void majDepuis(Candidature c) {
        entreprise = c.entreprise; poste = c.poste; lieu = c.lieu; lien = c.lien;
        source = c.source; dateCandidature = c.dateCandidature; dateLimite = c.dateLimite;
        statut = c.statut != null ? c.statut : statut; notes = c.notes;
    }

    public Long getId() { return id; }
    public String getEntreprise() { return entreprise; }
    public void setEntreprise(String v) { entreprise = v; }
    public String getPoste() { return poste; }
    public void setPoste(String v) { poste = v; }
    public String getLieu() { return lieu; }
    public void setLieu(String v) { lieu = v; }
    public String getLien() { return lien; }
    public void setLien(String v) { lien = v; }
    public String getSource() { return source; }
    public void setSource(String v) { source = v; }
    public LocalDate getDateCandidature() { return dateCandidature; }
    public void setDateCandidature(LocalDate v) { dateCandidature = v; }
    public LocalDate getDateLimite() { return dateLimite; }
    public void setDateLimite(LocalDate v) { dateLimite = v; }
    public Statut getStatut() { return statut; }
    public void setStatut(Statut v) { statut = v; }
    public String getNotes() { return notes; }
    public void setNotes(String v) { notes = v; }
    public OffsetDateTime getCreeLe() { return creeLe; }
    public OffsetDateTime getModifieLe() { return modifieLe; }
}
