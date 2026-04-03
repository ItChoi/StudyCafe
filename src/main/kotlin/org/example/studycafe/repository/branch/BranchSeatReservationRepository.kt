package org.example.studycafe.repository.branch

import org.example.studycafe.domain.branch.BranchSeatReservation
import org.springframework.data.jpa.repository.JpaRepository

interface BranchSeatReservationRepository : JpaRepository<BranchSeatReservation, Long>
