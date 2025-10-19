# Batch 模式下数据流转

## Source和Transformation算子在一个算子链上

1. Transformation的数据直接从数据源根据分片信息读取数据
   ![](images/Batch模式下数据流转20250519171441.png)
2. 实际数据源实现 `pollNext` 读取数据，如 `JdbcSourceReader`
   ![](images/Batch模式下数据流转20250519171913.png)
3. 将读取到的数据 `emitRecord` 到下一个算子
   ![](images/Batch模式下数据流转20250519172118.png)
   ![](images/Batch模式下数据流转20250519172242.png)
4. Transformation 进行处理
   ![](images/Batch模式下数据流转20250519172333.png)

## Transformation算子在一个独立的算子链

![](images/Batch模式下数据流转20250520092145.png)

1. Transformation算子的输入源是 `StreamTaskNetworkInput`
    ![](images/Batch模式下数据流转20250519174642.png)
2. 读取数据【根据偏移量从内存读取一条条记录】
   ![](images/Batch模式下数据流转20250520093400.png)
   <!--![](images/Batch模式下数据流转20250520093738.png)-->
   ![](images/Batch模式下数据流转20250520093819.png)
3. 每条记录处理
   ![](images/Batch模式下数据流转20250520094518.png)
   ![](images/Batch模式下数据流转20250520094612.png)