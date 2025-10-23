package net.myitian.no_caves;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Pair;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.world.gen.chunk.ChunkNoiseSampler;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;
import net.myitian.no_caves.config.Config;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public final class DensityFunctionCaveCleaner {
    private static final Map<String, Pair<Predicate<DensityFunction>, Function<DensityFunction, DensityFunction>>> customTransformerRegistry = new LinkedHashMap<>();

    /**
     * @return a mutable map that preserves insertion order
     */
    public static Map<String, Pair<Predicate<DensityFunction>, Function<DensityFunction, @Nullable DensityFunction>>> getCustomTransformerRegistry() {
        return customTransformerRegistry;
    }

    /**
     * @return null if the original DensityFunction is a cave DensityFunction,
     * otherwise the original DensityFunction will be transformed and returned.
     * @apiNote Some in-place transformations will be done, so the original DensityFunction may change!
     */
    @Nullable
    public static DensityFunction transform(DensityFunction original) {
        if (!customTransformerRegistry.isEmpty()) {
            for (var entry : customTransformerRegistry.entrySet()) {
                String id = entry.getKey();
                try {
                    var pair = entry.getValue();
                    if (pair != null
                            && pair.getLeft() != null
                            && pair.getRight() != null
                            && pair.getLeft().test(original)) {
                        return pair.getRight().apply(original);
                    }
                } catch (Exception e) {
                    throw new RuntimeException("An unhandled exception occurred in transformer " + id, e);
                }
            }
        } else if (original instanceof DensityFunctionTypes.RegistryEntryHolder holder) {
            return transformRegistryEntryHolder(holder);
        } else if (original instanceof DensityFunctionTypes.Noise noise) {
            return transformNoise(noise);
        } else if (original instanceof DensityFunctionTypes.RangeChoice rangeChoice) {
            return transformRangeChoice(rangeChoice);
        } else if (original instanceof DensityFunctionTypes.LinearOperation linear) {
            return transformLinear(linear);
        } else if (original instanceof DensityFunctionTypes.BinaryOperationLike binary) {
            return transformBinary(binary);
        } else if (original instanceof DensityFunctionTypes.Unary unary) {
            return transformUnary(unary);
        } else if (original instanceof DensityFunctionTypes.Wrapper wrapper) {
            return transformWrapper(wrapper);
        } else if (original instanceof DensityFunctionTypes.Positional positional) {
            return transformPositional(positional);
        }
        return original;
    }

    public static boolean isCaveDensityFunction(DensityFunction densityFunction) {
        return densityFunction instanceof DensityFunctionTypes.RegistryEntryHolder holder
                && isCaveDensityFunction(holder.function());
    }

    public static boolean isCaveDensityFunction(RegistryEntry<DensityFunction> densityFunction) {
        return densityFunction instanceof RegistryEntry.Reference<DensityFunction> reference
                && Config.getDensityFunctionCavePatterns().matches(reference.registryKey().getValue().toString());
    }

    public static boolean isCaveNoise(DensityFunction.Noise noise) {
        return isCaveNoise(noise.noiseData());
    }

    public static boolean isCaveNoise(RegistryEntry<DoublePerlinNoiseSampler.NoiseParameters> noise) {
        return noise instanceof RegistryEntry.Reference<DoublePerlinNoiseSampler.NoiseParameters> reference
                && Config.getNoiseCavePatterns().matches(reference.registryKey().getValue().toString());
    }

    @Nullable
    public static DensityFunction transformRegistryEntryHolder(DensityFunctionTypes.RegistryEntryHolder holder) {
        RegistryEntry<DensityFunction> function = holder.function();
        if (Config.isEnableDensityFunctionCaveFilter() && isCaveDensityFunction(function)) {
            return null;
        } else if (function instanceof RegistryEntry.Direct<DensityFunction>) {
            return transform(function.value());
        }
        return holder;
    }

    @Nullable
    public static DensityFunction transformNoise(DensityFunctionTypes.Noise noise) {
        return Config.isEnableNoiseCaveFilter() && isCaveNoise(noise.noise()) ? null : noise;
    }

    @Nullable
    private static DensityFunction transformRangeChoice(DensityFunctionTypes.RangeChoice rangeChoice) {
        DensityFunction input = rangeChoice.input();
        if (isCaveDensityFunction(input)) {
            // Might not cover all cases, but good enough for vanilla worldgen json.
            return transform(rangeChoice.whenOutOfRange());
        } else if (input instanceof DensityFunctionTypes.Constant constant) {
            double v = constant.value();
            if (v >= rangeChoice.minInclusive() && v < rangeChoice.maxExclusive()) {
                return transform(rangeChoice.whenInRange());
            } else {
                return transform(rangeChoice.whenOutOfRange());
            }
        } else {
            DensityFunction originalChild1 = rangeChoice.whenInRange();
            DensityFunction transformedChild1 = transform(originalChild1);
            DensityFunction originalChild2 = rangeChoice.whenOutOfRange();
            DensityFunction transformedChild2 = transform(originalChild2);
            if (transformedChild1 == null) {
                return transformedChild2;
            } else if (transformedChild2 == null) {
                return transformedChild1;
            } else {
                rangeChoice.whenInRange = transformedChild1;
                rangeChoice.whenOutOfRange = transformedChild2;
                return rangeChoice;
            }
        }
    }

    private static DensityFunction transformLinear(DensityFunctionTypes.LinearOperation linear) {
        DensityFunction originalChild = linear.input();
        DensityFunction transformedChild = transform(originalChild);
        if (transformedChild == null) {
            return DensityFunctionTypes.constant(linear.argument());
        } else if (transformedChild instanceof DensityFunctionTypes.Constant constant) {
            return DensityFunctionTypes.constant(linear.apply(constant.value()));
        } else {
            linear.input = transformedChild;
            return linear;
        }
    }

    @Nullable
    private static DensityFunction transformBinary(DensityFunctionTypes.BinaryOperationLike binary) {
        DensityFunction originalChild1 = binary.argument1();
        DensityFunction transformedChild1 = transform(originalChild1);
        DensityFunction originalChild2 = binary.argument2();
        DensityFunction transformedChild2 = transform(originalChild2);
        if (transformedChild1 == null) {
            return transformedChild2;
        } else if (transformedChild2 == null) {
            return transformedChild1;
        } else if (transformedChild1 instanceof DensityFunctionTypes.Constant constant1
                && transformedChild2 instanceof DensityFunctionTypes.Constant constant2) {
            double v1 = constant1.value();
            switch (binary.type()) {
                case ADD -> {
                    return DensityFunctionTypes.constant(v1 + constant2.value());
                }
                case MUL -> {
                    return DensityFunctionTypes.constant(v1 == 0.0 ? 0.0 : v1 * constant2.value());
                }
                case MIN -> {
                    return DensityFunctionTypes.constant(Math.min(v1, constant2.value()));
                }
                case MAX -> {
                    return DensityFunctionTypes.constant(Math.max(v1, constant2.value()));
                }
                default -> {
                }
            }
        }
        if (originalChild1 != transformedChild1 || originalChild2 != transformedChild2) {
            return DensityFunctionTypes.BinaryOperationLike.create(binary.type(), transformedChild1, transformedChild2);
        }
        return binary;
    }

    @Nullable
    private static DensityFunction transformUnary(DensityFunctionTypes.Unary unary) {
        DensityFunction originalChild = unary.input();
        DensityFunction transformedChild = transform(originalChild);
        if (transformedChild == null) {
            return null;
        } else if (transformedChild instanceof DensityFunctionTypes.Constant constant) {
            return DensityFunctionTypes.constant(unary.apply(constant.value()));
        } else if (transformedChild != originalChild) {
            if (unary instanceof DensityFunctionTypes.Clamp typedUnary) {
                typedUnary.input = transformedChild;
            } else if (unary instanceof DensityFunctionTypes.UnaryOperation typedUnary) {
                return DensityFunctionTypes.UnaryOperation.create(typedUnary.type(), transformedChild);
            }
            // Unknown types will be left as is
        }
        return unary;
    }

    @Nullable
    private static DensityFunction transformWrapper(DensityFunctionTypes.Wrapper wrapper) {
        DensityFunction originalChild = wrapper.wrapped();
        DensityFunction transformedChild = transform(originalChild);
        if (transformedChild == null) {
            return null;
        } else if (transformedChild != originalChild) {
            if (wrapper instanceof ChunkNoiseSampler.Cache2D typedWrapper) {
                typedWrapper.delegate = transformedChild;
            } else if (wrapper instanceof ChunkNoiseSampler.Cache2D typedWrapper) {
                typedWrapper.delegate = transformedChild;
            } else if (wrapper instanceof ChunkNoiseSampler.CacheOnce typedWrapper) {
                typedWrapper.delegate = transformedChild;
            } else if (wrapper instanceof ChunkNoiseSampler.CellCache typedWrapper) {
                typedWrapper.delegate = transformedChild;
            } else if (wrapper instanceof ChunkNoiseSampler.DensityInterpolator typedWrapper) {
                typedWrapper.delegate = transformedChild;
            } else if (wrapper instanceof ChunkNoiseSampler.FlatCache typedWrapper) {
                typedWrapper.delegate = transformedChild;
            } else if (wrapper instanceof DensityFunctionTypes.Wrapping typedWrapper) {
                typedWrapper.wrapped = transformedChild;
            }
            // Unknown types will be left as is
        }
        return wrapper;
    }

    @Nullable
    private static DensityFunction transformPositional(DensityFunctionTypes.Positional positional) {
        DensityFunction originalChild = positional.input();
        DensityFunction transformedChild = transform(originalChild);
        if (transformedChild == null) {
            return null;
        } else if (transformedChild != originalChild) {
            if (positional instanceof DensityFunctionTypes.BlendDensity typedPositional) {
                typedPositional.input = transformedChild;
            } else if (positional instanceof DensityFunctionTypes.WeirdScaledSampler typedPositional) {
                typedPositional.input = transformedChild;
            }
            // Unknown types will be left as is
        }
        return positional;
    }
}
