package com.example.service;

import com.example.dto.attach.AttachDTO;
import com.example.entity.attach.AttachEntity;
import com.example.exceptions.AppBadRequestException;
import com.example.exceptions.ItemNotFoundException;
import com.example.repository.AttachRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.UUID;

@Service
public class AttachService {
    @Autowired
    private AttachRepository attachRepository;
    @Value("${attach.folder.name}")
    private  String folderName;
    @Value("${attach.open.url}")
    private  String url;

    public String saverToSystem(MultipartFile file) {
        try {
            File folder = new File("attaches");
            if (!folder.exists()) {
                folder.mkdirs();
            }
            byte[] bytes = file.getBytes();
            Path path = Paths.get(folderName + "/" + file.getOriginalFilename());
            Files.write(path, bytes);
            return file.getOriginalFilename();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public AttachDTO save(MultipartFile file) {
        if (file.isEmpty()) throw new ItemNotFoundException("file not found");
        String pathFolder = getYMDString();
        File folder = new File(folderName + "/" + pathFolder);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String key = UUID.randomUUID().toString();
        String extension = getExtension(file.getOriginalFilename());
        System.out.println(extension);

        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(folderName + "/" + pathFolder + "/" + key + "." + extension);
            System.out.println("PATH:" + path.toString());
            Files.write(path, bytes);
            AttachEntity entity = new AttachEntity();
            entity.setOriginalName(file.getOriginalFilename());
            entity.setPath(pathFolder);
            entity.setSize(file.getSize());
            entity.setExtension(extension);
            entity.setId(key);
            attachRepository.save(entity);
            AttachDTO dto = new AttachDTO();
            dto.setId(key);
            dto.setOriginalName(entity.getOriginalName());
            dto.setUrl(folderName + "/" + pathFolder + "/" + key + "." + extension);
            return dto;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] loadImage(String filename) {
        try {
            BufferedImage originalImage = ImageIO.read(new File("attaches/" + filename));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(originalImage, "png", baos);
            baos.flush();
            baos.close();
            return baos.toByteArray();
        } catch (IOException e) {
            return new byte[0];
        }
    }

    private String getYMDString() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DATE);
        return year + "/" + month + "/" + day;
    }

    private String getExtension(String filename) {
        int lastIndex = filename.lastIndexOf(".");
        return filename.substring(lastIndex + 1);
    }

    public byte[] loadImageById(String id) {
        AttachEntity entity = getById(id);
        try {
            String url = folderName + "/" + entity.getPath() + "/" + id + "." + entity.getExtension();
            BufferedImage image = ImageIO.read(new File(url));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, entity.getExtension(), baos);
            baos.flush();
            baos.close();
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] loadByIdGeneral(String id) {
        AttachEntity entity = getById(id);
        try {
            String url = folderName + "/" + entity.getPath() + "/" + id + "." + entity.getExtension();
            File file = new File(url);
            byte[] bytes = new byte[(int) file.length()];
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytes);
            fileInputStream.close();
            return bytes;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public AttachEntity getById(String id) {
        return attachRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("item not found"));
    }

    public void deletePhoto(String photoId) {
        AttachEntity entity = getById(photoId);
        attachRepository.deleteById(photoId);
        File file = new File(getPath(entity.getPath(), entity.getId(), entity.getExtension()));
        if (file.exists()) {
            file.delete();
        }

    }

    private String getPath(String path, String id, String extension) {
        return folderName + "/" + path + "/" + id + "." + extension;
    }
    public String getAttachUrl( String id) {
        return url+ "/img" +"/" + id ;
    }

    public ResponseEntity<Resource> download(String id) {
        AttachEntity entity = getById(id);
        try {
            Path file=Paths.get(getPath(entity.getPath(), entity.getId(), entity.getExtension()));
            Resource resource=new UrlResource(file.toUri());
            if(resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + entity.getOriginalName() + "\"").body(resource);

            }else throw new AppBadRequestException("Could not read file");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

    }
}
