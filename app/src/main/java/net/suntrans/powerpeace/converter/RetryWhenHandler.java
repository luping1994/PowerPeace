package net.suntrans.powerpeace.converter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by Looney on 2018/10/15.
 * Des:
 */
final class RetryWhenHandler implements Func1<Observable<? extends Throwable>, Observable<Long>> {

    private static final int INITIAL = 1;
    private int maxConnectCount = 1;

    RetryWhenHandler(int retryCount) {
        this.maxConnectCount += retryCount;
    }

    @Override public Observable<Long> call(Observable<? extends Throwable> errorObservable) {
        return errorObservable.zipWith(Observable.range(INITIAL, maxConnectCount),
                new Func2<Throwable, Integer, ThrowableWrapper>() {
                    @Override public ThrowableWrapper call(Throwable throwable, Integer i) {

                        //①
                        if (throwable instanceof IOException) return new ThrowableWrapper(throwable, i);

                        return new ThrowableWrapper(throwable, maxConnectCount);
                    }
                }).concatMap(new Func1<ThrowableWrapper, Observable<Long>>() {
            @Override public Observable<Long> call(ThrowableWrapper throwableWrapper) {

                final int retryCount = throwableWrapper.getRetryCount();

                //②
                if (maxConnectCount == retryCount) {
                    return Observable.error(throwableWrapper.getSourceThrowable());
                }

                //③
                return Observable.timer((long) Math.pow(2, retryCount), TimeUnit.SECONDS,
                        Schedulers.immediate());
            }
        });
    }

    private static final class ThrowableWrapper {

        private Throwable sourceThrowable;
        private Integer retryCount;

        ThrowableWrapper(Throwable sourceThrowable, Integer retryCount) {
            this.sourceThrowable = sourceThrowable;
            this.retryCount = retryCount;
        }

        Throwable getSourceThrowable() {
            return sourceThrowable;
        }

        Integer getRetryCount() {
            return retryCount;
        }
    }
}

