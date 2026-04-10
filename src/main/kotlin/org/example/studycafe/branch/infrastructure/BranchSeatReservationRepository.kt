package org.example.studycafe.branch.infrastructure

import org.example.studycafe.branch.domain.BranchSeatReservation
import org.springframework.data.jpa.repository.JpaRepository

interface BranchSeatReservationRepository : JpaRepository<BranchSeatReservation, Long>
