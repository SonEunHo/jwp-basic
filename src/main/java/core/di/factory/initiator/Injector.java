package core.di.factory.initiator;

public interface Injector {
    Object inject(Class<?> beanType);
    boolean canSupport(Class<?> beanType);
}
