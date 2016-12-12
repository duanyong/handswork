package me.duanyong.handswork.context;

import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.duanyong.handswork.url.UrlMapping;
import me.duanyong.handswork.support.View;
import me.duanyong.handswork.util.DirectoryUtil;

public class FrameworkContext {
	public static final String BASE_PACKAGE = DirectoryUtil.toPackageName(DirectoryUtil.findDirectory("action"));

	private static final ThreadLocal<FrameworkContext> contexts = new ThreadLocal<FrameworkContext>();

	public static FrameworkContext getCurrentContext() {
		return contexts.get();
	}

	static void setCurrentContext(FrameworkContext context) {
		contexts.set(context);
	}

	public FrameworkContext() {
	}

	private HttpServletRequest request;

	private HttpServletResponse response;

	private UrlMapping mapping;

	private Map parameters;

	private Object action;

	private Method method;

	private ActionChain chain;

	private View view;

	private String language;

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Object getAction() {
		return action;
	}

	public ActionChain getChain() {
		return chain;
	}

	public UrlMapping getMapping() {
		return mapping;
	}

	public Method getMethod() {
		return method;
	}

	public Map getParameters() {
		return parameters;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public View getView() {
		return view;
	}

	void setAction(Object action) {
		this.action = action;
	}

	void setChain(ActionChain chain) {
		this.chain = chain;
	}

	void setMapping(UrlMapping mapping) {
		this.mapping = mapping;
	}

	void setMethod(Method method) {
		this.method = method;
	}

	void setParameters(Map parameters) {
		this.parameters = parameters;
	}

	void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	void setView(View view) {
		this.view = view;
	}

}
