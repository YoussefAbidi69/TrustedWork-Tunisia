package tn.esprit.msrecruitmentservice.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.msrecruitmentservice.entities.HiringContract;
import tn.esprit.msrecruitmentservice.repositories.IHiringContractRepository;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

/**
 * Service de génération de PDF pour HiringContract.
 *
 * Fonctionnement :
 *   1. Récupère le contrat depuis la DB
 *   2. Appelle HuggingFaceService pour générer les clauses juridiques via IA
 *   3. Construit le PDF avec iText (données dynamiques + clauses IA)
 *   4. Ajoute les champs de signature Freelancer et HR en bas du PDF
 *
 * Dépendance pom.xml :
 *   <dependency>
 *       <groupId>com.itextpdf</groupId>
 *       <artifactId>itextpdf</artifactId>
 *       <version>5.5.13.3</version>
 *   </dependency>
 */
@Service
public class ContractPdfService {

    @Autowired
    private IHiringContractRepository contractRepository;

    @Autowired
    private HuggingFaceService huggingFaceService;

    // ── Couleurs TrustedWork ──────────────────────────────────────────
    private static final BaseColor COLOR_PRIMARY    = new BaseColor(30, 64, 175);  // Bleu foncé
    private static final BaseColor COLOR_SECONDARY  = new BaseColor(59, 130, 246); // Bleu clair
    private static final BaseColor COLOR_LIGHT_GRAY = new BaseColor(243, 244, 246);
    private static final BaseColor COLOR_DARK       = new BaseColor(17, 24, 39);
    private static final BaseColor COLOR_BORDER     = new BaseColor(209, 213, 219);

    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // ─────────────────────────────────────────────────────────────────
    // MÉTHODE PRINCIPALE
    // ─────────────────────────────────────────────────────────────────

    /**
     * Génère le PDF du contrat d'embauche.
     * @param contractId  ID du HiringContract (doit avoir status = SIGNED)
     * @return            Bytes du PDF prêt à être téléchargé
     */
    public byte[] generateContractPdf(Long contractId) throws Exception {

        // 1. Récupérer le contrat
        HiringContract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new RuntimeException("Contrat non trouvé : " + contractId));

        // 2. Appel IA — Génération des clauses via HuggingFace
        String posteExact = (contract.getOffer() != null && contract.getOffer().getPosteExact() != null)
                ? contract.getOffer().getPosteExact()
                : contract.getTypeContrat().name();

        String clausesIA = huggingFaceService.generateContractClauses(
                contract.getTypeContrat().name(),
                contract.getSalaireFinal(),
                contract.getPeriodeEssai(),
                posteExact
        );

        // 3. Construire le PDF
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 50, 50, 60, 60);
        PdfWriter writer = PdfWriter.getInstance(document, baos);

        document.open();

        addHeader(document);
        addSeparator(document);
        addContractTitle(document, contract);
        addParties(document, contract);
        addContractDetails(document, contract);
        addAiClauses(document, clausesIA);
        addSignatureSection(document);

        document.close();

        return baos.toByteArray();
    }

    // ─────────────────────────────────────────────────────────────────
    // SECTIONS DU PDF
    // ─────────────────────────────────────────────────────────────────

    /** En-tête avec logo texte TrustedWork */
    private void addHeader(Document doc) throws DocumentException {
        // Bande bleue en-tête simulée avec un tableau pleine largeur
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new float[]{3f, 1f});

        // Cellule gauche — Nom plateforme
        PdfPCell leftCell = new PdfPCell();
        leftCell.setBackgroundColor(COLOR_PRIMARY);
        leftCell.setBorder(Rectangle.NO_BORDER);
        leftCell.setPadding(15);

        Font logoFont = new Font(Font.FontFamily.HELVETICA, 22, Font.BOLD, BaseColor.WHITE);
        Font taglineFont = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL,
                new BaseColor(191, 219, 254));

        leftCell.addElement(new Paragraph("TrustedWork Tunisia", logoFont));
        leftCell.addElement(new Paragraph("Plateforme Freelance Sécurisée — Recrutement CDI/CDD", taglineFont));
        headerTable.addCell(leftCell);

        // Cellule droite — Badge document
        PdfPCell rightCell = new PdfPCell();
        rightCell.setBackgroundColor(COLOR_SECONDARY);
        rightCell.setBorder(Rectangle.NO_BORDER);
        rightCell.setPadding(15);
        rightCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        rightCell.setHorizontalAlignment(Element.ALIGN_CENTER);

        Font badgeFont = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.WHITE);
        Paragraph badge = new Paragraph("CONTRAT DE TRAVAIL\nOFFICIEL", badgeFont);
        badge.setAlignment(Element.ALIGN_CENTER);
        rightCell.addElement(badge);
        headerTable.addCell(rightCell);

        doc.add(headerTable);
        doc.add(Chunk.NEWLINE);
    }

    /** Ligne séparatrice */
    private void addSeparator(Document doc) throws DocumentException {
        LineSeparator ls = new LineSeparator();
        ls.setLineColor(COLOR_SECONDARY);
        ls.setLineWidth(1.5f);
        doc.add(new Chunk(ls));
        doc.add(Chunk.NEWLINE);
    }

    /** Titre du contrat avec numéro et date */
    private void addContractTitle(Document doc, HiringContract contract) throws DocumentException {
        Font titleFont  = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, COLOR_PRIMARY);
        Font subFont    = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, COLOR_DARK);

        Paragraph title = new Paragraph("CONTRAT DE TRAVAIL — " + contract.getTypeContrat().name(), titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(4);
        doc.add(title);

        String dateStr = (contract.getDateContratSigne() != null)
                ? contract.getDateContratSigne().format(DATE_FMT)
                : "En attente de signature";

        Paragraph sub = new Paragraph(
                "Réf. TW-" + contract.getId() + "  •  Date de signature : " + dateStr, subFont);
        sub.setAlignment(Element.ALIGN_CENTER);
        sub.setSpacingAfter(16);
        doc.add(sub);

        doc.add(Chunk.NEWLINE);
    }

    /** Section "ENTRE / ET" — Les deux parties */
    private void addParties(Document doc, HiringContract contract) throws DocumentException {
        Font sectionFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, COLOR_PRIMARY);
        Font labelFont   = new Font(Font.FontFamily.HELVETICA, 9,  Font.BOLD, COLOR_DARK);
        Font valueFont   = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, COLOR_DARK);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1f, 1f});
        table.setSpacingBefore(8);
        table.setSpacingAfter(16);

        // ── Cellule Entreprise ──
        PdfPCell companyCell = new PdfPCell();
        companyCell.setBorderColor(COLOR_BORDER);
        companyCell.setPadding(12);
        companyCell.setBackgroundColor(COLOR_LIGHT_GRAY);

        companyCell.addElement(new Paragraph("EMPLOYEUR", sectionFont));
        companyCell.addElement(Chunk.NEWLINE);
        companyCell.addElement(new Paragraph("ID Entreprise", labelFont));
        companyCell.addElement(new Paragraph(String.valueOf(contract.getEntrepriseId()), valueFont));
        companyCell.addElement(Chunk.NEWLINE);
        companyCell.addElement(new Paragraph("Rôle", labelFont));
        companyCell.addElement(new Paragraph("Société Recrutante", valueFont));
        table.addCell(companyCell);

        // ── Cellule Freelancer ──
        PdfPCell freelancerCell = new PdfPCell();
        freelancerCell.setBorderColor(COLOR_BORDER);
        freelancerCell.setPadding(12);
        freelancerCell.setBackgroundColor(COLOR_LIGHT_GRAY);

        freelancerCell.addElement(new Paragraph("EMPLOYÉ(E)", sectionFont));
        freelancerCell.addElement(Chunk.NEWLINE);
        freelancerCell.addElement(new Paragraph("ID Freelancer", labelFont));
        freelancerCell.addElement(new Paragraph(String.valueOf(contract.getFreelancerId()), valueFont));
        freelancerCell.addElement(Chunk.NEWLINE);
        freelancerCell.addElement(new Paragraph("Rôle", labelFont));
        freelancerCell.addElement(new Paragraph("Freelancer Recruté(e)", valueFont));

        // Phase 2 : remplacer par CIN depuis Feign Client
        // freelancerCell.addElement(new Paragraph("CIN", labelFont));
        // freelancerCell.addElement(new Paragraph(freelancerDTO.getCin(), valueFont));

        table.addCell(freelancerCell);

        doc.add(table);
    }

    /** Tableau des détails du contrat */
    private void addContractDetails(Document doc, HiringContract contract) throws DocumentException {
        Font sectionFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, COLOR_PRIMARY);
        Font labelFont   = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, COLOR_DARK);
        Font valueFont   = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, COLOR_DARK);

        Paragraph sectionTitle = new Paragraph("CONDITIONS DU CONTRAT", sectionFont);
        sectionTitle.setSpacingAfter(8);
        doc.add(sectionTitle);

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1.2f, 1.8f, 1.2f, 1.8f});
        table.setSpacingAfter(16);

        addDetailRow(table, "Type de contrat",
                contract.getTypeContrat().name(),
                "Salaire mensuel brut",
                contract.getSalaireFinal() + " DT",
                labelFont, valueFont);

        String debut = (contract.getDateDebutEffective() != null)
                ? contract.getDateDebutEffective().format(DATE_FMT) : "À définir";

        addDetailRow(table, "Date de début",
                debut,
                "Période d'essai",
                contract.getPeriodeEssai() + " mois",
                labelFont, valueFont);

        addDetailRow(table, "Commission plateforme",
                contract.getCommissionPlateforme() + "%",
                "Statut contrat",
                contract.getStatus().name(),
                labelFont, valueFont);

        String poste = (contract.getOffer() != null && contract.getOffer().getPosteExact() != null)
                ? contract.getOffer().getPosteExact() : "Non spécifié";

        addDetailRow(table, "Poste",
                poste,
                "Avantages",
                (contract.getOffer() != null && contract.getOffer().getAvantages() != null)
                        ? contract.getOffer().getAvantages() : "—",
                labelFont, valueFont);

        doc.add(table);
    }

    /** Ligne de détail dans le tableau (2 paires label/valeur) */
    private void addDetailRow(PdfPTable table,
                              String label1, String value1,
                              String label2, String value2,
                              Font labelFont, Font valueFont) {
        PdfPCell c1 = new PdfPCell(new Phrase(label1, labelFont));
        c1.setBackgroundColor(COLOR_LIGHT_GRAY);
        c1.setPadding(8);
        c1.setBorderColor(COLOR_BORDER);

        PdfPCell c2 = new PdfPCell(new Phrase(value1, valueFont));
        c2.setPadding(8);
        c2.setBorderColor(COLOR_BORDER);

        PdfPCell c3 = new PdfPCell(new Phrase(label2, labelFont));
        c3.setBackgroundColor(COLOR_LIGHT_GRAY);
        c3.setPadding(8);
        c3.setBorderColor(COLOR_BORDER);

        PdfPCell c4 = new PdfPCell(new Phrase(value2, valueFont));
        c4.setPadding(8);
        c4.setBorderColor(COLOR_BORDER);

        table.addCell(c1);
        table.addCell(c2);
        table.addCell(c3);
        table.addCell(c4);
    }

    /** Section des clauses juridiques générées par HuggingFace */
    private void addAiClauses(Document doc, String clausesIA) throws DocumentException {
        Font sectionFont  = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, COLOR_PRIMARY);
        Font clauseFont   = new Font(Font.FontFamily.HELVETICA, 9.5f, Font.NORMAL, COLOR_DARK);
        Font aiBadgeFont  = new Font(Font.FontFamily.HELVETICA, 8, Font.ITALIC,
                new BaseColor(107, 114, 128));

        Paragraph sectionTitle = new Paragraph("CLAUSES CONTRACTUELLES", sectionFont);
        sectionTitle.setSpacingAfter(4);
        doc.add(sectionTitle);

        Paragraph aiBadge = new Paragraph(
                "✦ Clauses générées automatiquement ", aiBadgeFont);
        aiBadge.setSpacingAfter(10);
        doc.add(aiBadge);

        // Boîte de clauses avec fond légèrement coloré
        PdfPTable clauseBox = new PdfPTable(1);
        clauseBox.setWidthPercentage(100);
        clauseBox.setSpacingAfter(20);

        PdfPCell cell = new PdfPCell();
        cell.setPadding(14);
        cell.setBorderColor(COLOR_SECONDARY);
        cell.setBorderWidth(1.2f);
        cell.setBackgroundColor(new BaseColor(239, 246, 255)); // bleu très pâle

        Paragraph clausesPara = new Paragraph(clausesIA, clauseFont);
        clausesPara.setLeading(15);
        cell.addElement(clausesPara);

        clauseBox.addCell(cell);
        doc.add(clauseBox);
    }

    /** Champs de signature Freelancer + HR */
    private void addSignatureSection(Document doc) throws DocumentException {
        Font sectionFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, COLOR_PRIMARY);
        Font labelFont   = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, COLOR_DARK);
        Font noteFont    = new Font(Font.FontFamily.HELVETICA, 8, Font.ITALIC,
                new BaseColor(107, 114, 128));

        Paragraph sectionTitle = new Paragraph("SIGNATURES", sectionFont);
        sectionTitle.setSpacingAfter(12);
        doc.add(sectionTitle);

        PdfPTable sigTable = new PdfPTable(2);
        sigTable.setWidthPercentage(100);
        sigTable.setWidths(new float[]{1f, 1f});
        sigTable.setSpacingBefore(8);

        // ── Signature Freelancer ──
        PdfPCell freelancerSig = buildSignatureCell(
                "SIGNATURE DU FREELANCER",
                "Lu et approuvé — Bon pour accord",
                labelFont, noteFont
        );
        sigTable.addCell(freelancerSig);

        // ── Signature RH ──
        PdfPCell hrSig = buildSignatureCell(
                "SIGNATURE DU RESPONSABLE RH",
                "Cachet et signature de l'entreprise",
                labelFont, noteFont
        );
        sigTable.addCell(hrSig);

        doc.add(sigTable);

        // Note légale en bas
        doc.add(Chunk.NEWLINE);
        Font footerFont = new Font(Font.FontFamily.HELVETICA, 7.5f, Font.ITALIC,
                new BaseColor(156, 163, 175));
        Paragraph footer = new Paragraph(
                "Ce contrat est établi en deux exemplaires originaux, un pour chaque partie. " +
                        "TrustedWork Tunisia agit en tant qu'intermédiaire de mise en relation " +
                        "et perçoit une commission de placement conformément aux conditions générales d'utilisation.",
                footerFont);
        footer.setAlignment(Element.ALIGN_CENTER);
        doc.add(footer);
    }

    /** Construit une cellule de signature avec zone de saisie */
    private PdfPCell buildSignatureCell(String title, String note,
                                        Font labelFont, Font noteFont) {
        PdfPCell cell = new PdfPCell();
        cell.setBorderColor(COLOR_BORDER);
        cell.setPadding(14);
        cell.setMinimumHeight(130);

        Paragraph titlePara = new Paragraph(title, labelFont);
        titlePara.setSpacingAfter(6);
        cell.addElement(titlePara);

        Paragraph notePara = new Paragraph(note, noteFont);
        notePara.setSpacingAfter(40); // espace pour la signature manuscrite
        cell.addElement(notePara);

        // Ligne de signature
        LineSeparator line = new LineSeparator();
        line.setLineColor(COLOR_DARK);
        line.setLineWidth(0.8f);
        line.setPercentage(80);
        cell.addElement(new Chunk(line));

        Font dateLabelFont = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL,
                new BaseColor(107, 114, 128));
        cell.addElement(new Paragraph("Date : _____ / _____ / _________", dateLabelFont));

        return cell;
    }
}