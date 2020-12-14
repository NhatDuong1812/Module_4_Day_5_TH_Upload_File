package com.codegym.controller;

import com.codegym.entities.MyFormModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.*;

@Controller
@RequestMapping("/upload")
public class MyFormController {

    @GetMapping("/one-file")
    public ModelAndView uploadFile() {
        ModelAndView modelAndView = new ModelAndView("upload-one-file");
        modelAndView.addObject("myFormModel", new MyFormModel());
        System.out.println("Hello");
        return modelAndView;
    }

    @GetMapping("/multi-file")
    public ModelAndView uploadMultiFiles() {
        ModelAndView modelAndView = new ModelAndView("upload-multi-file");
        modelAndView.addObject("myFormModel", new MyFormModel());
        return modelAndView;
    }

    @PostMapping("/one-file")
    public ModelAndView uploadFile(HttpServletRequest request, @ModelAttribute("myFormModel") MyFormModel myFormModel) {
        return this.doUpload(request, myFormModel);
    }

    @PostMapping("/multi-file")
    public ModelAndView uploadMultiFiles(HttpServletRequest request, @ModelAttribute("myFormModel") MyFormModel myFormModel) {
        return this.doUpload(request, myFormModel);
    }

    private ModelAndView doUpload(HttpServletRequest request, MyFormModel myFormModel) {
        String uploadRootPath = request.getServletContext().getRealPath("uploads");
        File uploadRootDir = new File(uploadRootPath);
        if (!uploadRootDir.exists()) {
            uploadRootDir.mkdir(); // Tạo thư mục uploads nếu chưa có
        }

        CommonsMultipartFile[] files = myFormModel.getFiles();
        for (CommonsMultipartFile file : files) { // Duyệt tất cả files
            String name = file.getOriginalFilename();
            if (name != null && name.length() > 0) {
                try {
                    File writeFile = new File(uploadRootDir.getAbsolutePath() + File.separator + name);
                    BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(writeFile));
                    stream.write(file.getBytes());
                    stream.flush();
                    stream.close();

                    System.out.println("Write file: " + writeFile);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        ModelAndView modelAndView = new ModelAndView("upload-result");
        modelAndView.addObject(
                "myFormModel", myFormModel);
        return modelAndView;
    }
}
