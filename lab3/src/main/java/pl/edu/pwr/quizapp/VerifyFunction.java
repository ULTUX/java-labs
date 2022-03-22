package pl.edu.pwr.quizapp;


@FunctionalInterface
public interface VerifyFunction<T, R> {
    R apply(T t) throws Exception;
}
