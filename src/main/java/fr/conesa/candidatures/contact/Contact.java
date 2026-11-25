package fr.conesa.candidatures.contact;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.conesa.candidatures.candidature.Candidature;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "contact")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "candidature_id", nullable = false)
    private Candidature candidature;

    @NotBlank private String nom;
    private String fonction;
    @Email private String email;
    @Column(length = 50) private String telephone;
    @Column(length = 500) private String linkedin;
    @Column(columnDefinition = "text") private String notes;

    public void majDepuis(Contact c) {
        nom = c.nom; fonction = c.fonction; email = c.email;
        telephone = c.telephone; linkedin = c.linkedin; notes = c.notes;
    }

    public Long getId() { return id; }
    public Long getCandidatureId() { return candidature != null ? candidature.getId() : null; }
    public void setCandidature(Candidature v) { candidature = v; }
    public String getNom() { return nom; }
    public void setNom(String v) { nom = v; }
    public String getFonction() { return fonction; }
    public void setFonction(String v) { fonction = v; }
    public String getEmail() { return email; }
    public void setEmail(String v) { email = v; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String v) { telephone = v; }
    public String getLinkedin() { return linkedin; }
    public void setLinkedin(String v) { linkedin = v; }
    public String getNotes() { return notes; }
    public void setNotes(String v) { notes = v; }
}
