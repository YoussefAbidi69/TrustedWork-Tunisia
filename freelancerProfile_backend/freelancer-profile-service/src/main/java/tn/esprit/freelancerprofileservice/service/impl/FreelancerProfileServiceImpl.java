package tn.esprit.freelancerprofileservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.freelancerprofileservice.dto.*;
import tn.esprit.freelancerprofileservice.entity.*;
import tn.esprit.freelancerprofileservice.repository.*;
import tn.esprit.freelancerprofileservice.service.FreelancerProfileService;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FreelancerProfileServiceImpl implements FreelancerProfileService {

    private final FreelancerProfileRepository profileRepository;
    private final SkillBadgeRepository skillBadgeRepository;
    private final EndorsementRepository endorsementRepository;
    private final CertificationExamRepository certificationExamRepository;

    // ===================== PROFILE =====================

    @Override
    public FreelancerProfileResponse createProfile(FreelancerProfileRequest request) {
        FreelancerProfile profile = new FreelancerProfile();
        profile.setUserId(request.getUserId());
        profile.setBio(request.getBio());
        profile.setTitre(request.getTitre());
        profile.setTauxHoraire(request.getTauxHoraire());
        profile.setLocalisation(request.getLocalisation());
        profile.setDomaineExpertise(request.getDomaineExpertise());
        profile.setDisponibilite(
                FreelancerProfile.Disponibilite.valueOf(request.getDisponibilite())
        );
        return mapToResponse(profileRepository.save(profile));
    }

    @Override
    public FreelancerProfileResponse getProfileById(Long id) {
        FreelancerProfile profile = profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profil introuvable avec id: " + id));
        return mapToResponse(profile);
    }

    @Override
    public FreelancerProfileResponse updateProfile(Long id, FreelancerProfileRequest request) {
        FreelancerProfile profile = profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profil introuvable avec id: " + id));
        profile.setBio(request.getBio());
        profile.setTitre(request.getTitre());
        profile.setTauxHoraire(request.getTauxHoraire());
        profile.setLocalisation(request.getLocalisation());
        profile.setDomaineExpertise(request.getDomaineExpertise());
        profile.setDisponibilite(
                FreelancerProfile.Disponibilite.valueOf(request.getDisponibilite())
        );
        return mapToResponse(profileRepository.save(profile));
    }

    @Override
    public void deleteProfile(Long id) {
        profileRepository.deleteById(id);
    }

    @Override
    public List<FreelancerProfileResponse> getAllProfiles() {
        return profileRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ===================== SKILL BADGE =====================

    @Override
    public List<SkillBadgeResponse> getAllSkills() {
        return skillBadgeRepository.findAll()
                .stream()
                .map(this::mapSkillToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public SkillBadgeResponse getSkillById(Long id) {
        SkillBadge skill = skillBadgeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Skill introuvable avec id: " + id));
        return mapSkillToResponse(skill);
    }

    @Override
    public List<SkillBadgeResponse> getSkillsByProfile(Long profileId) {
        return skillBadgeRepository.findByProfileId(profileId)
                .stream()
                .map(this::mapSkillToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public SkillBadgeResponse addSkill(SkillBadgeRequest request) {
        if (request.getCertificatHash() != null &&
                skillBadgeRepository.existsByCertificatHash(request.getCertificatHash())) {
            throw new RuntimeException("Certificat déjà soumis par un autre freelancer !");
        }
        FreelancerProfile profile = profileRepository.findById(request.getProfileId())
                .orElseThrow(() -> new RuntimeException("Profil introuvable"));
        SkillBadge badge = new SkillBadge();
        badge.setProfile(profile);
        badge.setNomSkill(request.getNomSkill());
        badge.setNiveau(SkillBadge.Niveau.valueOf(request.getNiveau()));
        badge.setDateValidation(request.getDateValidation());
        badge.setCertificatHash(request.getCertificatHash());
        return mapSkillToResponse(skillBadgeRepository.save(badge));
    }

    @Override
    public SkillBadgeResponse updateSkill(Long id, SkillBadgeRequest request) {
        SkillBadge skill = skillBadgeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Skill introuvable avec id: " + id));
        skill.setNomSkill(request.getNomSkill());
        skill.setNiveau(SkillBadge.Niveau.valueOf(request.getNiveau()));
        skill.setDateValidation(request.getDateValidation());
        return mapSkillToResponse(skillBadgeRepository.save(skill));
    }

    @Override
    public void deleteSkill(Long id) {
        skillBadgeRepository.deleteById(id);
    }

    // ===================== ENDORSEMENTS =====================

    @Override
    public EndorsementResponse createEndorsement(EndorsementRequest request) {
        SkillBadge skill = skillBadgeRepository.findById(request.getSkillId())
                .orElseThrow(() -> new RuntimeException("Skill introuvable"));
        Endorsement endorsement = new Endorsement();
        endorsement.setFromProfileId(request.getFromProfileId());
        endorsement.setToProfileId(request.getToProfileId());
        endorsement.setSkill(skill);
        endorsement.setCommentaire(request.getCommentaire());
        endorsement.setIsModerated(false);
        return mapEndorsementToResponse(endorsementRepository.save(endorsement));
    }

    @Override
    public List<EndorsementResponse> getEndorsementsByProfile(Long profileId) {
        return endorsementRepository.findByToProfileIdAndIsModerated(profileId, true)
                .stream()
                .map(this::mapEndorsementToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public EndorsementResponse getEndorsementById(Long id) {
        Endorsement e = endorsementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Endorsement introuvable avec id: " + id));
        return mapEndorsementToResponse(e);
    }

    @Override
    public EndorsementResponse updateEndorsement(Long id, EndorsementRequest request) {
        Endorsement e = endorsementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Endorsement introuvable avec id: " + id));
        e.setCommentaire(request.getCommentaire());
        return mapEndorsementToResponse(endorsementRepository.save(e));
    }

    @Override
    public void deleteEndorsement(Long id) {
        endorsementRepository.deleteById(id);
    }

    @Override
    public void moderateEndorsement(Long id) {
        endorsementRepository.moderateEndorsement(id);
    }

    // ===================== CERTIFICATION EXAM =====================

    @Override
    public CertificationExam createExam(CertificationExamRequest request) {
        CertificationExam exam = new CertificationExam();
        exam.setDomaine(request.getDomaine());
        exam.setQuestions(request.getQuestions());
        exam.setDureeMinutes(request.getDureeMinutes());
        exam.setScoreMinimum(request.getScoreMinimum());
        exam.setTentatives(0);
        exam.setBaremeConfig(request.getBaremeConfig());
        return certificationExamRepository.save(exam);
    }

    @Override
    public CertificationExam getExamById(Long id) {
        return certificationExamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Examen introuvable avec id: " + id));
    }

    @Override
    public CertificationExam updateExam(Long id, CertificationExamRequest request) {
        CertificationExam exam = certificationExamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Examen introuvable avec id: " + id));
        exam.setDomaine(request.getDomaine());
        exam.setDureeMinutes(request.getDureeMinutes());
        exam.setScoreMinimum(request.getScoreMinimum());
        exam.setBaremeConfig(request.getBaremeConfig());
        return certificationExamRepository.save(exam);
    }

    @Override
    public List<CertificationExam> getAllExams() {
        return certificationExamRepository.findAll();
    }

    @Override
    public List<CertificationExam> getExamsByDomaine(String domaine) {
        return certificationExamRepository.findByDomaine(domaine);
    }

    @Override
    public void deleteExam(Long id) {
        certificationExamRepository.deleteById(id);
    }

    // ===================== MAPPERS =====================

    private FreelancerProfileResponse mapToResponse(FreelancerProfile p) {
        FreelancerProfileResponse res = new FreelancerProfileResponse();
        res.setId(p.getId());
        res.setUserId(p.getUserId());
        res.setBio(p.getBio());
        res.setTitre(p.getTitre());
        res.setTauxHoraire(p.getTauxHoraire());
        res.setLocalisation(p.getLocalisation());
        res.setDomaineExpertise(p.getDomaineExpertise());
        res.setTrustPassportUrl(p.getTrustPassportUrl());
        if (p.getDisponibilite() != null)
            res.setDisponibilite(p.getDisponibilite().name());
        return res;
    }

    private SkillBadgeResponse mapSkillToResponse(SkillBadge s) {
        SkillBadgeResponse res = new SkillBadgeResponse();
        res.setId(s.getId());
        res.setNomSkill(s.getNomSkill());
        res.setDateValidation(s.getDateValidation());
        if (s.getNiveau() != null)
            res.setNiveau(s.getNiveau().name());
        return res;
    }

    private EndorsementResponse mapEndorsementToResponse(Endorsement e) {
        EndorsementResponse res = new EndorsementResponse();
        res.setId(e.getId());
        res.setFromProfileId(e.getFromProfileId());
        res.setToProfileId(e.getToProfileId());
        res.setCommentaire(e.getCommentaire());
        res.setIsModerated(e.getIsModerated());
        return res;
    }
}