package kr.co.mcedu.common.controller;

import kr.co.mcedu.utils.SessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Controller
public class ViewController {
    private static final String MAIN_PAGE = "/custom/mng";
    private static final String LOGIN_PAGE = "login";
    private static final String ERROR_PAGE = "error";
    @GetMapping("/")
    public void mainView(HttpServletResponse response) throws IOException {
        response.sendRedirect(MAIN_PAGE);
    }

    /**
     * View 1depth
     */
    @GetMapping("/{path1:[a-z]*}")
    public String firstDepth(@PathVariable String path1, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        if(isExplorer(request)) {
            return ERROR_PAGE;
        }
        Optional<Authentication> principal = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
        if (LOGIN_PAGE.equals(path1)
                && !"anonymousUser".equals(principal.map(Authentication::getPrincipal).orElse("anonymousUser"))) {
            response.sendRedirect(MAIN_PAGE);
            return MAIN_PAGE;
        }
        return path1;
    }

    /**
     * View 2depth
     */
    @GetMapping("/{path1:[a-z]*}/{path2:[a-z]*}")
    public ModelAndView secondaryDepth(@PathVariable String path1, @PathVariable String path2, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        if(isExplorer(request)) {
            mav.setViewName(ERROR_PAGE);
            return mav;
        }
        mav.setViewName(path1 + "/" + path2);
        mav.addObject("userInfo", SessionUtils.getUserInfo());
        return mav;
    }

    /**
     * View Popup
     */
    @GetMapping("/popup/{path1:[a-z]*}/{path2:[a-z]*}")
    public String popupView(@PathVariable String path1, @PathVariable String path2, HttpServletRequest request) {
        if(isExplorer(request)) {
            return ERROR_PAGE;
        }
        return "popup/" + path1 + "_" + path2;
    }


    private boolean isExplorer(HttpServletRequest request) {
        String agent = request.getHeader("User-Agent");
        return agent.toLowerCase().contains("trident");
    }
}
