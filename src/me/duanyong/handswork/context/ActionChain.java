package me.duanyong.handswork.context;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.duanyong.handswork.profiling.Profiler;
import me.duanyong.handswork.support.Interceptor;
import me.duanyong.handswork.support.View;
import me.duanyong.handswork.support.annotation.InterceptorSwitch;
import me.duanyong.handswork.url.UrlMapping;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import me.duanyong.handswork.url.RestfulUrlMapper;
import me.duanyong.handswork.util.ActionLoaderUtil;
import me.duanyong.handswork.view.ErrorView;

public class ActionChain {

	private static final Logger log = LogManager.getLogger();

	@SuppressWarnings("unchecked")
	public static View fire(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Profiler.clear();
		Profiler.start("web request \"" + request.getRequestURI() + "\"");

		UrlMapping mapping = RestfulUrlMapper.map(request);
		if (mapping == null) {
			return null;
		}

		FrameworkContext context = new FrameworkContext();
		FrameworkContext.setCurrentContext(context);

		context.setRequest(request);
		context.setResponse(response);
		context.setMapping(mapping);
		//context.setLanguage(I18nLoader.findAvailableLanguage(request));
		context.setMethod(ActionLoaderUtil.loadMethod(mapping));
		context.setAction(ActionLoaderUtil.newAction(mapping));

		// 将所有HTTP参数(包括URI上的/id/1格式的参数)放入parameters中
		context.setParameters(new HashMap(mapping.getExtraParameters()));
		context.getParameters().putAll(new HashMap(request.getParameterMap()));

		// 准备ActionChain
		prepareActionChain();

		// 全部准备完毕，进入ActionChain，开始执行第一个Interceptor，直到某个View对象被返回
		try {
			context.setView(context.getChain().invoke());
		
		} catch (Throwable e) {
			// 打印该异常
			log.warn("", e);

			if (!context.getChain().actionExecuted) {
				// Action还没有得到执行，说明该异常由interceptor抛出，需要结束其性能监测
				Profiler.finish();
			}

			// 返回错误提示页面
			View view = new ErrorView(e);

			try {
				Profiler.start("View " + view);
				view.execute();
			} catch (Exception e2) {
				// 忽略
			} finally {
				Profiler.finish();
			}

			context.setView(view);
		} finally {
			byte[] bytes = null;
			try {
				if (context.getView() != null && (bytes = context.getView().outputs()) != null && bytes.length > 0) {
					context.getResponse().getOutputStream().write(bytes);
					context.getResponse().getOutputStream().flush();
				}
			} finally {
				Profiler.finish();
			}
		}

		return context.getView();
	}

	private static void prepareActionChain() {
		List<Interceptor> interceptors = new ArrayList<Interceptor>();
		List ons = new ArrayList();
		List offs = new ArrayList();

		// 拿到方法的注解配置
		InterceptorSwitch s = null;
		if ((s = FrameworkContext.getCurrentContext().getMethod().getAnnotation(InterceptorSwitch.class)) != null) {
			ons = Arrays.asList(s.on());
			offs = Arrays.asList(s.off());
		}

		// 拿到所有刚实例化的interceptor对象，按照注解配置装载需要执行的interceptor进入ActionChain
		// 注：此处ons和offs的逻辑判断也许可以简化，但是当前代码更容易表达配置准则：on优先，off其次，defaultOn最后
		for (Interceptor i : ActionLoaderUtil.loadAllInterceptors()) {
			if (ons.contains(i.getClass())) {
				interceptors.add(i);
			} else if (offs.contains(i.getClass()) || !i.isDefaultOn()) {
				continue;
			} else {
				interceptors.add(i);
			}
		}

		// 插入Framework自身的Interceptor（所谓的"plugin"）
		for (Interceptor i : ActionLoaderUtil.loadAllPlugins()) {
			if (i.isDefaultOn()) {
				interceptors.add(i);
			}
		}

		// 对所有Interceptor按照各自ordering的值由小到大排序
		Collections.sort(interceptors, new Comparator<Interceptor>() {
			public int compare(Interceptor o1, Interceptor o2) {
				return o1.ordering() - o2.ordering();
			}
		});

		FrameworkContext.getCurrentContext().setChain(new ActionChain(interceptors));
	}

	private List<Interceptor> interceptors = new ArrayList<Interceptor>();

	private int step;

	private boolean viewExecuted;

	private boolean actionExecuted;

	private ActionChain(List<Interceptor> interceptors) {
		this.interceptors = interceptors;
	}

	public boolean isViewExecuted() {
		return this.viewExecuted;
	}

	public View invoke() throws Throwable {
		View view = null;
		Interceptor interceptor = null;
		if (step < interceptors.size()) {
			if (step != 0) {
				// 不是第一个interceptor，结束上一个interceptor的性能监测
				Profiler.finish();
			}

			interceptor = interceptors.get(step++);

			Profiler.start("Interceptor " + interceptor.getClass().getSimpleName());

			view = interceptor.intercept(this);
		} else if (!actionExecuted) {
			// 结束最后一个interceptor的性能监测
			if (interceptors.size() > 0) {
				Profiler.finish();
			}

			actionExecuted = true;

			Method method = FrameworkContext.getCurrentContext().getMethod();
			Object action = FrameworkContext.getCurrentContext().getAction();

			Profiler.start("Action " + action.getClass().getSimpleName() + "." + method.getName() + "()");

			try {
				view = (View) method.invoke(action, new Object[] {});
			} catch (Exception e) {
				throw e.getCause();
			} finally {
				Profiler.finish();
			}
		}

		// 得到结果View，执行
		if (!viewExecuted && view != null) {
			Profiler.start("View " + view);

			viewExecuted = true;

			try {
				view.execute();
			} finally {
				Profiler.finish();
			}
		}

		return view;
	}
}
