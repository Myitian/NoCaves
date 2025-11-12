package net.myitian.no_caves;

import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.myitian.no_caves.config.Config;
import org.apache.commons.lang3.tuple.Pair;
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
        }
        if (original instanceof DensityFunctions.HolderHolder holder) {
            return transformRegistryEntryHolder(holder);
        } else if (original instanceof DensityFunctions.Noise noise) {
            return transformNoise(noise);
        } else if (original instanceof DensityFunctions.RangeChoice rangeChoice) {
            return transformRangeChoice(rangeChoice);
        } else if (original instanceof DensityFunctions.MulOrAdd linear) {
            return transformLinear(linear);
        } else if (original instanceof DensityFunctions.TwoArgumentSimpleFunction binary) {
            return transformBinary(binary);
        } else if (original instanceof DensityFunctions.PureTransformer unary) {
            return transformUnary(unary);
        } else if (original instanceof DensityFunctions.MarkerOrMarked wrapper) {
            return transformWrapper(wrapper);
        } else if (original instanceof DensityFunctions.TransformerWithContext positional) {
            return transformPositional(positional);
        }
        return original;
    }

    public static boolean isCaveDensityFunction(DensityFunction densityFunction) {
        return densityFunction instanceof DensityFunctions.HolderHolder holder
                && isCaveDensityFunction(holder.function());
    }

    public static boolean isCaveDensityFunction(Holder<DensityFunction> densityFunction) {
        return densityFunction instanceof Holder.Reference<DensityFunction> reference
                && Config.getDensityFunctionCavePatterns().matches(reference.key().location().toString());
    }

    public static boolean isCaveNoise(DensityFunction.NoiseHolder noise) {
        return isCaveNoise(noise.noiseData());
    }

    public static boolean isCaveNoise(Holder<NormalNoise.NoiseParameters> noise) {
        return noise instanceof Holder.Reference<NormalNoise.NoiseParameters> reference
                && Config.getNoiseCavePatterns().matches(reference.key().location().toString());
    }

    @Nullable
    public static DensityFunction transformRegistryEntryHolder(DensityFunctions.HolderHolder holder) {
        Holder<DensityFunction> function = holder.function();
        if (Config.isEnableDensityFunctionCaveFilter() && isCaveDensityFunction(function)) {
            return null;
        } else if (function instanceof Holder.Direct<DensityFunction>) {
            return transform(function.value());
        }
        return holder;
    }

    @Nullable
    public static DensityFunction transformNoise(DensityFunctions.Noise noise) {
        return Config.isEnableNoiseCaveFilter() && isCaveNoise(noise.noise()) ? null : noise;
    }

    @Nullable
    private static DensityFunction transformRangeChoice(DensityFunctions.RangeChoice rangeChoice) {
        DensityFunction input = rangeChoice.input();
        if (isCaveDensityFunction(input)) {
            // Might not cover all cases, but good enough for vanilla worldgen json.
            return transform(rangeChoice.whenOutOfRange());
        } else if (input instanceof DensityFunctions.Constant constant) {
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

    private static DensityFunction transformLinear(DensityFunctions.MulOrAdd linear) {
        DensityFunction originalChild = linear.input();
        DensityFunction transformedChild = transform(originalChild);
        if (transformedChild == null) {
            return DensityFunctions.constant(linear.argument());
        } else if (transformedChild instanceof DensityFunctions.Constant constant) {
            return DensityFunctions.constant(linear.transform(constant.value()));
        } else {
            linear.input = transformedChild;
            return linear;
        }
    }

    @Nullable
    private static DensityFunction transformBinary(DensityFunctions.TwoArgumentSimpleFunction binary) {
        DensityFunction originalChild1 = binary.argument1();
        DensityFunction transformedChild1 = transform(originalChild1);
        DensityFunction originalChild2 = binary.argument2();
        DensityFunction transformedChild2 = transform(originalChild2);
        if (transformedChild1 == null) {
            return transformedChild2;
        } else if (transformedChild2 == null) {
            return transformedChild1;
        } else if (transformedChild1 instanceof DensityFunctions.Constant constant1
                && transformedChild2 instanceof DensityFunctions.Constant constant2) {
            double v1 = constant1.value();
            switch (binary.type()) {
                case ADD -> {
                    return DensityFunctions.constant(v1 + constant2.value());
                }
                case MUL -> {
                    return DensityFunctions.constant(v1 == 0.0 ? 0.0 : v1 * constant2.value());
                }
                case MIN -> {
                    return DensityFunctions.constant(Math.min(v1, constant2.value()));
                }
                case MAX -> {
                    return DensityFunctions.constant(Math.max(v1, constant2.value()));
                }
                default -> {
                }
            }
        }
        if (originalChild1 != transformedChild1 || originalChild2 != transformedChild2) {
            return DensityFunctions.TwoArgumentSimpleFunction.create(binary.type(), transformedChild1, transformedChild2);
        }
        return binary;
    }

    @Nullable
    private static DensityFunction transformUnary(DensityFunctions.PureTransformer unary) {
        DensityFunction originalChild = unary.input();
        DensityFunction transformedChild = transform(originalChild);
        if (transformedChild == null) {
            return null;
        } else if (transformedChild instanceof DensityFunctions.Constant constant) {
            return DensityFunctions.constant(unary.transform(constant.value()));
        } else if (transformedChild != originalChild) {
            if (unary instanceof DensityFunctions.Clamp typedUnary) {
                typedUnary.input = transformedChild;
            } else if (unary instanceof DensityFunctions.Mapped typedUnary) {
                return DensityFunctions.Mapped.create(typedUnary.type(), transformedChild);
            }
            // Unknown types will be left as is
        }
        return unary;
    }

    @Nullable
    private static DensityFunction transformWrapper(DensityFunctions.MarkerOrMarked wrapper) {
        DensityFunction originalChild = wrapper.wrapped();
        DensityFunction transformedChild = transform(originalChild);
        if (transformedChild == null) {
            return null;
        } else if (transformedChild != originalChild) {
            if (wrapper instanceof NoiseChunk.Cache2D typedWrapper) {
                typedWrapper.function = transformedChild;
            } else if (wrapper instanceof NoiseChunk.Cache2D typedWrapper) {
                typedWrapper.function = transformedChild;
            } else if (wrapper instanceof NoiseChunk.CacheOnce typedWrapper) {
                typedWrapper.function = transformedChild;
            } else if (wrapper instanceof NoiseChunk.CacheAllInCell typedWrapper) {
                typedWrapper.noiseFiller = transformedChild;
            } else if (wrapper instanceof NoiseChunk.NoiseInterpolator typedWrapper) {
                typedWrapper.noiseFiller = transformedChild;
            } else if (wrapper instanceof NoiseChunk.FlatCache typedWrapper) {
                typedWrapper.noiseFiller = transformedChild;
            } else if (wrapper instanceof DensityFunctions.Marker typedWrapper) {
                typedWrapper.wrapped = transformedChild;
            }
            // Unknown types will be left as is
        }
        return wrapper;
    }

    @Nullable
    private static DensityFunction transformPositional(DensityFunctions.TransformerWithContext positional) {
        DensityFunction originalChild = positional.input();
        DensityFunction transformedChild = transform(originalChild);
        if (transformedChild == null) {
            return null;
        } else if (transformedChild != originalChild) {
            if (positional instanceof DensityFunctions.BlendDensity typedPositional) {
                typedPositional.input = transformedChild;
            } else if (positional instanceof DensityFunctions.WeirdScaledSampler typedPositional) {
                typedPositional.input = transformedChild;
            }
            // Unknown types will be left as is
        }
        return positional;
    }
}
