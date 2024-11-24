package app.getgainz.service.operation;

import app.getgainz.entity.Photo;
import app.getgainz.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PhotoService {

    @Autowired
    private PhotoRepository photoRepository;

    public Photo savePhoto(MultipartFile file, Long userId) throws IOException {
        // Create a new Photo object and populate it
        Photo photo = new Photo();
        photo.setFilename(file.getOriginalFilename()); // Set the file name
        photo.setPath("/uploads/" + file.getOriginalFilename()); // Set the file path
        photo.setUserId(userId); // Set the user ID
        photo.setUploadTime(LocalDateTime.now()); // Set the upload timestamp
        return photoRepository.save(photo); // Save photo metadata to the database
    }

    public List<Photo> getAllPhotosByUser(Long userId) {
        // Fetch all photos associated with the given user ID
        return photoRepository.findByUserId(userId);
    }
}
