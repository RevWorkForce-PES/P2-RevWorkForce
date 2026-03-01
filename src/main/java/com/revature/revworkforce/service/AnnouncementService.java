package com.revature.revworkforce.service;

import com.revature.revworkforce.dto.AnnouncementDTO;
import com.revature.revworkforce.model.Announcement;
import java.util.List;

public interface AnnouncementService {
    AnnouncementDTO createAnnouncement(AnnouncementDTO dto, String createdBy);

    AnnouncementDTO updateAnnouncement(Long announcementId, AnnouncementDTO dto);

    AnnouncementDTO getAnnouncementById(Long announcementId);

    List<Announcement> getAllAnnouncements();

    List<AnnouncementDTO> getActiveAnnouncements();

    List<AnnouncementDTO> getRecentAnnouncements(int limit);

    void deactivateAnnouncement(Long announcementId);

    void deleteAnnouncement(Long announcementId);

    void deactivateExpiredAnnouncements();

    AnnouncementDTO convertToDTO(Announcement announcement);

    List<AnnouncementDTO> getAllAnnouncementsAsDTO();
}
