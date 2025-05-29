package umc.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.study.domain.Member;
import umc.study.domain.enums.MemberStatus;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByNameAndStatus(String name, MemberStatus status);
    Optional<Member> findByEmail(String email);
}