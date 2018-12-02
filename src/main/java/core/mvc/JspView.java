package core.mvc;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Strings;

import sun.plugin.dom.exception.InvalidStateException;

public class JspView implements View {
    private String DEFAULT_REDIRECT_PREFIX = "redirect:";
    private String viewName;

    public JspView(String viewName) {
        if(Strings.isNullOrEmpty(viewName)) {
            throw new InvalidStateException("view Name must not be null or empty");
        }

        this.viewName = viewName;
    }

    @Override
    public void render(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (viewName.startsWith(DEFAULT_REDIRECT_PREFIX)) {
            response.sendRedirect(
                    viewName.substring(DEFAULT_REDIRECT_PREFIX.length())
            );
            return;
        }
        RequestDispatcher rd = request.getRequestDispatcher(viewName);
        rd.forward(request, response);
    }
}
