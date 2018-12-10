package core.mvc;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.nmvc.AnnotationHandlerMapping;
import core.nmvc.HandlerExecution;
import core.nmvc.HandlerKey;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private RequestMapping rm;
    private Map<HandlerKey, HandlerExecution> newRequestMap;
    private AnnotationHandlerMapping annotationHandlerMapping;

    @Override
    public void init() throws ServletException {
        rm = new RequestMapping();
        rm.initMapping();

        annotationHandlerMapping = new AnnotationHandlerMapping("core.nmvc");
        annotationHandlerMapping.initialize();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            ModelAndView mav = dispatch(req, resp);
            View view = mav.getView();
            view.render(mav.getModel(), req, resp);
        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    private ModelAndView dispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        Controller controller = rm.findController(req.getRequestURI());
        ModelAndView mav;

        if(controller == null) {
            HandlerExecution execution = annotationHandlerMapping.getHandler(req);
            if(execution == null)
                throw new ServletException("no matching case");
            mav = execution.handle(req, resp);

        } else {
            mav = controller.execute(req, resp);
        }

        return mav;
    }
}
