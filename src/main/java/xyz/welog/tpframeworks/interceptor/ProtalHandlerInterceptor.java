package xyz.welog.tpframeworks.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 拦截器：对请求做拦截处理
 * @author wenlong
 *
 */
public class ProtalHandlerInterceptor extends HandlerInterceptorAdapter {

	@Override
	// 预处理：在控制器方法调用前执行，返回值为是否中断，true,表示继续执行（下一个拦截器或处理器），false则会中断后续的所有操作，所以我们需要使用response来响应请求
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		return super.preHandle(request, response, handler);
	}

	@Override
	// 返回处理：在控制器方法调用后，解析视图前调用，我们可以对视图和模型做进一步渲染或修改
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	// 后处理：整个请求完成，即视图渲染结束后调用，这个时候可以做些资源清理工作，或日志记录等
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		super.afterCompletion(request, response, handler, ex);
	}

	@Override
	// 用于执行异步请求：当Controller中有异步请求方法的时候会触发该方法时，异步请求先支持preHandle、然后执行afterConcurrentHandlingStarted
	public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		super.afterConcurrentHandlingStarted(request, response, handler);
	}
	
}
