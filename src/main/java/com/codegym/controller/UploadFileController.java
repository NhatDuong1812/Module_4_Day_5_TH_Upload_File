package com.codegym.controller;

import com.codegym.entities.UploadFileModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/upload")
public class UploadFileController {

    @GetMapping("/one-file")
    public ModelAndView uploadFile() {
        ModelAndView modelAndView = new ModelAndView("upload-one-file");
        modelAndView.addObject("uploadFileModel", new UploadFileModel());
        return modelAndView;
    }

    @GetMapping("/multi-file")
    public ModelAndView uploadMultiFiles() {
        ModelAndView modelAndView = new ModelAndView("upload-multi-file");
        modelAndView.addObject("uploadFileModel", new UploadFileModel());
        return modelAndView;
    }

    @PostMapping("/one-file")
    public String uploadFile(HttpServletRequest request, Model model, @ModelAttribute("uploadFileModel") UploadFileModel uploadFileModel) {
        return this.doUpload(request, model, uploadFileModel);
    }

    @PostMapping("/multi-file")
    public String uploadMultiFiles(HttpServletRequest request, Model model, @ModelAttribute("uploadFileModel") UploadFileModel uploadFileModel) {
        return this.doUpload(request, model, uploadFileModel);
    }

    private String doUpload(HttpServletRequest request, Model model, UploadFileModel uploadFileModel) {
        Map<File, String> uploadedFiles = new HashMap<>();
        String uploadRootPath = request.getServletContext().getRealPath("uploads");
        File uploadRootDir = new File(uploadRootPath);
        if (!uploadRootDir.exists()) {
            uploadRootDir.mkdir(); // Tạo thư mục uploads nếu chưa có
        }

        CommonsMultipartFile[] files = uploadFileModel.getFiles();
        for (CommonsMultipartFile file : files) { // Duyệt tất cả files
            String name = file.getOriginalFilename();
            if (name != null && name.length() > 0) {
                try {
                    File writeFile = new File(uploadRootDir.getAbsolutePath() + File.separator + name);
                    BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(writeFile));
                    stream.write(file.getBytes());
                    stream.flush();
                    stream.close();
                    uploadedFiles.put(writeFile, name); // Gán đường dẫn upload file vào Map
                    System.out.println("Write file: " + writeFile);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        model.addAttribute("description", uploadFileModel.getDescription());
        model.addAttribute("uploadedFiles", uploadedFiles);
        return "upload-result";
    }
}
