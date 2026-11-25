async function requete(methode, url, corps) {
  const reponse = await fetch('/api' + url, {
    method: methode,
    headers: corps ? { 'Content-Type': 'application/json' } : {},
    body: corps ? JSON.stringify(corps) : undefined
  })
  if (!reponse.ok) {
    let message = `Erreur ${reponse.status}`
    try {
      const e = await reponse.json()
      if (e.message || e.detail) message += ` : ${e.message || e.detail}`
    } catch { /* corps vide ou non JSON */ }
    throw new Error(message)
  }
  return reponse.status === 204 ? null : reponse.json()
}

export const api = {
  candidatures: () => requete('GET', '/candidatures'),
  aRelancer: () => requete('GET', '/candidatures/a-relancer'),
  echeances: () => requete('GET', '/candidatures/echeances'),
  candidature: (id) => requete('GET', `/candidatures/${id}`),
  creer: (c) => requete('POST', '/candidatures', c),
  modifier: (id, c) => requete('PUT', `/candidatures/${id}`, c),
  supprimer: (id) => requete('DELETE', `/candidatures/${id}`),
  relancer: (id) => requete('POST', `/candidatures/${id}/relancer`),
  contacts: (id) => requete('GET', `/candidatures/${id}/contacts`),
  ajouterContact: (id, c) => requete('POST', `/candidatures/${id}/contacts`, c),
  supprimerContact: (id) => requete('DELETE', `/contacts/${id}`)
}

export const STATUTS = {
  A_ENVOYER: 'À envoyer', ENVOYEE: 'Envoyée', RELANCEE: 'Relancée', ENTRETIEN: 'Entretien',
  OFFRE: 'Offre', REFUSEE: 'Refusée', ABANDONNEE: 'Abandonnée'
}

export const formatDate = (d) => (d ? new Date(d + 'T00:00:00').toLocaleDateString('fr-FR') : '')

/** Prépare un objet pour l'API : champs vides en null, champs techniques retirés. */
export function nettoyer(objet) {
  const { id, creeLe, modifieLe, candidatureId, ...reste } = objet
  return Object.fromEntries(Object.entries(reste).map(([k, v]) => [k, v === '' ? null : v]))
}
