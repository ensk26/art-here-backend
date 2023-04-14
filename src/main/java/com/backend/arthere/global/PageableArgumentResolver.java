package com.backend.arthere.global;

import com.backend.arthere.details.exception.InvalidFormatException;
import com.backend.arthere.details.exception.InvalidSizeException;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PageableArgumentResolver extends PageableHandlerMethodArgumentResolver {

    private static int MAX_SIZE = 10;
    private static int MIN_SIZE = 1;
    private static final Pattern NUMBER_PATTERN = Pattern.compile("^[0-9]*$");

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return super.supportsParameter(parameter);
    }

    @Override
    public Pageable resolveArgument(MethodParameter methodParameter, ModelAndViewContainer mavContainer,
                                    NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        final String page = webRequest.getParameter("page");
        validPage(page);
        final String size = webRequest.getParameter("size");
        final String validSize = validSize(size);

        final Pageable pageable = super.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);
        Sort sort = addSortById(pageable.getSort());

        return PageRequest.of(pageable.getPageNumber(), Integer.parseInt(validSize), sort);
    }

    private Sort addSortById(final Sort sort) {
        if(sort.isEmpty()) {
            return sort.and(Sort.by("id").descending());
        }
        return sort;
    }

    private void validPage(final String page) {
        if(page == null) {
            return;
        }

        Matcher matcher = NUMBER_PATTERN.matcher(page);
        if(!matcher.matches()) {
            throw new InvalidFormatException();
        }
    }

    private String validSize(final String size) {
        if(size == null) {
            return String.valueOf(MAX_SIZE);
        }

        Matcher matcher = NUMBER_PATTERN.matcher(size);
        if(!matcher.matches()) {
            throw new InvalidFormatException();
        }

        int sizeNumber = Integer.parseInt(size);
        if(sizeNumber > MAX_SIZE || sizeNumber < MIN_SIZE) {
            throw new InvalidSizeException();
        }
        return size;
    }
}
