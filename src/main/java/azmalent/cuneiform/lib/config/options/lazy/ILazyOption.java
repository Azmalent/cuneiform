package azmalent.cuneiform.lib.config.options.lazy;

public interface ILazyOption {
    void initValue();
    void invalidate();
}
