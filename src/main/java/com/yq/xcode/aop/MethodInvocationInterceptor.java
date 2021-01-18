package com.yq.xcode.aop;

import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.core.Ordered;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.yq.xcode.aop.event.SyncEventPublisher;

import lombok.extern.slf4j.Slf4j;

@Component
@Aspect
@Slf4j
public class MethodInvocationInterceptor implements MethodInterceptor, ApplicationEventPublisherAware, Ordered, InitializingBean {

	private static Log LOG = LogFactory.getLog(MethodInvocationInterceptor.class);
	private ApplicationEventPublisher eventPublisher;
	private TaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();

	public MethodInvocationInterceptor() {
	}

	@Override
	public Object invoke(MethodInvocation mi) throws Throwable {
		log.info("MethodInvocationInterceptor ==> invoke method: process method name is {}", mi.getMethod().getName());
		boolean isNewContext = MethodInvocationContextHolder.pushContext();
		if (isNewContext) {
			MethodInvocationContextHolder.getContext().setImmediateEventPublisher(eventPublisher);
		}
		try {
			Object result = mi.proceed();
			if (isNewContext) {
				success(MethodInvocationContextHolder.getContext(), result);
			}
			return result;
		} catch (Throwable t) {
			if (isNewContext) {
				failed(MethodInvocationContextHolder.getContext(), t);
			}
			throw t;
		} finally {
			if (isNewContext) {
				MethodInvocationContextHolder.popContext();
			}
		}
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.eventPublisher = applicationEventPublisher;
	}

	@Override
	public int getOrder() {
		return 0;
	}

	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	protected void publishEvent(SyncEventPublisher syncEventPublisher) {
		SecurityContext sc = SecurityContextHolder.getContext();
		Authentication auth0 = null;
		if (sc != null) {
			auth0 = sc.getAuthentication();
		}
		final Authentication auth = auth0;
		final List<ApplicationEvent> events = syncEventPublisher.getEventQueue();
		if (!events.isEmpty()) {
			LOG.debug("Start to dispatch events in another thread.");
			taskExecutor.execute(new Runnable() {
				@Override
				public void run() {
					if (auth != null) {
						SecurityContextHolder.getContext().setAuthentication(auth);
					}
					try {
						for (ApplicationEvent evt : events) {
							eventPublisher.publishEvent(evt);
						}
					} finally {
						if (auth != null) {
							SecurityContextHolder.clearContext();
						}
					}
				}
			});
		}

	}

	protected void success(final MethodInvocationContext context, final Object result) {
		final List<SuccessCallback> callbacks = context.getSuccessCallbacks();
		if (!callbacks.isEmpty()) {
			LOG.debug("Start to invoke success callbacks.");
			final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			taskExecutor.execute(new Runnable() {
				@Override
				public void run() {
					if (auth != null) {
						SecurityContextHolder.getContext().setAuthentication(auth);
					}
					try {
						for (SuccessCallback cb : callbacks) {
							try {
								cb.success(result);
							} catch (Exception e) {
								LOG.error("", e);
							}
						}
					} finally {
						if (auth != null) {
							SecurityContextHolder.clearContext();
						}
					}

				}

			});
		}
		publishEvent((SyncEventPublisher) context.getAfterTransactionEventPublisher());
	}

	protected void failed(final MethodInvocationContext context, final Throwable t) {

		final List<FailedCallback> callbacks = context.getFailedCallbacks();
		if (!callbacks.isEmpty()) {
			LOG.debug("Start to invoke failed  callbacks.");
			final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			taskExecutor.execute(new Runnable() {
				@Override
				public void run() {
					if (auth != null) {
						SecurityContextHolder.getContext().setAuthentication(auth);
					}
					try {
						for (FailedCallback cb : callbacks) {
							try {
								cb.failed(t);
							} catch (Exception e) {
								LOG.error("", e);
							}
						}
					} finally {
						if (auth != null) {
							SecurityContextHolder.clearContext();
						}
					}
				}

			});
		}

	}

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("MethodInvocationInterceptor Created...");
		
	}

}
