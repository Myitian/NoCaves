# 工作原理

**简体中文** | [English](HOW_IT_WORKS.en.md)

---

此模块在 JSON 资源加载和注册阶段拦截特定类型并应用转换：

### `worldgen/biome`

过滤特定的雕刻器。这是最简单的转换。

### `worldgen/noise_settings`

转换在噪声设置中的最终密度，以滤除洞穴密度函数和噪声。

### `worldgen/density_function`

转换密度函数，以滤除洞穴密度函数和噪声。

### 在转换中……

`DensityFunctionCaveCleaner` 类将递归地转换密度函数。以下是一些内置的转换器：

<table>
<thead>
<tr>
<th>目标类型</th>
<th>原理</th>
</tr>
</thead>
<tbody>
<tr>
<td><code>DensityFunctions.HolderHolder</code></td>
<td>如果是<code>Reference</code>型，那么会判断引用对象被是否为洞穴密度函数，是则返回<code>null</code>，否则返回本身。<br>如果是<code>Direct</code>型，则会转换其存储的密度函数并返回。</td>
</tr>
<tr>
<td><code>DensityFunctions.Noise</code></td>
<td>如果噪声参数被识别为洞穴噪声，则返回<code>null</code>，否则返回本身。</td>
</tr>
<tr>
<td><code>DensityFunctions.RangeChoice</code></td>
<td>如果输入被识别为洞穴密度函数，则始终会选择<code>whenOutOfRange</code>分支，转换后返回。<br>如果输入被识别为常量，则会进行优化，选择对应的分支，转换后返回。<br>其他情况下，两个分支都会被转换，若一方为<code>null</code>，则直接返回另一方；若没有变化，则返回原对象；否则返回新对象，其中对应的密度函数被替换成转换后的。</td>
</tr>
<tr>
<td><code>DensityFunctions.MulOrAdd</code></td>
<td>输入会被转换。<br>如果结果为<code>null</code>，则会将线性操作的参数作为常量返回。<br>如果结果为常量，则会进行优化并返回计算后的常量。<br>其他情况下，若没有变化，则返回原对象；否则返回新对象，其中对应的密度函数被替换成转换后的。</td>
</tr>
<tr>
<td><code>DensityFunctions.Marker</code></td>
<td>被包裹的密度函数会被转换。<br>如果结果为<code>null</code>，则会返回<code>null</code>。<br>其他情况下，若没有变化或类型未知，则返回原对象；否则返回新对象，其中对应的密度函数被替换成转换后的。</td>
</tr>
<tr>
<td><code>DensityFunctions.TwoArgumentSimpleFunction</code></td>
<td>两个参数都会被转换。<br>如果转换结果一方为<code>null</code>，则直接返回另一方。<br>如果转换结果两方均为常量，则会进行优化并返回计算后的常量。<br>其他情况下，用转换结果两方创建新的相同类型二元操作后返回。</td>
</tr>
<tr>
<td><code>DensityFunctions.PureTransformer</code></td>
<td>输入会被转换。<br>如果结果为<code>null</code>，则会返回<code>null</code>。<br>如果结果为常量，则会进行优化并返回计算后的常量。<br>其他情况下，如果是<code>Clamp</code>型，更新原对象的字段后返回；如果是<code>UnaryOperation</code>型，用转换结果创建新的相同类型一元操作后返回；否则返回原始对象（无法处理未知类型）。</td>
</tr>
<tr>
<td><code>DensityFunctions.TransformerWithContext</code></td>
<td>输入会被转换。<br>如果结果为<code>null</code>，则会返回<code>null</code>。<br>其他情况下，若没有变化或类型未知，则返回原对象；否则返回新对象，其中对应的密度函数被替换成转换后的。</td>
</tr>
</tbody>
</table>

其他模组可以将其转换器注册到 CustomTransformerRegistry 以进行自定义转换。NoCaves 会按照 map 中的顺序逐个检查，若匹配断言，就执行对应的转换函数。当没有任何自定义转换器匹配时，才会执行内置转换器。以下是注册自定义转换器的示例代码：

```java
Predicate<DensityFunction> pred = df -> {
    return df instanceof YuorCustomDensityFunction;
};
Function<DensityFunction, DensityFunction> func = df -> {
    // 在此编写自定义逻辑
    return df;
};
var reg = DensityFunctionCaveCleaner.getCustomTransformerRegistry();
reg.put("id", new Pair<>(pred, func));
```
