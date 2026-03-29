package tn.esprit.freelancerprofileservice.service;

import tn.esprit.freelancerprofileservice.dto.*;
import tn.esprit.freelancerprofileservice.entity.CertificationExam;
import java.util.List;

public interface FreelancerProfileService {

    // ===================== PROFILE =====================
    FreelancerProfileResponse createProfile(FreelancerProfileRequest request);
    FreelancerProfileResponse getProfileById(Long id);
    FreelancerProfileResponse updateProfile(Long id, FreelancerProfileRequest request);
    void deleteProfile(Long id);
    List<FreelancerProfileResponse> getAllProfiles();

    // ===================== SKILL BADGE =====================
    List<SkillBadgeResponse> getAllSkills();
    SkillBadgeResponse getSkillById(Long id);
    List<SkillBadgeResponse> getSkillsByProfile(Long profileId);
    SkillBadgeResponse addSkill(SkillBadgeRequest request);
    SkillBadgeResponse updateSkill(Long id, SkillBadgeRequest request);
    void deleteSkill(Long id);

    // ===================== ENDORSEMENTS =====================
    EndorsementResponse createEndorsement(EndorsementRequest request);
    List<EndorsementResponse> getEndorsementsByProfile(Long profileId);
    EndorsementResponse getEndorsementById(Long id);
    EndorsementResponse updateEndorsement(Long id, EndorsementRequest request);
    void deleteEndorsement(Long id);
    void moderateEndorsement(Long id);

    // ===================== CERTIFICATION EXAM =====================
    CertificationExam createExam(CertificationExamRequest request);
    CertificationExam getExamById(Long id);
    CertificationExam updateExam(Long id, CertificationExamRequest request);
    List<CertificationExam> getAllExams();
    List<CertificationExam> getExamsByDomaine(String domaine);
    void deleteExam(Long id);
}