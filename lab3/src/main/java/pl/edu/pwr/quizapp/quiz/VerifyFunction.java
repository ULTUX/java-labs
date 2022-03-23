package pl.edu.pwr.quizapp.quiz;


@FunctionalInterface
public interface VerifyFunction<T, R> {
    R apply(T t) throws Exception;
}
