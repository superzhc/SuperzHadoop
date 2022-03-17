# TaskManager

每一个 TaskManager 是个 JVM，每个 JVM 中可以执行一个或者多个 subTask，JVM 中 taskSlot 的数量决定了接受多少个 task，每个 taskSlot 都有固定的资源。