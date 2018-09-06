//package vn.com.omart.sharedkernel.application;
//
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.slf4j.Logger;
//import org.springframework.amqp.core.AmqpTemplate;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.core.MessageDeliveryMode;
//import org.springframework.amqp.core.MessageProperties;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.AnnotatedElementUtils;
//import org.springframework.core.annotation.Order;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import java.lang.reflect.Method;
//import java.net.InetAddress;
//import java.net.UnknownHostException;
//import java.util.concurrent.ExecutorService;
//
//import static org.slf4j.LoggerFactory.getLogger;
//
///**
// * Rest service logging aspect
// */
//@SuppressWarnings("Duplicates")
//@Aspect
//@Order(Ordered.HIGHEST_PRECEDENCE + 100)
//public class SpringControllerMonitor {
//
//    private static final Logger LOG = getLogger("service_monitor");
//    private static final ThreadLocal<ServiceInfo> requestArguments = new ThreadLocal<>();
//    private static String hostname = "local";
//    private final MessageProperties messageProperties;
//    private final AmqpTemplate amqpTemplate;
//    private final ExecutorService executorService;
//    private String serviceName;
//    private final Rabback rabback;
//
//    static {
//        try {
//            InetAddress addr = InetAddress.getLocalHost();
//            hostname = addr.getCanonicalHostName();
//        } catch (UnknownHostException ignored) {
//        }
//
//        hostname = hostname.replaceAll("\\.", "-");
//    }
//
//    public SpringControllerMonitor(
//        String serviceName,
//        AmqpTemplate amqpTemplate,
//        ExecutorService executorService,
//        Rabback rabback) {
//        this.serviceName = serviceName;
//        this.amqpTemplate = amqpTemplate;
//        this.executorService = executorService;
//        this.rabback = rabback;
//        this.messageProperties = new MessageProperties();
//        messageProperties.setDeliveryMode(MessageDeliveryMode.NON_PERSISTENT);
//        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
//    }
//
//    @Before("(@annotation(org.springframework.web.bind.annotation.RequestMapping) " +
//        "        || @annotation(org.springframework.web.bind.annotation.GetMapping) \n" +
//        "        || @annotation(org.springframework.web.bind.annotation.PutMapping) \n" +
//        "        || @annotation(org.springframework.web.bind.annotation.PostMapping) \n" +
//        "        || @annotation(org.springframework.web.bind.annotation.PatchMapping) \n" +
//        "        || @annotation(org.springframework.web.bind.annotation.DeleteMapping)) \n" +
//        " && (within(vn.com.omart..*))\n" +
//        " && !within(vn.com.omart.sharedkernel..*)")
//    public void before(JoinPoint jp) {
//        // Init service info using current method call info
//        ServiceInfo info = new ServiceInfo();
//        info.setHostname(SpringControllerMonitor.hostname);
//        info.setBeginTime(System.nanoTime());
//        info.setParams(jp.getArgs());
//        info.setCorrelationId(CorrelationId.value());
//        info.setServletRequest(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
//
//        String path = "";
//        String method = "GET";
//        RequestMapping controllerRequestMapping = AnnotatedElementUtils.findMergedAnnotation(jp.getTarget().getClass(), RequestMapping.class);
//
//        if (controllerRequestMapping != null) {
//            RequestMethod[] controllerMappingMethods = controllerRequestMapping.method();
//            if (controllerMappingMethods.length > 0) {
//                method = controllerMappingMethods[0].name();
//            }
//
//            String[] controllerRequestMappingValues = controllerRequestMapping.value();
//            if (controllerRequestMappingValues.length > 0) {
//                path += controllerRequestMappingValues[0];
//            }
//        }
//
//        MethodSignature methodSignature = (MethodSignature) jp.getSignature();
//        RequestMapping methodRequestMapping = AnnotatedElementUtils.findMergedAnnotation(methodSignature.getMethod(), RequestMapping.class);
//        String[] methodRequestMappingValues = methodRequestMapping.value();
//        RequestMethod[] methodRequestMethods = methodRequestMapping.method();
//
//        if (methodRequestMappingValues.length > 0) {
//            path += methodRequestMappingValues[0];
//        }
//
//        if (methodRequestMethods.length > 0) {
//            method = methodRequestMethods[0].name();
//        }
//
////        info.setName(this.serviceName);
////        info.setProtocol("http");
////        info.setRemoteAddress("0.0.0.0");
////        info.setMethod(method);
////        info.setEndpoint(path);
//
//        // Save current service info to current thread for later access
//        requestArguments.set(info);
//    }
//
//    @AfterReturning(
//        value = "(@annotation(org.springframework.web.bind.annotation.RequestMapping) \n" +
//            "        || @annotation(org.springframework.web.bind.annotation.GetMapping) \n" +
//            "        || @annotation(org.springframework.web.bind.annotation.PutMapping) \n" +
//            "        || @annotation(org.springframework.web.bind.annotation.PostMapping) \n" +
//            "        || @annotation(org.springframework.web.bind.annotation.PatchMapping) \n" +
//            "        || @annotation(org.springframework.web.bind.annotation.DeleteMapping) \n" +
//            "        || @annotation(org.springframework.web.bind.annotation.ExceptionHandler)) \n" +
//            " && (within(vn.com.omart..*) || within(vn.com.omart..*))\n" +
//            " && (within(vn.com.omart.sharedkernel.*) || !within(vn.com.omart.sharedkernel..*))",
//        returning = "returnedVal")
//    public void afterReturning(JoinPoint jp, Object returnedVal) {
//
//        // Get service info saved before method invocation
//        ServiceInfo info = requestArguments.get();
//        if (info == null) {
//            return;
//        }
//
//        // Set end time to service info
//        info.setEndTime(System.nanoTime());
//
//        StringBuilder serializedArguments = new StringBuilder("");
//        Object[] currentArguments = info.getParams();
//
//        // Loop through params of invoked method to serialize them to string
//        int count = 0;
//        for (Object argument : currentArguments) {
//            if (argument instanceof BindingResult) {
//                count++;
//                continue;
//            }
//
//            if (++count > 1) {
//                serializedArguments.append("; ");
//            }
//
//            serializedArguments.append(argument == null ? "null" : argument.toString());
//        }
//
//        MethodSignature methodSignature = (MethodSignature) jp.getSignature();
//        Method method = methodSignature.getMethod();
//        // ExceptionHandler exceptionHandlerAnnotation = method.getAnnotation(ExceptionHandler.class);
//
//        final int finalResponseStatus = getStatusCode(method, returnedVal);
//        Object[] methodArgs = jp.getArgs();
//        Throwable exception = findException(methodArgs);
//
//        if (exception != null && finalResponseStatus == 500) {
//            // Log unknown error
//            // Log error message with exception root cause
//            LOG.error("From={}\tExecTime={}ms\tMethod={}\tRequest={}\tResponse={}\tRoot cause:{}",
//                info.getRemoteAddress(),
//                info.execMillis(),
//                info.getQualifiedMethodName(),
//                serializedArguments,
//                returnedVal,
//                getRoot(exception));
//
//            this.rabback.reportServiceError(info, exception);
//        } else {
//            // Log regular return && well-known exception
//            LOG.info("From={}\tExecTime={}ms\tMethod={}\tRequest={}\tResponse={}",
//                info.getRemoteAddress(),
//                info.execMillis(),
//                info.getQualifiedMethodName(),
//                serializedArguments,
//                returnedVal);
//        }
//
//        // Record response status
//        if (this.amqpTemplate != null && this.executorService != null) {
//            this.executorService.execute(() -> {
//                try {
//                    String countMessage = info.getName() +
//                        "." + info.getHostname() +
//                        "." + info.getProtocol() +
//                        ".status." + finalResponseStatus +
//                        "." + info.getMethod() +
//                        "." + info.getDirSafeEndpoint() + ".count" +
//                        " 1 " + (System.currentTimeMillis() / 1000);
//                    amqpTemplate.send(new Message(countMessage.getBytes(), this.messageProperties));
//
//                    // Record service response time
//                    String message = info.getName() +
//                        "." + info.getHostname() +
//                        "." + info.getProtocol() +
//                        ".status." + finalResponseStatus +
//                        "." + info.getMethod() +
//                        "." + info.getDirSafeEndpoint() + ".response_time" +
//                        " " + info.execMicroseconds()
//                        + " " + (System.currentTimeMillis() / 1000);
//                    amqpTemplate.send(new Message(message.getBytes(), this.messageProperties));
//                } catch (Exception e) {
//                    LOG.warn("Could not send metrics to rabbit: {}", e.getMessage());
//                }
//            });
//        }
//
//        // Clear service info from thread local (to prevent memory leak)
//        requestArguments.remove();
//    }
//
//    private Throwable getRoot(Throwable t) {
//        if (t == null) {
//            return null;
//        }
//
//        Throwable result = t;
//
//        while (result.getCause() != null) {
//            result = result.getCause();
//        }
//
//        return result;
//    }
//
//    /*
//    private boolean isExceptionHandler(ExceptionHandler exceptionHandler) {
//        return exceptionHandler != null;
//    }
//
//    private boolean isUnknownException(ExceptionHandler exceptionHandlerAnnotation) {
//        if (exceptionHandlerAnnotation == null) {
//            return false;
//        }
//        Class<? extends Throwable>[] caughtExceptionClasses = exceptionHandlerAnnotation.value();
//        for (Class<? extends Throwable> exceptionClass : caughtExceptionClasses) {
//            if (exceptionClass.equals(Exception.class)) {
//                return true;
//            }
//        }
//
//        return false;
//    }
//    */
//
//    private Throwable findException(Object[] args) {
//        for (Object arg : args) {
//            if (arg instanceof Throwable) {
//                return (Throwable) arg;
//            }
//        }
//
//        return null;
//    }
//
//    private int getStatusCode(Method method, Object object) {
//        // IMPORTANT check @ResponseStatus first because it will override status code in ResponseEntity
//        ResponseStatus methodResponseStatus = method.getAnnotation(ResponseStatus.class);
//        if (methodResponseStatus != null) {
//            HttpStatus methodResponseStatusValue = methodResponseStatus.value();
//            return methodResponseStatusValue.value();
//        }
//
//        if (object instanceof RestError) {
//            return ((RestError) object).getCode();
//        }
//
//        if (object instanceof ResponseEntity) {
//            return ((ResponseEntity) object).getStatusCodeValue();
//        }
//
//        // Cannot find any information relate to response status, use 200 as default
//        return 200;
//    }
//
//}
