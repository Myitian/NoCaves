# How It Works

[简体中文](HOW_IT_WORKS.zh-Hans.md) | **English**

---

This mod intercepts specific types during the JSON resource loading and registration phase and applies transformations:

### `worldgen/biome`

Filters specific carvers. It's the simplest transformation.

### `worldgen/noise_settings`

Transforms the final density in noise settings to filter out cave density function and noise.

### `worldgen/density_function`

Transforms density functions to filter out cave density function and noise.

### During the Transformation…

The `DensityFunctionCaveCleaner` class will recursively transform the density function. The following are some built-in transformers:

<table>
<thead>
<tr>
<th>Target Type</th>
<th>Principle</th>
</tr>
</thead>
<tbody>
<tr>
<td><code>DensityFunctions.HolderHolder</code></td>
<td>If the type is <code>Reference</code>, it checks whether the referenced density function is a cave density function. If so, it returns <code>null</code>; otherwise, it returns the object itself.<br>If the type is <code>Direct</code>, it transforms the stored density function and returns it.</td>
</tr>
<tr>
<td><code>DensityFunctions.Noise</code></td>
<td>If the noise parameter is identified as cave noise, it returns <code>null</code>; otherwise, it returns the object itself.</td>
</tr>
<tr>
<td><code>DensityFunctions.RangeChoice</code></td>
<td>If the input is identified as a cave density function, the <code>whenOutOfRange</code> branch is always selected and transformed and returned.<br>If the input is identified as a constant, optimization is performed, the corresponding branch is selected, transformed and returned.<br>In other cases, both branches will be transformed. If one branch is <code>null</code>, the other branch will be returned directly. If there is no change, the original object will be returned. Otherwise, a new object will be returned, in which the corresponding density function will be replaced with the transformed one.</td>
</tr>
<tr>
<td><code>DensityFunctions.MulOrAdd</code></td>
<td>The input is transformed.<br>If the result is <code>null</code>, the linear operation's argument is returned as a constant.<br>If the result is a constant, optimization is performed and the calculated constant is returned.<br>In other cases, if there is no change, the original object is returned; otherwise, a new object is returned, in which the corresponding density function is replaced with the transformed one.</td>
</tr>
<tr>
<td><code>DensityFunctions.Marker</code></td>
<td>The wrapped density function is transformed.<br>If the result is <code>null</code>, <code>null</code> is returned.<br>In other cases, if there is no change or the type is unknown, the original object is returned; otherwise, a new object is returned, in which the corresponding density function is replaced with the transformed one.</td>
</tr>
<tr>
<td><code>DensityFunctions.TwoArgumentSimpleFunction</code></td>
<td>Both arguments are converted.<br>If either result is <code>null</code>, the other is returned directly.<br>If both results are constants, optimization is performed and the calculated constant is returned.<br>In other cases, a new binary operation of the same type is created using both results and returned.</td>
</tr>
<tr>
<td><code>DensityFunctions.PureTransformer</code></td>
<td>The input is transformed.<br>If the result is <code>null</code>, <code>null</code> is returned.<br>If the result is a constant, optimization is performed and the calculated constant is returned.<br>In other cases, if the type is <code>Clamp</code>, the original object's fields are updated and the original object is returned; if the type is <code>UnaryOperation</code>, a new unary operation of the same type is created using the transformation result and returned; otherwise, the original object is returned (unknown types cannot be handled).</td>
</tr>
<tr>
<td><code>DensityFunctions.TransformerWithContext</code></td>
<td>The input is transformed.<br>If the result is <code>null</code>, <code>null</code> is returned.<br>In other cases, if there is no change or the type is unknown, the original object is returned; otherwise, a new object is returned, in which the corresponding density function is replaced with the transformed one.</td>
</tr>
</tbody>
</table>

Other mods can register their transformers into the CustomTransformerRegistry to perform custom transformations. NoCaves will check each transformer in the order of the map and execute the corresponding transformation function if it matches the predicate. If no custom transformer matches, the built-in transformers will be executed. The following is an example code to register a custom transformer:

```java
Predicate<DensityFunction> pred = df -> {
    return df instanceof YuorCustomDensityFunction;
};
Function<DensityFunction, DensityFunction> func = df -> {
    // Your custom logic here
    return df;
};
var reg = DensityFunctionCaveCleaner.getCustomTransformerRegistry();
reg.put("your_id", Pair.of(pred, func));
```
