CREATE TABLE candidature (
    id               BIGSERIAL PRIMARY KEY,
    entreprise       VARCHAR(255) NOT NULL,
    poste            VARCHAR(255) NOT NULL,
    lieu             VARCHAR(255),
    lien             VARCHAR(1000),
    source           VARCHAR(255),
    date_candidature DATE,
    date_limite      DATE,
    statut           VARCHAR(30)  NOT NULL,
    notes            TEXT,
    cree_le          TIMESTAMPTZ  NOT NULL,
    modifie_le       TIMESTAMPTZ  NOT NULL
);
