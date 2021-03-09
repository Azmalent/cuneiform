package azmalent.cuneiform.lib.config.options.lazy;

@SuppressWarnings("unused")
public interface ILazyOption {
    void initValue();
    void invalidate();
}
