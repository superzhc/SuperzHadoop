SELECT Report_C.ItemText,Report_C.OrderNo,
       
     -- 省内企业成交-总项目数量
    (IFNULL(Report1.Report_GGB1_ALL_Count, 0) + IFNULL(Report2.Report_GGB2_ALL_Count, 0)) AS Report_ALL_Count,
    -- 省内企业成交-省内企业成交总项目数量
    (IFNULL(Report1.Report_GGB1_All_Count_SN, 0) + IFNULL(Report2.Report_GGB2_All_Count_SN, 0)) AS Report_ALL_Count_SN,
    -- 占比
      case (IFNULL(Report1.Report_GGB1_ALL_Count, 0) + IFNULL(Report2.Report_GGB2_ALL_Count, 0)) when 0 then '0.00%' else CONCAT(CONVERT(CAST( (IFNULL(Report1.Report_GGB1_All_Count_SN, 0) + IFNULL(Report2.Report_GGB2_All_Count_SN, 0) ) / (IFNULL(Report1.Report_GGB1_ALL_Count, 0) + IFNULL(Report2.Report_GGB2_ALL_Count, 0)) * 100 AS DECIMAL(18,2)), CHAR), '%') END AS Report_ALL_Count_Per,
  
    -- 省内企业成交-总成交金额
    CAST((IFNULL(Report1.Report_GGB1_All_ZBPrice, 0) + IFNULL(Report2.Report_GGB2_All_ZBPrice, 0)) AS DECIMAL(18,2)) AS Report_ALL_ZBPrice,
    -- 省内企业成交-省内企业成交总金额
       CAST((IFNULL(Report1.Report_GGB1_All_ZBPrice_SN, 0) + IFNULL(Report2.Report_GGB2_All_ZBPrice_SN, 0)) AS DECIMAL(18,2)) AS Report_ALL_ZBPrice_SN,
    -- 占比
      case (IFNULL(Report1.Report_GGB1_All_ZBPrice, 0) + IFNULL(Report2.Report_GGB2_All_ZBPrice, 0) ) when 0 then '0.00%' else CONCAT(CONVERT(CAST((IFNULL(Report1.Report_GGB1_All_ZBPrice_SN, 0) + IFNULL(Report2.Report_GGB2_All_ZBPrice_SN, 0)) / (IFNULL(Report1.Report_GGB1_All_ZBPrice, 0) + IFNULL(Report2.Report_GGB2_All_ZBPrice, 0) ) * 100 AS DECIMAL(18,2)), CHAR), '%') END AS Report_ALL_ZBPrice_Per,
    
    -- GGB1-汇总
    IFNULL(Report1.Report_GGB1_ALL_Count, 0) AS Report_GGB1_ALL_Count,
    IFNULL(Report1.Report_GGB1_All_Count_SN, 0) AS Report_GGB1_All_Count_SN,
    case IFNULL(Report1.Report_GGB1_ALL_Count, 0) when 0 then '0.00%' else CONCAT(CONVERT(CAST( IFNULL(Report1.Report_GGB1_All_Count_SN, 0) / Report1.Report_GGB1_ALL_Count * 100 AS DECIMAL(18,2)), CHAR), '%') END AS Report_GGB1_ALL_Count_Per,
    
    CAST(IFNULL(Report1.Report_GGB1_All_ZBPrice ,0) AS DECIMAL(18,2)) AS Report_GGB1_All_ZBPrice,
    CAST(IFNULL(Report1.Report_GGB1_All_ZBPrice_SN, 0) AS DECIMAL(18,2)) AS Report_GGB1_All_ZBPrice_SN,
    case IFNULL(Report1.Report_GGB1_All_ZBPrice, 0) when 0 then '0.00%' else CONCAT(CONVERT(CAST( IFNULL(Report1.Report_GGB1_All_ZBPrice_SN, 0) / Report1.Report_GGB1_All_ZBPrice * 100 AS DECIMAL(18,2)), CHAR), '%') END AS Report_GGB1_ALL_ZBPrice_Per,
    
    -- GGB1-房建市政
    IFNULL(Report1.Report_GGB1_FJSZ_Count, 0) AS Report_GGB1_FJSZ_Count,
    IFNULL(Report1.Report_GGB1_FJSZ_Count_SN, 0) AS Report_GGB1_FJSZ_Count_SN,
    case IFNULL(Report1.Report_GGB1_FJSZ_Count, 0) when 0 then '0.00%' else CONCAT(CONVERT(CAST( IFNULL(Report1.Report_GGB1_FJSZ_Count_SN, 0) / Report1.Report_GGB1_FJSZ_Count * 100 AS DECIMAL(18,2)), CHAR), '%') END AS Report_GGB1_FJSZ_Count_Per,
    
    CAST(IFNULL(Report1.Report_GGB1_FJSZ_ZBPrice ,0) AS DECIMAL(18,2)) AS Report_GGB1_FJSZ_ZBPrice,
    CAST(IFNULL(Report1.Report_GGB1_FJSZ_ZBPrice_SN, 0) AS DECIMAL(18,2)) AS Report_GGB1_FJSZ_ZBPrice_SN,
    case IFNULL(Report1.Report_GGB1_FJSZ_ZBPrice, 0) when 0 then '0.00%' else CONCAT(CONVERT(CAST( IFNULL(Report1.Report_GGB1_FJSZ_ZBPrice_SN, 0) / Report1.Report_GGB1_FJSZ_ZBPrice * 100 AS DECIMAL(18,2)), CHAR), '%') END AS Report_GGB1_FJSZ_ZBPrice_Per,
    
    -- GGB1-交通运输
    IFNULL(Report1.Report_GGB1_JTYS_Count, 0) AS Report_GGB1_JTYS_Count,
    IFNULL(Report1.Report_GGB1_JTYS_Count_SN, 0) AS Report_GGB1_JTYS_Count_SN,
    case IFNULL(Report1.Report_GGB1_JTYS_Count, 0) when 0 then '0.00%' else CONCAT(CONVERT(CAST( IFNULL(Report1.Report_GGB1_JTYS_Count_SN, 0) / Report1.Report_GGB1_JTYS_Count * 100 AS DECIMAL(18,2)), CHAR), '%') END AS Report_GGB1_JTYS_Count_Per,
    
    CAST(IFNULL(Report1.Report_GGB1_JTYS_ZBPrice ,0) AS DECIMAL(18,2)) AS Report_GGB1_JTYS_ZBPrice,
    CAST(IFNULL(Report1.Report_GGB1_JTYS_ZBPrice_SN, 0) AS DECIMAL(18,2)) AS Report_GGB1_JTYS_ZBPrice_SN,
    case IFNULL(Report1.Report_GGB1_JTYS_ZBPrice, 0) when 0 then '0.00%' else CONCAT(CONVERT(CAST( IFNULL(Report1.Report_GGB1_JTYS_ZBPrice_SN, 0) / Report1.Report_GGB1_JTYS_ZBPrice * 100 AS DECIMAL(18,2)), CHAR), '%') END AS Report_GGB1_JTYS_ZBPrice_Per,
    
    -- GGB1-水利水电
    IFNULL(Report1.Report_GGB1_SLSD_Count, 0) AS Report_GGB1_SLSD_Count,
    IFNULL(Report1.Report_GGB1_SLSD_Count_SN, 0) AS Report_GGB1_SLSD_Count_SN,
    case IFNULL(Report1.Report_GGB1_SLSD_Count, 0) when 0 then '0.00%' else CONCAT(CONVERT(CAST( IFNULL(Report1.Report_GGB1_SLSD_Count_SN, 0) / Report1.Report_GGB1_SLSD_Count * 100 AS DECIMAL(18,2)), CHAR), '%') END AS Report_GGB1_SLSD_Count_Per,
    
    CAST(IFNULL(Report1.Report_GGB1_SLSD_ZBPrice ,0) AS DECIMAL(18,2)) AS Report_GGB1_SLSD_ZBPrice,
    CAST(IFNULL(Report1.Report_GGB1_SLSD_ZBPrice_SN, 0) AS DECIMAL(18,2)) AS Report_GGB1_SLSD_ZBPrice_SN,
    case IFNULL(Report1.Report_GGB1_SLSD_ZBPrice, 0) when 0 then '0.00%' else CONCAT(CONVERT(CAST( IFNULL(Report1.Report_GGB1_SLSD_ZBPrice_SN, 0) / Report1.Report_GGB1_SLSD_ZBPrice * 100 AS DECIMAL(18,2)), CHAR), '%') END AS Report_GGB1_SLSD_ZBPrice_Per,
    
    -- GGB1-其他工程
    IFNULL(Report1.Report_GGB1_QTGC_Count, 0) AS Report_GGB1_QTGC_Count,
    IFNULL(Report1.Report_GGB1_QTGC_Count_SN, 0) AS Report_GGB1_QTGC_Count_SN,
    case IFNULL(Report1.Report_GGB1_QTGC_Count, 0) when 0 then '0.00%' else CONCAT(CONVERT(CAST( IFNULL(Report1.Report_GGB1_QTGC_Count_SN, 0) / Report1.Report_GGB1_QTGC_Count * 100 AS DECIMAL(18,2)), CHAR), '%') END AS Report_GGB1_QTGC_Count_Per,
    
    CAST(IFNULL(Report1.Report_GGB1_QTGC_ZBPrice ,0) AS DECIMAL(18,2)) AS Report_GGB1_QTGC_ZBPrice,
    CAST(IFNULL(Report1.Report_GGB1_QTGC_ZBPrice_SN, 0) AS DECIMAL(18,2)) AS Report_GGB1_QTGC_ZBPrice_SN,
    case IFNULL(Report1.Report_GGB1_QTGC_ZBPrice, 0) when 0 then '0.00%' else CONCAT(CONVERT(CAST( IFNULL(Report1.Report_GGB1_QTGC_ZBPrice_SN, 0) / Report1.Report_GGB1_QTGC_ZBPrice * 100 AS DECIMAL(18,2)), CHAR), '%') END AS Report_GGB1_QTGC_ZBPrice_Per,
    
    -- GGB2-汇总
    IFNULL(Report2.Report_GGB2_ALL_Count, 0) AS Report_GGB2_ALL_Count,
    IFNULL(Report2.Report_GGB2_All_Count_SN, 0) AS Report_GGB2_All_Count_SN,
    case IFNULL(Report2.Report_GGB2_ALL_Count, 0) when 0 then '0.00%' else CONCAT(CONVERT(CAST( IFNULL(Report2.Report_GGB2_All_Count_SN, 0) / Report2.Report_GGB2_ALL_Count * 100 AS DECIMAL(18,2)), CHAR), '%') END AS Report_GGB2_ALL_Count_Per,
    
    CAST(IFNULL(Report2.Report_GGB2_All_ZBPrice ,0) AS DECIMAL(18,2)) AS Report_GGB2_All_ZBPrice,
    CAST(IFNULL(Report2.Report_GGB2_All_ZBPrice_SN, 0) AS DECIMAL(18,2)) AS Report_GGB2_All_ZBPrice_SN,
    case IFNULL(Report2.Report_GGB2_All_ZBPrice, 0) when 0 then '0.00%' else CONCAT(CONVERT(CAST( IFNULL(Report2.Report_GGB2_All_ZBPrice_SN, 0) / Report2.Report_GGB2_All_ZBPrice * 100 AS DECIMAL(18,2)), CHAR), '%') END AS Report_GGB2_ALL_ZBPrice_Per,
    
    -- GGB2-房建市政
    IFNULL(Report2.Report_GGB2_FJSZ_Count, 0) AS Report_GGB2_FJSZ_Count,
    IFNULL(Report2.Report_GGB2_FJSZ_Count_SN, 0) AS Report_GGB2_FJSZ_Count_SN,
    case IFNULL(Report2.Report_GGB2_FJSZ_Count, 0) when 0 then '0.00%' else CONCAT(CONVERT(CAST( IFNULL(Report2.Report_GGB2_FJSZ_Count_SN, 0) / Report2.Report_GGB2_FJSZ_Count * 100 AS DECIMAL(18,2)), CHAR), '%') END AS Report_GGB2_FJSZ_Count_Per,
    
    CAST(IFNULL(Report2.Report_GGB2_FJSZ_ZBPrice ,0) AS DECIMAL(18,2)) AS Report_GGB2_FJSZ_ZBPrice,
    CAST(IFNULL(Report2.Report_GGB2_FJSZ_ZBPrice_SN, 0) AS DECIMAL(18,2)) AS Report_GGB2_FJSZ_ZBPrice_SN,
    case IFNULL(Report2.Report_GGB2_FJSZ_ZBPrice, 0) when 0 then '0.00%' else CONCAT(CONVERT(CAST( IFNULL(Report2.Report_GGB2_FJSZ_ZBPrice_SN, 0) / Report2.Report_GGB2_FJSZ_ZBPrice * 100 AS DECIMAL(18,2)), CHAR), '%') END AS Report_GGB2_FJSZ_ZBPrice_Per,
    
    -- GGB2-交通运输
    IFNULL(Report2.Report_GGB2_JTYS_Count, 0) AS Report_GGB2_JTYS_Count,
    IFNULL(Report2.Report_GGB2_JTYS_Count_SN, 0) AS Report_GGB2_JTYS_Count_SN,
    case IFNULL(Report2.Report_GGB2_JTYS_Count, 0) when 0 then '0.00%' else CONCAT(CONVERT(CAST( IFNULL(Report2.Report_GGB2_JTYS_Count_SN, 0) / Report2.Report_GGB2_JTYS_Count * 100 AS DECIMAL(18,2)), CHAR), '%') END AS Report_GGB2_JTYS_Count_Per,
    
    CAST(IFNULL(Report2.Report_GGB2_JTYS_ZBPrice ,0) AS DECIMAL(18,2)) AS Report_GGB2_JTYS_ZBPrice,
    CAST(IFNULL(Report2.Report_GGB2_JTYS_ZBPrice_SN, 0) AS DECIMAL(18,2)) AS Report_GGB2_JTYS_ZBPrice_SN,
    case IFNULL(Report2.Report_GGB2_JTYS_ZBPrice, 0) when 0 then '0.00%' else CONCAT(CONVERT(CAST( IFNULL(Report2.Report_GGB2_JTYS_ZBPrice_SN, 0) / Report2.Report_GGB2_JTYS_ZBPrice * 100 AS DECIMAL(18,2)), CHAR), '%') END AS Report_GGB2_JTYS_ZBPrice_Per,
    
    -- GGB2-水利水电
    IFNULL(Report2.Report_GGB2_SLSD_Count, 0) AS Report_GGB2_SLSD_Count,
    IFNULL(Report2.Report_GGB2_SLSD_Count_SN, 0) AS Report_GGB2_SLSD_Count_SN,
    case IFNULL(Report2.Report_GGB2_SLSD_Count, 0) when 0 then '0.00%' else CONCAT(CONVERT(CAST( IFNULL(Report2.Report_GGB2_SLSD_Count_SN, 0) / Report2.Report_GGB2_SLSD_Count * 100 AS DECIMAL(18,2)), CHAR), '%') END AS Report_GGB2_SLSD_Count_Per,
    
    CAST(IFNULL(Report2.Report_GGB2_SLSD_ZBPrice ,0) AS DECIMAL(18,2)) AS Report_GGB2_SLSD_ZBPrice,
    CAST(IFNULL(Report2.Report_GGB2_SLSD_ZBPrice_SN, 0) AS DECIMAL(18,2)) AS Report_GGB2_SLSD_ZBPrice_SN,
    case IFNULL(Report2.Report_GGB2_SLSD_ZBPrice, 0) when 0 then '0.00%' else CONCAT(CONVERT(CAST( IFNULL(Report2.Report_GGB2_SLSD_ZBPrice_SN, 0) / Report2.Report_GGB2_SLSD_ZBPrice * 100 AS DECIMAL(18,2)), CHAR), '%') END AS Report_GGB2_SLSD_ZBPrice_Per,
    
    -- GGB2-其他工程
    IFNULL(Report2.Report_GGB2_QTGC_Count, 0) AS Report_GGB2_QTGC_Count,
    IFNULL(Report2.Report_GGB2_QTGC_Count_SN, 0) AS Report_GGB2_QTGC_Count_SN,
    case IFNULL(Report2.Report_GGB2_QTGC_Count, 0) when 0 then '0.00%' else CONCAT(CONVERT(CAST( IFNULL(Report2.Report_GGB2_QTGC_Count_SN, 0) / Report2.Report_GGB2_QTGC_Count * 100 AS DECIMAL(18,2)), CHAR), '%') END AS Report_GGB2_QTGC_Count_Per,
    
    CAST(IFNULL(Report2.Report_GGB2_QTGC_ZBPrice ,0) AS DECIMAL(18,2)) AS Report_GGB2_QTGC_ZBPrice,
    CAST(IFNULL(Report2.Report_GGB2_QTGC_ZBPrice_SN, 0) AS DECIMAL(18,2)) AS Report_GGB2_QTGC_ZBPrice_SN,
    case IFNULL(Report2.Report_GGB2_QTGC_ZBPrice, 0) when 0 then '0.00%' else CONCAT(CONVERT(CAST( IFNULL(Report2.Report_GGB2_QTGC_ZBPrice_SN, 0) / Report2.Report_GGB2_QTGC_ZBPrice * 100 AS DECIMAL(18,2)), CHAR), '%') END AS Report_GGB2_QTGC_ZBPrice_Per
    
FROM
(SELECT
  rep.ItemText,
 rep.ItemValue,
 
 -- GGB1-汇总
 sum( IF ( ggb1.biaoduanno is not null, 1, 0 ) ) AS Report_GGB1_ALL_Count,
 sum( IF ( ggb1.IsShengNeiNew = '1', 1, 0 ) ) AS Report_GGB1_All_Count_SN,
 IFNULL(sum(ggb1.ZhongBiaoPrice), 0) AS Report_GGB1_All_ZBPrice,
  sum( IF ( ggb1.IsShengNeiNew = '1', ggb1.ZhongBiaoPrice, 0 ) ) AS Report_GGB1_All_ZBPrice_SN,
 
 -- GGB1-房建市政：房建、市政
 sum( IF ( ggb1.ProjectZTBType in ('01', '02'), 1, 0 ) ) AS Report_GGB1_FJSZ_Count,
 sum( IF ( ggb1.ProjectZTBType in ('01', '02') and ggb1.IsShengNeiNew = '1', 1, 0 ) ) AS Report_GGB1_FJSZ_Count_SN,
 sum( IF ( ggb1.ProjectZTBType in ('01', '02'), ggb1.ZhongBiaoPrice, 0 ) ) AS Report_GGB1_FJSZ_ZBPrice,
 sum( IF ( ggb1.ProjectZTBType in ('01', '02') and ggb1.IsShengNeiNew = '1', ggb1.SNZBPrice, 0 ) ) AS Report_GGB1_FJSZ_ZBPrice_SN,
 
 -- GGB1-交通运输：公路、水运
 sum( IF ( ggb1.ProjectZTBType in ('03', '06'), 1, 0 ) ) AS Report_GGB1_JTYS_Count,
 sum( IF ( ggb1.ProjectZTBType in ('03', '06') and ggb1.IsShengNeiNew = '1', 1, 0 ) ) AS Report_GGB1_JTYS_Count_SN,
 sum( IF ( ggb1.ProjectZTBType in ('03', '06'), ggb1.ZhongBiaoPrice, 0 ) ) AS Report_GGB1_JTYS_ZBPrice,
 sum( IF ( ggb1.ProjectZTBType in ('03', '06') and ggb1.IsShengNeiNew = '1', ggb1.SNZBPrice, 0 ) ) AS Report_GGB1_JTYS_ZBPrice_SN,
 
 -- GGB1-水利水电：水利 
 sum( IF ( ggb1.ProjectZTBType = '07', 1, 0 ) ) AS Report_GGB1_SLSD_Count,
 sum( IF ( ggb1.ProjectZTBType = '07' and ggb1.IsShengNeiNew = '1', 1, 0 ) ) AS Report_GGB1_SLSD_Count_SN,
 sum( IF ( ggb1.ProjectZTBType = '07', ggb1.ZhongBiaoPrice, 0 ) ) AS Report_GGB1_SLSD_ZBPrice,
 sum( IF ( ggb1.ProjectZTBType = '07' and ggb1.IsShengNeiNew = '1', ggb1.SNZBPrice, 0 ) ) AS Report_GGB1_SLSD_ZBPrice_SN,
 
 -- GGB1-其他工程：铁路、民航、能源、邮电通信、其他
 sum( IF ( ggb1.ProjectZTBType in ('04', '05', '08', '09', '99'), 1, 0 ) ) AS Report_GGB1_QTGC_Count,
 sum( IF ( ggb1.ProjectZTBType in ('04', '05', '08', '09', '99') and ggb1.IsShengNeiNew = '1', 1, 0 ) ) AS Report_GGB1_QTGC_Count_SN,
 sum( IF ( ggb1.ProjectZTBType in ('04', '05', '08', '09', '99'), ggb1.ZhongBiaoPrice, 0 ) ) AS Report_GGB1_QTGC_ZBPrice,
 sum( IF ( ggb1.ProjectZTBType in ('04', '05', '08', '09', '99') and ggb1.IsShengNeiNew = '1', ggb1.SNZBPrice, 0 ) ) AS Report_GGB1_QTGC_ZBPrice_SN
 
 FROM (select biaoduanno, IsShengNeiNew, ZhongBiaoPrice, SNZBPrice, ProjectZTBType, quyu from yw_baobiaodata WHERE DATE_FORMAT(SBR_Date,'%Y-%m-%d')>={StartDate} AND DATE_FORMAT(SBR_Date,'%Y-%m-%d')<={EndDate} and ProjectJiaoYiType = 'A') ggb1
 RIGHT JOIN ( SELECT itemtext, ItemValue FROM view_codemain_codeitems WHERE codename = '省共管办报表区域' ) rep ON instr(rep.ItemValue, ggb1.QuYu) > 0 
 GROUP BY rep.ItemValue ) Report1
 
 INNER JOIN
 
 (SELECT
  rep.ItemText,
 rep.ItemValue,
 
 -- GGB2-汇总
 sum( IF ( ggb2.biaoduanno is not null, 1, 0 ) ) AS Report_GGB2_ALL_Count,
 sum( IF ( ggb2.IsShengNeiNew = '1', 1, 0 ) ) AS Report_GGB2_All_Count_SN,
 IFNULL(sum(ggb2.ZhongBiaoPrice), 0) AS Report_GGB2_All_ZBPrice,
  sum( IF ( ggb2.IsShengNeiNew = '1', ggb2.ZhongBiaoPrice, 0 ) ) AS Report_GGB2_All_ZBPrice_SN,
 
 -- GGB2-房建市政：房建、市政
 sum( IF ( ggb2.ProjectZTBType in ('01', '02'), 1, 0 ) ) AS Report_GGB2_FJSZ_Count,
 sum( IF ( ggb2.ProjectZTBType in ('01', '02') and ggb2.IsShengNeiNew = '1', 1, 0 ) ) AS Report_GGB2_FJSZ_Count_SN,
 sum( IF ( ggb2.ProjectZTBType in ('01', '02'), ggb2.ZhongBiaoPrice, 0 ) ) AS Report_GGB2_FJSZ_ZBPrice,
 sum( IF ( ggb2.ProjectZTBType in ('01', '02') and ggb2.IsShengNeiNew = '1', ggb2.SNZBPrice, 0 ) ) AS Report_GGB2_FJSZ_ZBPrice_SN,
 
 -- GGB2-交通运输：公路、水运
 sum( IF ( ggb2.ProjectZTBType in ('03', '06'), 1, 0 ) ) AS Report_GGB2_JTYS_Count,
 sum( IF ( ggb2.ProjectZTBType in ('03', '06') and ggb2.IsShengNeiNew = '1', 1, 0 ) ) AS Report_GGB2_JTYS_Count_SN,
 sum( IF ( ggb2.ProjectZTBType in ('03', '06'), ggb2.ZhongBiaoPrice, 0 ) ) AS Report_GGB2_JTYS_ZBPrice,
 sum( IF ( ggb2.ProjectZTBType in ('03', '06') and ggb2.IsShengNeiNew = '1', ggb2.SNZBPrice, 0 ) ) AS Report_GGB2_JTYS_ZBPrice_SN,
 
 -- GGB2-水利水电：水利 
 sum( IF ( ggb2.ProjectZTBType = '07', 1, 0 ) ) AS Report_GGB2_SLSD_Count,
 sum( IF ( ggb2.ProjectZTBType = '07' and ggb2.IsShengNeiNew = '1', 1, 0 ) ) AS Report_GGB2_SLSD_Count_SN,
 sum( IF ( ggb2.ProjectZTBType = '07', ggb2.ZhongBiaoPrice, 0 ) ) AS Report_GGB2_SLSD_ZBPrice,
 sum( IF ( ggb2.ProjectZTBType = '07' and ggb2.IsShengNeiNew = '1', ggb2.SNZBPrice, 0 ) ) AS Report_GGB2_SLSD_ZBPrice_SN,
 
 -- GGB2-其他工程：铁路、民航、能源、邮电通信、其他
 sum( IF ( ggb2.ProjectZTBType in ('04', '05', '08', '09', '99'), 1, 0 ) ) AS Report_GGB2_QTGC_Count,
 sum( IF ( ggb2.ProjectZTBType in ('04', '05', '08', '09', '99') and ggb2.IsShengNeiNew = '1', 1, 0 ) ) AS Report_GGB2_QTGC_Count_SN,
 sum( IF ( ggb2.ProjectZTBType in ('04', '05', '08', '09', '99'), ggb2.ZhongBiaoPrice, 0 ) ) AS Report_GGB2_QTGC_ZBPrice,
 sum( IF ( ggb2.ProjectZTBType in ('04', '05', '08', '09', '99') and ggb2.IsShengNeiNew = '1', ggb2.SNZBPrice, 0 ) ) AS Report_GGB2_QTGC_ZBPrice_SN
 
 FROM ( select biaoduanno, IsShengNeiNew, ProjectZTBType, ZhongBiaoPrice, SNZBPrice, QuYu from yw_baobiaodata_ggb2 WHERE DATE_FORMAT(SBR_Date,'%Y-%m-%d')>={StartDate} AND DATE_FORMAT(SBR_Date,'%Y-%m-%d')<={EndDate} and ProjectJiaoYiType = 'A') ggb2
 RIGHT JOIN ( SELECT itemtext, ItemValue FROM view_codemain_codeitems WHERE codename = '省共管办报表区域' ) rep ON instr(rep.ItemValue, ggb2.QuYu) > 0 
 GROUP BY rep.ItemValue ) Report2 ON Report1.ItemValue = Report2.ItemValue
 RIGHT JOIN (select ItemText, ItemValue, OrderNo from VIEW_CodeMain_CodeItems WHERE CodeName='省共管办报表区域') Report_C on Report1.ItemValue = Report_C.ItemValue

 