# Apache Doris

## 1GB

**导入**

| TableName              | NumberTotalRows | LoadBytes | LoadTimeMs | ReadDataTimeMs | WriteDataTimeMs |
| ---------------------- | :-------------: | :-------: | :--------: | :------------: | :-------------: |
| dbgen_version          |        1        |    84     |    1272    |       0        |       292       |
| customer_address       |      50000      |  5452165  |    1530    |       15       |      1487       |
| customer_demographics  |     1920800     | 78739296  |    4465    |      732       |      4316       |
| date_dim               |      73049      | 10244389  |    643     |       12       |       605       |
| warehouse              |        5        |    580    |     66     |       0        |       31        |
| ship_mode              |       20        |   1093    |    270     |       0        |       219       |
| time_dim               |      86400      |  5021380  |    512     |       8        |       471       |
| reason                 |       35        |   1304    |     49     |       0        |       20        |
| income_band            |       20        |    308    |    257     |       0        |       230       |
| item                   |      18000      |  5033899  |    294     |       6        |       262       |
| store                  |       12        |   3143    |     92     |       0        |       45        |
| call_center            |        6        |   1885    |     61     |       0        |       33        |
| customer               |     100000      | 13110282  |    784     |       60       |       743       |
| web_site               |       30        |   8741    |    138     |       0        |       101       |
| store_returns          |     287514      | 32422491  |    2378    |      315       |      2311       |
| household_demographics |      7200       |  144453   |    323     |       0        |       292       |
| web_page               |       60        |   5716    |    332     |       0        |       298       |
| promotion              |       300       |   36933   |     85     |       0        |       40        |
| catalog_page           |      11718      |  1620074  |    132     |       1        |       105       |
| inventory              |    11745000     | 224675139 |   49289    |      9067      |      48482      |
| catalog_returns        |     144067      | 21234304  |    1485    |      247       |      1441       |
| web_returns            |      71763      |  9734473  |    923     |       7        |       881       |
| web_sales              |     719384      | 146158290 |    7662    |      2470      |      7590       |
| catalog_sales          |     1441548     | 294468836 |   44709    |      7881      |      44587      |
| store_sales            |     2880404     | 385565005 |   54638    |     16305      |      54329      |

**查询**

| Query SQL |   耗时【单位毫秒】   |
| --------- | :------------------: |
| query1    |         266          |
| query2    |         327          |
| query3    |         100          |
| query4    |         775          |
| query5    |         1.5s         |
| query6    |         295          |
| query7    |         176          |
| query8    | 152(未查询到数据???) |
| query9    |         173          |
| query10   |         193          |
| query11   |         614          |
| query12   |         153          |
| query13   |         245          |
| query14   |   4.583s(两条语句)   |
| query15   |    238                  |
| query16   |       279               |
| query17   |       434               |
| query18   |       534               |
| query19   |       223               |
| query20   |      152                |
| query21   |       116               |
| query22   |        636              |
| query23   |                      |
| query24   |                      |
| query25   |                      |
| query26   |                      |
| query27   |                      |
| query28   |                      |
| query29   |                      |
| query30   |                      |
| query31   |                      |
| query32   |                      |
| query33   |                      |
| query34   |                      |
| query35   |                      |
| query36   |                      |
| query37   |                      |
| query38   |                      |
| query39   |                      |
| query40   |                      |
| query41   |                      |
| query42   |                      |
| query43   |                      |
| query44   |                      |
| query45   |                      |
| query46   |                      |
| query47   |                      |
| query48   |                      |
| query49   |                      |
| query50   |                      |
| query51   |                      |
| query52   |                      |
| query53   |                      |
| query54   |                      |
| query55   |                      |
| query56   |                      |
| query57   |                      |
| query58   |                      |
| query59   |                      |
| query60   |                      |
| query61   |                      |
| query62   |                      |
| query63   |                      |
| query64   |                      |
| query65   |                      |
| query66   |                      |
| query67   |                      |
| query68   |                      |
| query69   |                      |
| query70   |                      |
| query71   |                      |
| query72   |                      |
| query73   |                      |
| query74   |                      |
| query75   |                      |
| query76   |                      |
| query77   |                      |
| query78   |                      |
| query79   |                      |
| query80   |                      |
| query81   |                      |
| query82   |                      |
| query83   |                      |
| query84   |                      |
| query85   |                      |
| query86   |                      |
| query87   |                      |
| query88   |                      |
| query89   |                      |
| query90   |                      |
| query91   |                      |
| query92   |                      |
| query93   |                      |
| query94   |                      |
| query95   |                      |
| query96   |                      |
| query97   |                      |
| query98   |                      |
| query99   |                      |
| 测试时间: |                      |