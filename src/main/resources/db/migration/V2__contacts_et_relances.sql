ALTER TABLE candidature ADD COLUMN date_relance DATE;

CREATE TABLE contact (
    id             BIGSERIAL PRIMARY KEY,
    candidature_id BIGINT       NOT NULL REFERENCES candidature(id) ON DELETE CASCADE,
    nom            VARCHAR(255) NOT NULL,
    fonction       VARCHAR(255),
    email          VARCHAR(255),
    telephone      VARCHAR(50),
    linkedin       VARCHAR(500),
    notes          TEXT
);
CREATE INDEX idx_contact_candidature ON contact(candidature_id);
