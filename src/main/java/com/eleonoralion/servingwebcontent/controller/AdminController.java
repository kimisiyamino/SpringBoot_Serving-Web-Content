package com.eleonoralion.servingwebcontent.controller;

import com.eleonoralion.servingwebcontent.entity.Picture;
import com.eleonoralion.servingwebcontent.entity.User;
import com.eleonoralion.servingwebcontent.repository.PictureRepository;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@ControllerAdvice
@Controller
public class AdminController {

    private final PictureRepository pictureRepository;

    @Autowired
    public AdminController(PictureRepository pictureRepository){
        this.pictureRepository = pictureRepository;
    }

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/admin/main")
    public String adminMenu(){
        return "admin/adminMenu";
    }

    @GetMapping("/admin/testpage")
    public String getTestpage(Model model){
        model.addAttribute("pictures", pictureRepository.findAllByOrderById());
        return "/admin/testpage";
    }

    @PostMapping("/admin/testpage")
    public String addPicture(@AuthenticationPrincipal User user,
                         @RequestParam(name = "file", required = true) MultipartFile file,
                         RedirectAttributes redirectAttributes){

        if(file.isEmpty()){
            redirectAttributes.addFlashAttribute("message", "Файл отсутствует!");
            return "redirect:/admin/testpage";
        }

        if(!Objects.equals(file.getContentType(), "image/jpeg") && !Objects.equals(file.getContentType(), "image/png")) {
            redirectAttributes.addFlashAttribute("message", "Неверный формат!");
            return "redirect:/admin/testpage";
        }

        Picture picture = new Picture("", user);

        if(file != null && !file.getOriginalFilename().isEmpty()){

            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                System.out.println(uploadDir.mkdir());
            }

            String uuidFile = UUID.randomUUID().toString();

            String resultFileName = uuidFile + "." + file.getOriginalFilename();

            try {
                file.transferTo(new File(uploadPath + "/" + resultFileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
            picture.setFileName(resultFileName);
            pictureRepository.save(picture);
        }

        return "redirect:/admin/testpage";
    }

    @PostMapping("/admin/testpage/delete/{id}")
    public String deletePicture(@PathVariable Long id, RedirectAttributes redirectAttributes){
        pictureRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Файл удалён!");
        return "redirect:/admin/testpage";
    }

    @ExceptionHandler(SizeLimitExceededException.class)
    public String handleMultipartException(SizeLimitExceededException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "Большой размер файла!");
        return "redirect:/admin/testpage";
    }
}
