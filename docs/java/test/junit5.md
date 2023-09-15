# JUnit5

## 注解

JUnit Jupiter 支持下列注解，用于配置测试和扩展框架。

所有核心注解位于 `junit-jupiter-api` 模块中的 `org.junit.jupiter.api` 包中。

|注解|描述|
|:---:|----|
|`@Test`|表示方法是测试方法。与 JUnit4 的 `@Test` 注解不同的是，这个注解没有声明任何属性，因为 JUnit Jupiter 中的测试扩展是基于他们自己的专用注解来操作的。除非被覆盖，否则这些方法可以继承。|
|`@ParameterizedTest`|表示方法是参数化测试。 除非被覆盖，否则这些方法可以继承。|
|`@RepeatedTest`|表示方法是用于重复测试的测试模板。除非被覆盖，否则这些方法可以继承。|
|`@TestFactory`|表示方法是用于动态测试的测试工厂。除非被覆盖，否则这些方法可以继承。|
|`@TestInstance`|用于为被注解的测试类配置测试实例生命周期。 这个注解可以继承。|
|`@TestTemplate`|表示方法是测试用例的模板，设计为被调用多次，调用次数取决于自注册的提供者返回的调用上下文。除非被覆盖，否则这些方法可以继承。|
|`@DisplayName`|声明测试类或测试方法的自定义显示名称。这个注解不被继承。|
|`@BeforeEach`|表示被注解的方法应在当前类的每个 `@Test`，`@RepeatedTest`，`@ParameterizedTest` 或 `@TestFactory `方法之前执行; 类似于 JUnit4 的 `@Before`。 除非被覆盖，否则这些方法可以继承。|
|`@AfterEach`|表示被注解的方法应在当前类的每个 `@Test`，`@RepeatedTest`，`@ParameterizedTest` 或 `@TestFactory` 方法之后执行; 类似于 JUnit4 的 `@After`。 除非被覆盖，否则这些方法可以继承。|
|`@BeforeAll`|表示被注解的方法应该在当前类的所有 `@Test`，`@RepeatedTest`，`@ParameterizedTest` 和 `@TestFactory` 方法之前执行; 类似于 JUnit4 的 `@BeforeClass`。 这样的方法可以继承（除非被隐藏或覆盖），并且必须是静态的（除非使用“per-class”测试实例生命周期）。|
|`@AfterAll`|表示被注解的方法应该在当前类的所有 `@Test`，`@RepeatedTest`，`@ParameterizedTest` 和 `@TestFactory` 方法之后执行; 类似于 JUnit4 的 `@AfterClass`。 这样的方法可以继承（除非被隐藏或覆盖），并且必须是静态的（除非使用“per-class”测试实例生命周期）。|
|`@Nested`|表示被注解的类是一个嵌套的非静态测试类。除非使用“per-class”测试实例生命周期，否则 `@BeforeAll` 和 `@AfterAll` 方法不能直接在 `@Nested` 测试类中使用。 这个注解不能继承。|
|`@Tag`|在类或方法级别声明标签，用于过滤测试; 类似于 TestNG 中的 test group 或 JUnit4 中的 Categories。这个注释可以在类级别上继承，但不能在方法级别上继承。|
|`@Disabled`|用于禁用测试类或测试方法; 类似于 JUnit4 的 `@Ignore`。这个注解不能继承。|
|`@ExtendWith`|用于注册自定义扩展。 这个注解可以继承。|

使用 `@Test`，`@TestTemplate`，`@RepeatedTest`，`@BeforeAll`，`@AfterAll`，`@BeforeEach` 或 `@AfterEach` 注解的方法不能有返回值。