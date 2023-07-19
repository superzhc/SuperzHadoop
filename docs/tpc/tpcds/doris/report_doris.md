# Apache Doris

## 导入

**1GB**

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

**10GB**

| TableName              | NumberTotalRows | LoadBytes  | LoadTimeMs | ReadDataTimeMs | WriteDataTimeMs |
| ---------------------- | :-------------: | :--------: | :--------: | :------------: | :-------------: |
| dbgen_version          |        1        |     77     |    116     |       0        |       66        |
| customer_address       |     250000      |  27457596  |    2332    |       45       |      2254       |
| customer_demographics  |     1920800     |  78739296  |    4802    |      311       |      4646       |
| date_dim               |      73049      |  10244389  |    965     |       11       |       927       |
| warehouse              |       10        |    1176    |     67     |       0        |       27        |
| ship_mode              |       20        |    1093    |    126     |       0        |       89        |
| time_dim               |      86400      |  5021380   |    647     |       3        |       617       |
| reason                 |       45        |    1604    |    411     |       0        |       381       |
| income_band            |       20        |    308     |    277     |       0        |       248       |
| item                   |     102000      |  28753325  |    1135    |      112       |      1082       |
| store                  |       102       |   26983    |     80     |       0        |       50        |
| call_center            |       24        |    7520    |     73     |       0        |       42        |
| customer               |     500000      |  66377545  |    2829    |      267       |      2769       |
| web_site               |       42        |   12125    |    263     |       0        |       236       |
| store_returns          |     2875432     | 335488115  |   51590    |     13310      |      51366      |
| household_demographics |      7200       |   144453   |    310     |       0        |       272       |
| web_page               |       200       |   19131    |     94     |       0        |       67        |
| promotion              |       500       |   61659    |     65     |       0        |       37        |
| catalog_page           |      12000      |  1659048   |    421     |       1        |       371       |
| inventory              |    133110000    | 2629887971 |   474442   |     405516     |     469433      |
| catalog_returns        |     1439749     | 220138943  |   29536    |     15140      |      29272      |
| web_returns            |     719217      | 101336920  |    5774    |      1002      |      5690       |
| web_sales              |     7197566     | 1504223803 |   113958   |     106644     |     112411      |
| catalog_sales          |    14401261     | 3023483057 |   254045   |     188260     |     249031      |
| store_sales            |    28800991     | 3970120305 |   437943   |     404856     |     432924      |

## 查询

| Query SQL | 1GB-耗时【单位毫秒】 | 10GB-耗时【单位毫秒】 |
| --------- | :------------------: | :-------------------: |
| query1    |         266          |                       |
| query2    |         327          |                       |
| query3    |         100          |                       |
| query4    |         775          |                       |
| query5    |         1.5s         |                       |
| query6    |         295          |                       |
| query7    |         176          |                       |
| query8    |    152(无数据???)    |                       |
| query9    |         173          |                       |
| query10   |         193          |                       |
| query11   |         614          |                       |
| query12   |         153          |                       |
| query13   |         245          |                       |
| query14   |   4.583s(两条语句)   |                       |
| query15   |         238          |                       |
| query16   |         279          |                       |
| query17   |         434          |                       |
| query18   |         534          |                       |
| query19   |         223          |                       |
| query20   |         152          |                       |
| query21   |         116          |                       |
| query22   |         636          |                       |
| query23   |    1.803(无数据)     |                       |
| query24   |     838(无数据)      |                       |
| query25   |     221(无数据)      |                       |
| query26   |         219          |                       |
| query27   |         175          |                       |
| query28   |         198          |                       |
| query29   |         446          |                       |
| query30   |         195          |                       |
| query31   |         295          |                       |
| query32   |         123          |                       |
| query33   |         150          |                       |
| query34   |         272          |                       |
| query35   |         218          |                       |
| query36   |         203          |                       |
| query37   |     194(无数据)      |                       |
| query38   |         305          |                       |
| query39   |    380(两条语句)     |                       |
| query40   |         107          |                       |
| query41   |          80          |                       |
| query42   |          89          |                       |
| query43   |         183          |                       |
| query44   |         128          |                       |
| query45   |         224          |                       |
| query46   |         432          |                       |
| query47   |         447          |                       |
| query48   |         177          |                       |
| query49   |         267          |                       |
| query50   |         152          |                       |
| query51   |        1.10s         |                       |
| query52   |          91          |                       |
| query53   |         102          |                       |
| query54   |     480(无数据)      |                       |
| query55   |          78          |                       |
| query56   |         391          |                       |
| query57   |         253          |                       |
| query58   |     265(无数据)      |                       |
| query59   |         616          |                       |
| query60   |         155          |                       |
| query61   |     166(无数据)      |                       |
| query62   |         125          |                       |
| query63   |         122          |                       |
| query64   |        2.379s        |                       |
| query65   |         151          |                       |
| query66   |         284          |                       |
| query67   |        1.465s        |                       |
| query68   |         401          |                       |
| query69   |         148          |                       |
| query70   |         216          |                       |
| query71   |         109          |                       |
| query72   |         428          |                       |
| query73   |         249          |                       |
| query74   |         321          |                       |
| query75   |         235          |                       |
| query76   |         134          |                       |
| query77   |         152          |                       |
| query78   |        1.870s        |                       |
| query79   |         316          |                       |
| query80   |         206          |                       |
| query81   |         231          |                       |
| query82   |         286          |                       |
| query83   |         155          |                       |
| query84   |         146          |                       |
| query85   |         307          |                       |
| query86   |         127          |                       |
| query87   |         272          |                       |
| query88   |         160          |                       |
| query89   |         143          |                       |
| query90   |          88          |                       |
| query91   |          97          |                       |
| query92   |     128(无数据)      |                       |
| query93   |     159(无数据)      |                       |
| query94   |         177          |                       |
| query95   |        2.153s        |                       |
| query96   |          82          |                       |
| query97   |         177          |                       |
| query98   |         198          |                       |
| query99   |         150          |                       |
| DATE      |   2023-07-19 15:48   |                       |