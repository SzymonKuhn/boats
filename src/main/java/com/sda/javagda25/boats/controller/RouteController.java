package com.sda.javagda25.boats.controller;

import com.sda.javagda25.boats.model.Account;
import com.sda.javagda25.boats.model.CharacteristicPoint;
import com.sda.javagda25.boats.model.MeasurePoint;
import com.sda.javagda25.boats.model.Route;
import com.sda.javagda25.boats.service.AccountService;
import com.sda.javagda25.boats.service.CharacteristicPointService;
import com.sda.javagda25.boats.service.MeasurePointService;
import com.sda.javagda25.boats.service.RouteService;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;

@Controller
@RequestMapping("/route/")
public class RouteController {

    private RouteService routeService;
    private AccountService accountService;
    private MeasurePointService measurePointService;
    private CharacteristicPointService characteristicPointService;

    @Autowired
    public RouteController(RouteService routeService, AccountService accountService,
                           MeasurePointService measurePointService,
                           CharacteristicPointService characteristicPointService) {
        this.routeService = routeService;
        this.accountService = accountService;
        this.measurePointService = measurePointService;
        this.characteristicPointService = characteristicPointService;
    }

    @PostMapping("/add")
    public String add(Route route, Principal principal, @RequestParam(value = "file", required = false) MultipartFile file) {

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

        if (route.getId() != null) {
            Route existingRoute = routeService.getById(route.getId());
            route.setMeasurePoints(existingRoute.getMeasurePoints());
            route.setCharacteristicPoints(existingRoute.getCharacteristicPoints());
        }

        Long id = routeService.save(route);
        Route routeSaved = routeService.getById(id);
        return "redirect:/route/addMeasurePoint/" + routeSaved.getId();
    }

    @GetMapping("/addMeasurePoint/{id}")
    public String addMeasurePoint(Model model, Principal principal,
                                  @PathVariable(name = "id") Long id,
                                  @RequestParam(name = "pointId", required = false) Long pointId,
                                  @RequestParam(name = "sort", defaultValue = "pointName") String sort,
                                  @RequestParam(name = "search", defaultValue = "") String search,
                                  @RequestParam(name = "page", defaultValue = "0") int page,
                                  @RequestParam(name = "size", defaultValue = "20") int size) {
        Route route = routeService.getById(id);
        Account account = accountService.getByUsername(principal.getName());
        if (!route.getAccount().equals(account)) {
            return "redirect:/account/details";
        }

        if (pointId != null) {
            MeasurePoint measurePoint = measurePointService.getById(pointId);
            if (!route.getMeasurePoints().contains(measurePoint)) {
                route.getMeasurePoints().add(measurePoint);
                routeService.save(route);
            }
        }

        Page<MeasurePoint> measurePointsPage = measurePointService.measurePointPage(search, search, PageRequest.of(page, size, Sort.by(sort)));
        model.addAttribute("sort", sort);
        model.addAttribute("search", search);
        model.addAttribute("measurePoints", measurePointsPage);
        model.addAttribute("route", route);
        return "route-add-measure-point";
    }


    @GetMapping("/deleteMeasurePoint/{id}")
    public String deleteMeasurePoint(Model model, Principal principal,
                                     @PathVariable(name = "id") Long id,
                                     @RequestParam(name = "pointId", required = false) Long pointId,
                                     @RequestParam(name = "sort", defaultValue = "pointName") String sort,
                                     @RequestParam(name = "search", defaultValue = "") String search,
                                     @RequestParam(name = "page", defaultValue = "0") int page,
                                     @RequestParam(name = "size", defaultValue = "20") int size) {
        Route route = routeService.getById(id);
        Account account = accountService.getByUsername(principal.getName());
        if (!route.getAccount().equals(account)) {
            return "redirect:/account/details";
        }

        if (pointId != null) {
            MeasurePoint measurePoint = measurePointService.getById(pointId);
            if (route.getMeasurePoints().contains(measurePoint)) {
                route.getMeasurePoints().remove(measurePoint);
                routeService.save(route);
            }
        }

        Page<MeasurePoint> measurePointsPage = measurePointService.measurePointPage(search, search, PageRequest.of(page, size, Sort.by(sort)));
        model.addAttribute("sort", sort);
        model.addAttribute("search", search);
        model.addAttribute("measurePoints", measurePointsPage);
        model.addAttribute("route", route);
        return "route-add-measure-point";
    }

    @GetMapping("/addCharacteristicPoint/{id}")
    public String addCharacteristicPoint(Model model, Principal principal,
                                         @PathVariable(name = "id") Long id,
                                         @RequestParam(name = "pointId", required = false) Long pointId,
                                         @RequestParam(name = "sort", defaultValue = "name") String sort,
                                         @RequestParam(name = "search", defaultValue = "") String search,
                                         @RequestParam(name = "page", defaultValue = "0") int page,
                                         @RequestParam(name = "size", defaultValue = "20") int size) {
        Route route = routeService.getById(id);
        Account account = accountService.getByUsername(principal.getName());
        if (!route.getAccount().equals(account)) {
            return "redirect:/account/details";
        }

        if (pointId != null) {
            CharacteristicPoint characteristicPoint = characteristicPointService.getById(pointId);
            if (!route.getCharacteristicPoints().contains(characteristicPoint)) {
                route.getCharacteristicPoints().add(characteristicPoint);
                routeService.save(route);
            }
        }

        Page<CharacteristicPoint> characteristicPointPage = characteristicPointService.userAndPublicCharacteristicPointPage(search, PageRequest.of(page, size, Sort.by(sort)), account);
        model.addAttribute("sort", sort);
        model.addAttribute("search", search);
        model.addAttribute("characteristicPoints", characteristicPointPage);
        model.addAttribute("route", route);
        return "route-add-characteristic-point";
    }

    @GetMapping("/deleteCharacteristicPoint/{id}")
    public String deleteCharacteristicPoint(Model model, Principal principal,
                                            @PathVariable(name = "id") Long id,
                                            @RequestParam(name = "pointId", required = false) Long pointId,
                                            @RequestParam(name = "sort", defaultValue = "name") String sort,
                                            @RequestParam(name = "search", defaultValue = "") String search,
                                            @RequestParam(name = "page", defaultValue = "0") int page,
                                            @RequestParam(name = "size", defaultValue = "20") int size) {
        Route route = routeService.getById(id);
        Account account = accountService.getByUsername(principal.getName());
        if (!route.getAccount().equals(account)) {
            return "redirect:/account/details";
        }

        if (pointId != null) {
            CharacteristicPoint characteristicPoint = characteristicPointService.getById(pointId);
            if (route.getCharacteristicPoints().contains(characteristicPoint)) {
                route.getCharacteristicPoints().remove(characteristicPoint);
                routeService.save(route);
            }
        }

        Page<CharacteristicPoint> characteristicPointPage = characteristicPointService.userAndPublicCharacteristicPointPage(search, PageRequest.of(page, size, Sort.by(sort)), account);
        model.addAttribute("sort", sort);
        model.addAttribute("search", search);
        model.addAttribute("characteristicPoints", characteristicPointPage);
        model.addAttribute("route", route);
        return "route-add-characteristic-point";
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

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable(name = "id") Long id, Principal principal) {
        Account account = accountService.getByUsername(principal.getName());
        Route route = routeService.getById(id);
        if (!route.getAccount().equals(account)) {
            return "redirect:/account/details";
        }
        routeService.delete(route);
        return "redirect:/route/list";
    }

    @GetMapping("/details/{id}")
    public String editRoute(Model model, @PathVariable(name = "id") Long id, Principal principal) {
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
