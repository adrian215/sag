package pl.edu.pw.elka.sag.actors

import pl.edu.pw.elka.sag.model.CandidatePopularity

package object classify {
  type Tweet = String
  type FileName = String
  type Candidate = String
  type Votes = Int
  type Popularity = Double
  type ClassificationFinished = (List[CandidatePopularity]) => Unit
}
