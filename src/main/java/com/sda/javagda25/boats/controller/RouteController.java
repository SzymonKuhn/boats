package com.sda.javagda25.boats.controller;

import com.sda.javagda25.boats.model.Account;
import com.sda.javagda25.boats.model.MeasurePoint;
import com.sda.javagda25.boats.model.Route;
import com.sda.javagda25.boats.service.AccountService;
import com.sda.javagda25.boats.service.MeasurePointService;
import com.sda.javagda25.boats.service.RouteService;
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
import java.util.HashSet;
import java.util.Map;

@Controller
@RequestMapping("/route/")
public class RouteController {

    private RouteService routeService;
    private AccountService accountService;
    private MeasurePointService measurePointService;

    @Autowired
    public RouteController(RouteService routeService, AccountService accountService, MeasurePointService measurePointService) {
        this.routeService = routeService;
        this.accountService = accountService;
        this.measurePointService = measurePointService;
    }

    @PostMapping("/add")
    public String add(Route route, Principal principal, @RequestParam(value = "file", required = false) MultipartFile file, Model model) {

        Account account = accountService.getByUsername(principal.getName());

        if (route == null) {
            return "redirect:/account/details";
        }
        if (route.getAccount() == null) {
            route.setAccount(account);
        }

        if (!file.isEmpty()) {
            try {
                route.setPhoto(file.getBytes());
                ByteArrayOutputStream thumbnail = createThumbnail(file, 70);
                route.setThumbnail(thumbnail.toByteArray());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Long id = routeService.save(route);
        Route routeSaved = routeService.getById(id);
        model.addAttribute("route", routeSaved);
        return "route-edit";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("route", new Route());
        model.addAttribute("measurePoints", measurePointService.findAllMeasurePoints());
        return "route-add";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable(name = "id") Long id, Principal principal) {
        Account account = accountService.getByUsername(principal.getName());
        Route route = routeService.getById(id);
        if (!route.getAccount().equals(account)) {
            return "redirect:/account/details";
        }
        model.addAttribute("route", route);
        model.addAttribute("measurePoints", measurePointService.findAllMeasurePoints());
        return "route-edit";
    }

    @GetMapping("/details/{id}")
    public String editRoute (Model model, @PathVariable(name = "id") Long id, Principal principal) {
        Account account = accountService.getByUsername(principal.getName());
        Route route = routeService.getById(id);
        boolean editable = route.getAccount().equals(account);
        model.addAttribute("route", route);
        model.addAttribute("editable", editable);
        return "route-details";
    }

    @GetMapping("/list")
    public String listAll(Model model, Principal principal) {
        Account account = accountService.getByUsername(principal.getName());
        model.addAttribute("routes", routeService.findAllByAccountIsLike(account));
        return "route-list";
    }

    @GetMapping("/public")
    public String listPublic(Model model) {
        model.addAttribute("routes", routeService.findAllPublicRoutes());
        return "route-public-list";
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
