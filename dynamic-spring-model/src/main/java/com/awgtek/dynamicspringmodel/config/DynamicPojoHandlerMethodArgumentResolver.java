package com.awgtek.dynamicspringmodel.config;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletRequest;

import org.springframework.core.Conventions;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.awgtek.dynamicspringmodel.annotations.DynamicModelAttribute;
import com.awgtek.dynamicspringmodel.entities.SomeType;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;

public class DynamicPojoHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return (parameter.hasParameterAnnotation(DynamicModelAttribute.class));
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		String name = getNameForParameter(parameter);
		// Subscription subscription = new Subscription();
		Object subscription = createDynamicObjectViaJavassist();
		//Object subscription = createDynamicObjectViaASM();
		WebDataBinder binder = binderFactory.createBinder(webRequest, subscription, name);
		if (binder.getTarget() != null) {
			if (!mavContainer.isBindingDisabled(name)) {
				bindRequestParameters(binder, webRequest);
			}
			validateIfApplicable(binder, parameter);
			if (binder.getBindingResult().hasErrors() && isBindExceptionRequired(binder, parameter)) {
				throw new BindException(binder.getBindingResult());
			}
		}

		// Add resolved attribute and BindingResult at the end of the model
		Map<String, Object> bindingResultModel = binder.getBindingResult().getModel();
		mavContainer.removeAttributes(bindingResultModel);
		mavContainer.addAllAttributes(bindingResultModel);
		return subscription;
	}

	protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest request) {
		// ((WebRequestDataBinder) binder).bind(request);
		ServletRequest servletRequest = request.getNativeRequest(ServletRequest.class);
		ServletRequestDataBinder servletBinder = (ServletRequestDataBinder) binder;
		servletBinder.bind(servletRequest);
	}

	protected void validateIfApplicable(WebDataBinder binder, MethodParameter methodParam) {
		Annotation[] annotations = methodParam.getParameterAnnotations();
		for (Annotation ann : annotations) {
			Validated validatedAnn = AnnotationUtils.getAnnotation(ann, Validated.class);
			if (validatedAnn != null || ann.annotationType().getSimpleName().startsWith("Valid")) {
				Object hints = (validatedAnn != null ? validatedAnn.value() : AnnotationUtils.getValue(ann));
				Object[] validationHints = (hints instanceof Object[] ? (Object[]) hints : new Object[] { hints });
				binder.validate(validationHints);
				break;
			}
		}
	}

	protected boolean isBindExceptionRequired(WebDataBinder binder, MethodParameter methodParam) {
		int i = methodParam.getParameterIndex();
		Class<?>[] paramTypes = methodParam.getMethod().getParameterTypes();
		boolean hasBindingResult = (paramTypes.length > (i + 1) && Errors.class.isAssignableFrom(paramTypes[i + 1]));
		return !hasBindingResult;
	}

	private String getNameForParameter(MethodParameter parameter) {
		DynamicModelAttribute ann = parameter.getParameterAnnotation(DynamicModelAttribute.class);
		String name = (ann != null ? ann.value() : null);
		return StringUtils.hasText(name) ? name : Conventions.getVariableNameForParameter(parameter);
	}
	

	public Object createDynamicObjectViaJavassist() throws Exception {
		Map<String, Class<?>> props = new HashMap<String, Class<?>>();
		props.put("someType", SomeType.class);
		props.put("email", String.class);

		Class<?> clazz = generate(
		        "some.SubscriptionGenerated", props);

		Object obj = clazz.newInstance();
		return obj;
	}
	
	public static Class generate(String className, Map<String, Class<?>> properties)
			throws NotFoundException, CannotCompileException, ClassNotFoundException {

		ClassPool pool = ClassPool.getDefault();
		pool.insertClassPath(new ClassClassPath(SomeType.class));
		CtClass cc = pool.getOrNull(className);
		if (cc!= null) {
			return Class.forName(className);
		} 
		//else {
			 cc = pool.makeClass(className);
	
			// add this to define a super class to extend
			// cc.setSuperclass(resolveCtClass(MySuperClass.class));
	
			// add this to define an interface to implement
			cc.addInterface(resolveCtClass(Serializable.class));
	
			for (Entry<String, Class<?>> entry : properties.entrySet()) {
	
				cc.addField(new CtField(resolveCtClass(entry.getValue()), entry.getKey(), cc));
				if (entry.getKey().equals("email")) {
					
					// add getter
					cc.addMethod(generateGetter(cc, entry.getKey(), entry.getValue(), "org.hibernate.validator.constraints.Email"));
				} else {
					cc.addMethod(generateGetter(cc, entry.getKey(), entry.getValue(), ""));
					
				}
	
				// add setter
				cc.addMethod(generateSetter(cc, entry.getKey(), entry.getValue()));
			}
			return cc.toClass();

		//}
	}

	private static CtMethod generateGetter(CtClass declaringClass, String fieldName, Class fieldClass, 
			String validationAnnotationClass )
			throws CannotCompileException {

		String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

		StringBuffer sb = new StringBuffer();
		sb.append("public ").append(fieldClass.getName()).append(" ").append(getterName).append("(){")
				.append("return this.").append(fieldName).append(";").append("}");
		CtMethod ctMethod = CtMethod.make(sb.toString(), declaringClass);

		if (!StringUtils.isEmpty(validationAnnotationClass)) {
			// create the annotation
			ClassFile ccFile = declaringClass.getClassFile();
			ConstPool constpool = ccFile.getConstPool();
			AnnotationsAttribute attr = new AnnotationsAttribute(constpool, AnnotationsAttribute.visibleTag);
			// Annotation annot = AnnotationUtils.synthesizeAnnotation(
			// org.hibernate.validator.constraints.Email.class);
			javassist.bytecode.annotation.Annotation annot = new javassist.bytecode.annotation.Annotation(
					validationAnnotationClass, constpool);
			// annot.addMemberValue("name", new StringMemberValue("World!! (dynamic
			// annotation)",ccFile.getConstPool()));
			attr.addAnnotation(annot);
			// add the annotation to the method descriptor
			ctMethod.getMethodInfo().addAttribute(attr);
	
		}
		
		return ctMethod;
	}

	private static CtMethod generateSetter(CtClass declaringClass, String fieldName, Class fieldClass)
			throws CannotCompileException {

		String setterName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

		StringBuffer sb = new StringBuffer();
		sb.append("public void ").append(setterName).append("(").append(fieldClass.getName()).append(" ")
				.append(fieldName).append(")").append("{").append("this.").append(fieldName).append("=")
				.append(fieldName).append(";").append("}");
		return CtMethod.make(sb.toString(), declaringClass);
	}

	private static CtClass resolveCtClass(Class clazz) throws NotFoundException {
		ClassPool pool = ClassPool.getDefault();
		return pool.get(clazz.getName());
	}
}
