package pl.edu.pw.elka.sag.model

import pl.edu.pw.elka.sag.actors.classify.{Candidate, Popularity}

case class CandidatePopularity(candidate: Candidate, popularity: Popularity)
