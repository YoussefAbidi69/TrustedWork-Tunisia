package tn.esprit.reviewservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.reviewservice.entity.Badge;

@Repository
public interface BadgeRepository extends JpaRepository<Badge, Long> {
}