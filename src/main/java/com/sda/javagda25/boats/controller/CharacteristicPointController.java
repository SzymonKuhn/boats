package com.sda.javagda25.boats.controller;

import com.sda.javagda25.boats.model.Account;
import com.sda.javagda25.boats.model.CharacteristicPoint;
import com.sda.javagda25.boats.model.PointCategory;
import com.sda.javagda25.boats.service.AccountService;
import com.sda.javagda25.boats.service.CharacteristicPointService;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;

@Controller
@RequestMapping("/point/")
public class CharacteristicPointController {

    private CharacteristicPointService characteristicPointService;
    private AccountService accountService;

    @Autowired
    public CharacteristicPointController(CharacteristicPointService characteristicPointService, AccountService accountService) {
        this.characteristicPointService = characteristicPointService;
        this.accountService = accountService;
    }

    @PostMapping("/add")
    public String add(CharacteristicPoint characteristicPoint, Principal principal, @RequestParam(value = "file", required = false) MultipartFile file) {

        Account account = accountService.getByUsername(principal.getName());
        if (characteristicPoint == null) {
            return "redirect:/account/details";
        }
        if (characteristicPoint.getAccount() == null) {
            characteristicPoint.setAccount(account);
        }

        if (!file.isEmpty()) {
            try {
                characteristicPoint.setPhoto(file.getBytes());
                ByteArrayOutputStream thumbnail = createThumbnail(file, 70);
                characteristicPoint.setThumbnail(thumbnail.toByteArray());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        characteristicPointService.save(characteristicPoint);

        return "redirect:/point/list";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("characteristicPoint", new CharacteristicPoint());
        model.addAttribute("pointCategories", PointCategory.values());
        return "characteristic-point-add";
    }

    @GetMapping("/list")
    public String listAll(Model model, Principal principal) {
        Account account = accountService.getByUsername(principal.getName());
        model.addAttribute("characteristicPoints", characteristicPointService.findAllByAccountIsLike(account));
        return "characteristic-point-list";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable(name = "id") Long id, Model model, Principal principal, HttpServletRequest request) {
        Account account = accountService.getByUsername(principal.getName());
        CharacteristicPoint characteristicPoint = characteristicPointService.getById(id);
        boolean editable = characteristicPoint.getAccount().equals(account);
        model.addAttribute("point", characteristicPoint);
        model.addAttribute("lat", characteristicPoint.getLat());
        model.addAttribute("lng", characteristicPoint.getLng());
        model.addAttribute("editable", editable);
        return "characteristic-point-details";
    }

    @GetMapping("/public")
    public String listPublic(Model model) {
        model.addAttribute("characteristicPoints", characteristicPointService.getAllPublicPoints());
        return "characteristic-point-public-list";
    }

    private ByteArrayOutputStream createThumbnail(MultipartFile originalFile, Integer width) throws IOException {
        ByteArrayOutputStream thumbOutput = new ByteArrayOutputStream();
        BufferedImage thumbImg = null;
        BufferedImage img = ImageIO.read(originalFile.getInputStream());
        thumbImg = Scalr.resize(img, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, width, Scalr.OP_ANTIALIAS);
        ImageIO.write(thumbImg, originalFile.getContentType().split("/")[1], thumbOutput);
        return thumbOutput;
    }

}
