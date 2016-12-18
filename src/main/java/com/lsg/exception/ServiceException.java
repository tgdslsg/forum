package com.lsg.exception;

/**
 * Created by tgdsl on 2016/12/17.
 */
public class ServiceException extends RuntimeException {
    private static final long serialVersionID=1L;
    public ServiceException(){};
    public ServiceException(String message){super(message);};
    public ServiceException(Throwable th){super(th);};
    public ServiceException(String message,Throwable th){super(message,th);};
}
