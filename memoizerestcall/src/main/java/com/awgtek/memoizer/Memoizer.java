package com.awgtek.memoizer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value="request", proxyMode =ScopedProxyMode.TARGET_CLASS)
public class Memoizer<T, U> {

	private final Map<T, U> cache = new ConcurrentHashMap<>();

	public Memoizer() {
	}

	public Function<T, U> memoize(final Function<T, U> function) {
		return input -> cache.computeIfAbsent(input, function::apply);
	}

//	public static <T, U> Function<T, U> memoize(final Function<T, U> function) {
//		return new Memoizer<T, U>().doMemoize(function);
//	}
}