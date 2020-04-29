package com.kor.switter.controller;

import com.kor.switter.domain.Message;
import com.kor.switter.domain.User;
import com.kor.switter.domain.dto.MessageDto;
import com.kor.switter.repos.MessageRepo;
import com.kor.switter.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Controller
public class MessageController {
    @Autowired
    private MessageRepo messageRepo;
    @Autowired
    private MessageService messageService;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/")
    public String greeting(Model model) {
        model.addAttribute("activeHome", true);

        return "greeting";
    }

    @GetMapping("/main")
    public String main(
            @RequestParam(required = false, defaultValue = "") String filter,
            Model model,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal User user) {
        Page<MessageDto> page = messageService.messageList(pageable, filter, user);

        model.addAttribute("page", page);
        model.addAttribute("url", "/main");
        model.addAttribute("filter", filter);

        model.addAttribute("activeNews", true);

        return "main";
    }

    @PostMapping("/main")
    public String add(
            @AuthenticationPrincipal User user,
            @Valid Message message,
            BindingResult bindingResult,
            Model model,
            @RequestParam("file") MultipartFile file) throws IOException {
        createNewMessage(user, message, bindingResult, model, file);

        return "redirect:/main";
    }

    private void saveFile(@AuthenticationPrincipal User user, @Valid Message message, @RequestParam("file") MultipartFile file) throws IOException {
        if (file != null) {
            File uploadDir = new File(uploadPath + "/" + user.getId());

            if (!uploadDir.exists() && !file.getOriginalFilename().isEmpty()) {
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = user.getId() + "/" + uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultFilename));

            message.setFilename(resultFilename);
        }
    }

    @GetMapping("/user-messages/{author}")
    public String editMessage(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User author,
            Model model,
            @RequestParam(required = false) Message message,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<MessageDto> page = messageService.messageListForUser(pageable, currentUser, author);

        model.addAttribute("userChannel", author);
        model.addAttribute("subscriptionsCount", author.getSubscriptions().size());
        model.addAttribute("subscribersCount", author.getSubscribers().size());
        model.addAttribute("isSubscriber", author.getSubscribers().contains(currentUser));
        model.addAttribute("page", page);
        model.addAttribute("isCurrentUser", currentUser.equals(author));
        model.addAttribute("url", "/user-messages/" + author.getId());

        model.addAttribute("activeMyMessages", true);

        return "userMessages";
    }

    @GetMapping("/messages/{message}/like")
    public String like(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Message message,
            RedirectAttributes redirectAttributes,
            @RequestHeader(required = false) String referer
    ) {
        Set<User> likes = message.getLikes();

        if (likes.contains(currentUser)) {
            likes.remove(currentUser);
        } else {
            likes.add(currentUser);
        }

        UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();
        components.getQueryParams()
                .entrySet()
                .forEach(pair -> redirectAttributes.addAttribute(pair.getKey(), pair.getValue()));

        return "redirect:" + components.getPath();
    }

    @GetMapping("/messages-edit")
    public String editMessage(@AuthenticationPrincipal User user,
                              @RequestParam("message") Message message,
                              Model model,
                              RedirectAttributes redirectAttributes,
                              @RequestHeader(required = false) String referer) {
        model.addAttribute("message", message);

        if (!StringUtils.isEmpty(referer)) {
            UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();
            components.getQueryParams()
                    .entrySet()
                    .forEach(pair -> redirectAttributes.addAttribute(pair.getKey(), pair.getValue()));

            if (!components.getPath().contains("/messages-edit")) {
                model.addAttribute("prevPage", components.getPath());
            }
        }

        return "messageEdit";
    }

    @PostMapping("/messages-edit")
    public String updateMessage(
            @AuthenticationPrincipal User currentUser,
            @RequestParam("id") Message message,
            @RequestParam("text") String text,
            @RequestParam("tag") String tag,
            @RequestParam("file") MultipartFile file,
            @RequestParam("prevPage") String prevPage,
            RedirectAttributes redirectAttributes) throws IOException {

        if (message.getAuthor().equals(currentUser)) {
            if (!StringUtils.isEmpty(text)) {
                message.setText(text);
            }

            if (!StringUtils.isEmpty(text)) {
                message.setTag(tag);
            }

            if (!StringUtils.isEmpty(file.getOriginalFilename()) && !message.getShoreFilename().equals(file.getOriginalFilename())) {
                saveFile(currentUser, message, file);
            }

            messageRepo.save(message);
        }

        redirectAttributes.addFlashAttribute("prevPage", prevPage);

        return "redirect:/messages-edit?message=" + message.getId();
    }

    @PostMapping("/user-messages/{user}")
    public String createMessage(@AuthenticationPrincipal User user,
                                @Valid Message message,
                                BindingResult bindingResult,
                                Model model,
                                @RequestParam("file") MultipartFile file) throws IOException {
        createNewMessage(user, message, bindingResult, model, file);

        return "redirect:/user-messages/+" + user.getId();
    }

    private void createNewMessage(
            @AuthenticationPrincipal User user,
            @Valid Message message, BindingResult bindingResult,
            Model model, @RequestParam("file") MultipartFile file) throws IOException {
        message.setAuthor(user);

        Date date = new Date(System.currentTimeMillis());
        message.setTimeOfCreation(new Timestamp(date.getTime()));

        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getStringStringMap(bindingResult);
            model.mergeAttributes(errorsMap);
            model.addAttribute("message", message);
        } else {
            saveFile(user, message, file);
            model.addAttribute("message", null);
            messageRepo.save(message);
        }
    }
}
